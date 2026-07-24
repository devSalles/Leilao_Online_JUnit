package LeilaoOnlineJUnit.controller;

import LeilaoOnlineJUnit.service.ItemService;
import LeilaoOnlineJUnit.service.LeilaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "/leilao")
@RequiredArgsConstructor
public class LeilaoController {

    private final LeilaoService leilaoService;
}
