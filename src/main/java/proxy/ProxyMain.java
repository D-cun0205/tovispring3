package proxy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class ProxyMain {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

    //회원, 팀 정보 출력
    static void printUserAndTeam(Long memberId, EntityManager em) {
        Member member = em.find(Member.class, memberId);
        Team team = member.getTeam();
        System.out.println("회원이름 : " + member.getUsername());
        System.out.println("팀이름 : " + team.getName());

    }

    //회원 정보 출력
    static void printUser(Long memberId, EntityManager em) {
        Member member = em.find(Member.class, memberId);
        //조회된 엔티티를 실제 사용하기 직전까지 미루다가 사용시 조회하는 메소드 getReference
        //데이터베이스를 조회하지않고 프록시 객체를 반환
        //프록시클래스는 실제클래스를 상속받으므로 형태가 같다
        //프록시객체는 실제 객체에 대한 참조를 보관하며 프록시객체 호출 시 프록시객체는 실제 객체를 호출한다
        //Member proxyMember = em.getReference(Member.class, memberId);
        System.out.println("회원이름 : " + member.getUsername());
    }

    /*  프록시와 식별자
    *   엔티티를 프록시로 조회할 때 식별자(PK) 값을 파라미터로 전달하며 프록시객체는 식별자값을 보관
    *   프록시는 식별자의 값을 보관하고 있으므로 식별자에 대한 get Method가 호출되어도
    *   프록시에 대한 초기화를 진행하지 않는다
    *   단 AccessType 이 PROPERTY 일 경우에만 FIELD로 설정되어 있으면 초기화 진행한다
    *   이유는 getId() 메소드가 id만 조회하는 메소드인지 다른 필드까지 활용해서 어떤작업을 이루어지게 하는지 알지 못하기 때문
    * */

    /*  즉시로딩과 지연로딩
    *   프록시 객체는 주로 연관된 엔티티를 지연로딩시 사용
    *   FetchType.EAGER, FetchType.LAZY를 연관관계설정 fetch속성에 사용할 수 있다
    *   nullable 을 설정하지 않은상태에서 내부조인이 실행되면 데이터가 조회되지 않은 문제점을 막기위해서
    *   외부조인을 강제하는데 성능적으로 보았을 때 내부조인이 성능최적화에 유리하며 @JoinColumn 속성으로
    *   nullable = false or Optional : false로 하면 내부조인을 강제하여 성능을 최적화 할 수 있다
    * */

    public static void main(String[] args) {
        //지연로딩 : Team team = member.getTeam(); 이부분에서 데이터베이스의 작업이 발생하지 않고
        //          team.getName() 처럼 팀에있는 데이터를 호출할 때 실제 작업이 발생
        //지연로딩 기능이 사용되려면 실제 엔티티 객체 대신 데이터베이스 조회를 지연 할 가짜 객체가 필요하며 이것을 프록시 객체라고한다
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();

        et.begin();
        printUserAndTeam(1L, em);
        et.commit();
        em.close();
        emf.close();
    }
}
