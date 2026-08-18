package monitor_qualidade.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "integracao_log")
public class IntegracaoLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "canal_venda", nullable = false)
    private String canalVenda;

    @Column(name = "codigo_pedido_externo")
    private String codigoPedidoExterno;

    @Column(name = "status_integracao", nullable = false)
    private String statusIntegracao; // SUCESSO, FALHA, DIVERGENCIA

    @Column(name = "mensagem_erro", columnDefinition = "TEXT")
    private String mensagemErro;

    @Column(name = "data_registro", nullable = false)
    private LocalDateTime dataRegistro;

    // Construtor padrão exigido pelo JPA
    public IntegracaoLog() {
    }

    // Construtor prático para registro rápido nos Services
    public IntegracaoLog(String canalVenda, String codigoPedidoExterno, String statusIntegracao, String mensagemErro) {
        this.canalVenda = canalVenda;
        this.codigoPedidoExterno = codigoPedidoExterno;
        this.statusIntegracao = statusIntegracao;
        this.mensagemErro = mensagemErro;
        this.dataRegistro = LocalDateTime.now();
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

    public String getStatusIntegracao() {
        return statusIntegracao;
    }

    public void setStatusIntegracao(String statusIntegracao) {
        this.statusIntegracao = statusIntegracao;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}