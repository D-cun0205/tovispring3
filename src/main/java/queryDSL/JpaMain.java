package queryDSL;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");



//    /**
//     * 성능 & 관리 문제로 추천하지 않은 방법 (다대일 양방향 매핑을 권장)
//     * Member 엔티티는 Team에 대해서 알 수 없다
//     * 그래서 Member 엔티티를 저장할 때 Team Key를 알 수 없으므로
//     * Member1, 2 엔티티를 저장하고 Team 엔티티를 저장한 후
//     * update sql로 Member 테이블에 Team key(외래키)를 저장해 준다.
//     */
//    static void testSave(EntityManager em) {
//        Member member1 = new Member();
//        member1.setUsername("member1");
//
//        Member member2 = new Member();
//        member2.setUsername("member2");
//
//        Team team1 = new Team();
//        team1.setName("team1");
//        team1.getMembers().add(member1);
//        team1.getMembers().add(member2);
//
//        em.persist(member1);
//        em.persist(member2);
//        em.persist(team1);
//    }

    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();

        transaction.commit();

        emf.close();
    }
}
