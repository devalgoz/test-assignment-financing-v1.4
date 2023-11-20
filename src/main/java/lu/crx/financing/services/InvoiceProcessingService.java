package lu.crx.financing.services;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.crx.financing.entities.FinancedInvoice;
import lu.crx.financing.entities.Invoice;
import lu.crx.financing.repository.FinancedInvoiceRepository;
import lu.crx.financing.repository.InvoiceRepository;
import lu.crx.financing.unpersisted.Financing;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static lu.crx.financing.util.Constants.DAYS_PER_MONTH;
import static lu.crx.financing.util.Constants.DAYS_PER_YEAR;

@Slf4j
@Service
@RequiredArgsConstructor
@Builder
public class InvoiceProcessingService {

    private final FinancedInvoiceRepository financedInvoiceRepository;

    private final InvoiceRepository invoiceRepository;

    @Transactional
    public FinancedInvoice process(final @NonNull Financing financing) {
        Invoice invoice = financing.invoice();
        invoice.setFinanced(true);
        invoiceRepository.save(invoice);

        long amount =
                invoice.getValueInCents() -
                (financing.purchaserFinancingSettings().getAnnualRateInBps() * DAYS_PER_MONTH / DAYS_PER_YEAR);
        FinancedInvoice financedInvoice = new FinancedInvoice(financing, amount);
        return financedInvoiceRepository.save(financedInvoice);
    }

}
