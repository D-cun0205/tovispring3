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
     *
     *  집합 함수 : DISTINCT를 COUNT에서 사용할 때 임베디드 타입은 지원하지 않음음
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

    static void testingQuery(EntityManager em) {
        //GROUP BY
        Query query = em.createQuery(
                "SELECT t.name, COUNT(m.age), SUM(m.age), AVG(m.age), MAX(m.age), MIN(m.age) FROM Member m LEFT JOIN m.team t GROUP BY t.name"
        );

        //GROUP BY + HAVING
        Query query2 = em.createQuery(
                "SELECT t.name, AVG(m.age), SUM(m.age) FROM Member m LEFT JOIN m.team t GROUP BY t.name having SUM(m.age) >= 10"
        );

        //ORDER BY ASC, DESC
        Query query3 = em.createQuery(
                "SELECT m.age, m.username FROM Member m ORDER BY m.username ASC, m.age DESC"
        );
    }

    static void innerJoin(EntityManager em) {
        //INNER JOIN
        String teamName = "팀A";
        String query = "SELECT m FROM Member m INNER JOIN m.team t WHERE t.name = :teamName";
        List<Member> members = em.createQuery(query, Member.class).setParameter("teamName", teamName).getResultList();

        List<Object[]> result = em.createQuery(query).getResultList();
        for (Object[] row : result) {
            Member member = (Member) row[0];
            Team team = (Team) row[1];
        }

        //OUTER JOIN : INNER JOIN과 사용법이 거의 같으며 LEFT, RIGHT를 입력하면 OUTER를 제외하여도 동일

        //COLLECTION JOIN : JPA의 조인방식은 엔티티로 접근하므로 엔티티 연관필드를 사용하여 컬렉션 조인이 가능

        //THETA JOIN : WHERE 절을 사용하는 방법으로 내부 조인만 지원하며 서로 연관성이 하나도 없는 엔티티도 조인이 가능하다
        Query thetaQuery = em.createQuery(
                "SELECT count(m) FROM Member m, Team t WHERE m.username = t.name"
        );

        //JOIN ON 절 : 조인할 때 조인의 대상을 필터링한 후 조인한다
        Query joinOnQuery = em.createQuery(
          "SELECT m, t FROM Member m LEFT JOIN m.team t on t.name = 'A'"
        );

        //FETCH JOIN : SQL의 조인 종류는 아니며 JPQL의 성능 최적화를 위한 조인 연관된 엔티티, 컬렉션을 한번에 같이 조회
        String jpql = "SELECT m FROM Member m JOIN FETCH m.team";
        //지연로딩을 설정하여도 fetch로 설정하면 지연로딩 설정을 무시하며 프록시를 사용하지 않고 실제 엔티티를 사용하여 즉시 로딩
        List<Member> memberss = em.createQuery(jpql, Member.class).getResultList();
        for(Member member : memberss) {
            System.out.println(member.getUsername() + member.getTeam().getName());
        }

        //COLLECTION FETCH JOIN : 일대다 관계 컬렉션 패치 조인
        String oneToManyFetchJoinJpql = "SELECT t FROM Team t JOIN FETCH t.members WHERE t.name = '팀A'";
        List<Team> teams = em.createQuery(oneToManyFetchJoinJpql, Team.class).getResultList();
        for(Team team : teams) {
            System.out.println(team.getName());
            for(Member member : team.getMembers()) {
                System.out.println(member.getUsername());
            }
        }

        //FETCH JOIN + DISTINCT : 조회쿼리에 DISTINCT를 추가하며, 어플리케이션에서 한번 더 중복 제거한다
        //테이블 관점으로보면 팀은 같지만 회원이 다르므로 DISTINCT가 적용되지 않는다
        //엔티티 관점에서보면 참조하는 주소값이 같은 2개의 동일 값이 있으므로 1개는 제거하게 된다

        //경로표현식 :
        // 상태 필드 경로 : 경로 탐색의 끝
        String stateField = "SELECT m.username, m.age FROM Member m";
        // 단일 값 연관 경로 : 묵시적 내부 조인, 단일 값 연관 경로는 계속 탐색 가능(연관 경로 : 엔티티안에 필드)
        String singleValue = "SELECT o.member FROM Orders o";
            // 실행 결과 : SELECT m.* FROM ORDER O INNER JOIN MEMBER M ON M.MEMBER_ID = O.MEMBER_ID
            // 묵시적 내부 조인  : jpql에서 o.member를 실제 SQL에서 봤을 때 내부조인이 일어나는데 이것을 묵시적 조인(내부)이라 칭함
            // 명시적 조인 : JOIN을 직접 입력하여 사용하는 경우
        // 컬렉션 값 연관 경로 : 묵시적 내부 조인, 탐색 불가지만 FROM절에서 조인을 통해 별칭을 얻으면 별칭을 통한 탐색 가능
        String faildCollectionValueSql = "SELECT t.members.username FROM Team t"; //사용 불가
        String collectionValue = "SELECT m.username FROM Team INNER JOIN t.members m"; //조인을 통해 별칭을 얻어서 접근 가능

        //Sub Query : where, having절에만 사용 가능 select, from 사용 불가 그러나 JPA 구현체마다 차이가 있어 보임
        String ttt = "SELECT m FROM Member m WHERE m.age > (SELECT avg(m2.age) FROM Member m2)";
        String tttt = "SELECT m FROM Member m WHERE (SELECT COUNT(o) FROM Orders o WHERE m = o.member) > 0";
            //위 JPQL은 SELECT m FROM Member m WHERE m.orders.size > 0 으로 대체 가능

        //Sub Query - EXISTS : 서브쿼리에 결과가 존재하면 참 NOT은 반대
        String oioi = "SELECT m FROM Member m WHERE EXISTS (SELECT t FROM TEAM t WHERE t.name = '팀A')";

        //Sub Query - IN : 서브쿼리의 결과 중 하나라도 같은 것이 있으면 참
        String poio = "SELECT t FROM Team t WHERE t IN (SELECT t2 FROM Team t2 JOIN t2.members m2 where m2.age >= 20)";

        //ENUM을 사용하고 싶은경우 패키지명을 포함하는 전체 이름을 사용

        /**
         * 연산자 우선순위
         * 경로 탐색 연산 > 수학 연산 > 비교 연산 > 논리 연산산
         * BETWEEN, IN, LIKE, NULL 사용법 동일
         * 컬렉션 식 : empty를 사용하여 컬렉션이 비어있는지 확인할 수 있다.
         *      ex) SELECT m FROM Memer m WHERE m.orders is not Empty
         * 컬렉션 식을 IS NULL로 체크하면 안되니 주의필요!
         * 컬렉션 멤버 식 : 엔티티나 값이 컬렉션안에 포함되어 있으면 참
         *      ex) SELECT t FROM Team t WHERE :memberParam member of t.members
         *
         * 스칼라 식
         *      수학 식 : +, - (단항 연산자) , *,/,+,- (사칙연산)
         *      문자함수
         *          CONCAT : 문자 합치기
         *          SUBSTRING : 문자열 자르기, 위치부터 시작해 길이 만큼 문자를 구한다, 값이 단일값이면 나머지 전체길이 구함
         *          TRIM : TRIM 문자를 제거
         *          LOWER : 소문자로 변경
         *          UPPER : 대문자로 변경
         *          LENGTH : 길이
         *          LOCATE : 찾는 문자의 시작위치 못찾으면 0을 반환
         *      수학함수
         *          ABS : 절대값
         *          SQRT : 제곱근
         *          MOD : 나머지
         *          SIZE : 컬렉션 크기
         *          INDEX : LIST 타입의 컬렉션 위치값 구하기 @OrderColumn(테이블에 저장할 때 순서값을 정해서 넣음)을 사용하는 LIST 타입
         *      날짜함수
         *          CURRENT_DATE : 현재 날짜
         *          CURRENT_TIME : 현재 시간
         *          CURRENT_TIMESTAMP : 현재 날짜 시간
         *          하이버네이트의 경우 날짜 타입에서 년 ~ 시간 초 까지 각각 구할 수 있다
         */

        //CASE
        //기본 CASE : 조건식을 사용하는 방식
        String basicCase = "SELECT CASE WHEN m.age <= 10 THEN '학생요금' END FROM Member m";
        //심플 CASE : 조건식을 사용하지 않은 방식
        String simpleCase = "SELECT CASE t.name WHEN '팀A' THEN '인센티브100%' ELSE '인센티브200%' END FROM Team t";
        //COALESCE : null이면 대체 문자로 반환
        String coalesce = "SELECT COALESCE(m.username, '이름 없는 회원') FROM Member m";
        //NULLIF : 두 값이 같으면 NULL 다르면 첫번째 값 반환, 집합 함수는 null을 포함하지 않으므로 보통 집합 함수와 함께 사용
        String nullif = "SELECT NULLIF(m.username, '관리자') FROM Member m";

        //다형성 쿼리
        // TYPE : 엔티티의 상속 구조에서 조회 대상을 특정 자식 타입으로 한정할 때 주로 사용
        String polymorphismType = "SELECT i FROM Item i WHERE TYPE(i) IN (Book, Movie)";
        // trans SQL : SELECT item FROM ITEM item WHERE item.DTYPE IN ('B', 'M')

        // TREAT : 자바의 타입 캐스팅과 비슷하며 상속 구조에서 부모 타입을 특정 자식 타입으로 변경하여 자식 타입 엔티티 필드에 접근한다
        String inheritanceTreat = "SELECT i FROM Item i WHERE TREAT(i as Book).author = 'kim'";

    }

    public void getUsername(EntityManager em) {
        List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class).setParameter("username", "회원1").getResultList();
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
