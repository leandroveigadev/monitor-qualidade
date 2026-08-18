package monitor_qualidade.repository;

import monitor_qualidade.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Permite buscar um pedido pelo código externo do marketplace (essencial para atualizações)
    Optional<Pedido> findByCodigoPedidoExterno(String codigoPedidoExterno);

    // Permite ao motor de relatórios listar rapidamente todos os pedidos com divergência ativa
    List<Pedido> findByDivergente(boolean divergente);

    // Permite filtrar auditorias por canal específico
    List<Pedido> findByCanalVenda(String canalVenda);

}