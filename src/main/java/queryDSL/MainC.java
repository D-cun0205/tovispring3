package queryDSL;

import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.lang.reflect.Proxy;
import java.util.List;

public class MainC {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

    public void insUser(EntityManager em) {
        Team team = new Team("팀A");
        Member m = new Member("회원1", 21, team);
        em.persist(m);
    }

    public void dslTest(EntityManager em) {
        JPAQuery query = new JPAQuery(em);
        QMember qMember = new QMember("m");
        List<Member> members =
                (List<Member>) query.from(qMember)
                        .where(qMember.username.eq("회원1"))
                        .orderBy(qMember.username.desc());
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
