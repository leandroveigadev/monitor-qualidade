package monitor_qualidade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MonitorQualidadeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitorQualidadeApplication.class, args);
	}

}
