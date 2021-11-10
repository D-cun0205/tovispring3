package queryDSL;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
/**
    1개
    @AttributeOverride(name = "id", column = @Column(name = "MEMBER_ID")) //부모로 부터 물려받은 매핑 정보 재정의

    1개 이상
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "MEMBER_ID")),
            @AttributeOverride(name = "name", column = @Column(name = "MEMBER_NAME"))
    })
 */
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "MEMBER_ID")),
        @AttributeOverride(name = "name", column = @Column(name = "MEMBER_NAME"))
})
@Getter
@Setter
public class Member extends BaseEntity {

    private String email;
}
