package queryDSL;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    /**
     * 동일성, 동등성 : 동일한 SQL을 실행하여 얻은 엔티티를 비교하면 true, JPA가 아닌 DAO로 호출시 new 생성으로 false
     * 트랜잭션을 지원하는 쓰기 지연 : 트랜잭션 이 커밋되기 전까지 SQL을 모아둔다
     * 변경 감지 : 엔티티의 변경사항을 데이터베이스에 자동 반영 (영속 상태의 엔티티에만 적용)
     *      ㄴ UPDATE SQL은 모든 필드를 가져온다.
     *      ㄴ 필요한 필드만 동적으로 생성하고 싶을 경우 엔티티에 @DynamicUpdate, @DynamicInsert 사용
     *      ㄴ 컬럼이 30개 이상의 경우 동적필드 생성 SQL이 더 빠르다
     *
     * 플러시(flush) : 영속성 컨텍스트의 변경 내용(스냅샷과 엔티티 비교)을 데이터베이스에 동기화 하는 작업
     * 스냅샷 : JPA는 엔티티 영속성 컨텍스트를 보관할 때 최초 상태를 복사해서 저장
     *
     * merge(병합) : save or update
     *      ㄴ merge 파라미터(Object)가 1차캐시나 DB에서 조회 가능한 경우 병합 없을 경우 새로 생성 후 병합
     *
     * */

    /** EntityManagerFactory는 어플리케이션 전체에서 딱 한번만 생성하고 공유해서 사용(생성비용이 큼) */
    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");

    static void setData(EntityManager em) {
        String id1 = "id1";
        Member member1 = new Member();
        member1.setId(id1);
        member1.setUsername("sang");
        member1.setAge(10);
        em.persist(member1);

        String id2 = "id2";
        Member member2 = new Member();
        member2.setId(id2);
        member2.setUsername("hun");
        member2.setAge(20);
        em.persist(member2);

        String id3 = "id3";
        Member member3 = new Member();
        member3.setId(id3);
        member3.setUsername("dicun");
        member3.setAge(30);
        em.persist(member3);
    }

    static void tableReset(EntityManager em) {
        Member member = em.find(Member.class, "id1");
        em.close();
    }

    public static void main(String[] args) {
        /** EntityManager가 JPA의 기능을 대부분 가지고있다, 데이터베이스 커넥션과 관계가 있으므로 스레드간의 공유 또는 재사용 금지 */
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        tableReset(em);
        transaction.commit();

        emf.close();
    }
}
