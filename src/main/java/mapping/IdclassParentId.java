package mapping;

import javafx.scene.Parent;

import java.io.Serializable;

public class IdclassParentId implements Serializable {

    private String id1;
    private String id2;

    public IdclassParentId(){}
    public IdclassParentId(String id1, String id2) {
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
