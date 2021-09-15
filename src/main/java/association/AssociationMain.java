package association;

import javax.persistence.*;
import java.util.List;

public class AssociationMain {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

    static void testSave(EntityManager em) {

    }

    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        testSave(em);
        transaction.commit();
        em.close();
        emf.close();
    }
}
