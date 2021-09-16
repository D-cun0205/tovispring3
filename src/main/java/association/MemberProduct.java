package association;

import javax.persistence.*;

//@Entity
//@IdClass(MemberProductId.class)
public class MemberProduct {

//    @Id
//    @ManyToOne
//    @JoinColumn(name = "MEMBER_ID")
    private Member member; //MemberProductId 클래스의 member와 매핑

//    @Id
//    @ManyToOne
//    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    private int orderAmount;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }
}
