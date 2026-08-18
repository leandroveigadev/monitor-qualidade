package monitor_qualidade.repository;

import monitor_qualidade.model.IntegracaoLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntegracaoLogRepository extends JpaRepository<IntegracaoLog, Long> {

}