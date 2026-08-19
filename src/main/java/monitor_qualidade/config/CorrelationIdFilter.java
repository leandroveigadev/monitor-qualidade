package monitor_qualidade.config;

import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Gera um ID único de 8 caracteres para rastrear este pedido específico
        String correlationId = UUID.randomUUID().toString().substring(0, 8);

        // Coloca o ID no contexto de log (MDC)
        MDC.put("correlationId", correlationId);

        try {
            chain.doFilter(request, response);
        } finally {
            // Limpa o contexto para não vazar para outras requisições
            MDC.remove("correlationId");
        }
    }
}
