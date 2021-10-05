package ooql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.*;
import java.util.List;

public class ObjectOQLMain {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

    // ge : >=, gt : >, le : <=, lt : <

    //서브쿼리
    static void subQuery(EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> mainQuery = cb.createQuery(Member.class);

        Subquery<Double> subQuery = mainQuery.subquery(Double.class);
        Root<Member> m2 = subQuery.from(Member.class);
        subQuery.select(cb.avg(m2.<Integer>get("age")));

        Root<Member> m = mainQuery.from(Member.class);

        //select * from Member m where  m.age >= (subquery)
        //서브쿼리 사용하고 싶을 경우 Root, Join을 통한 별칭으로 접근
        mainQuery.select(m).where(cb.ge(m.<Integer>get("age"), subQuery));
    }

    //상호 관계
    static void mutualRelationship(EntityManager em) {
        //exists : 값이 존재하는지
        //select m from Member m where exists (select t from m.team t where t.name = '팀A')
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> mainQuery = cb.createQuery(Member.class);
        Root<Member> m = mainQuery.from(Member.class);

        Subquery<Team> subQuery = mainQuery.subquery(Team.class);
        //서브쿼리에서 메인쿼리 정보를 사용하고 싶은경우 메인쿼리에서 적용한 별칭을 이용하여 적용
        //메인쿼리의 별칭을 가져옴
        Root<Member> subM = subQuery.correlate(m);

        Join<Member,Team> t = subM.join("team");
        subQuery.select(t).where(cb.equal(t.get("name"), "팀A"));

        mainQuery.select(m).where(cb.exists(subQuery));
        List<Member> resultList = em.createQuery(mainQuery).getResultList();
    }

    //in절
    static void inQuery(EntityManager em) {
        //select m from Member m where m.username in ('회원1', '회원2')
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> query = cb.createQuery(Member.class);
        Root<Member> m = query.from(Member.class);
        query.select(m).where(cb.in(m.get("username")).value("회원1").value("회원2"));
    }

    //case절
    static void caseQuery(EntityManager em) {
        //select
        //  m.username
        //  , case when m.age >= 60 then 600
        //          when m.age <= 15 then 500
        //          else 1000
        //from Member m
        //
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> query = cb.createQuery(Member.class);
        Root<Member> m = query.from(Member.class);

        query.multiselect(m.get("username"), cb.selectCase()
                .when(cb.ge(m.<Integer>get("age"), 60), 600)
                .when(cb.le(m.<Integer>get("age"), 15), 500)
                .otherwise(1000)
        );
    }

    //파라미터정의
    static void paramManager(EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> query = cb.createQuery(Member.class);
        Root<Member> m = query.from(Member.class);

        //cb.parameter 파라미터정의, setParameter("", "") 파라미터바인딩
        query.select(m).where(cb.equal(m.get("username"), cb.parameter(String.class, "usernameParam")));
        List<Member> resultList = em.createQuery(query).setParameter("usernameParam", "회원1").getResultList();
    }

    static void nativeFun(EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Member> m = query.from(Member.class);
        Expression<Long> function = cb.function("SUM", Long.class, m.get("age"));
        query.select(function);
    }

    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();

        transaction.commit();
        em.close();
        emf.close();
    }

}
