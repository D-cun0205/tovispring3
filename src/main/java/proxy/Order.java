package proxy;

import javax.persistence.*;

@Entity
public class Order {

    @Id
    @Column(name = "ORDER_ID")
    private String id;

    private Integer cnt;

    @ManyToOne
    private Member member;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
