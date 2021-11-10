package queryDSL;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
//상속 관계 매핑 전략 설정
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
////조인 전략을 사용할 경우 테이블은 객체처럼 타입별로 구분할 수 있는 방법이 없으므로 컬럼을 추가해 주어야 한다.
//@DiscriminatorColumn // 디폴트 (name = "DTYPE")
@Getter
@Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;

    private String name;
    private int price;
}
