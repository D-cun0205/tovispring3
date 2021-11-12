package queryDSL;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Parent {

    @Id @GeneratedValue
    @Column(name = "PARENT_ID")
    private Long id;
    
    /** 
        cascade = CascadeType.PERSIST를 선언하여 자식객체에 부모객체를 넣고 부모객체를 영속 상태로 만들면 자식 객체들도 영속 상태로 전이
        cascade = CascadeType.REMOVE를 선언하여 부모 엔티티를 영속 상태에서 제거(& 테이블에 delete)하면 자식 엔티티도 영속 제거
        CascadeType.PERSIST, CascadeType.REMOVE는 persist() 또는 remove() 함수 호출 시 진행되지 않고 flash()가 호출될 때 작동
     */ 
    @OneToMany(mappedBy = "parent", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Child> childs = new ArrayList<>();

    public void addChilds(Child child) {
        this.childs.add(child);
        child.setParent(this);
    }

}
