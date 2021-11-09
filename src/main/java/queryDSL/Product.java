package queryDSL;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @Id @GeneratedValue
    @Column(name = "PRODUCT_ID")
    private Long id;

    private String name;

    @ManyToMany
    /**
        관계형 데이터베이스는 2개의 테이블로 다대다 관계를 표현할 수 없으므로
        일대다 다대일 관계로 풀어내는 연결 테이블을 사용
     */
    @JoinTable(name = "MEMBER_PRODUCT",
            //주 조인컬럼
            joinColumns = @JoinColumn(name = "MEMBER_ID"),
            //대상 조인컬럼
            inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID")
    )
    private List<Product> products = new ArrayList<>();

}
