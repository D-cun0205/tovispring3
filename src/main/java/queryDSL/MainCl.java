package queryDSL;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class MainCl {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");

    /**
        프록시 : 객체는 객체 그래프로 객체들을 탐색하는데 데이터베이스 구조로 접근하면 원하는대로 탐색하기 어려워서 JPA는 프록시라는 기술을 사용
                프록시를 사용하면 연관 객체를 바로 조회하지 않고 실제로 사용되는 시점에 조회
                프록시 객체와 엔티티는 동일한 기준으로 생성되며 실제 객체의 참조(target)를 보관하므로 실제 객체와 동일하게 사용
                프록시 객체 초기화 : 실제 객체가 조회될 때 데이터베이스를 조회하여 실제 엔티티 객체를 생성

        영속성 전이와 고아 객체 : 연관된 객체를 함께 저장 또는 삭제 할 수 있는 영속성 전이와 고아 객체 제거라는 기능을 사용

        지연 로딩 : 조회 후 사용 시점에 조회 (하이버네이트에서 즉시 로딩을 지원하는 2가지 방법 프록시, 바이트코드 수정)
            실제 엔티티 객체 대신에 데이터베이스 조회를 지연할 수 있는 가짜 객체가 필요하며 이것을 프록시 객체라 칭함

        즉시 로딩 : 조회 시점에 조회
     */

    static void printUserAndTeam(String memberId, EntityManager em) {
        Member member = em.find(Member.class, memberId);
        Team team = member.getTeam();

    }

    public static void main(String[] args) {

        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();

        try {
            et.begin();

            et.commit();
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
