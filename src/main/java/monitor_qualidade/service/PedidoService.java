package monitor_qualidade.service;

import monitor_qualidade.metrics.IndicadoresComerciaisMetrics;
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
    private final IndicadoresComerciaisMetrics metrics;

    // Injeção de dependência atualizada com o IndicadoresComerciaisMetrics
    public PedidoService(PedidoRepository repository, PedidoStatusHistoricoRepository historicoRepository, IndicadoresComerciaisMetrics metrics) {
        this.repository = repository;
        this.historicoRepository = historicoRepository;
        this.metrics = metrics;
    }

    @Transactional
    public Pedido processarPedido(String canalOrigem, String codigoExterno, String novoStatusMarketplace) {
        // Busca se o pedido já existe para descobrir o status anterior (Auditoria)
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

        // Atualiza o status vindo do marketplace e recalcula divergências
        pedido.setStatusMarketplace(novoStatusMarketplace);
        pedido.setUltimaAtualizacao(LocalDateTime.now());
        pedido.avaliarDivergencia();

        // Salva o pedido oficial
        Pedido pedidoSalvo = repository.save(pedido);

        // Dispara a auditoria de mudança de status (Event Sourcing)
        registrarHistoricoDeStatus(pedidoSalvo, statusAntigo, novoStatusMarketplace, "Webhook: " + canalOrigem);

        // GATILHOS PARA O GRAFANA (Área Comercial)

        // Registra +1 no volume total de pedidos processados
        metrics.registrarPedidoProcessado();

        // Registra +1 se o motor de regras detectou divergência
        // Nota: Assumi que a classe Pedido possui o metodo 'isDivergente()'.

        if (pedidoSalvo.isDivergente()) {
            metrics.registrarDivergencia();
        }

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