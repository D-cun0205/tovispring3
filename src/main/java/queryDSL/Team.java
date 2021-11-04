package queryDSL;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Team {

    @Id
    @Column(name = "TEAM_ID")
    private String id;

    private String name;

    /**
        1:N 연관관계 매핑
        mappedby 사용 이유
            객체 연관관계는 2개의 단방향(양방향 효과), 테이블 연관관계는 1개의 외래키로 양방향
            엔티티를 양방향처럼 설정하면 참조는 2개이며 외래키는 1개가 되므로
            연관관계에 해당되는 2개의 엔티티중 하나를 정해서 테이블의 외래키를 관리하는 연관관계의 주인(mappedby)을 정한다
            * 연관관계 주인은 데이터베이스 연관관계 매핑, 외래키 관리(등록, 수정, 삭제)
            * 연관관계 주인이 아닌 쪽은 읽기만 허용
            * 연관관계의 주인 설정은 테이블에 외래키가 있는 곳에 해당하는 엔티티로 정해야 한다
     */
    @OneToMany(mappedBy = "team")
    public List<Member> members = new ArrayList<>();

    public Team(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
