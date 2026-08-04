package LeilaoOnlineJUnit.repository;

import LeilaoOnlineJUnit.Enum.StatusLeilao;
import LeilaoOnlineJUnit.entity.Leilao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface LeilaoRepository extends JpaRepository<Leilao,Long> {

    boolean existsByCriadorIdAndStatusLeilaoIn(Long criadorId, List<StatusLeilao> statusLeilao);
    boolean existsByItemId(Long itemId);
    boolean existsByItemIdAndStatusLeilaoIn(Long idItem, Collection<StatusLeilao> statusLeilao);

    List<Leilao> findByStatusLeilao(StatusLeilao statusLeilao);
    List<Leilao> findByDataInicioBetween(LocalDateTime dataInicial, LocalDateTime dataFinal);
    List<Leilao> findByDataFimBetween(LocalDateTime dataInicial, LocalDateTime dataFinal);
    List<Leilao> findByCriadorId(Long criadorId);
    List<Leilao> findByVencedorId(Long vencedorId);
}
