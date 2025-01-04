package it.unibo.samplejavafx;

import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@Data
@ConfigurationProperties(prefix = "cinema")
public class CinemaConfigurationProperties {

  Correlation correlation = new Correlation();

  SSE sse = new SSE();

  @Data
  public static class Correlation {
    Duration timeout = Duration.ofSeconds(1);

    /** Indica se propagare l'header X-CORRELATION-ID eventualmente passato dal client. */
    private Boolean propagateCorrelationId = true;
  }

  @Data
  public static class SSE {
    private Duration pingerInterval = Duration.ofSeconds(60);
  }

  @Bean(name = "entityManagerFactory")
  public LocalSessionFactoryBean sessionFactory() {
    return new LocalSessionFactoryBean();
  }
}
