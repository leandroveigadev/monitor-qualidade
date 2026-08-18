package monitor_qualidade.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

// Ignora qualquer campo extra que a API enviar para economizar memoria
@JsonIgnoreProperties(ignoreUnknown = true)
public record WebContinentalPedidoDTO(

        @JsonProperty("codigo_pedido")
        String codigoPedidoExterno,

        @JsonProperty("status_oficial")
        String statusOficial,

        @JsonProperty("valor_total")
        BigDecimal valorTotal

) {
    // Como é um 'record', o Java já mesmo já vai criar construtores, getters (codigoPedidoExterno()),
    // equals, hashCode e toString automaticamente por baixo dos panos!
}
