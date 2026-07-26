package LeilaoOnlineJUnit.dto.usuario;

import LeilaoOnlineJUnit.Enum.StatusUsuario;
import LeilaoOnlineJUnit.entity.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record UsuarioRequestDTO(

        @NotBlank(message = "Nome de participante obrigatório") @Size(min = 3,message = "O nome deve ter no mínimo 3 letras")
        @Pattern(regexp = "^[\\p{L}\\s]+$", message = "Nome deve conter apenas letras")
        String nome,

        @NotBlank(message = "Email de participante obrigatório") @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "CPF de participante obrigatório") @CPF(message = "Formato de CPF inválido")
        String cpf
) {

        public Usuario toUsuario() {

            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setStatusUsuario(StatusUsuario.ATIVO);

            return usuario;
        }
}
