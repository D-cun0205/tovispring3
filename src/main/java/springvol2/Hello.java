package springvol2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Hello {

    String name;

    @Resource(name = "printer")
    Printer printer;

    public void setPrinter(StringPrinter printer) {
        this.printer = printer;
    }

    public String sayHello() {
        return "Hello " +  name;
    }

    public void print() {
        this.printer.print(sayHello());
    }

    public void setName(String name) {
        this.name = name;
    }
}
