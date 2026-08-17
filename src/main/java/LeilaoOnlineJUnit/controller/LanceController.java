package LeilaoOnlineJUnit.controller;

import LeilaoOnlineJUnit.dto.lance.LanceRequestDTO;
import LeilaoOnlineJUnit.dto.lance.LanceResponseDTO;
import LeilaoOnlineJUnit.service.ItemService;
import LeilaoOnlineJUnit.service.LanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lance")
@RequiredArgsConstructor
public class LanceController {

    private final LanceService lanceService;

    @PostMapping("/realizar-lance")
    public ResponseEntity<LanceResponseDTO> controllerRealizarLance(@RequestBody @Valid LanceRequestDTO lanceRequestDTO) {
        LanceResponseDTO response = lanceService.realizarLance(lanceRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/buscar-lance/{id}")
    public ResponseEntity<LanceResponseDTO> controllerBuscarLance(@PathVariable Long id) {
        LanceResponseDTO response = lanceService.buscarLance(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar-lances-leilao/{idLeilao}")
    public ResponseEntity<List<LanceResponseDTO>> controllerBuscarLancesPorLeilao(@PathVariable Long idLeilao) {
        List<LanceResponseDTO> response = lanceService.buscarLancesPorLeilao(idLeilao);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/listar-lances")
    public ResponseEntity<List<LanceResponseDTO>> controllerListarLances() {
        List<LanceResponseDTO> response = lanceService.listarLances();
        return ResponseEntity.ok(response);
    }
}
