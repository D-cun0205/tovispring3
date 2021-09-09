package jpabook.start;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class CloseJpaMain {

    public static void closeEntityManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();

        Member memberA = em.find(Member.class, "memberA");
        Member memberB = em.find(Member.class, "memberB");

        transaction.commit();

        em.close();

    }

    public static void main(String[] args) {
        closeEntityManager();
    }
}
