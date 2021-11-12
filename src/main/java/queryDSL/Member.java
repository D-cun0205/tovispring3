package queryDSL;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    /**
        JPA는 Null값에 대해 조회가 안될경우를 방지하기 위해 OUTER JOIN을 디폴트로 하며
        성능최적화를 위해서는 INNER JOIN이 유리하므로 nullable = false를 명시할 경우
        JPA는 설정값을 읽고 INNER JOIN을 실행 한다.

        @JoinColumn(name = "", nullable = true) : Null 허용, 외부 조인 사용
        @JoinColumn(name = "", nullable = false) : Null 허용하지 않음, 내부 조인 사용
     */
    @JoinColumn(name = "TEAM_ID", nullable = false)
    private Team team;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Orders> orders = new ArrayList<>();

}
