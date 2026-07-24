package LeilaoOnlineJUnit.dto.usuario;

import LeilaoOnlineJUnit.Enum.StatusUsuario;
import LeilaoOnlineJUnit.entity.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record UsuarioRequestDTO(

        @NotBlank(message = "Nome de participante obrigatório") @Min(value = 3,message = "O nome deve ter no mínimo 3 letras")
        String nome,

        @NotBlank(message = "Email de participante obrigatório") @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "CPF de participante obrigatório") @CPF(message = "Formato de CPF inválido") @Min(value = 11,message = "Tamanho de CPF inválido")
        String cpf
) {

        public Usuario toUsuario() {

            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setCpf(cpf);
            usuario.setStatusUsuario(StatusUsuario.ATIVO);

            return usuario;
        }
}
