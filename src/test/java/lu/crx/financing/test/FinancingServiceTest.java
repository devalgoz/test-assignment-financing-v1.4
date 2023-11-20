package lu.crx.financing.test;

import lu.crx.financing.entities.FinancedInvoice;
import lu.crx.financing.entities.Invoice;
import lu.crx.financing.entities.PurchaserFinancingSettings;
import lu.crx.financing.repository.FinancedInvoiceRepository;
import lu.crx.financing.repository.PurchaserFinancingSettingsRepository;
import lu.crx.financing.services.FinancingService;
import lu.crx.financing.services.SeedingService;
import lu.crx.financing.test.config.TestDataUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.Comparator;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = {"lu.crx.financing"})
public class FinancingServiceTest extends FinancingTest {

    @Autowired
    private SeedingService seedingService;

    @Autowired
    private FinancingService financingService;

    @Autowired
    private PurchaserFinancingSettingsRepository purchaserFinancingSettingsRepository;

    @Autowired
    private FinancedInvoiceRepository financedInvoiceRepository;


    @Test
    public void testFinance() {
        seedingService.seedMasterData();
        seedingService.seedInvoices();
        financingService.finance();

        List<PurchaserFinancingSettings> purchaserFinancingSettings = purchaserFinancingSettingsRepository.findAll();
        List<FinancedInvoice> financedInvoices = financedInvoiceRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparing(financedInvoice -> financedInvoice.getInvoice().getId()))
                .toList();
        Assertions.assertEquals(financedInvoices.size(), 4);
        List<Long> amounts = List.of(199998L, 799998L, 599998L, 499998L);
        for (int i = 0; i < financedInvoices.size(); i++) {
            verifyFinancig(financedInvoices.get(i), purchaserFinancingSettings, amounts.get(i));
        }
    }

    private void verifyFinancig(FinancedInvoice financedInvoice, List<PurchaserFinancingSettings> purchaserFinancingSettings, Long amount) {
        Invoice invoice = financedInvoice.getInvoice();
        PurchaserFinancingSettings purchaserFinancingSetting =
                purchaserFinancingSettings
                        .stream()
                        .filter(financingSetting -> isPurchaserEligible(invoice, financingSetting))
                        .min(Comparator.comparing(PurchaserFinancingSettings::getAnnualRateInBps))
                        .orElseThrow(() -> new IllegalStateException(String.format("No Eligible purchaser for invoice %s", invoice)));

        Assertions.assertEquals(financedInvoice.getPurchaserFinancingSettings(), purchaserFinancingSetting);
        Assertions.assertEquals(financedInvoice.getAmount(), amount);
    }
}
