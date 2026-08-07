package LeilaoOnlineJUnit.repository;

import LeilaoOnlineJUnit.Enum.StatusUsuario;
import LeilaoOnlineJUnit.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {


    Usuario findByEmail(String email);
    Usuario findByCpf(String cpf);

    List<Usuario> findByStatusUsuario(StatusUsuario statusUsuario);

    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    boolean existsByEmailAndIdNot(String email, Long id);

}
