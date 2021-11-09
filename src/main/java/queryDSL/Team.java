package queryDSL;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Team {

    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

//    //일대다 단방향 양방향
//    @Id @GeneratedValue
//    @Column(name = "TEAM_ID")
//    private Long id;
//
//    private String name;
//
//    @OneToMany
//    /**
//        일대다 단방향 관계를 매핑할 때 명시해야 하는 어노테이션
//          명시하지 않을 경우 각 테이블의 중간에 연관관계를 관리하는 조인 테이블 전략을 기본으로 사용해서 매핑한다.
//     */
//    @JoinColumn(name = "TEAM_ID")
//    private List<Member> members = new ArrayList<>();

//    //다대일 단방향 양방향
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "TEAM_ID")
//    private Long id;
//
//    private String name;
//
//    @OneToMany(mappedBy = "team")
//    private List<Member> members = new ArrayList<>();
//
//    public void addMember(Member member) {
//        this.members.add(member);
//        if(member.getTeam() != this)
//            member.setTeam(this);
//    }
}
