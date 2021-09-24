package valuetype;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Embedded
    private Address address;

    //값 타입을 여러개 저장하기위해서는 컬렉션을 사용
    //데이터베이스에서는 하나의로우에 여러개의 타입을 저장할 수 없으므로
    //컬렉션용에 해당되는 테이블에 매핑 키와 값 타입 리스트를 저장하도록 설정
    @ElementCollection
    //@CollectionTable.name = 생성 될 테이블 이름, @CollectionTable.joinColumns = Member테이블과 조인에 사용할 컬럼설정
    @CollectionTable(name = "FAVORITE_FOODS", joinColumns = @JoinColumn(name = "MEMBER_ID"))
    //사용하는 값이 하나일 경우 컬럼을 사용해서 별도의 클래스를 생성하지 않고 사용할 수 있다
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();

    @ElementCollection
    //@CollectionTable을 생략하면 매핑 값으로 {엔티티이름}_{컬렉션속성이름} ex) Member_addressHistory 테이블과 매핑
    @CollectionTable(name = "ADDRESS", joinColumns = @JoinColumn(name = "MEMBER_ID"))
    private List<Address> addressHistory = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<String> getFavoriteFoods() {
        return favoriteFoods;
    }

    public void setFavoriteFoods(Set<String> favoriteFoods) {
        this.favoriteFoods = favoriteFoods;
    }

    public List<Address> getAddressHistory() {
        return addressHistory;
    }

    public void setAddressHistory(List<Address> addressHistory) {
        this.addressHistory = addressHistory;
    }
}
