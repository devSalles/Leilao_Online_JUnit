package LeilaoOnlineJUnit.service;

import LeilaoOnlineJUnit.Enum.StatusItem;
import LeilaoOnlineJUnit.Enum.StatusLeilao;
import LeilaoOnlineJUnit.Enum.StatusUsuario;
import LeilaoOnlineJUnit.dto.leilao.LeilaoRequestDTO;
import LeilaoOnlineJUnit.dto.leilao.LeilaoResponseDTO;
import LeilaoOnlineJUnit.entity.Item;
import LeilaoOnlineJUnit.entity.Leilao;
import LeilaoOnlineJUnit.entity.Usuario;
import LeilaoOnlineJUnit.infra.exception.leilao.DataIncorretaException;
import LeilaoOnlineJUnit.infra.exception.item.ItemEmLeilaoException;
import LeilaoOnlineJUnit.infra.exception.item.ItemVendidoException;
import LeilaoOnlineJUnit.infra.exception.item.ItemVinculadoAoLeilaoException;
import LeilaoOnlineJUnit.infra.exception.participante.UsuarioBloqueadoException;
import LeilaoOnlineJUnit.repository.LeilaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeilaoService {

    private final LeilaoRepository leilaoRepository;
    private final ItemService itemService;
    private final UsuarioService usuarioService;

    public LeilaoResponseDTO agendarLeilao(LeilaoRequestDTO  leilaoRequestDTO)
    {
        Usuario criadorID = usuarioService.buscarIdUsuario(leilaoRequestDTO.idCriador());
        Item itemID = itemService.buscarID(leilaoRequestDTO.idItem());

        validarAgendamentoLeilao(criadorID, itemID);
        validarDatasAgendamentoLeilao(leilaoRequestDTO);

        boolean itemVinculadoLeilao = leilaoRepository.existsByItemIdAndStatusLeilaoIn(
                itemID.getId(), List.of(StatusLeilao.ABERTO, StatusLeilao.AGENDADO));
        if(itemVinculadoLeilao)
        {
            throw new ItemVinculadoAoLeilaoException("Esse item já esta vínculado ao leilão");
        }

        Leilao leilaoSalvar = leilaoRequestDTO.toLeilao(itemID,criadorID);

        this.leilaoRepository.save(leilaoSalvar);
        return LeilaoResponseDTO.fromLeilao(leilaoSalvar);
    }

    //--- Metodos Auxiliares ---

    public void validarDatasAgendamentoLeilao(LeilaoRequestDTO  leilaoRequestDTO)
    {
        if (leilaoRequestDTO.dataInicio().isAfter(leilaoRequestDTO.dataFim())) {
            throw new DataIncorretaException();
        }

        if (!leilaoRequestDTO.dataInicio().isAfter(LocalDateTime.now())) {
            throw new DataIncorretaException("A data de início está incorreta");
        }

        if (!leilaoRequestDTO.dataFim().isAfter(leilaoRequestDTO.dataInicio())) {
            throw new DataIncorretaException("A data de encerramento está incorreta");
        }
    }

    public void validarAgendamentoLeilao(Usuario criador, Item item)
    {
        if(criador.getStatusUsuario().equals(StatusUsuario.BLOQUEADO))
        {
            throw new UsuarioBloqueadoException("usuario bloqueado não pode fazer agendamento de leilão");
        }

        if(item.getStatusItem().equals(StatusItem.EM_LEILAO))
        {
            throw new ItemEmLeilaoException("Item em leilão não pode ser vínculado");
        }

        if(item.getStatusItem().equals(StatusItem.VENDIDO))
        {
            throw new ItemVendidoException("Item vendido não pode ser vínculado");
        }
    }
}
