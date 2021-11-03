package queryDSL;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/** JPA에게 테이블과 매핑한다고 알려주며 엔티티클래스라고 칭함 */
@Entity
/** 엔티티클래스에 매핑할 테이블 정보 전달 */
@Table(name = "MEMBER", uniqueConstraints = {@UniqueConstraint(
        name = "NAME_AGE_UNIQUE",
        columnNames = {"NAME", "AGE"}
)})
@Getter
@Setter
public class Member {

    /** 엔티티클래스의 필드를 테이블의 기본키(Primary Key)에 매핑하며 식별자 필드라고 칭함 */
    @Id
    /** 필드를 테이블 컬럼에 매핑한다 */
    @Column(name = "ID")
    private String id;
    @Column(name = "NAME", nullable = false, length = 10)
    private String username;

    /**
     * @Column을 사용하지 않을 경우 필드명으로 컬럼명에 매핑한다
     * 대소문자를 구분하는 데이터베이스의 경우 @Column(name = "")을 명시적으로 매핑 필수
     */
    private Integer age;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    private String description;

}
