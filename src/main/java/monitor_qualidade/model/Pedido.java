package monitor_qualidade.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido", indexes = {
        @Index(name = "idx_codigo_externo", columnList = "codigo_pedido_externo", unique = true)
})
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "canal_venda", nullable = false)
    private String canalVenda; // Ex: Mercado Livre, B2W, Amazon

    @Column(name = "codigo_pedido_externo", nullable = false, unique = true)
    private String codigoPedidoExterno; // ID do pedido no Marketplace

    @Column(name = "status_marketplace", nullable = false)
    private String statusMarketplace; // Status vindo do canal de venda

    @Column(name = "status_erp", nullable = false)
    private String statusErp; // Status correspondente dentro do ERP interno

    @Column(name = "divergente", nullable = false)
    private boolean divergente; // Flag automática para indicar divergência de status

    @Column(name = "data_venda")
    private LocalDateTime dataVenda;

    @Column(name = "ultima_atualizacao", nullable = false)
    private LocalDateTime ultimaAtualizacao;

    // Construtor padrão exigido pelo JPA
    public Pedido() {
    }

    // Construtor utilitário para instanciar o pedido já avaliando divergências
    public Pedido(String canalVenda, String codigoPedidoExterno, String statusMarketplace, String statusErp, LocalDateTime dataVenda) {
        this.canalVenda = canalVenda;
        this.codigoPedidoExterno = codigoPedidoExterno;
        this.statusMarketplace = statusMarketplace;
        this.statusErp = statusErp;
        this.dataVenda = dataVenda;
        this.avaliarDivergencia();
    }

    // --- Automação Sênior: Atualiza a data automaticamente no banco ---
    @PrePersist
    protected void onCreate() {
        this.ultimaAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.ultimaAtualizacao = LocalDateTime.now();
    }

    // Regra de negócio sênior: Motor interno que detecta se os status batem
    public void avaliarDivergencia() {
        if (statusMarketplace == null || statusErp == null) {
            this.divergente = true;
            return;
        }

        String marketTrim = statusMarketplace.trim().toUpperCase();
        String erpTrim = statusErp.trim().toUpperCase();

        if (marketTrim.contains("APROVADO") && erpTrim.contains("PENDENTE")) {
            this.divergente = true;
        } else if (!marketTrim.equals(erpTrim) && !marketTrim.contains("PROCESSANDO")) {
            this.divergente = true;
        } else {
            this.divergente = false;
        }
    }

    // --- Getters e Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCanalVenda() {
        return canalVenda;
    }

    public void setCanalVenda(String canalVenda) {
        this.canalVenda = canalVenda;
    }

    public String getCodigoPedidoExterno() {
        return codigoPedidoExterno;
    }

    public void setCodigoPedidoExterno(String codigoPedidoExterno) {
        this.codigoPedidoExterno = codigoPedidoExterno;
    }

    public String getStatusMarketplace() {
        return statusMarketplace;
    }

    public void setStatusMarketplace(String statusMarketplace) {
        this.statusMarketplace = statusMarketplace;
        this.avaliarDivergencia(); // Recalcula automaticamente ao mudar o status
    }

    public String getStatusErp() {
        return statusErp;
    }

    public void setStatusErp(String statusErp) {
        this.statusErp = statusErp;
        this.avaliarDivergencia(); // Recalcula automaticamente ao mudar o status
    }

    public boolean isDivergente() {
        return divergente;
    }

    public void setDivergente(boolean divergente) {
        this.divergente = divergente;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    public LocalDateTime getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }
}