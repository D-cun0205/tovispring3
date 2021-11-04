package queryDSL;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Member {

    @Id
    @Column(name = "MEMEBER_ID")
    private String id;

    private String username;

    //연관관계 매핑
    @ManyToOne(
            // optional = false //연관된 엔티티가 항상 있어야함
            // fetch = FetchType.EAGER or FetchType.LAZY //EAGER : 즉시 실행, LAZY : 지연 실행
            // cascade =  //영속성 전이 추후 확인 필요
            // targetEntity = Member.class //연관된 엔티티의 타입 정보를 설정, 거의 사용하지 않음
    )
    //테이블 연관관계, 외래 키 매핑시 사용, 생략 가능한 어노테이션
    @JoinColumn(
            name = "TEAM_ID" //매핑할 외래 키 이름 지정
            //referencedColumnName = "", //외래 키가 참조하는 대상 테이블의 컬럼명
            //foreignKey = //외래 키 제약조건 설정
            //나머지 속성은 @Column 속성과 동일
    )
    private Team team; //객체 연관관계

    public Member(String id, String username) {
        this.id = id;
        this.username = username;
    }

    //연관관계 설정
    public void setTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
