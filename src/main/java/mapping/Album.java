package mapping;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
//엔티티를 저장할 때 구분 컬럼에 입력할 값 세팅
//@PrimaryKeyJoinColumn(name = "ALBUM_ID") 기본값으로 부모키를 자식테이블에서 사용하는데 부모키의명을 변경하고 싶을때 사용
@DiscriminatorValue("A")
public class Album extends Item {

    String artist;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
