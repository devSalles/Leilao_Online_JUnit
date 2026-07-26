package LeilaoOnlineJUnit.repository;

import LeilaoOnlineJUnit.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {


    Usuario findByEmail(String email);
    Usuario findByCpf(String cpf);

    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    boolean existsByEmailAndIdNot(String email, Long id);
}
