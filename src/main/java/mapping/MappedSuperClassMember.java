package mapping;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
//공통 매핑 정보를 재정의하기 위한 어노테이션
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "SUPER_MEMBER_ID")),
        @AttributeOverride(name = "name", column = @Column(name = "SUPER_MEMBER_NAME"))
})
public class MappedSuperClassMember extends MappedSuperClassBaseEntity {

    private String tel;
}
