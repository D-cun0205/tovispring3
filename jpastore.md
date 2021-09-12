```java

package jpastore;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;

    private String name; //상품 명
    private int price; //상품 가격 (@Column 설정 없으므로 NOT NULL 자동 설정)
    private int stockQuantity; //상품 재고 (@Column 설정 없으므로 NOT NULL 자동 설정)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}

package jpastore;

        import javax.persistence.Column;
        import javax.persistence.Entity;
        import javax.persistence.GeneratedValue;
        import javax.persistence.Id;

@Entity
public class Member {

    @Id //Primary Key
    @GeneratedValue //persistence.xml에 설정된 DB에 맞는 시퀀스 자동 증가 설정 (H2 : SEQUENCE)
    @Column(name = "MEMBER_ID") //테이블 컬럼과 매핑할 이름
    private Long id;

    private String name;

    private String city;
    private String street;
    private String zipcode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}

package jpastore;

        import javax.persistence.*;
        import java.util.Date;

@Entity
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    @Column(name = "MEMBER_ID") //Member Primary Key의 복합키 설정
    private Long memberId;

    @Temporal(TemporalType.TIMESTAMP) //2021-01-01 12:12:12 형식의 Date 타입 매핑
    private Date orderDate;

    @Enumerated(EnumType.STRING) //Enum(ORDER, CANCEL) 매핑
    private OrderStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}

package jpastore;

        import javax.persistence.*;

@Entity
@Table(name = "ORDER_ITEM") //테이블 명과 매핑
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "ORDER_ITEM_ID")
    private Long id;

    @Column(name = "ITEM_ID") //상품 Primary Key의 복합키 설정
    private Long itemId;

    @Column(name = "ORDER_ID") //주문 Primary Key의 복합키 설정
    private Long orderId;

    private int orderPrice; //주문 가격 (@Column 설정 없으므로 NOT NULL 자동 설정)
    private int count; //주문 수량 (@Column 설정 없으므로 NOT NULL 자동 설정)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

package jpastore;

public enum OrderStatus { ORDER, CANCEL }


```