package monitor_qualidade.service;

import monitor_qualidade.dto.WebContinentalPedidoDTO;
import monitor_qualidade.metrics.IndicadoresComerciaisMetrics; // <-- Importação nova
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@Service
public class WebContinentalIntegrationService {

    private static final Logger log = LoggerFactory.getLogger(WebContinentalIntegrationService.class);

    private final RestClient restClient;
    private final IndicadoresComerciaisMetrics metrics; // <-- Declaramos a dependência das métricas

    // Padrão Sênior: Injetamos as métricas pelo construtor
    public WebContinentalIntegrationService(RestClient webContinentalRestClient, IndicadoresComerciaisMetrics metrics) {
        this.restClient = webContinentalRestClient;
        this.metrics = metrics;
    }

    public Optional<WebContinentalPedidoDTO> buscarPedidoNaWebContinental(String codigoPedidoExterno) {
        try {
            String url = "https://api.sandbox.webcontinental.com.br/v1/pedidos/" + codigoPedidoExterno;
            log.info("Consultando pedido [{}] ativamente na API da Web Continental...", codigoPedidoExterno);

            WebContinentalPedidoDTO resposta = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(WebContinentalPedidoDTO.class);

            log.info("Consulta do pedido [{}] realizada com sucesso.", codigoPedidoExterno);
            return Optional.ofNullable(resposta);

        } catch (RestClientException e) {
            log.error("Gargalo Operacional: Falha ao consultar o pedido [{}] na Web Continental. Motivo: {}",
                    codigoPedidoExterno, e.getMessage());

            // DISPARO DA MÉTRICA: Avisa o Prometheus/Grafana que houve uma falha!
            metrics.registrarFalhaIntegracao();

            return Optional.empty();
        }
    }
}