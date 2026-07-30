package LeilaoOnlineJUnit.repository;

import LeilaoOnlineJUnit.Enum.StatusItem;
import LeilaoOnlineJUnit.entity.Item;
import LeilaoOnlineJUnit.entity.Leilao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {

    boolean existsByProprietarioIdAndLeilaoIdIsNotNull(Long usuarioId);

    List<Item> findByCategoria(String categoria);
    List<Item> findByStatusItem(StatusItem statusItem);
    List<Item> findByProprietarioId(Long idProprietario);
    List<Item> findByNome(String nome);
}
