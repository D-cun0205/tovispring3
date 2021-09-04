import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import springvol2.Hello;
import springvol2.Printer;
import springvol2.StringPrinter;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class vol2Test {

    String basePath;

    @Before
    public void setUp() {
        basePath = StringUtils.cleanPath(ClassUtils.classPackageAsResourcePath(getClass())) + "/";
    }


    @Test
    public void test() {
        StaticApplicationContext ac = new StaticApplicationContext();
        ac.registerSingleton("hello1", Hello.class);

        Hello hello1 = ac.getBean("hello1", Hello.class);
        assertThat(hello1, is(notNullValue()));

        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        //빈으로 등록할때 프로퍼티를 설정하여 빈을 생성시 설정값을 호출할 수 있는 설정정보를 세팅
        helloDef.getPropertyValues().addPropertyValue("name", "Spring");
        ac.registerBeanDefinition("hello2", helloDef);

        Hello hello2 = ac.getBean("hello2", Hello.class);
        assertThat(hello2.sayHello(), is("Hello Spring"));

        assertThat(hello1, is(not(hello2)));
        assertThat(ac.getBeanFactory().getBeanDefinitionCount(), is(2));
    }

    @Test
    public void registerBeanWithDependency() {
        StaticApplicationContext ac = new StaticApplicationContext(); //셀프 컨텍스트생성

        //프로퍼티의 수정없이 등록할 경우 생성자로 루트빈데피니션으로 빈등록
        ac.registerBeanDefinition("printer", new RootBeanDefinition(StringPrinter.class)); // StringPrinter Bean 등록

        //프로퍼티의 세팅이 필요할 경우 생성자로 먼저 생성하고 프로퍼티 세팅 후
        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name", "Spring");
        helloDef.getPropertyValues().addPropertyValue("printer", new RuntimeBeanReference("printer"));

        //위와동일하게 세팅하며 BeanDefinition 객체를 등록
        ac.registerBeanDefinition("hello", helloDef);

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString(), is("Hello Spring"));
    }

    @Test
    public void genericApplicationContext() {
        GenericApplicationContext ac = new GenericApplicationContext();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(ac);
        reader.loadBeanDefinitions("/genericApplicationContext.xml");
        ac.refresh(); //여러개의 빈설정리더로 문제될수있으니 초기화작업을 진행한다.

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString(), is("Hello Spring"));
    }

    @Test
    public void childParentRelationship() {
        ApplicationContext parent = new GenericXmlApplicationContext(basePath + "parentContext.xml");
        GenericApplicationContext child = new GenericApplicationContext(parent);
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(child);
        reader.loadBeanDefinitions(basePath + "childContext.xml");
        child.refresh();

        Printer printer = child.getBean("printer", Printer.class);
        assertThat(printer, is(notNullValue()));

        Hello hello = child.getBean("hello", Hello.class);
        hello.print();

        assertThat(printer.toString(), is("Hello child"));
    }
}
