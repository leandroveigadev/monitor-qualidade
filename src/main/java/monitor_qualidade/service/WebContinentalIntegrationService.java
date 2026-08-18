package monitor_qualidade.service;

import monitor_qualidade.dto.WebContinentalPedidoDTO;
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

    // Injeção de dependência via construtor chamando o Bean que criamos no Passo 1
    public WebContinentalIntegrationService(RestClient webContinentalRestClient) {
        this.restClient = webContinentalRestClient;
    }

    /**
     * Consulta o status atualizado de um pedido diretamente na API da Web Continental.
     * Retorna um Optional para proteger o sistema em caso de falha de comunicação.
     */
    public Optional<WebContinentalPedidoDTO> buscarPedidoNaWebContinental(String codigoPedidoExterno) {
        try {
            // URL fictícia de Sandbox.
            // Quando a documentação oficial da Web Continental for fornecida, ajustamos aqui.
            String url = "https://api.sandbox.webcontinental.com.br/v1/pedidos/" + codigoPedidoExterno;

            log.info("Consultando pedido [{}] ativamente na API da Web Continental...", codigoPedidoExterno);

            WebContinentalPedidoDTO resposta = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(WebContinentalPedidoDTO.class);

            log.info("Consulta do pedido [{}] realizada com sucesso.", codigoPedidoExterno);
            return Optional.ofNullable(resposta);

        } catch (RestClientException e) {
            // Blindagem: Logamos o erro com clareza para a auditoria, mas retornamos vazio para não travar a thread.
            log.error("Gargalo Operacional: Falha ao consultar o pedido [{}] na Web Continental. Motivo: {}",
                    codigoPedidoExterno, e.getMessage());
            return Optional.empty();
        }
    }
}