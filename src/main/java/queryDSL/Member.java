package queryDSL;

import lombok.*;

import javax.persistence.*;

@ToString(of = {"id", "username", "age"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    public void add(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

}
