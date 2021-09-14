package association;

import javax.persistence.*;

@Entity
public class Member {

    @Id
    @Column(name = "MEMBER_ID")
    private String id;

    private String username;

    @ManyToOne //다대일 관계 매핑 어노테이션
    @JoinColumn(name = "TEAM_ID") //외래키 매핑, JoinColumn 생략 시 ex) team_TEAM_ID 외래 키 사용
    private Team team;

    public Member(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public Member() {}

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

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        if(this.team != null)
            this.team.getMembers().remove(this);
        this.team = team;
        team.getMembers().add(this); //연관관계 편의 메소드
    }
}
