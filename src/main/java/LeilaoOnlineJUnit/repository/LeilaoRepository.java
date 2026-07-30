package LeilaoOnlineJUnit.repository;

import LeilaoOnlineJUnit.Enum.StatusLeilao;
import LeilaoOnlineJUnit.entity.Leilao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LeilaoRepository extends JpaRepository<Leilao,Long> {

    boolean existsByCriadorIdAndStatusLeilaoIn(Long criadorId, List<StatusLeilao> statusLeilao);
    boolean existsByItemId(Long itemId);
}
