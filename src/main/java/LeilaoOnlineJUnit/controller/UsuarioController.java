package LeilaoOnlineJUnit.controller;

import LeilaoOnlineJUnit.dto.usuario.UsuarioRequestDTO;
import LeilaoOnlineJUnit.dto.usuario.UsuarioResponseDTO;
import LeilaoOnlineJUnit.dto.usuario.UsuarioUpdateRequestDTO;
import LeilaoOnlineJUnit.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/usuario")
@Tag(name = "Usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/salvar-usuario")
    public ResponseEntity<UsuarioResponseDTO> controllerSalvarUsuario(@RequestBody @Valid UsuarioRequestDTO usuarioRequestDTO)
    {
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.salvarUsuario(usuarioRequestDTO);
        URI creation = URI.create("/usuario/" +usuarioResponseDTO.id());
        return ResponseEntity.created(creation).body(usuarioResponseDTO);
    }

    @PutMapping("/atualizar-usuario/{idUser}")
    public ResponseEntity<UsuarioResponseDTO> controllerAtualizarUsuario(
            @PathVariable Long idUser,
            @RequestBody @Valid UsuarioUpdateRequestDTO usuarioUpdtRequestDTO)
    {
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.atualizarUsuario(idUser, usuarioUpdtRequestDTO);
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @GetMapping("/exibir-por-id/{idUser}")
    public ResponseEntity<UsuarioResponseDTO> controllerExibirPorId(@PathVariable Long idUser)
    {
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.exibirPorId(idUser);
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @GetMapping("/exibir-todos")
    public ResponseEntity<List<UsuarioResponseDTO>> controllerExibirTodosUsuarios()
    {
        List<UsuarioResponseDTO> usuarios = usuarioService.exibirTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/exibir-por-cpf/{cpf}")
    public ResponseEntity<UsuarioResponseDTO> controllerExibirPorCpf(@PathVariable String cpf)
    {
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.exibirPorCpf(cpf);
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @GetMapping("/exibir-por-email/{email}")
    public ResponseEntity<UsuarioResponseDTO> controllerExibirPorEmail(@PathVariable String email)
    {
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.exibirPorEmail(email);
        return ResponseEntity.ok(usuarioResponseDTO);
    }
}