package association;

import java.io.Serializable;

//복합키를 위한 식별자 클래스
//Serializable 필수
public class MemberProductId implements Serializable {

    private String member;
    private String product;

    //필수
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    //필수
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
