package mapping;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/*  @Entity는 실제 테이블과 매핑되지만 @MappedSuperclass는 테이블과 매핑되지않으며
*   자식 클래스에게 매핑 정보만 전달하고 싶을 때 사용한다 (매핑 정보 상속 목적)
*   엔티티로 등록되지 않았기 때문에 엔티티매니저의 속성을 통한 데이터 핸들은 불가
* */
@MappedSuperclass
public abstract class MappedSuperClassBaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
