package jpabook.start;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class CloseJpaMain {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

    public static Member createMember(String id, String name) {
        EntityManager em1 = emf.createEntityManager();
        EntityTransaction transaction = em1.getTransaction();
        transaction.begin();

        Member member1 = new Member();
        member1.setId(id);
        member1.setUsername(name);
        member1.setAge(20);

        em1.persist(member1);
        transaction.commit();

        em1.close();

        return member1;
    }

    public static void mergeMember(Member member) {
        EntityManager em2 = emf.createEntityManager();
        EntityTransaction transaction2 = em2.getTransaction();

        transaction2.begin();
        //creatMember 메소드에서 생성된 영속 컨텍스트안에 엔티티 상태는 영속 상태 였다가 엔티티 매니저의 close함수로 인해 준영속 상태가 된다
        //mergeMember 메소드에서 엔티티 매니저의 merge 메소드를 호출하여 mergeMember는 영속 상태의 엔티티를 받게 되는데
        //엔티티 매니저의 merge 메소드에 member 엔티티를 줬다고 준영속 상태의 엔티티가 영속 상태의 엔티티가 되는게 아니고
        //새로운 영속 상태의 엔티티를 받게 되는것
        //파라미터로 넣은 member는 계속 준영속 상태의 엔티티로 남아있음, 결과적으로 mergeMember, member 엔티티는 서로 다른 인스턴스
        //Member mergeMember = em2.merge(member);
        //member 인스턴스는 준영속 상태로 남아있기떄문에 인자로 받은 member 인스턴스에 영속상태의 엔티티를 전달하여 재사용
        member = em2.merge(member);
        //준영속 상태가 아닌 비영속 상태의 엔티티도 영속상태로 만들 수 있다.
        //Member 비영속Member = new Member();
        //Member new영속Member = em2.merge(비영속Member);

        //병합 순서
        //파라미터 엔티티의 식별자로 영속성 1.컨텍스트 조회 > 2.DB 조회 > 3.새로운 엔티티 생성, 1~2번에서 발견되면 반환하여 영속 상태 관리

        transaction2.commit();

        System.out.println("member = " + member.getUsername());
        System.out.println("mergeMember = " + member.getUsername());

        //EntityManager.contains() = 영속성 컨텍스트가 파라미터로 넘어온 엔티티를 관리하는지의 여부, return : boolean
        System.out.println("em2 contains member = " + em2.contains(member));
        System.out.println("em2 contains mergeMember = " + em2.contains(member));

        em2.close();
    }

    public static void main(String[] args) {
        Member member = createMember("memberA", "회원1");
        member.setUsername("회원1명변경");
        mergeMember(member);
    }
}
