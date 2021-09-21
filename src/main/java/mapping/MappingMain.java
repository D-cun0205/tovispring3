package mapping;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class MappingMain {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

    static void save(EntityManager em) {
        IdclassParent ip = new IdclassParent();
        ip.setId1("myId1");
        ip.setId2("myId2");
        ip.setName("parentName");
        em.persist(ip);
    }

    static void find(EntityManager em) {
        IdclassParentId ipi = new IdclassParentId("myId1", "myId2");
        IdclassParent ip = em.find(IdclassParent.class, ipi);

        System.out.println(ip.getName());
    }

    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();

        et.begin();
        find(em);
        et.commit();
        em.close();
        emf.close();
    }
}
