package jpabook.start;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    private static void logic(EntityManager em){
        String id = "id1";
        Member member = new Member();
        member.setId(id);
        member.setUsername("상훈");
        member.setAge(10);

        //등록
        em.persist(member);

        //수정
        member.setAge(20);

        //1건 조회
        Member findMember = em.find(Member.class, id);
        System.out.println("findMember=" + findMember.getUsername() + ", age=" + findMember.getAge());

        //목록 조회
        //JPQL : 검색 쿼리 같은 경우 테이블이 아닌 엔티티 객체를 대상으로 검색을 진행해야 하는데
        //       이런 경우 검색 조건이 포함된 SQL을 사용해야 하며 이 문제를 해결하기 위해 JPQL을 제공
        //       SQL과 문법이 유사하여 SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN 등을 사용할 수 있다.
        //차이점 : JPQL은 엔티티 객체를 기준하여 쿼리, SQL은 데이터베이스 테이블을 기준하여 쿼리
        List<Member> members = em.createQuery("SELECT m FROM Member m", Member.class).getResultList();
        System.out.println("members.size = " + members.size());

        //삭제
        em.remove(member);
    }

    public static void main(String[] args) {

        // *** 사용이 끝나면 반드시 자원을 역순으로 반납 ***

        //엔티티 매니저 팩토리 - 생성
        //엔티티매니저팩토리는 JPA 동작 객체와 JPA 구현체에 따라 DBCP도 생성하여 비용이 크기때문에 한번만 생성하고 공유하여 사용
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        //엔티티 매니저 - 생성
        //JPA의 기능 대부분을 엔티티 매니저에서 제공, CRUD 기능
        //엔티티 매니저는 내부에 커넥션을 유지하며 DB와 통신, 가상의 데이터베이스
        //커넥션을 보유하기 때문에 스레드간의 공유 및 재사용 시 문제가 발생할 수 있음.
        EntityManager em = emf.createEntityManager();
        //트랜잭션 획득
        EntityTransaction tx = em.getTransaction();

        try {

            // *** JPA를 사용할 경우 데이터의 변경은 트랜잭션 안에서 변경되어야 하며 트랜잭션 밖에서 진행 시 예외 발생 ***

            tx.begin(); //트랜잭션 - 시작
            logic(em); //비지니스 로직 실행
            tx.commit(); //트랜잭션 - 커밋
        } catch(Exception e) {
            tx.rollback(); //트랜잭션 - 롤백
        } finally {
            em.close(); //엔티티매니저 종료
        }
        emf.close(); //엔티티매니저팩토리 종료
    }
}
