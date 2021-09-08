package spring;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "spring")
@Import(SqlServiceContext.class)
@PropertySource("classpath:database.properties")
public class AppContext {

    @Value("${db.driverClass}")
    private String driverClassName;
    @Value("${db.url}")
    private String url;
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;

//    @Bean
//    public DataSource dataSource() {
//        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
//        dataSource.setDriverClass(org.postgresql.Driver.class); //org.postgresql.Driver
//        dataSource.setPassword("1234");
//        /*회사*/
//        dataSource.setUrl("jdbc:postgresql://localhost:5432/springbootdb");
//        dataSource.setUsername("postgres");
//        /*집*/
////        dataSource.setUrl("jdbc:postgresql://localhost:5432/springtest");
////        dataSource.setUsername("dcun");
//        return dataSource;
//    }

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        //hikariConfig.setMaxLifetime(); 커넥션 최대 수명시간, 사용중인 커넥션 해당안됨, 커넥션 별 시간설정 default : 1800000(30분)
        //hikariConfig.getMinimumIdle(); = default : maximumPoolSize으로 Hikari에서는 설정을 권장하지않음
        hikariConfig.setMaximumPoolSize(10); // default : 10
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setConnectionTimeout(30000); // default 30초

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        tm.setDataSource(dataSource());
        return tm;
    }

}
