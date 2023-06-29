package woohaengsi.qnadiary.bloomedflower.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import woohaengsi.qnadiary.common.BaseEntity;
import woohaengsi.qnadiary.flower.domain.Flower;
import woohaengsi.qnadiary.member.domain.Member;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BloomedFlower extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Flower flower;

    public BloomedFlower(Member member, Flower flower) {
        this.member = member;
        this.flower = flower;
    }
}
