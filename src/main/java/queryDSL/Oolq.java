package queryDSL;

import javax.persistence.*;
import java.util.List;

public class Oolq {

    /**
     *  파라미터 바인딩 (이름 기준 파라미터를 권장)
     *      이름 기준 파라미터 : 파라미터를 이름으로 구분하며 이름앞에 : 가 붙는다 ex) :username, :age
     *      위치 기준 파라미터 : 파라미터를 위치로 구분하며 ? 뒤에 숫자를 붙인다 ex) ?1, ?2
     *      파라미터 바인딩 방식을 사용하지않고 직접ㅈ JPQL을 만들 경우 SQL 인젝션 공격을 당할 수 있으므로 주의
     *
     *  프로젝션 (조회의 대상)
     *      SELECT [프로젝션] FROM 테이블
     *      엔티티 프로젝션 : SELECT m FROM Member m
     *      임베디드 타입 프로젝션 : SELECT o.address FROM Order o,  틀린 예시) SELECT a FROM Address a << 임베디드 타입을 프롬에 걸 수 없음.
     *      스칼라 타입 프로젝션 : 기본 데이터 타입
     */

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");

    static void setDB(EntityManager em) {
        Member member = em.find(Member.class, 1L);
        System.out.println(member);
    }

    static void getDB(EntityManager em) {
        //타입이 확실할 때
        TypedQuery<Member> typedQuery = em.createQuery("select m from Member m", Member.class);
        List<Member> typedResultList = typedQuery.getResultList();
        for(Member member : typedResultList) {
            System.out.println(member);
        }
        //타입이 확실하지 않을 때
        Query query = em.createQuery("select m.username, m.age from Member m");
        List resultList = query.getResultList();
        for(Object o : resultList) {
            Object[] result = (Object[]) o;
            System.out.println(result[0]);
        }
    }

    public static void main(String[] args) {

        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();

        try {
            et.begin();
            setDB(em);
            et.commit();
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
