package LeilaoOnlineJUnit.service;


import LeilaoOnlineJUnit.Enum.StatusLeilao;
import LeilaoOnlineJUnit.Enum.StatusUsuario;
import LeilaoOnlineJUnit.dto.lance.LanceRequestDTO;
import LeilaoOnlineJUnit.dto.lance.LanceResponseDTO;
import LeilaoOnlineJUnit.entity.Lance;
import LeilaoOnlineJUnit.entity.Leilao;
import LeilaoOnlineJUnit.entity.Usuario;
import LeilaoOnlineJUnit.infra.exception.item.LanceInvalidoException;
import LeilaoOnlineJUnit.infra.exception.item.LeilaoNaoAbertoException;
import LeilaoOnlineJUnit.infra.exception.item.PrimeiroLanceInvaidoException;
import LeilaoOnlineJUnit.infra.exception.lance.ValorLanceInvalidoException;
import LeilaoOnlineJUnit.infra.exception.participante.UsuarioBloqueadoException;
import LeilaoOnlineJUnit.repository.LanceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
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

        validarUsuario(participante);
        validarLeilao(leilao);
        validarValorLance(lanceRequestDTO.valorLance(),leilao);

        Lance lanceSalvar = lanceRequestDTO.toLance(participante,leilao);

        lanceRepository.save(lanceSalvar);
        return LanceResponseDTO.fromLance(lanceSalvar);
    }

    //--- Metodos Auxiliares ---

    private void validarLeilao(Leilao leilao)
    {
        if(leilao.getStatusLeilao() != StatusLeilao.ABERTO)
        {
            throw new LeilaoNaoAbertoException();
        }
    }

    private void validarUsuario(Usuario usuario)
    {
        if(usuario.getStatusUsuario() != StatusUsuario.ATIVO)
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
