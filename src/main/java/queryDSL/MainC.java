package queryDSL;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.lang.reflect.Proxy;
import java.util.List;

public class MainC {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

    public void insUser(EntityManager em) {
        Team team = new Team();
        team.setName("팀A");
        em.persist(team);

        Member m = new Member();
        m.setUsername("회원1");
        m.setAge(20);
        m.setTeam(team);
        m.add(team);

        em.persist(m);
    }

    public void dslTest(EntityManager em) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        QMember qMember = new QMember("m");
        //아래 생성자는 기본인스턴스를 사용
        //QMember qMember = QMember.member;
        //  public static final QMember member = new QMember("member1");
        List<Member> members = query
                .selectFrom(qMember)
                .where(qMember.username.eq("회원1"))
                .orderBy(qMember.username.desc())
                .fetch();

        System.out.println(members.get(0).getAge() + members.get(0).getUsername() + members.get(0).getId() + members.get(0).getTeam());
    }

    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        MainC mainc = new MainC();
        transaction.begin();
        mainc.insUser(em);
        //mainc.dslTest(em);
        transaction.commit();
    }
}
