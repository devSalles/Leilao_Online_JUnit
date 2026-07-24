package LeilaoOnlineJUnit.controller;

import LeilaoOnlineJUnit.service.LeilaoService;
import LeilaoOnlineJUnit.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
}
