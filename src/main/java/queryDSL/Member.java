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

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

//    //주 테이블에 외래 키가 있는 단방향 관계
//    @Id @GeneratedValue
//    @Column(name = "MEMBER_ID")
//    private Long id;
//
//    private String username;
//
//    @OneToOne
//    @JoinColumn(name = "LOCKER_ID")
//    private Locker locker;

//    //일대다 단방향 양방향
//    @Id @GeneratedValue
//    @Column(name = "MEMBER_ID")
//    private Long id;
//
//    private String username;
//
//    /**
//        다대일 단방향 매핑 (읽기전용)
//        이렇게 사용하면 양쪽에서 외래키를 관리하여 문제가 발생할 수 있으므로
//        등록, 수정을 false로 설정하여 읽기만 가능하게
//     */
//    @ManyToOne
//    @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false)
//    private Team team;

//    //다대일 단방향 양방향
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "MEMBER_ID")
//    private Long id;
//
//    @Column(name = "USERNAME")
//    private String username;
//
//    @ManyToOne
//    @JoinColumn(name = "TEAM_ID")
//    private Team team;
//
//    public void setTeam(Team team) {
//        this.team = team;
//
//        //getMembers 안에 본인이 없는 경우에 추가
//        if(!team.getMembers().contains(this))
//            team.getMembers().add(this);
//    }
}
