package lu.crx.financing.test;

import lu.crx.financing.entities.Invoice;
import lu.crx.financing.entities.PurchaserFinancingSettings;
import lu.crx.financing.repository.FinancedInvoiceRepository;
import lu.crx.financing.repository.InvoiceRepository;
import lu.crx.financing.services.PurchaserSelectingService;
import lu.crx.financing.test.config.PuchaserSelectingServiceTestConfig;
import lu.crx.financing.test.config.TestDataUtil;
import lu.crx.financing.unpersisted.Financing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages  = {"lu.crx.financing.repository"})
@Import({PuchaserSelectingServiceTestConfig.class, TestDataUtil.class})
public class PurchaserSelectingServiceTest extends FinancingTest {

    private static final long INVOICE_ID1 = 19L;
    private static final long INVOICE_ID2 = 22L;

    private Invoice invoice1;
    private Invoice invoice2;

    @Autowired
    private PurchaserSelectingService purchaserSelectingService;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private FinancedInvoiceRepository financedInvoiceRepository;

    @BeforeEach
    public void setup() {
        financedInvoiceRepository.deleteAllInBatch();

        invoice1 = invoiceRepository.findById(INVOICE_ID1).orElseThrow(EntityNotFoundException::new);
        invoice2 = invoiceRepository.findById(INVOICE_ID2).orElseThrow(EntityNotFoundException::new);
    }

    @Test
    public void testGetEligiblePurchasers() {
        Map<Invoice, List<Financing>> financingRegistry = purchaserSelectingService.getEligiblePurchasers(List.of(19L, 22L));
        List<Invoice> invoices = financingRegistry
                .keySet()
                .stream()
                .sorted(Comparator.comparing(Invoice::getId))
                .toList();
        Assertions.assertEquals(invoices.get(0), invoice1);
        Assertions.assertEquals(invoices.get(1), invoice2);

        List<Financing> financings1 = financingRegistry.get(invoice1);
        List<Financing> financings2 = financingRegistry.get(invoice2);
        List.of(financings1, financings2).forEach(financings -> Assertions.assertEquals(financings.size(), 1));

        PurchaserFinancingSettings purchaserFinancingSettings1 = financings1.get(0).purchaserFinancingSettings();
        PurchaserFinancingSettings purchaserFinancingSettings2 = financings2.get(0).purchaserFinancingSettings();
        Assertions.assertTrue(isPurchaserEligible(invoice1, purchaserFinancingSettings1));
        Assertions.assertTrue(isPurchaserEligible(invoice2, purchaserFinancingSettings2));
    }

}
