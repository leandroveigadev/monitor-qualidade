package monitor_qualidade.controller;

import monitor_qualidade.model.Pedido;
import monitor_qualidade.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService service;

    // Injeção de dependência via construtor (Padrão Sênior que substitui o @Autowired de campo)
    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody Pedido pedido) {
        // Passa os 3 valores exigidos pelo PedidoService
        Pedido pedidoProcessado = service.processarPedido(
                pedido.getCanalVenda(),
                pedido.getCodigoPedidoExterno(),
                pedido.getStatusMarketplace()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoProcessado);
    }
}