import org.junit.Test;

import java.lang.reflect.Proxy;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class HelloTest {

    @Test
    public void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("Toby"), is("Hello Toby"));
        assertThat(hello.sayHi("Toby"), is("Hi Toby"));
        assertThat(hello.sayThankYou("Toby"), is("Thank you Toby"));
    }

    @Test
    public void proxyTest() {
        Hello proxieHello = (Hello) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{Hello.class}, new UppercaseHandler(new HelloTarget()));
        assertThat(proxieHello.sayHello("Toby"), is("HELLO TOBY"));
        assertThat(proxieHello.sayHi("Toby"), is("HI TOBY"));
        assertThat(proxieHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
    }
}
