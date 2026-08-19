package monitor_qualidade.service;

import monitor_qualidade.dto.WebContinentalPedidoDTO;
import monitor_qualidade.metrics.IndicadoresComerciaisMetrics;
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
    private final IndicadoresComerciaisMetrics metrics;

    // Injeta as métricas pelo construtor
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

            // DISPARO DA MÉTRICA DE SUCESSO: Avisa o Prometheus/Grafana que a API externa respondeu bem!
            metrics.registrarSucessoIntegracao();

            return Optional.empty(); // (ou return Optional.ofNullable(resposta); conforme regra atual)

        } catch (RestClientException e) {
            log.error("Gargalo Operacional: Falha ao consultar o pedido [{}] na Web Continental. Motivo: {}",
                    codigoPedidoExterno, e.getMessage());

            // Disparo da métrica de falha. Avisa o Prometheus/Grafana que houve um erro!
            metrics.registrarFalhaIntegracao();

            return Optional.empty();
        }
    }
}