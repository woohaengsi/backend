package woohaengsi.qnadiary.flower.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Flower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String flowerLanguage;
    private String imageUrl;

    public Flower(String name, String flowerLanguage, String imageUrl) {
        this.name = name;
        this.flowerLanguage = flowerLanguage;
        this.imageUrl = imageUrl;
    }
}
