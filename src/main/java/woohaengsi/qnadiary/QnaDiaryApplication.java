package woohaengsi.qnadiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class QnaDiaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(QnaDiaryApplication.class, args);
	}

}
