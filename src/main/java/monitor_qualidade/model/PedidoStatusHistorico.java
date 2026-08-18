package monitor_qualidade.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido_status_historico", indexes = {
        @Index(name = "idx_historico_codigo", columnList = "codigo_pedido_externo")
})
public class PedidoStatusHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mantém a referência sem forçar um JOIN pesado com a tabela Pedido
    @Column(name = "codigo_pedido_externo", nullable = false)
    private String codigoPedidoExterno;

    @Column(name = "status_anterior")
    private String statusAnterior;

    @Column(name = "status_novo", nullable = false)
    private String statusNovo;

    @Column(name = "origem", nullable = false)
    private String origem; // Ex: "MARKETPLACE" ou "ERP"

    @Column(name = "data_mudanca", nullable = false)
    private LocalDateTime dataMudanca;

    // Construtor vazio obrigatório pelo JPA
    public PedidoStatusHistorico() {
    }

    // Construtor sênior focado na regra de negócio
    public PedidoStatusHistorico(String codigoPedidoExterno, String statusAnterior, String statusNovo, String origem) {
        this.codigoPedidoExterno = codigoPedidoExterno;
        this.statusAnterior = statusAnterior;
        this.statusNovo = statusNovo;
        this.origem = origem;
        this.dataMudanca = LocalDateTime.now();
    }

    // --- Getters e Setters Essenciais ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoPedidoExterno() {
        return codigoPedidoExterno;
    }

    public void setCodigoPedidoExterno(String codigoPedidoExterno) {
        this.codigoPedidoExterno = codigoPedidoExterno;
    }

    public String getStatusAnterior() {
        return statusAnterior;
    }

    public void setStatusAnterior(String statusAnterior) {
        this.statusAnterior = statusAnterior;
    }

    public String getStatusNovo() {
        return statusNovo;
    }

    public void setStatusNovo(String statusNovo) {
        this.statusNovo = statusNovo;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public LocalDateTime getDataMudanca() {
        return dataMudanca;
    }

    public void setDataMudanca(LocalDateTime dataMudanca) {
        this.dataMudanca = dataMudanca;
    }
}