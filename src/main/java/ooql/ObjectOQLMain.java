package ooql;

import proxy.Team;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

        public class ObjectOQLMain {

            static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

            static void getUser(EntityManager em) {
                //criteria 사용준비
                CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> query = cb.createQuery(Member.class);

        //루트클래스(조회를 시작할 클래스)
        Root<Member> m = query.from(Member.class);

        String name = "클라이언트 요청에 의해 넘어온 유저 명";
        CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), name));
        List<Member> resultList = em.createQuery(cq).getResultList();

//        String jpql = "select o.member.team from order o where o.product.name = 'productA' and a.address.city = 'JINJU'";
//        String jpql = "select m from Member as m where m.username = 'kim'";
//        List<Member> resultList = em.createQuery(jpql, Member.class).getResultList();
    }

    //엔티티 직접 사용 (기본 키)
    static void getInfo(EntityManager em) {
        Member member = new Member();
        //작성한 JPQL은 파라미터로 엔티티를 받는다
        String sqlString = "select m from Member m where m = :member";
        //실제 SQL에서는 Member 엔티티의 id값으로 조회
        //select m.* from Member m where m.id = ?
        List<Member> members = em.createQuery(sqlString).setParameter("member", member).getResultList();
        for(Member member1 : members) {
            System.out.println(member1.getUsername());
        }
    }

    //엔티티 직접 사용 (외래 키)
    static void getInfoForeignKey(EntityManager em) {
        Team team = em.find(Team.class, 1L);
        String qlString = "select m from Member m where m.team = :team";
        List resultList = em.createQuery(qlString).setParameter("team", team).getResultList();
    }

    static void tte(EntityManager em) {
        List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class).setParameter("username", "회원1").getResultList();
    }

    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        getUser(em);
        transaction.commit();
        em.close();
        emf.close();
    }
}
