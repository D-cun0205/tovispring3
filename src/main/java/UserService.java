import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

public class UserService {

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    private PlatformTransactionManager transactionManager;
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevels() throws Exception {
        //JTATransactionManager, HibernateTransactionManager Dao에 적용한하이버네이트 JTA
        //PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource); //JDBC 트랜잭션 추상 오브젝트 생성
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        //트랜잭션동기화관리 클래스에서 초기화진행
        //TransactionSynchronizationManager.initSynchronization();
        //sql datasource가 아닌 spring datasourceutils로 커넥션을 받아서 트랜잭션 동기화에 사용하도록 스프링이 저장소에 바인딩
        //Connection c = DataSourceUtils.getConnection(dataSource);
        //c.setAutoCommit(false);

        try {
            List<User> users = userDao.getAll();
            for(User user : users) {
                if(canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            this.transactionManager.commit(status);
        } catch(Exception e) {
            //c.rollback();
            this.transactionManager.rollback(status);
            throw e;
        } finally {
            //DataSourceUtils.releaseConnection(c, dataSource); //spring datasourceutils로 커넥션 닫음
            //동기화 작업 종료 및 정리
            //TransactionSynchronizationManager.unbindResource(this.dataSource);
            //TransactionSynchronizationManager.clearSynchronization();
        }
    }

    public void add(User user) {
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }

    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level : " + currentLevel);
        }
    }

    private void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }

    static class TestUserService extends UserService {
        private String id;
        TestUserService(String id) {
            this.id = id;
        }

        protected void upgradeLevel(User user) {
            if(user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {}
}
