package springvol2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class HelloConfig {

    private Printer printer;
    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    @Bean
    public Hello hello() {
        Hello hello = new Hello();
        hello.setName("Spring");
        //hello.setPrinter(this.printer);
        return hello;
    }

    @Bean
    public Hello hello2() {
        Hello hello = new Hello();
        hello.setName("Spring2");
        //hello.setPrinter(this.printer);
        return hello;
    }

    @Bean
    private Printer printer() { return new StringPrinter(); }
}
