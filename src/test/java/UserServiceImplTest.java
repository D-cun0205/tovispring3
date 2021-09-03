import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spring.*;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppContext.class, TestAppContext.class })
public class UserServiceImplTest extends UserServiceImpl {

    private Logger logger = LogManager.getLogger(UserServiceImplTest.class);

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

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
    public void realAdd() {
        userService.deleteAll();
        assertThat(userService.getCount(), is(0));
    }

    @Test
    public void add() {
        userService.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userService.get(userWithLevel.getId());
        User userWithoutLevelRead = userService.get(userWithoutLevel.getId());

        assertThat(userWithLevel.getLevel(), CoreMatchers.is(userWithLevelRead.getLevel()));
        assertThat(userWithoutLevel.getLevel(), CoreMatchers.is(userWithoutLevelRead.getLevel()));
    }

    @Test
    public void upgradeLevels() {
        userService.deleteAll();
        for(User user : users) userService.add(user);

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

    @Test
    public void transactionSync() {
        userService.deleteAll();
        userService.add(users.get(0));
        userService.add(users.get(1));
    }

    @Test
    public void loggingAnyTest() {
        logger.debug("[debug] log!");
        logger.info("[info] log!");
        logger.warn("[warn] log!");
        logger.error("[error] log!");
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User updateUser = userService.get(user.getId());
        if(upgraded) {
            assertThat(updateUser.getLevel(), CoreMatchers.is(user.getLevel().nextLevel()));
        } else {
            assertThat(updateUser.getLevel(), CoreMatchers.is(user.getLevel()));
        }
    }

}
