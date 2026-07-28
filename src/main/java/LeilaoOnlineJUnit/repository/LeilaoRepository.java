package LeilaoOnlineJUnit.repository;

import LeilaoOnlineJUnit.Enum.StatusLeilao;
import LeilaoOnlineJUnit.entity.Leilao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface LeilaoRepository extends JpaRepository<Leilao,Long> {
    boolean existsByUsuarioIdAndStatus(Long usuarioId, Collection<StatusLeilao> status);

}
