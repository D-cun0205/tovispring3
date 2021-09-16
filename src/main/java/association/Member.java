package association;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    @Id
    @Column(name = "MEMBER_ID")
    private String id;
    private String username;

    @OneToMany(mappedBy = "member")
    private List<MemberProduct> orders = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<MemberProduct> getOrders() {
        return orders;
    }

    public void setOrders(List<MemberProduct> orders) {
        this.orders = orders;
    }
}
