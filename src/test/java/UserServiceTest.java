import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    PlatformTransactionManager transactionManager;

    List<User> users;

    @Before
    public void setUp() {
        users = Arrays.asList(
                new User("bora", "보라돌이", "p1", Level.BASIC, userService.MIN_LOGCOUNT_FOR_SILVER-1, 0),
                new User("ddu", "뚜비", "p2", Level.BASIC, userService.MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("nana", "나나", "p3", Level.SILVER, userService.MIN_LOGCOUNT_FOR_SILVER, userService.MIN_RECOMMEND_FOR_GOLD-1),
                new User("bbo", "뽀오", "p4", Level.SILVER, userService.MIN_LOGCOUNT_FOR_SILVER, userService.MIN_RECOMMEND_FOR_GOLD),
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
    public void upgradeLevels() throws Exception {
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
        UserService testUserService = new UserService.TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setTransactionManager(transactionManager);
        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        try {
            testUserService.upgradeLevels();
//            fail("TestUserServiceException expeted");
        } catch(UserService.TestUserServiceException e) {}

        //checkLevelUpgraded(users.get(1), false);
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
