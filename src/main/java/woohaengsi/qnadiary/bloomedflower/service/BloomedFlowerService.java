package woohaengsi.qnadiary.bloomedflower.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woohaengsi.qnadiary.bloomedflower.domain.BloomedFlower;
import woohaengsi.qnadiary.bloomedflower.repository.BloomedFlowerRepository;
import woohaengsi.qnadiary.flower.domain.Flower;
import woohaengsi.qnadiary.flower.repository.FlowerRepository;
import woohaengsi.qnadiary.member.domain.Member;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BloomedFlowerService {

    private final FlowerRepository flowerRepository;
    private final BloomedFlowerRepository bloomedFlowerRepository;

    @Transactional
    public void giveFlowerToMember(Member member) {
        Long nextFlowerNumer = member.getNextFlowerNumber();
        Flower flower = flowerRepository.findById(nextFlowerNumer)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 답변입니다."));
        BloomedFlower bloomedFlower = bloomedFlowerRepository.save(BloomedFlower.from(member, flower));
        member.addBloomedFlower(bloomedFlower);
    }
}
