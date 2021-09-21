package mapping;

import javax.persistence.*;

@Entity
/*  조인 전략
*   Inhereitance : 상속, 전략 = 상속타입.조인
*   DiscriminatorColumn : 판별자컬럼, default : DTYPE
* */
@Inheritance(strategy = InheritanceType.JOINED)

/*  단일 테이블 전략
*   테이블 한개로 모든값을 저장하기 위해 자식엔티티의 매핑정보는 모두 NULL이 허용되어야 한다
*   DiscriminatorColumn를 꼭 설정해야하며 DiscriminatorValue를 설정하지않으면 자식엔티티 네임을 디폴트로 사용한다
* */
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

/*  구현 클래스마다 테이블 전략 (추천하지 않은 전략)
*
* */
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "DTYPE")
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;

    private String name; //이름
    private String price; //가격

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
