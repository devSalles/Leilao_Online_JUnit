package LeilaoOnlineJUnit.factory;

import LeilaoOnlineJUnit.Enum.StatusUsuario;
import LeilaoOnlineJUnit.entity.Usuario;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UsuarioFactory {

    public static Usuario criarUsuarioPronto(){

        Usuario usuario = new Usuario();

        usuario.setId(1L);
        usuario.setNome("Bernardo");
        usuario.setCpf("14282943688");
        usuario.setEmail("bernardo89@gmail.com");
        usuario.setStatusUsuario(StatusUsuario.ATIVO);

        return usuario;
    }

    public static Usuario criarUsuarioPersonalizado(Long id, String nome, String cpf,StatusUsuario statusUsuario)
    {
        Usuario usuario = new Usuario();

        usuario.setId(id);
        usuario.setNome(nome);
        usuario.setCpf(cpf);
        usuario.setStatusUsuario(statusUsuario);
        usuario.setEmail(nome.toLowerCase().replace(" ","") + "@gmail.com");

        return usuario;
    }
}
