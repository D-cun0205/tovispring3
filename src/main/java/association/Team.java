package association;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@Entity
//@Table(name = "team")
public class Team {

//    @Id
//    @GeneratedValue
//    @Column(name = "TEAM_ID")
    private long id;

    private String name;

    public Team(String name) {
        this.name = name;
    }

    public Team() {}

//    @OneToMany
//    @JoinColumn(name = "TEAM_ID")
    private List<Member> members = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
