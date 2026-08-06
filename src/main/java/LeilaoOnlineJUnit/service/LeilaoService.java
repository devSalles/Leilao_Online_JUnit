package LeilaoOnlineJUnit.service;

import LeilaoOnlineJUnit.Enum.StatusItem;
import LeilaoOnlineJUnit.Enum.StatusLeilao;
import LeilaoOnlineJUnit.Enum.StatusUsuario;
import LeilaoOnlineJUnit.dto.leilao.LeilaoRequestDTO;
import LeilaoOnlineJUnit.dto.leilao.LeilaoResponseDTO;
import LeilaoOnlineJUnit.entity.Item;
import LeilaoOnlineJUnit.entity.Leilao;
import LeilaoOnlineJUnit.entity.Usuario;
import LeilaoOnlineJUnit.infra.exception.IdNaoEncontradoException;
import LeilaoOnlineJUnit.infra.exception.NenhumRegistroException;
import LeilaoOnlineJUnit.infra.exception.leilao.DataIncorretaException;
import LeilaoOnlineJUnit.infra.exception.item.ItemEmLeilaoException;
import LeilaoOnlineJUnit.infra.exception.item.ItemVendidoException;
import LeilaoOnlineJUnit.infra.exception.item.ItemVinculadoAoLeilaoException;
import LeilaoOnlineJUnit.infra.exception.leilao.DataInicioLeilaoException;
import LeilaoOnlineJUnit.infra.exception.leilao.LeilaoAbertoException;
import LeilaoOnlineJUnit.infra.exception.leilao.StatusDeLeilaoIncorretoException;
import LeilaoOnlineJUnit.infra.exception.participante.UsuarioBloqueadoException;
import LeilaoOnlineJUnit.repository.LeilaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
public class LeilaoService {

    private final LeilaoRepository leilaoRepository;
    private final ItemService itemService;
    private final UsuarioService usuarioService;

