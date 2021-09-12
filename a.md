```java
package jpabook.start;

import javax.persistence.*;

@Entity
//@SequenceGenerator 어노테이션은 @Id 설정된 값에 직접 설정 가능
@SequenceGenerator(
        name = "BOARD_SEQ_GENERATOR",
        sequenceName = "BOARD_SEQ",
        initialValue = 1, //초기값
        /*
            allocationSize 의 동작원리 (allocationSize : 50 기준)
            EntityManager의 persist()가 처음 동작할 때 시퀀스의 할당 기준을 잡으며 (첫)1~(끝)51까지의 세팅할 수 있는 값을 메모리에 저장
            기존에 시퀀스가 자동증가 되는 동작원리는 해당 테이블의 시퀀스가 몇인지 확인 후 값 + 1 이라면
            allocationSize를 사용하면 메모리에 51까지 등록이 가능하여 1부터 51까지는 메모리에서 값을 불러와 자동증가한다
            DB를 거치지 않고 메모리에서 해결 가능하기때문에 성능향상에 좋음
            시퀀스가 끝으로 세팅해놓은 값을 다 사용할 때 다시한번 DB에 시퀀스를 호출하여 끝(MAX)값 - (allocationSize - 1) 공식으로 값을 다시 세팅
            *** DB시퀀스 증가값이 1인경우 반드시 allcationSize도 1로 맞춰서 사용
        */
        allocationSize = 1
)
public class Board {

    @Id
    /*
        @GeneratedValue GenerationType.SEQUENCE
        GenerationType.IDENTITY와 사용방법이 비슷해 보이나 IDENTITY의 경우 시퀀스를 DB에 저장하고 사용하는 방식이며
        저장이 일어나고 시퀀스를 받아야지 엔티티가 영속상태가 되는데 반해
        GenerationType.SEQUENCE는 persist() 호출 시 저장 하기전에 DB에 시퀀스를 요청하여 받아서 해당 엔티티에
        시퀀스를 등록하고 영속상태로 만들고 트랜잭션 commit이 발생하면 flush가 호출되며 DB에 저장한다
    */
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

```

```java
package jpabook.start;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class CloseJpaMain {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

    public static Member createMember(String name) {
        EntityManager em1 = emf.createEntityManager();
        EntityTransaction transaction = em1.getTransaction();
        transaction.begin();

        Member member1 = new Member();
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

    private static void logic() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        TableTypeBoard board = new TableTypeBoard();

        transaction.begin();
        em.persist(board);
        transaction.commit();

        System.out.println(board.getId());

        em.close();
    }

    public static void main(String[] args) {
//        Member member = createMember("회원1");
//        member.setUsername("회원1명변경");
//        mergeMember(member);

        logic();
        emf.close();
    }
}

```

```java
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

```

```java
package jpabook.start;

import javax.persistence.*;
import java.util.Date;

/*
    UniqueConstraints 같은 기능들은 DDL을 자동 생성할 때만 사용되며
    JPA의 실행 로직에는 영향을 주지 않으며
    개발자입장에서 DB메타데이터를 확인하지 않고도 제약조건을 확인하기 쉽다
*/
@Entity
@Table(name = "MEMBER", uniqueConstraints = {@UniqueConstraint(
        name = "NAME_AGE_UNIQUE",
        columnNames = {"NAME", "AGE"})})
//@Access 속성으로 FIELD, PROPERTY를 받으며 전자는 필드에 직접 접근하고 후자는 접근자(Getter)를 이용, 필드 및 생성자에 직접 사용 가능
public class Member {

    /*
    *   @Entity, @Table, @Column : 매핑정보
    *   @Id : Primary Key
    *   Entity : 클래스와 관계형 DB 테이블을 매핑한다고 JPA에 설정 전달, Entity가 적용된 클래스를 엔티티클래스라고 칭함
    *   Table : 엔티티클래스에 매핑할 테이블 설정 정보 전달, name 속성을 사용하여 Member와 MEMBER를 매핑
    *           Table annotation을 생략 할 경우 클래스 이름을 테이블 이름(엔티티이름)으로 매핑
    *   Id : 엔티티클래스의 필드(id)를 테이블의 기본 키(ID)에 매핑, 다른말로 식별자 필드
    *   Column : 클래스 필드를 테이블 컬럼에 매핑, name 속성을 사용하여 username 필드를 NAME 컬럼에 매핑
    *   매핑 정보 없는 필드 : 필드명을 사용하여 컬럼명으로 매핑, 주의사항 DB가 대소문자 구분시 name 속성으로 명시적 매핑 필수
    */

    /*
        @Id
        적용 가능 자바 타입 (자바 기본 형, 자바 래퍼 형, String, Date, BigDecimal, BigInteger)
        기본 키 직접 할당 전략에서 식별자 값 없이 저장하면 예외 발생
        JPA 최상위 예외 = PersistenceException, 내부 하이버네이트 IdentifierGenerationException 예외를 포함
     */
    @Id
    @Column(name = "ID")
    /*
        @GeneratedValue의 GenerationType.IDENTITY 사용 시 주우의사항
        엔티티가 영속 상태가 되기위해서 식별자가 반드시 필요, IDENTITY 식별자 생성 전략은 엔티티를 DB에 저장해야 식별자를 구함
        그러므로 persist()를 호출하면 INSERT SQL이 데이터베이스에 전달되어 트랜잭션을 지원하는 쓰기 지연이 동작할 수 없다
    */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id; //아이디

//    nullable = false 로 지정하면 DDL에 Not Null 제약조건 추가
    @Column(name = "NAME", nullable = false, length = 10)
    private String username; //이름

    @Transient //매핑, DB저장 하지 않음
    private String transientField;

    //매핑정보없음
    private Integer age; //나이

    //4장에서 추가
    @Enumerated(EnumType.STRING) //Enum Class에 등록 되어있는 문자열 값 저장
    /*
        @Enumerated(EnumType.ORDINAL) //Enum Class에 등록 되어있는 순서대로 0, 1 숫자로 저장
        주의사항
        EnumType.ORDINAL을 사용할 경우 중간에 값이 추가되면 기존에 있던 DB의 데이터는
        추가되기전의 값으로 설정되니 추가되기전의 데이터를 추가된 데이터와 동기화시키고 추가해야한다.
     */
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP) //날짜타입 매핑에 사용하는 어노테이션
    //TemporalType.DATE : 날짜, TemporalType.TIEM : 시간, TemporalType.TIMESTAMP : 날짜 + 시간
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob //BLOB, CLOB를 매핑하며 엔티티 필드 속성이 String이면 CLOB, byte[]면 BLOB로 매핑
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

```

```java
package jpabook.start;

public enum RoleType {
    ADMIN, USER
}

```

```java
package jpabook.start;

import javax.persistence.*;

@Entity
@TableGenerator(
        name = "BOARD_SEQ_GENERATOR",
        table = "MY_SEQUENCES",
        pkColumnValue = "BOARD_SEQ",
        allocationSize = 1
)
public class TableTypeBoard {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)를 사용하게 되면 엔티티매니저팩토리에 설정 된 DB에
    //ORACLE, MYSQL, POSTGRES 등등 등록되어있는 DB에 맞춰서 IDENTITY, SEQUENCE, TABLE을 사용한다
    //그러나 SEQUENCE, TABLE을 사용해야하는 DB는 미리 생성이 필요하다
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "BOARD_SEQ_GENERATOR")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

```