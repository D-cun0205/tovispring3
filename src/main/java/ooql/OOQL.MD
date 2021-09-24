##객체지향 쿼리 언어(Object Oriented Query Language)

###JPQL
    Criteria, QueryDSL(추천)은 JPQL을 편리하게 사용할 수 있도록 도와주는 기술
    객체지향 쿼리 언어이므로 테이블 대상이 아닌 엔티티를 대상으로 쿼리
    SQL 추상화로 특정 데이터베이스의 SQL에 의존하지 않음
    JPQL도 SQL로 변환하여 쿼리
    대소문자 구분, 엔티티명(테이블명X), 별칭 필수(Member as m)

###파라미터 바인딩
    이름 기준 파라미터(추천), 위치 기준 파라미터
    JPQL에 파라미터 바인딩을 사용하지 않고 직접 문자열에 파라미터를 추가해서 사용할 경우
    사용할 수 는 있으나 성능문제, SQL 인젝션 공격이 발생할 수 있으니 파라미터 바인딩은 선택 아닌 필수

###경로 표현식
    상태필드(값) 을 사용할 경우 기존 SQL과 차이가 없으나
    단일 값, 컬렉션 연관 경로 탐색을 사용하게 되면 묵시적 내부 조인이 발생
    select t.members from Team t
    -> select t.*, m.* from Team t inner join member m on t.id = m.team_id
    묵시적 조인을 할 경우 성능에 이슈가 발생할 수 있으니 성능과 관련된 JSQL을 작성 할 경우
    JOIN절을 직접 입력하여 명시적으로 변경하여 JSQL을 완성해주어야 성능 이슈 문제가 발생하지 않음

```sql
    select t.members from Team t /* 성공 */
    /* 컬렉션에서의 경로탐색 시작을 허용하지 않음 */
    select t.members.username from Team t /* 실패 */
    /* 컬렉션에서의 경로탐색을 허용하고 싶으면 별칭을 얻어서 스칼라타입의 경로탐색을 허용한다 */
    select m.username from Team t join t.members m
    
    /* COUNT함수를 사용하는 SQL로 변환 */
    select t.members.size from Team t
```

###서브쿼리
    JPQL 서브 쿼리는 WHERE, HAVING 절에서만 사용 가능 

```sql
    select m from member m
    where m.orders.size > 0
```

###조건식 컬렉션 식
    컬렉션에만 사용하는 특별한 기능이며 이외 기능은 컬렉션에 사용불가
    

```sql
    /* IS [NOT] EMPTY 컬렉션에 값이 비어있으면 true */
    /* 주문이 하나라도 있는 회원 조회 */
    /* is null을 통한 조회 불가 */
    select m from Member m where m.orders is not empty
    
    /* [NOT] MEMBER [OF] */
    /* 엔티티나 값이 컬렉션에 포함되어 있으면 true */
    select t from Team t where :memberParam member of t.members
    
    CURRENT_DATE  /* 현재 날짜 */
    CURRENT_TIME  /* 현재 시간 */
    CURRENT_TIMESTAMP  /* 현재 날짜 시간 */
    YEAR , MONTH , DAY , HOUR , MINUTE , SECOND /* 필요한 날짜 및 시간 */
    select year(CURRENT_TIMESTAMP) ..
```
    