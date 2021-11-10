package queryDSL;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
//@DiscriminatorValue("M")
//@PrimaryKeyJoinColumn(name = "MOVIE_ID") //부모 테이블의 ID 컬럼명을 사용하지 않고 재정의 하고 싶은 경우
@Getter
@Setter
public class Movie extends Item {

}
