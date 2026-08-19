package monitor_qualidade.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class IndicadoresComerciaisMetrics {

    private final MeterRegistry registry; // Adicionado para gerarmos tags dinâmicas
    private final Counter pedidosProcessadosCounter;
    private final Counter divergenciasEncontradasCounter;
    private final Counter sucessoIntegracaoCounter;

    public IndicadoresComerciaisMetrics(MeterRegistry registry) {
        this.registry = registry;

        this.pedidosProcessadosCounter = Counter.builder("comercial_pedidos_processados_total")
                .description("Total de pedidos processados pelo motor de reconciliacao")
                .register(registry);

        this.divergenciasEncontradasCounter = Counter.builder("comercial_pedidos_divergentes_total")
                .description("Total de pedidos com divergencia detectada")
                .register(registry);

        this.sucessoIntegracaoCounter = Counter.builder("comercial_sucesso_integracao_total")
                .description("Total de consultas bem sucedidas na API da Web Continental")
                .register(registry);

        // Nota: O falhasIntegracaoCounter foi removido daqui do construtor,
        // pois agora ele é criado dinamicamente no metodo abaixo para aceitar as tags.
    }

    public void registrarPedidoProcessado() {
        this.pedidosProcessadosCounter.increment();
    }

    public void registrarDivergencia() {
        this.divergenciasEncontradasCounter.increment();
    }

    public void registrarSucessoIntegracao() {
        this.sucessoIntegracaoCounter.increment();
    }

    // Substitui o antigo registrarFalhaIntegracao()
    public void registrarFalhaIntegracao(String canal, String motivoErro, double valorPerdido) {

        // Registra a falha, mas agora anexa o motivo exato (ex: 404, Timeout)
        registry.counter("comercial_falhas_integracao_total",
                        "canal", canal,
                        "motivo", motivoErro)
                .increment();

        // Nova metrica, soma o valor em R$ do pedido que falhou
        registry.counter("comercial_risco_financeiro_total",
                        "canal", canal)
                .increment(valorPerdido);
    }
}