package monitor_qualidade.service;

import monitor_qualidade.model.Pedido;
import monitor_qualidade.model.PedidoStatusHistorico;
import monitor_qualidade.repository.PedidoRepository;
import monitor_qualidade.repository.PedidoStatusHistoricoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PedidoService {

    private final PedidoRepository repository;
    private final PedidoStatusHistoricoRepository historicoRepository;

    public PedidoService(PedidoRepository repository, PedidoStatusHistoricoRepository historicoRepository) {
        this.repository = repository;
        this.historicoRepository = historicoRepository;
    }

    @Transactional
    public Pedido processarPedido(String canalOrigem, String codigoExterno, String novoStatusMarketplace) {
        // Passo A: Busca se o pedido já existe para descobrir o status anterior (Auditoria)
        Pedido pedido = repository.findByCodigoPedidoExterno(codigoExterno)
                .orElseGet(() -> {
                    // Se não existe, cria um novo objeto base
                    Pedido novo = new Pedido();
                    novo.setCanalVenda(canalOrigem);
                    novo.setCodigoPedidoExterno(codigoExterno);
                    novo.setStatusErp("PENDENTE");
                    return novo;
                });

        String statusAntigo = pedido.getStatusMarketplace();

        // Se for o primeiro cadastro, o status anterior é considerado "INICIAL" ou "CRIADO"
        if (statusAntigo == null) {
            statusAntigo = "CRIADO";
        }

        // Passo B: Atualiza o status vindo do marketplace e recalcula divergências
        pedido.setStatusMarketplace(novoStatusMarketplace);
        pedido.setUltimaAtualizacao(LocalDateTime.now());
        pedido.avaliarDivergencia();

        // Passo C: Salva o pedido oficial
        Pedido pedidoSalvo = repository.save(pedido);

        // Passo D: Dispara a auditoria de mudança de status (Event Sourcing)
        registrarHistoricoDeStatus(pedidoSalvo, statusAntigo, novoStatusMarketplace, "Webhook: " + canalOrigem);

        return pedidoSalvo;
    }

    private void registrarHistoricoDeStatus(Pedido pedido, String statusAntigo, String statusNovo, String origem) {
        // Evita gravar histórico duplicado se o status não mudou
        if (statusNovo != null && statusNovo.equals(statusAntigo)) {
            return;
        }

        PedidoStatusHistorico historico = new PedidoStatusHistorico();
        historico.setCodigoPedidoExterno(pedido.getCodigoPedidoExterno());
        historico.setStatusAnterior(statusAntigo);
        historico.setStatusNovo(statusNovo);
        historico.setDataMudanca(LocalDateTime.now());
        historico.setOrigem(origem);

        historicoRepository.save(historico);
    }
}