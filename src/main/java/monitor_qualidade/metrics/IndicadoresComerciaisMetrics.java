package monitor_qualidade.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class IndicadoresComerciaisMetrics {

    private final Counter pedidosProcessadosCounter;
    private final Counter divergenciasEncontradasCounter;
    private final Counter falhasIntegracaoCounter;
    private final Counter sucessoIntegracaoCounter;

    public IndicadoresComerciaisMetrics(MeterRegistry registry) {

        this.pedidosProcessadosCounter = Counter.builder("comercial_pedidos_processados_total")
                .description("Total de pedidos processados pelo motor de reconciliacao")
                .register(registry);

        this.divergenciasEncontradasCounter = Counter.builder("comercial_pedidos_divergentes_total")
                .description("Total de pedidos com divergencia detectada")
                .register(registry);

        this.falhasIntegracaoCounter = Counter.builder("comercial_falhas_integracao_total")
                .description("Total de falhas de comunicacao com a API da Web Continental")
                .register(registry);

        // Novo indicador para medir a taxa de sucesso da integração
        this.sucessoIntegracaoCounter = Counter.builder("comercial_sucesso_integracao_total")
                .description("Total de consultas bem sucedidas na API da Web Continental")
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

    // Novo metodo de registro de sucesso
    public void registrarSucessoIntegracao() {
        this.sucessoIntegracaoCounter.increment();
    }
}