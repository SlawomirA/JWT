package thesis.backend.jwt.auditing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class AuditConfig {

    @Bean
    public AuditorAware<Integer> auditorProvider() {
        return new ApplicationAuditAware();
    }
}
