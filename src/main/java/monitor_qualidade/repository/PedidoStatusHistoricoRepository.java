package monitor_qualidade.repository;

import monitor_qualidade.model.PedidoStatusHistorico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoStatusHistoricoRepository extends JpaRepository<PedidoStatusHistorico, Long> {

    // Metodo para o diagnóstico de causa raiz: traz a linha do tempo completa de um
    // pedido (ordenada da mais recente para a mais antiga)
    List<PedidoStatusHistorico> findByCodigoPedidoExternoOrderByDataMudancaDesc(String codigoPedidoExterno);

}