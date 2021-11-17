package queryDSL;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@IdClass(OrdersKeySet.class)
public class Orders {

    @Id @GeneratedValue
    @Column(name = "ORDERS_ID")
    private Long id;

    private int orderAmount;

    @Embedded
    Address address;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;
}
