import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceImplTest extends UserServiceImpl {

    @Autowired
    UserService userService;

    @Autowired
    UserService testUserService;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    UserDao userDao;

    List<User> users;

    @Autowired
    ApplicationContext context;

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
    @Transactional
    public void getGetAllUpdate() throws Exception {
        userService.deleteAll();
        for(User user : users) userService.add(user);
        User getUser = userService.get(users.get(0).getId());
        List<User> getUsers = userService.getAll();
        userService.update(users.get(1));
    }

    @Test
    public void upgradeAllOrNothing() throws Exception {
        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        try {
            //testUserService.upgradeLevels();
            this.testUserService.upgradeLevels();
        } catch(UserServiceImpl.TestUserServiceException e) {}

        //checkLevelUpgraded(users.get(1), false);
    }

    @Test
    public void transactionSync() {
        userService.deleteAll();
        userService.add(users.get(0));
        userService.add(users.get(1));
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User updateUser = userDao.get(user.getId());
        if(upgraded) {
            assertThat(updateUser.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(updateUser.getLevel(), is(user.getLevel()));
        }
    }

}
