package ch.bbw.obelix.quarry;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuarryRepository extends JpaRepository<MenhirEntity, UUID> {

    List<MenhirEntity> findByStoneTypeContainingIgnoreCase(String stoneType);

    List<MenhirEntity> findMenhirByDecorativeness(
        MenhirEntity.Decorativeness decorativeness
    );
}