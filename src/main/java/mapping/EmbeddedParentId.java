package mapping;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class EmbeddedParentId implements Serializable {

    @Column(name = "EMBEDDED_PARENT_ID1")
    private String id1;

    @Column(name = "EMBEDDED_PARENT_ID2")
    private String id2;

    public EmbeddedParentId(){}
    public EmbeddedParentId(String id1, String id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
