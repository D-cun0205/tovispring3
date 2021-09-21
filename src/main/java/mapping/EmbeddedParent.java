package mapping;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EmbeddedParent {

    @EmbeddedId
    private EmbeddedParentId id;

    private String name;

    public EmbeddedParentId getId() {
        return id;
    }

    public void setId(EmbeddedParentId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
