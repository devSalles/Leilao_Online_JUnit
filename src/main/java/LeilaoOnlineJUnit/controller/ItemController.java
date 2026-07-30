package LeilaoOnlineJUnit.controller;

import LeilaoOnlineJUnit.Enum.StatusItem;
import LeilaoOnlineJUnit.dto.item.ItemResponseDTO;
import LeilaoOnlineJUnit.dto.item.ItemResquestDTO;
import LeilaoOnlineJUnit.dto.item.ItemUpdateRequestDTO;
import LeilaoOnlineJUnit.service.ItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
@Tag(name = "Item")
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/salvar-item")
    public ResponseEntity<ItemResponseDTO> salvarItem(@RequestBody @Valid ItemResquestDTO itemResquestDTO) {
        ItemResponseDTO itemSalvo = itemService.salvarItem(itemResquestDTO);
        URI creation = URI.create("/item/" +itemSalvo.id());
        return ResponseEntity.created(creation).body(itemSalvo);
    }

    @PutMapping("/atualizar-item/{idItem}")
    public ResponseEntity<ItemResponseDTO> atualizarItem(
            @PathVariable Long idItem,
            @RequestBody @Valid ItemUpdateRequestDTO itemUpdateRequestDTO) {
        ItemResponseDTO itemAtualizado = itemService.atualizarItem(idItem, itemUpdateRequestDTO);
        return ResponseEntity.ok(itemAtualizado);
    }

    @GetMapping("/buscar-todos")
    public ResponseEntity<List<ItemResponseDTO>> buscarTodosItems() {
        List<ItemResponseDTO> itens = itemService.buscarTodosItems();
        return ResponseEntity.ok(itens);
    }

    @GetMapping("/buscar-item/{idItem}")
    public ResponseEntity<ItemResponseDTO> buscarItem(@PathVariable Long idItem) {
        ItemResponseDTO item = itemService.buscarItem(idItem);
        return ResponseEntity.ok(item);
    }
    @GetMapping("/buscar-categoria/{categoria}")
    public ResponseEntity<List<ItemResponseDTO>> buscarPorCategoria(@PathVariable String categoria) {

        List<ItemResponseDTO> itens = itemService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(itens);
    }

    @GetMapping("/buscar-status/{statusItem}")
    public ResponseEntity<List<ItemResponseDTO>> buscarItemPorStatus(@PathVariable StatusItem statusItem) {

        List<ItemResponseDTO> itens = itemService.buscarItemPorStatus(statusItem);
        return ResponseEntity.ok(itens);
    }

    @GetMapping("/buscar-proprietario/{proprietarioId}")
    public ResponseEntity<List<ItemResponseDTO>> buscarItemPorProprietario(@PathVariable Long proprietarioId) {

        List<ItemResponseDTO> itens = itemService.buscarItemPorProprietario(proprietarioId);
        return ResponseEntity.ok(itens);
    }

    @GetMapping("/buscar-nome/{nome}")
    public ResponseEntity<List<ItemResponseDTO>> buscarPorNome(@PathVariable String nome) {

        List<ItemResponseDTO> itens = itemService.buscarPorNome(nome);
        return ResponseEntity.ok(itens);
    }

    @DeleteMapping("/remover-item/{idItem}")
    public ResponseEntity<Void> removerItem(@PathVariable Long idItem) {
        itemService.removerItem(idItem);
        return ResponseEntity.noContent().build();
    }

}