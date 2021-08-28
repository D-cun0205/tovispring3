import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceImplTest extends UserServiceImpl {

    @Autowired
    UserService userService;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    UserDao userDao;

    @Mock
    UserDao mockUserDao;

    List<User> users;

    @Before
    public void setUp() {
        users = Arrays.asList(
                new User("bora", "보라돌이", "p1", Level.BASIC, UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER-1, 0),
                new User("ddu", "뚜비", "p2", Level.BASIC, UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("nana", "나나", "p3", Level.SILVER, UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER, UserServiceImpl.MIN_RECOMMEND_FOR_GOLD-1),
                new User("bbo", "뽀오", "p4", Level.SILVER, UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER, UserServiceImpl.MIN_RECOMMEND_FOR_GOLD),
                new User("samsung", "텔레토비동산청소기", "p5", Level.GOLD, 100, 100)
        );
    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevel.getLevel(), is(userWithLevelRead.getLevel()));
        assertThat(userWithoutLevel.getLevel(), is(userWithoutLevelRead.getLevel()));
    }

    @Test
    public void upgradeLevels() {
        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

    @Test
    public void upgradeAllOrNothing() throws Exception {
        UserServiceImpl.TestUserServiceImpl testUserService = new UserServiceImpl.TestUserServiceImpl(users.get(3).getId());
        testUserService.setUserDao(this.userDao);

        TransactionHandler txHandler = new TransactionHandler();
        txHandler.setTarget(testUserService);
        txHandler.setTransactionManager(transactionManager);
        txHandler.setPattern("upgradeLevels");
        UserService txUserService = (UserService) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { UserService.class }, txHandler);

        //TransactionHandler를 사용하지 않고 직접 생성해서 사용했을때
//        UserServiceTx txUserService = new UserServiceTx();
//        txUserService.setTransactionManager(this.transactionManager);
//        txUserService.setUserService(txUserService);

        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        try {
            testUserService.upgradeLevels();
        } catch(UserServiceImpl.TestUserServiceException e) {}
    }

// Spring Framework에서 권하는 단위테스트에 필요한 mokito 툴 사용법 6장 초반부에서 다루고 다시 사용하지않으므로 주석처리
//    @Test
//    public void mockUpgradeLevels() throws Exception {
//        UserServiceImpl userServiceImpl = new UserServiceImpl();
//
//        UserDao mockUserDao = mock(UserDao.class); //가짜 UserDao 생성
//        when(mockUserDao.getAll()).thenReturn(this.users); //getAll이라는 메소드가 호출되면 users에서 값을 세팅하도록
//        userServiceImpl.setUserDao(mockUserDao); // UserServiceImpl에 가짜객체를 DI해줌.
//
//        userServiceImpl.upgradeLevels();
//
//        // times() : method 호출 횟수 검증
//        // any() : 파라미터 내용 무시상태로 호출 횟수 확인
//        verify(mockUserDao, times(2)).update(any(User.class)); //mockUserDao의 update메소드가 2번 호출됬는지 확인
//        verify(mockUserDao, times(2)).update(any(User.class));
//        //users.get(1)에 해당하는 두번째사용자가 update메소드가 호출된적이 있는지?
//        verify(mockUserDao).update(users.get(1));
//        assertThat(users.get(1).getLevel(), is(Level.SILVER));
//        //users.get(1)에 해당하는 네번째사용자가 update메소드가 호출된적이 있는지?
//        verify(mockUserDao).update(users.get(3));
//        assertThat(users.get(3).getLevel(), is(Level.GOLD));
//    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User updateUser = userDao.get(user.getId());
        if(upgraded) {
            assertThat(updateUser.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(updateUser.getLevel(), is(user.getLevel()));
        }
    }

}
