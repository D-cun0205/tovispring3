package mapping;

import javax.persistence.*;

@Entity
public class IdclassChild {

    @Id
    private String id;

    @ManyToOne
    @JoinColumns({
            //name, referencedColumnName이 같으면 referencedColumnName은 제외 가능
            @JoinColumn(name = "PARENT_ID01", referencedColumnName = "PARENT_ID01"),
            @JoinColumn(name = "PARENT_ID02", referencedColumnName = "PARENT_ID02")
    })
    private IdclassParent idclassParent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IdclassParent getIdclassParent() {
        return idclassParent;
    }

    public void setIdclassParent(IdclassParent idclassParent) {
        this.idclassParent = idclassParent;
    }
}
