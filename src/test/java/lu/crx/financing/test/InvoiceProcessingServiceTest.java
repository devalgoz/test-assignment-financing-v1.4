package lu.crx.financing.test;

import lu.crx.financing.entities.FinancedInvoice;
import lu.crx.financing.entities.Invoice;
import lu.crx.financing.entities.PurchaserFinancingSettings;
import lu.crx.financing.repository.FinancedInvoiceRepository;
import lu.crx.financing.repository.InvoiceRepository;
import lu.crx.financing.services.InvoiceProcessingService;
import lu.crx.financing.unpersisted.Financing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class InvoiceProcessingServiceTest {

    @Mock
    FinancedInvoiceRepository financedInvoiceRepository;

    @Mock
    InvoiceRepository invoiceRepository;

    private InvoiceProcessingService invoiceProcessingService;

    Invoice invoice;
    PurchaserFinancingSettings purchaserFinancingSettings;
    Financing financing;

    @BeforeEach
    public void setup() {
        invoiceProcessingService = Optional
                .ofNullable(invoiceProcessingService)
                .orElse(new InvoiceProcessingService(financedInvoiceRepository, invoiceRepository));

        invoice = Invoice.builder()
                .id(7L)
                .valueInCents(80000)
                .build();

        purchaserFinancingSettings = PurchaserFinancingSettings.builder()
                .annualRateInBps(50)
                .build();

        financing = new Financing(invoice, purchaserFinancingSettings);
    }

    @Test
    public void processTest_exact_amount() {
        Mockito.when(financedInvoiceRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());
        Mockito.when(invoiceRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());
        FinancedInvoice financedInvoice = invoiceProcessingService.process(financing);
        Assertions.assertEquals(financedInvoice.getAmount(), 79996);
    }

}

