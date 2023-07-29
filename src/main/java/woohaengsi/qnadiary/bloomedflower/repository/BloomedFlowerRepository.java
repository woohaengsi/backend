package woohaengsi.qnadiary.bloomedflower.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import woohaengsi.qnadiary.bloomedflower.domain.BloomedFlower;

@Repository
public interface BloomedFlowerRepository extends JpaRepository<BloomedFlower, Long> {

}
