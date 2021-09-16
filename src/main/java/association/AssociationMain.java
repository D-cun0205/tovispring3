package association;

import javax.persistence.*;
import java.util.List;

public class AssociationMain {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

    static void save(EntityManager em) {
        Member member1 = new Member();
        member1.setId("member1");
        member1.setUsername("회원1");
        em.persist(member1);

        Product product1 = new Product();
        product1.setId("product1");
        product1.setName("상품1");
        em.persist(product1);

        Order order = new Order();
        order.setMember(member1);
        order.setProduct(product1);
        order.setOrderAmount(2);
        em.persist(order);
    }

    static void find(EntityManager em) {
        Long orderId = 1L;
        Order order = em.find(Order.class, orderId);

        Member member = order.getMember();
        Product product = order.getProduct();

        System.out.println("member : " + member.getUsername());
        System.out.println("product : " + product.getName());
        System.out.println("orderAmount : " + order.getOrderAmount());
    }

    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        save(em);
        transaction.commit();
        em.close();
        emf.close();
    }
}
