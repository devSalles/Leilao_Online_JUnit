package LeilaoOnlineJUnit.controller;

import LeilaoOnlineJUnit.dto.usuario.UsuarioRequestDTO;
import LeilaoOnlineJUnit.dto.usuario.UsuarioResponseDTO;
import LeilaoOnlineJUnit.service.LeilaoService;
import LeilaoOnlineJUnit.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(name = "/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/salvar-usuario")
    public ResponseEntity<UsuarioResponseDTO> controllerSalvarUsuario(@RequestBody UsuarioRequestDTO usuarioRequestDTO)
    {
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.salvarUsuario(usuarioRequestDTO);
        return ResponseEntity.ok(usuarioResponseDTO);
    }
}
