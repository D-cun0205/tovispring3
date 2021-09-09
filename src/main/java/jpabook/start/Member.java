package jpabook.start;

import javax.persistence.*;

@Entity
@Table(name = "MEMBER")
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

    @Id
    @Column(name = "ID")
    private String id; //아이디
    
    @Column(name = "name")
    private String username; //이름
    
    //매핑정보없음
    private Integer age; //나이

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
}
