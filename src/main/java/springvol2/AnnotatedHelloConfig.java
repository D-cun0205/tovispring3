package springvol2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;

@Configuration
public class AnnotatedHelloConfig {

    @Bean
    public AnnotatedHello annotatedHello() {
        return new AnnotatedHello();
    }
}
