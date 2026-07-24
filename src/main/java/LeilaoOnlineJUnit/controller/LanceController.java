package LeilaoOnlineJUnit.controller;

import LeilaoOnlineJUnit.service.ItemService;
import LeilaoOnlineJUnit.service.LanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "/lance")
@RequiredArgsConstructor
public class LanceController {

    private final LanceService lanceService;
}
