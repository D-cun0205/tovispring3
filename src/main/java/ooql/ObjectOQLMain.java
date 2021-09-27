package ooql;

import proxy.Team;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.lang.reflect.Type;
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

    //DISTINCT 사용법
    static void useCriteria(EntityManager em) {
        //JPQL : SELECT DISTINCT m.username, m.age FROM Member m 아래와 동일
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Member> m = cq.from(Member.class);
        cq.multiselect(m.get("username"), m.get("age")).distinct(true);

        TypedQuery<Object[]> query = em.createQuery(cq);
        List<Object[]> list = query.getResultList();
    }

    //NEW, construct()
    public void useConstruct(EntityManager em) {
        //JPQL : SELECT new jpabook.domain.MemberDTO(m.username, m.age) from Member m
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> cq = cb.createQuery(Member.class);
        Root<Member> m = cq.from(Member.class);
        cq.select(cb.construct(Member.class, m.get("username"), m.get("age")));

        TypedQuery<Member> query = em.createQuery(cq);
        List<Member> list = query.getResultList();
    }

    //Tuple
    public void useTuple(EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        //CriteriaQuery<Tuple> cqt = cb.createTupleQuery(); 아래와 동일
        CriteriaQuery<Tuple> cqt = cb.createQuery(Tuple.class);
        Root<Member> m = cqt.from(Member.class);
        cqt.multiselect(
                m.get("username").alias("username"),
                m.get("age").alias("age")
        );

        TypedQuery<Tuple> query = em.createQuery(cqt);
        List<Tuple> list = query.getResultList();
        for(Tuple tuple : list) {
            String username = tuple.get("username", String.class);
        }
    }

    //GROUP BY
    public void useGroupBy(EntityManager em) {
        //JPQL : SELECT m.team.name, MAX(m.age), MIN(m.age) FROM Member m GROUP BY m.team.name
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Member> m = cq.from(Member.class);

        //Expression maxAge = cb.max(m<Integer>.get("age"));
        Expression<Number> maxAge = cb.max(m.get("age"));
        Expression<Number> minAge = cb.min(m.get("age"));

        cq.multiselect(m.get("team").get("name"), maxAge, minAge);
        cq.groupBy(m.get("team").get("name"));
        //HAVING - 팀에서 가장 나이 어린사람 중 나이가 10을 초과
        //JPQL : HAVING min(m.age) > 10 와 동일
        cq.having(cb.gt(minAge, 10));
        //ORDER BY
        cq.orderBy(cb.desc(m.get("username")));

        TypedQuery<Object[]> query = em.createQuery(cq);
        List<Object[]> list = query.getResultList();
    }

    //JOIN
    public void useJoin(EntityManager em) {
        //JPQL : SELECT m, t FROM Member m INNER JOIN m.team t WHERE t.name = '팀A';
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Member> m = cq.from(Member.class);
        Join<Member, ooql.Team> t = m.join("team", JoinType.INNER); //내부조인
        m.fetch("team", JoinType.LEFT);
        cq.multiselect(m, t).where(cb.equal(t.get("name"),"팀A"));
    }

    //SUB QUERY
    public void useSubQuery(EntityManager em) {
        //JPQL : SELECT m FROM Member m WHERE m.age >= (SELECT AVG(m2.age) FROM Member m2)
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> cq = cb.createQuery(Member.class);
        Subquery<Double> subquery = cq.subquery(Double.class);
        Root<Member> m = subquery.from(Member.class);
        subquery.select(cb.avg(m.get("age")));

        Root<Member> mainM = cq.from(Member.class);
        cq.select(mainM).where(cb.ge(m.get("age"), subquery));
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
