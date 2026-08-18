package monitor_qualidade.repository;

import monitor_qualidade.model.EventoWebhookRaw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoWebhookRawRepository extends JpaRepository<EventoWebhookRaw, Long> {

    // O Motor de Reconciliação vai usar este metodo para buscar no banco
    // apenas os eventos que ainda não foram processados (ex: "PENDENTE").
    List<EventoWebhookRaw> findByStatusProcessamento(String statusProcessamento);

}