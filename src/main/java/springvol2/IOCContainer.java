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
}
