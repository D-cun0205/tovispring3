package spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestAppContext {

    @Bean
    public UserService testUserService() {
        return new UserServiceImpl.TestUserService();
    }
}
