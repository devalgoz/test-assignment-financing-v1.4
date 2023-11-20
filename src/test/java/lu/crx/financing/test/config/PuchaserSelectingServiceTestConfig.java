package lu.crx.financing.test.config;

import lu.crx.financing.repository.PurchaserFinancingSettingsRepository;
import lu.crx.financing.services.PurchaserSelectingService;
import org.springframework.context.annotation.Bean;

public class PuchaserSelectingServiceTestConfig {

    @Bean
    PurchaserSelectingService puchaserSelectingService(PurchaserFinancingSettingsRepository purchaserFinancingSettingsRepository) {
        return new PurchaserSelectingService(purchaserFinancingSettingsRepository);
    }
}
