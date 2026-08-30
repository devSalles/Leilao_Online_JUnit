package LeilaoOnlineJUnit.service;

import LeilaoOnlineJUnit.Enum.StatusItem;
import LeilaoOnlineJUnit.Enum.StatusLeilao;
import LeilaoOnlineJUnit.Enum.StatusUsuario;
import LeilaoOnlineJUnit.dto.leilao.EncerramentoLeilaoResponseDTO;
import LeilaoOnlineJUnit.dto.leilao.LeilaoRequestDTO;
import LeilaoOnlineJUnit.dto.leilao.LeilaoResponseDTO;
import LeilaoOnlineJUnit.entity.Item;
import LeilaoOnlineJUnit.entity.Lance;
import LeilaoOnlineJUnit.entity.Leilao;
import LeilaoOnlineJUnit.entity.Usuario;
import LeilaoOnlineJUnit.infra.exception.*;
import LeilaoOnlineJUnit.infra.exception.UsuarioBloqueadoException;
import LeilaoOnlineJUnit.repository.LanceRepository;
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
    private final LanceRepository lanceRepository;
    private final ItemService itemService;
    private final UsuarioService usuarioService;

    @Transactional
    public LeilaoResponseDTO agendarLeilao(LeilaoRequestDTO leilaoRequestDTO)
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
            throw new StatusDeLeilaoIncorretoException("Apenas leilões com status de AGENDADO podem ser atualizados");
        }

        Item item = itemService.buscarID(leilaoRequestDTO.idItem());
        Usuario criador = usuarioService.buscarIdUsuario(leilaoRequestDTO.idCriador());

        validarCriadorItemLeilao(criador,item);

        if(!item.getId().equals(leilao.getItem().getId()))
        {
            validarItemVinculado(item);
        }

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
        leilao.getItem().setStatusItem(StatusItem.EM_LEILAO);

        leilaoRepository.save(leilao);

        return  LeilaoResponseDTO.fromLeilao(leilao);
    }

    @Transactional
    public LeilaoResponseDTO cancelarLeilao(Long idLeilao)
    {
        Leilao leilao = buscarLeilaoID(idLeilao);

        if(leilao.getStatusLeilao().equals(StatusLeilao.CANCELADO))
        {
            throw new StatusDeLeilaoIncorretoException("Não e possível cancelar leilão, pois ele já está cancelado");
        }

        if(leilao.getStatusLeilao()!=StatusLeilao.AGENDADO)
        {
            throw new StatusDeLeilaoIncorretoException("Leilão só pode ser cancelado se estiver com status de agendado");
        }


        if(lanceRepository.existsByLeilaoId(idLeilao))
        {
            throw new PossuiLanceVinculadoException();
        }

        leilao.setStatusLeilao(StatusLeilao.CANCELADO);
        leilao.getItem().setStatusItem(StatusItem.DISPONIVEL);

        this.leilaoRepository.save(leilao);

        return LeilaoResponseDTO.fromLeilao(leilao);
    }

    @Transactional
    public EncerramentoLeilaoResponseDTO encerrarLeilao(Long id) {

        Leilao leilao = buscarLeilaoID(id);

        if(leilao.getStatusLeilao() != StatusLeilao.ABERTO)
        {
            throw new StatusDeLeilaoIncorretoException("Apenas leilões ABERTOS podem ser encerrados");
        }

        Lance maiorLance = lanceRepository.findFirstByLeilaoOrderByValorDesc(leilao).orElse(null);

        validarEncerramentoLeilao(leilao, maiorLance);

        leilao.setStatusLeilao(StatusLeilao.ENCERRADO);
        leilaoRepository.save(leilao);

        return EncerramentoLeilaoResponseDTO.fromLeilao(leilao);
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

    private void validarItemVinculado(Item item)
    {
        boolean itemVinculado = leilaoRepository.existsByItemIdAndStatusLeilaoIn(item.getId(), List.of(StatusLeilao.AGENDADO, StatusLeilao.ABERTO));

        if(itemVinculado)
        {
            throw new ItemVinculadoAoLeilaoException("Esse item já está vinculado a outro leilão");
        }
    }

    private void validarEncerramentoLeilao(Leilao leilao, Lance maiorLance) {

        Item item = leilao.getItem();

        if(maiorLance == null)
        {
            leilao.setVencedor(null);
            item.setStatusItem(StatusItem.DISPONIVEL);
            return;
        }

        Usuario vencedor = maiorLance.getUsuario();
        leilao.setVencedor(vencedor);

        item.setStatusItem(StatusItem.VENDIDO);
        item.setProprietario(vencedor);
    }


    private void validarAberturaLeilao(Leilao leilao)
    {
        if(leilao.getStatusLeilao() != StatusLeilao.AGENDADO)
        {
            throw new StatusDeLeilaoIncorretoException("Apenas leilões com status AGENDADO podem ser abertos");
        }

        if(LocalDateTime.now().isBefore(leilao.getDataInicio()))
        {
            throw new DataInicioLeilaoException();
        }
    }

    private List<LeilaoResponseDTO> realizarBuscaEntreDatas(LocalDate dataInicial, LocalDate dataFinal, BiFunction<LocalDateTime, LocalDateTime, List<Leilao>> leilao)
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

    private void validarDatasLeilao(LeilaoRequestDTO  leilaoRequestDTO)
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

    private void validarCriadorItemLeilao(Usuario criador, Item item)
    {
        if(criador.getStatusUsuario().equals(StatusUsuario.BLOQUEADO))
        {
            throw new UsuarioBloqueadoException("usuario bloqueado não pode fazer agendamento de leilão");
        }

        if(!item.getProprietario().getId().equals(criador.getId()))
        {
            throw new UsuarioNaoProprietarioException();
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
