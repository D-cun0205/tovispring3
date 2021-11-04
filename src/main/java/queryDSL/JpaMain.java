package queryDSL;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");

    static void testSave(EntityManager em) {
        Team team1 = new Team("team1", "팀1");
        em.persist(team1);

        Member member1 = new Member("member1", "회원1");
        //양방향 연관관계 설정 1
        member1.setTeam(team1); //연관관계 편의 메소드로 변경
        em.persist(member1);

        Member member2 = new Member("member2", "회원2");
        //양방향 연관관계 설정2
        member2.setTeam(team1); //연관관계 편의 메소드로 변경
        em.persist(member2);
    }

    static void select(EntityManager em) {
        Member member = em.find(Member.class,  "member1");
        Team team = member.getTeam(); //객체 그래프 탐색
        System.out.println(team.getName());
    }

    static void jpqlSelect(EntityManager em) {
        /**
            객체(엔티티) Member의 Team(필드)을 호출하여 조인한다
            :teamName : 파라미터 바인딩 문법
         */
        String jpql = "select m from Member m join m.team t where t.name = :teamName";
        List<Member> resultList = em.createQuery(jpql, Member.class)
                .setParameter("teamName","팀1")
                .getResultList();

        for (Member member : resultList) {
            System.out.println("유저명 : " + member.getUsername());
        }
    }

    static void update(EntityManager em) {
        Team team2 = new Team("team2", "팀2");
        em.persist(team2);

        Member member = em.find(Member.class, "member1");
        member.setTeam(team2);
    }

    static void deleteRelation(EntityManager em) {
        Member member = em.find(Member.class, "member1");
        member.setTeam(null);

        /**
            엔티티를 삭제하기 전에 연관관계에 사용된 엔티티가 있으면 먼저 제거 후 삭제를 진행
            강제로 삭제 진행 시 외래 키 제약조건으로 에러발생
         */
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

    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        testSave(em);
        transaction.commit();

        emf.close();
    }
}
