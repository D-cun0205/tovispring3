package valuetype;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class ValueTypeMain {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

    static void useValueType(EntityManager em) {
        Member member = new Member();
    }

    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();

        et.begin();

        et.commit();
        em.close();
        emf.close();
    }
}
