package springvol2;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

public class IOCContainer {

    //POJO클래스?
    //어플리케이션 컨텍스트는 BeanDefinition(인터페이스)으로 만들어진 메타정보를 담은 오브젝트를 사용해 IOC와 DI작업을 수행.
    //BeanDefinition에 담겨있는 설정정보를 사용할수있도록 오브젝트로 변환해주는 BeanDefinitionReader가 있음.
    //빈 메타정보 : 빈 이름, 클래스 이름, 스코프, 프로퍼티, 생성자 파라미터 등등..
    //IOC컨테이너 -> 빈설정 메타정보(컨테이너가 확인) -> 빈 생성 -> 생성된 빈들을 프로퍼티 및 생성자로 의존성 주입
    //결론 : 스프링 어플리케이션이란 POJO클래스, 설정 메타정보를 이용해 IOC컨테이너가 만들어주는 오브젝트의 조합
    //** IOC컨테이너가 관리하는 빈은 오브젝트단위, 보통은 클래스당 하나의 오브젝트를 생성하여 사용하긴함 그러나 여러개의 오브젝트를 만들어 사용할때도있음
    StaticApplicationContext ac = new StaticApplicationContext(); //IOC컨테이너를 사용하는 어플리케이션 컨텍스트

    //제네릭어플리케이션컨텍스트는 위에 컨텍스트와 다르게 XML파일과 같은 외부의 리소스에 있는 빈 설정 메타정보를 리더를 통해 읽어서
    //메타정보로 전환하여 사용한다.
    //BeanDefinitionReader : 빈 설정 정보를 변환
    //XmlBeanDefinitionReader : XML로 작성된 빈설정 정보를 읽어서 컨테이너에 전달
        //해당 Reader로 GenericApplicationContext가 이용하도록 할 수 있다.
    GenericApplicationContext gac;

    //XmlBeanDefinitionReader을 내장하고있는 GenericApplicationContext가 아래의 구현클래스
    //해당 클래스에서 XML을 통해 생성한 객체는 refresh()를 포함하고있다.
    GenericXmlApplicationContext gxac;

    //웹어플리케이션컨텍스트 동작 구조
    //클라이언트 요청 -> 서블릿 컨테이너(웹어플리케이션) -> WebApplicationContext 생성 -> 설정메타, POJO클래스 확인하여 클래스생성 및 DI 적용
    //DispatcherServlet : 어플리케이션컨텍스트 생성, 설정메타정보에 대한 초기화, 각 요청에 맞는 빈을 찾아서 이를 실행해줌
    //WebApplicationCotext

    //스프링 웹 기능을 지원하는 프론트 컨트롤러 서블릿 : DispatcherServlet
    //DispatcherServlet은 web.xml에서 등록해서 사용하는 서블릿
    //DispatcherServlet은 웹어플리케이션 레벨에 등록되며 등록 될 시 루트어플리케이션컨텍스트를 찾아서 루트를 부모컨텍스트로 사용한다.
    //web.xml파일에 DispatcherServlet이 servlet태그안에 선언되어있음.
    //DispatcherServlet을 사용할 때 네임스페이스를 주의해서 사용하는이유는 설정한 네임에 해당하는 독립적인 서블릿을 가지게 된다.
    //DispatcherServlet이 생성하는 applicationContext는 디스패처서블릿의 네임으로 인해 구분되어진다.
    //웹어플리케이션이 기동되면 web.xml의 서블릿 설정값을 통해 WEB_INF 하위에 서블릿에 설정한 네임 + '-servlet.xml'로 파일을 생성한다.

    //<context-annotation-config : @Resource와 같은 애노테이션 의존관계정보를 읽어서 메타정보를 추가해주는 기능을 가진 빈 후처리기를 등록
    //<context-component-scan : 빈 스캐닝을 통한 빈 등록 방법 지정
    //AnnotationConfigApplicationContext : 빈 스캐너, 애노테이션 의존관계 정보를 읽는 후처리기 두가지를 내장한 어플리케이션컨텍스트 사용
}
