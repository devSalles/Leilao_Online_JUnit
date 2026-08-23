package LeilaoOnlineJUnit.service;

import LeilaoOnlineJUnit.Enum.StatusLeilao;
import LeilaoOnlineJUnit.Enum.StatusUsuario;
import LeilaoOnlineJUnit.dto.lance.LanceRequestDTO;
import LeilaoOnlineJUnit.dto.lance.LanceResponseDTO;
import LeilaoOnlineJUnit.entity.Lance;
import LeilaoOnlineJUnit.entity.Leilao;
import LeilaoOnlineJUnit.entity.Usuario;
import LeilaoOnlineJUnit.infra.exception.*;
import LeilaoOnlineJUnit.repository.LanceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LanceService {

    private final LanceRepository lanceRepository;
    private final UsuarioService usuarioService;
    private final LeilaoService leilaoService;

    @Transactional
    public LanceResponseDTO realizarLance(LanceRequestDTO lanceRequestDTO)
    {
        Usuario participante = usuarioService.buscarIdUsuario(lanceRequestDTO.idUsuario());
        Leilao leilao = leilaoService.buscarLeilaoID(lanceRequestDTO.idLeilao());

        validarUsuario(participante,leilao);
        validarLeilao(leilao);
        validarValorLance(lanceRequestDTO.valorLance(),leilao);

        Lance lanceSalvar = lanceRequestDTO.toLance(participante,leilao);

        lanceRepository.save(lanceSalvar);
        return LanceResponseDTO.fromLance(lanceSalvar);
    }

    public LanceResponseDTO buscarLance(Long id)
    {
        Lance lance = lanceRepository.findById(id).orElseThrow(()-> new IdNaoEncontradoException("ID de lance não encontrado"));
        return  LanceResponseDTO.fromLance(lance);
    }

    public List<LanceResponseDTO> buscarLancesPorLeilao(Long idLeilao)
    {
        List<Lance> leilaoId = lanceRepository.findByLeilaoId(idLeilao);
        if(leilaoId.isEmpty())
        {
            throw new NenhumRegistroException("Nenhum registro foi encontrado");
        }
        return leilaoId.stream().map(LanceResponseDTO::fromLance).toList();
    }

    public List<LanceResponseDTO> listarLances()
    {
        List<Lance> lances = lanceRepository.findAll();
        if(lances.isEmpty())
        {
            throw new NenhumRegistroException("Nenhum registro foi encontrado");
        }
        return lances.stream().map(LanceResponseDTO::fromLance).toList();
    }

    //--- Metodos Auxiliares ---

    private void validarLeilao(Leilao leilao)
    {
        if(leilao.getStatusLeilao() != StatusLeilao.ABERTO)
        {
            throw new LeilaoNaoAbertoException();
        }
    }

    private void validarUsuario(Usuario participante,Leilao leilao)
    {
        if(participante.getId().equals(leilao.getCriador().getId()))
        {
            throw new UsuarioProprietarioException();
        }

        if(participante.getStatusUsuario() != StatusUsuario.ATIVO)
        {
            throw  new UsuarioBloqueadoException("Usuário bloqueado não pode realizar novos lances");
        }
    }

    private void validarValorLance(BigDecimal valorLance, Leilao leilao)
    {

        if(valorLance ==null || valorLance.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new ValorLanceInvalidoException();
        }

        Optional<Lance> maiorLance = lanceRepository.findFirstByLeilaoOrderByValorDesc(leilao);

        if(maiorLance.isEmpty())
        {
            if(valorLance.compareTo(leilao.getItem().getValorInicial())<0)
            {
                throw new PrimeiroLanceInvaidoException();
            }

            return;
        }

        if(valorLance.compareTo(maiorLance.get().getValor())<=0)
        {
            throw new LanceInvalidoException();
        }
    }
}