    @Transactional
    public LeilaoResponseDTO agendarLeilao(LeilaoRequestDTO  leilaoRequestDTO)
    {
        Usuario criadorID = usuarioService.buscarIdUsuario(leilaoRequestDTO.idCriador());
        Item itemID = itemService.buscarID(leilaoRequestDTO.idItem());

        validarCriadorItemLeilao(criadorID, itemID);
        validarDatasLeilao(leilaoRequestDTO);

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

    @Transactional
    public LeilaoResponseDTO atualizarLeilao(Long idLeilao, LeilaoRequestDTO leilaoRequestDTO)
    {
        validarDatasLeilao(leilaoRequestDTO);

        Leilao leilao = buscarLeilaoID(idLeilao);
        if (leilao.getStatusLeilao() != StatusLeilao.AGENDADO)
        {
            throw new StatusDeLeilaoIncorretoException();
        }

        Item item = itemService.buscarID(leilaoRequestDTO.idItem());
        Usuario criador = usuarioService.buscarIdUsuario(leilaoRequestDTO.idCriador());

        validarCriadorItemLeilao(criador,item);

        Leilao leilaoAtualizado = leilaoRequestDTO.updateLeilao(leilao, item, criador);

        leilaoRepository.save(leilaoAtualizado);

        return LeilaoResponseDTO.fromLeilao(leilaoAtualizado);
    }

    @Transactional
    public LeilaoResponseDTO abrirLeilao(Long id)
    {
        Leilao leilao = buscarLeilaoID(id);

        validarAberturaLeilao(leilao);

        leilao.setStatusLeilao(StatusLeilao.ABERTO);

        leilaoRepository.save(leilao);

        return  LeilaoResponseDTO.fromLeilao(leilao);
    }

    public List<LeilaoResponseDTO> listarTodosLeiloes()
    {
        List<Leilao> leiloes = leilaoRepository.findAll();

        if(leiloes.isEmpty())
        {
          throw new NenhumRegistroException("Nenhum registro foi encontrado");
        }

        return leiloes.stream().map(LeilaoResponseDTO::fromLeilao).toList();
    }

    public LeilaoResponseDTO listarID(Long idLeilao)
    {
        Leilao leilaoID = buscarLeilaoID(idLeilao);
        return LeilaoResponseDTO.fromLeilao(leilaoID);
    }

    public List<LeilaoResponseDTO> listarLeiloesPorStatus(StatusLeilao statusLeilao)
    {
        List<Leilao> leiloes = leilaoRepository.findByStatusLeilao(statusLeilao);

        if(leiloes.isEmpty())
        {
            throw new NenhumRegistroException("Nenhum registro com esse status foi encontrado");
        }

        return leiloes.stream().map(LeilaoResponseDTO::fromLeilao).toList();
    }

    public List<LeilaoResponseDTO> listarPorVencedor(Long idVencedor)
    {
        List<Leilao> leilaoIdVencedor =  leilaoRepository.findByVencedorId(idVencedor);

        if(leilaoIdVencedor.isEmpty())
        {
            throw new NenhumRegistroException("Nenhum registro com esse id foi encontrado");
        }

        return leilaoIdVencedor.stream().map(LeilaoResponseDTO::fromLeilao).toList();
    }

    public List<LeilaoResponseDTO> listarPorCriadorId(Long idCriador)
    {
        List<Leilao> leilaoIdCriador =  leilaoRepository.findByCriadorId(idCriador);

        if(leilaoIdCriador.isEmpty())
        {
            throw new NenhumRegistroException("Nenhum registro com esse id foi encontrado");
        }

        return leilaoIdCriador.stream().map(LeilaoResponseDTO::fromLeilao).toList();
    }

    public List<LeilaoResponseDTO> realizarBuscaPorDataInicial(LocalDate dataInicial, LocalDate dataFinal)
    {
        return realizarBuscaEntreDatas(dataInicial,dataFinal,leilaoRepository::findByDataInicioBetween);
    }

    public List<LeilaoResponseDTO> realizarBuscarEntreDatasFinais(LocalDate dataInicial, LocalDate dataFinal)
    {
        return realizarBuscaEntreDatas(dataInicial,dataFinal,leilaoRepository::findByDataFimBetween);
    }

    //--- Metodos Auxiliares ---

    public Leilao buscarLeilaoID(Long idLeilao)
    {
        return leilaoRepository.findById(idLeilao).orElseThrow(()->new IdNaoEncontradoException("ID de lelião não encontrado"));
    }

    public void validarAberturaLeilao(Leilao leilao)
    {
        if(leilao.getStatusLeilao().equals(StatusLeilao.ABERTO))
        {
            throw new LeilaoAbertoException();
        }

        if(LocalDateTime.now().isBefore(leilao.getDataInicio()))
        {
            throw new DataInicioLeilaoException();
        }
    }

    public List<LeilaoResponseDTO> realizarBuscaEntreDatas(LocalDate dataInicial, LocalDate dataFinal, BiFunction<LocalDateTime, LocalDateTime, List<Leilao>> leilao)
    {
        if(dataFinal.isBefore(dataInicial))
        {
            throw new DataIncorretaException("Datas de início esta posterior a data final");
        }

        LocalDateTime dataInicialFormatada = dataInicial.atStartOfDay();
        LocalDateTime dataFinalFormatada = dataFinal.atTime(LocalTime.MAX);

        List<Leilao> leiloes = leilao.apply(dataInicialFormatada,dataFinalFormatada);
        if (leiloes.isEmpty())
        {
            throw new NenhumRegistroException("Nenhum registro encontrado com essas datas");
        }
        return  leiloes.stream().map(LeilaoResponseDTO::fromLeilao).toList();
    }

    public void validarDatasLeilao(LeilaoRequestDTO  leilaoRequestDTO)
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

    public void validarCriadorItemLeilao(Usuario criador, Item item)
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
