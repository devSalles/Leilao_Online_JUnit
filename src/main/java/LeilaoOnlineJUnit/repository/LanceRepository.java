package LeilaoOnlineJUnit.repository;

import LeilaoOnlineJUnit.entity.Lance;
import LeilaoOnlineJUnit.entity.Leilao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanceRepository extends JpaRepository<Lance,Long> {

    Optional<Lance> findFirstByLeilaoOrderByValorDesc(Leilao  leilao);
}
