package LeilaoOnlineJUnit.controller;

import LeilaoOnlineJUnit.Enum.StatusLeilao;
import LeilaoOnlineJUnit.dto.leilao.LeilaoRequestDTO;
import LeilaoOnlineJUnit.dto.leilao.LeilaoResponseDTO;
import LeilaoOnlineJUnit.service.LeilaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @PutMapping("/atualizar-leilao/{idLeilao}")
    public ResponseEntity<LeilaoResponseDTO> atualizarLeilaoController(
            @PathVariable Long idLeilao,
            @Valid @RequestBody LeilaoRequestDTO leilaoRequestDTO)
    {
        LeilaoResponseDTO leilaoResponseDTO = leilaoService.atualizarLeilao(idLeilao, leilaoRequestDTO);
        return ResponseEntity.ok(leilaoResponseDTO);
    }

    @GetMapping("/listar-todos")
    public ResponseEntity<List<LeilaoResponseDTO>> listarTodosLeiloesController()
    {
        return ResponseEntity.ok(leilaoService.listarTodosLeiloes());
    }

    @GetMapping("/buscar-id/{idLeilao}")
    public ResponseEntity<LeilaoResponseDTO> buscarLeilaoPorIdController(@PathVariable Long idLeilao)
    {

        return ResponseEntity.ok(leilaoService.buscarID(idLeilao));
    }

    @GetMapping("/buscar-por-status/{statusLeilao}")
    public ResponseEntity<List<LeilaoResponseDTO>> listarLeiloesPorStatusController(@PathVariable StatusLeilao statusLeilao)
    {

        return ResponseEntity.ok(leilaoService.listarLeiloesPorStatus(statusLeilao));
    }

    @GetMapping("/buscar-por-vencedor/{idVencedor}")
    public ResponseEntity<List<LeilaoResponseDTO>> listarLeiloesPorVencedorController(
            @PathVariable Long idVencedor) {

        return ResponseEntity.ok(leilaoService.listarPorVencedor(idVencedor));
    }

    @GetMapping("/buscar-por-criador/{idCriador}")
    public ResponseEntity<List<LeilaoResponseDTO>> listarLeiloesPorCriadorController(
            @PathVariable Long idCriador) {

        return ResponseEntity.ok(leilaoService.listarPorCriadorId(idCriador));
    }

    @GetMapping("/buscar-por-data-inicial")
    public ResponseEntity<List<LeilaoResponseDTO>> buscarLeiloesPorDataInicialController(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataInicial,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataFinal)
    {

        return ResponseEntity.ok(leilaoService.realizarBuscaPorDataInicial(dataInicial, dataFinal));
    }

    @GetMapping("/buscar-por-data-final")
    public ResponseEntity<List<LeilaoResponseDTO>> buscarLeiloesPorDataFinalController(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataIncial,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataFinal
    )
    {
        return ResponseEntity.ok(leilaoService.realizarBuscarEntreDatasFinais(dataIncial, dataFinal));
    }
}