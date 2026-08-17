package LeilaoOnlineJUnit.controller;

import LeilaoOnlineJUnit.dto.lance.LanceRequestDTO;
import LeilaoOnlineJUnit.dto.lance.LanceResponseDTO;
import LeilaoOnlineJUnit.service.ItemService;
import LeilaoOnlineJUnit.service.LanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/lance")
@RequiredArgsConstructor
public class LanceController {

    private final LanceService lanceService;

    @PostMapping("/realizar-lance")
    public ResponseEntity<LanceResponseDTO> controllerRealizarLance(@RequestBody @Valid LanceRequestDTO lanceRequestDTO) {
        LanceResponseDTO response = lanceService.realizarLance(lanceRequestDTO);
        URI create = URI.create("/lance/" +response.id());
        return ResponseEntity.created(create).body(response);
    }
}
