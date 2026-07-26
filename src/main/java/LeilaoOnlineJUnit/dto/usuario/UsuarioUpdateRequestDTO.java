package LeilaoOnlineJUnit.dto.usuario;

import LeilaoOnlineJUnit.entity.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateRequestDTO(
        @NotBlank(message = "Nome de participante obrigatório") @Size(min = 3,message = "O nome deve ter no mínimo 3 letras")
        @Pattern(regexp = "^[\\p{L}\\s]+$", message = "Nome deve conter apenas letras")
        String nome,

        @NotBlank(message = "Email de participante obrigatório") @Email(message = "Formato de email inválido")
        String email
) {

    public void UpdateUsuario(Usuario usuario)
    {
        usuario.setNome(nome);
        usuario.setEmail(email);
    }
}

