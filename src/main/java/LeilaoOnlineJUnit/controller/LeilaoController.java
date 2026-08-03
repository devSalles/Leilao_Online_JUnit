package LeilaoOnlineJUnit.controller;

import LeilaoOnlineJUnit.dto.leilao.LeilaoRequestDTO;
import LeilaoOnlineJUnit.dto.leilao.LeilaoResponseDTO;
import LeilaoOnlineJUnit.service.ItemService;
import LeilaoOnlineJUnit.service.LeilaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/leilao")
@RequiredArgsConstructor
@Tag(name = "Leilão")
public class LeilaoController {

    private final LeilaoService leilaoService;

    @PostMapping("/agendar-leilão")
    public ResponseEntity<LeilaoResponseDTO> agendarLeilaoController(@Valid @RequestBody LeilaoRequestDTO leilaoRequestDTO)
    {
        LeilaoResponseDTO leilaoResponseDTO = leilaoService.agendarLeilao(leilaoRequestDTO);
        URI creation = URI.create("/leilao/" + leilaoResponseDTO.id());
        return ResponseEntity.created(creation).body(leilaoResponseDTO);
    }

}
