package woohaengsi.qnadiary.flower.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import woohaengsi.qnadiary.flower.domain.Flower;

@Repository
public interface FlowerRepository extends JpaRepository<Flower, Long> {

}
