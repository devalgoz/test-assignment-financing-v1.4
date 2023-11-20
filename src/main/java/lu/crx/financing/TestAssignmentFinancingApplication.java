package lu.crx.financing;

import lombok.extern.slf4j.Slf4j;
import lu.crx.financing.services.FinancingService;
import lu.crx.financing.services.SeedingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
@Slf4j
public class TestAssignmentFinancingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestAssignmentFinancingApplication.class, args);
    }


    @Bean
    @Profile("production")
    public CommandLineRunner runPerformanceTest(@Value("${performanceTestMode:false}") boolean performanceTestMode,SeedingService seedingService, FinancingService financingService) {

        return args -> {
            if (performanceTestMode) {
                log.info("Running application in performance test mode");
                seedingService.clearDatabase();
                seedingService.seedMasterData();
                log.info("Seeding the invoices. This may take several minutes...");
                for (int i = 0; i < 1000; i++) {
                    seedingService.seedPerformanceTestPaidInvoices();
                }
            } else {
                log.info("Running application in performance normal mode");
                seedingService.seedMasterData();
                log.info("Seeding the invoices");
                seedingService.seedInvoices();
            }
            LocalDateTime start = LocalDateTime.now();
            financingService.finance();
            Duration duration = Duration.between(start, LocalDateTime.now());
            log.info("Processing took {} seconds and {} milliseconds", duration.getSeconds(), duration.getNano() / 1000000);
        };
    }

}
