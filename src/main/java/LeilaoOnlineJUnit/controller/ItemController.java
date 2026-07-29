package LeilaoOnlineJUnit.controller;

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
}