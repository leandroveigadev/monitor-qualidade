package monitor_qualidade.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import monitor_qualidade.model.EventoWebhookRaw;
import monitor_qualidade.model.IntegracaoLog;
import monitor_qualidade.repository.EventoWebhookRawRepository;
import monitor_qualidade.repository.IntegracaoLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProcessadorEventosService {

    private static final Logger log = LoggerFactory.getLogger(ProcessadorEventosService.class);

    private final EventoWebhookRawRepository eventoRepository;
    private final IntegracaoLogRepository logRepository;
    private final PedidoService pedidoService; // Delegamos a regra do pedido para o service especialista
    private final ObjectMapper objectMapper;

    public ProcessadorEventosService(EventoWebhookRawRepository eventoRepository,
                                     IntegracaoLogRepository logRepository,
                                     PedidoService pedidoService,
                                     ObjectMapper objectMapper) {
        this.eventoRepository = eventoRepository;
        this.logRepository = logRepository;
        this.pedidoService = pedidoService;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 30000)
    @Transactional
    public void processarEventosPendentes() {
        List<EventoWebhookRaw> pendentes = eventoRepository.findByStatusProcessamento("PENDENTE");

        if (pendentes.isEmpty()) return;

        log.info("Motor de Reconciliação: Processando {} eventos...", pendentes.size());

        for (EventoWebhookRaw evento : pendentes) {
            try {
                JsonNode json = objectMapper.readTree(evento.getPayloadBruto());

                // Leitura blindada com .path() para evitar NullPointerException
                String idExterno = json.path("codigoPedidoExterno").asText("ID_NAO_INFORMADO");
                String statusMarket = json.path("statusMarketplace").asText("STATUS_DESCONHECIDO");

                // Delega o processamento de negócio e auditoria para o PedidoService
                pedidoService.processarPedido(evento.getCanalOrigem(), idExterno, statusMarket);

                // Log de sucesso
                logRepository.save(new IntegracaoLog(evento.getCanalOrigem(), idExterno, "SUCESSO", "Processado com auditoria de status."));
                evento.setStatusProcessamento("PROCESSADO");

            } catch (Exception e) {
                log.error("Erro crítico no processamento do evento {}: {}", evento.getId(), e.getMessage());
                logRepository.save(new IntegracaoLog(evento.getCanalOrigem(), "N/A", "FALHA", e.getMessage()));
                evento.setStatusProcessamento("ERRO");
            }

            eventoRepository.save(evento);
        }
    }
}