package LeilaoOnlineJUnit.service;

import LeilaoOnlineJUnit.repository.LanceRepository;
import LeilaoOnlineJUnit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
}
