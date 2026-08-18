package monitor_qualidade.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evento_webhook_raw")
public class EventoWebhookRaw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "canal_origem", nullable = false)
    private String canalOrigem;

    @Column(name = "payload_bruto", columnDefinition = "TEXT", nullable = false)
    private String payloadBruto;

    @Column(name = "data_recebimento", nullable = false)
    private LocalDateTime dataRecebimento;

    @Column(name = "status_processamento", nullable = false)
    private String statusProcessamento; // PENDENTE, PROCESSADO, ERRO

    // Construtor padrão exigido pelo JPA
    public EventoWebhookRaw() {
    }

    // Construtor prático para a regra de negócio do Controller
    public EventoWebhookRaw(String canalOrigem, String payloadBruto) {
        this.canalOrigem = canalOrigem;
        this.payloadBruto = payloadBruto;
        this.dataRecebimento = LocalDateTime.now();
        this.statusProcessamento = "PENDENTE"; // Entra na fila para o motor processar
    }

    // --- Getters e Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCanalOrigem() { return canalOrigem; }
    public void setCanalOrigem(String canalOrigem) { this.canalOrigem = canalOrigem; }

    public String getPayloadBruto() { return payloadBruto; }
    public void setPayloadBruto(String payloadBruto) { this.payloadBruto = payloadBruto; }

    public LocalDateTime getDataRecebimento() { return dataRecebimento; }
    public void setDataRecebimento(LocalDateTime dataRecebimento) { this.dataRecebimento = dataRecebimento; }

    public String getStatusProcessamento() { return statusProcessamento; }
    public void setStatusProcessamento(String statusProcessamento) { this.statusProcessamento = statusProcessamento; }
}