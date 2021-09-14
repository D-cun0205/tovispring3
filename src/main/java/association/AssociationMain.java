package association;

import javax.persistence.*;
import java.util.List;

public class AssociationMain {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

    static void testSave(EntityManager em) {
        Team team1 = new Team("team1", "팀1");
        em.persist(team1);


        Member member1 = new Member("member1", "회원1");
        member1.setTeam(team1);
        em.persist(member1);

        Member member2 = new Member("member2", "회원2");
        member2.setTeam(team1);
        em.persist(member2);
    }

    static void testGet(EntityManager em) {
        Team team = new Team("team1", "팀1");
        em.persist(team);

        Member member1 = new Member("member1", "회원1");
        member1.setTeam(team);
        em.persist(member1);

        Member member3 = em.find(Member.class, "member1");
        Team team3 = member3.getTeam();
        System.out.println(team3.getName());
    }

    static void queryLogicJoin(EntityManager em) {
        String jpql = "SELECT m FROM Member m JOIN m.team t WHERE t.name = :teamName";
        List<Member> resultList = em.createQuery(jpql, Member.class).setParameter("teamName", "팀1").getResultList();
        for(Member member : resultList) {
            System.out.println(member.getUsername());
        }
    }

    static void updateRelation(EntityManager em) {
        Team team = new Team("team2", "팀2");
        em.persist(team);

        Member member = em.find(Member.class, "member1");
        member.setTeam(team);
    }

    static void deleteRelation(EntityManager em) {
        Member member1 = em.find(Member.class, "member2");
        member1.setTeam(null);

        Team team = em.find(Team.class, "team2");
        em.remove(team);
    }

    static void biDirection(EntityManager em) {
        Team team = em.find(Team.class, "team1");
        List<Member> members = team.getMembers();

        for(Member member : members) {
            System.out.println(member.getUsername());
        }

    }

    static void testSaveNonOwner(EntityManager em) {
        Member member1 = new Member("member1", "회원1");
        em.persist(member1);

        Member member2 = new Member("member2", "회원2");
        em.persist(member2);

        Team team1 = new Team("team1", "팀1");
        team1.getMembers().add(member1);
        team1.getMembers().add(member2);

        em.persist(team1);

    }

    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        //testSave(em);
        //testGet(em);
        //queryLogicJoin(em);
        //updateRelation(em);
        //deleteRelation(em);
        //biDirection(em);
        testSaveNonOwner(em);
        transaction.commit();
        em.close();
        emf.close();
    }
}
