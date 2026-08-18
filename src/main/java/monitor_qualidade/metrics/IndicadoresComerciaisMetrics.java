package monitor_qualidade.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class IndicadoresComerciaisMetrics {

    private final Counter pedidosProcessadosCounter;
    private final Counter divergenciasEncontradasCounter;
    private final Counter falhasIntegracaoCounter;

    // Injeção via construtor do MeterRegistry (o motor do Micrometer/Prometheus)
    public IndicadoresComerciaisMetrics(MeterRegistry registry) {

        // Métrica 1: Volume total de pedidos que passaram pelo sistema
        this.pedidosProcessadosCounter = Counter.builder("comercial_pedidos_processados_total")
                .description("Total de pedidos processados pelo motor de reconciliacao")
                .register(registry);

        // Métrica 2: O termômetro de problemas (Divergências ERP vs Web Continental)
        this.divergenciasEncontradasCounter = Counter.builder("comercial_pedidos_divergentes_total")
                .description("Total de pedidos com divergencia detectada")
                .register(registry);

        // Métrica 3: Gargalos de infraestrutura externa
        this.falhasIntegracaoCounter = Counter.builder("comercial_falhas_integracao_total")
                .description("Total de falhas de comunicacao com a API da Web Continental")
                .register(registry);
    }

    public void registrarPedidoProcessado() {
        this.pedidosProcessadosCounter.increment();
    }

    public void registrarDivergencia() {
        this.divergenciasEncontradasCounter.increment();
    }

    public void registrarFalhaIntegracao() {
        this.falhasIntegracaoCounter.increment();
    }
}