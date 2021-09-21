package mapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(IdclassParentId.class)
public class IdclassParent {

    @Id
    @Column(name = "PARENT_ID01")
    private String id1;

    @Id
    @Column(name = "PARENT_ID02")
    private String id2;

    private String name;

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
