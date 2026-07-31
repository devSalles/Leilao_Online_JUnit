package LeilaoOnlineJUnit.service;


import LeilaoOnlineJUnit.entity.Lance;
import LeilaoOnlineJUnit.entity.Leilao;
import LeilaoOnlineJUnit.repository.LanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class LanceService {

    private final LanceRepository lanceRepository;

    private void validarValorLance(BigDecimal valorLance, Leilao leilao)
    {

    }
}
