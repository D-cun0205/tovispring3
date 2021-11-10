package queryDSL;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");

    /**
        상속 관계 매핑 : 객체의 상속 관계를 데이터베이스에 매핑
        @MappedSuperclass : 여러 엔티티에서 공통(등록일, 수정일)으로 사용하는 매핑 정보만 상속받는 기능
        복합 키와 식별 관계 매핑 : DB에 식별자가 하나 이상일 때 매핑방법, 식별 관계, 비식별 관계
        조인 테이블 : 외래 키를 사용하지 않고 연관관계를 연결하는 테이블을 매핑 하는 방법
        엔티티 하나에 여러 테이블 매핑 : 문장 그대로 해석
     */

    /**
        상속 관계 매핑 : 객체의 상속 과 데이터베이스의 슈퍼타입, 서브타입 을 매핑 하는 방법
            3가지 방법 : 조인전략, 단일 테이블 전략, 구현 클래스마다 테이블 전략

        조인전략 : 상속 클래스, 상속 대상 클래스를 모두 생성하며 테이블도 동일하게 생성된다
                    상속 클래스 생성하여 @Inheritance(strategy = InheritanceType.JOINED) 설정
                    @DiscriminatorColumn(name = "타입구분명" << 디폴트 (name = "DTYPE")) 설정하여 자식테이블을 구분
                    상속받을 클래스 생성 @DiscriminatorValue("타입구분값") 입력 이 외에 다른 설정 없음

        단일 테이블 전략 : 상속 클래스, 상속 대상 클래스를 모두 생성하며 테이블은 단일로 생성하여 모든 값을 넣는다
                    상속 클래스 생성하여 @Inheritance(strategy = InheritanceType.SINGLE_TALBE) 설정
                    나머지 설정은 조인전략과 동일


        구현 클래스마다 테이블 전략 : 상속 클래스, 상속 대상 클래스를 모두 생성하며 테이블은 자식 테이블만 생성한다 (** 추천안함 **)
                    상속 클래스 생성하여 @Inheritance(strategy = InheritanceType.SINGLE_TALBE) 설정
                    자식테이블에서 모든 값을 가지고 있으므로 @DiscriminatorColumn, @DiscriminatorValue는 의미가 없기 때문에 설정을 제거한다
     */

    /**
        @MappedSuperclass : 부모 클래스를 테이블과 매핑하지 않고 공통으로 사용하는 매핑 필드만 자식 클래스에게 전달
                    위 어노테이션을 붙이면 엔티티가 아니므로 EntityManager를 사용할 수 없다.
                    위 클래스는 직접 사용할 일이 없으므로 추상클래스로 만드는 것을 권장
     */

    /**
        복합 키와 식별 관계(비식별 관계) 매핑 :
            식별 관계 : 부모 키를 받아서 기본 키 + 외래 키 를 사용하는 관계
            비식별 관계 : 부모 키를 받아서 외래 키로만 사용
                외래 키에 NULL 허용 유무로 필수적 비식별 관계(NOT NULL, 연관관계 필수), 선택적 비식별 관계로 구분(NULL, 연관관계 선택)
     */

    static void insert(EntityManager em) {
        Member member = new Member();
        member.setName("상훈");
        member.setEmail("sanghun@mail.com");
        em.persist(member);
    }

    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        insert(em);
        transaction.commit();

        emf.close();
    }
}
