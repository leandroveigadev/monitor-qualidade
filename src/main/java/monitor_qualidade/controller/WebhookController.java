package monitor_qualidade.controller;

import monitor_qualidade.model.EventoWebhookRaw;
import monitor_qualidade.repository.EventoWebhookRawRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private final EventoWebhookRawRepository repository;

    // Injeção de dependência nativa pelo construtor (Padrão Sênior e Seguro)
    public WebhookController(EventoWebhookRawRepository repository) {
        this.repository = repository;
    }

    // A rota recebe o nome do canal dinamicamente na URL (ex: /api/webhooks/mercadolivre)
    @PostMapping("/{canal}")
    public ResponseEntity<String> receberWebhook(
            @PathVariable String canal,
            @RequestBody String payloadBruto) {

        // 1. Cria a entidade com o nome do canal e o JSON exato que chegou
        EventoWebhookRaw evento = new EventoWebhookRaw(canal, payloadBruto);

        // 2. Salva no banco instantaneamente
        repository.save(evento);

        // 3. Libera a conexão do marketplace devolvendo um Status 200 OK
        return ResponseEntity.ok("Evento recebido com sucesso no canal: " + canal);
    }
}