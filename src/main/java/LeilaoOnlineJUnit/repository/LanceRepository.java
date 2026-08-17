package LeilaoOnlineJUnit.repository;

import LeilaoOnlineJUnit.entity.Lance;
import LeilaoOnlineJUnit.entity.Leilao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LanceRepository extends JpaRepository<Lance,Long> {

    boolean existsByLeilaoId(Long LeilaoId);


    List<Lance> findByLeilaoId(Long LeilaoId);

    Optional<Lance> findFirstByLeilaoOrderByValorDesc(Leilao  leilao);
}
