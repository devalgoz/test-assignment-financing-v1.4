package lu.crx.financing.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.crx.financing.entities.Invoice;
import lu.crx.financing.entities.FinancedInvoice;
import lu.crx.financing.repository.InvoiceRepository;
import lu.crx.financing.unpersisted.Financing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinancingService {

    private final InvoiceRepository invoiceRepository;

    private final PurchaserSelectingService purchaserSelectingService;
    private final InvoiceProcessingService invoiceProcessingService;

    @Value("${application.pageSize}")
    private int pageSize;

    @Transactional
    public void finance() {
        log.info("Financing started");
        log.info("Databse contains {} financed invoices", invoiceRepository.countByFinancedTrue());
        log.info("Processing {} unpaid invoices", invoiceRepository.countByFinancedFalse());
        AtomicInteger financedInvoicesCount = new AtomicInteger();
        boolean stop;
        int pageNumber = 0;
        do {
            PageRequest pageRequest = PageRequest.of(pageNumber++, pageSize, Sort.unsorted());
            Slice<Long> ids = invoiceRepository.findNonFinancedInvoicesIds(pageRequest);
            Map<Invoice, List<Financing>> financings = purchaserSelectingService.getEligiblePurchasers(ids.getContent());

            for (Invoice invoice : financings.keySet()) {
                financings
                        .get(invoice)
                        .stream()
                        .min(Comparator.comparing(financing -> financing.purchaserFinancingSettings().getAnnualRateInBps()))
                        .ifPresent(financing -> {
                            financedInvoicesCount.getAndIncrement();
                            FinancedInvoice financedInvoice = invoiceProcessingService.process(financing);
                            log.info("Saving financed invoice {} based on  annual rate {} by purchaser {} for amout rate {}",
                                    financedInvoice.getInvoice(),
                                    financing.purchaserFinancingSettings().getAnnualRateInBps(),
                                    financing.purchaserFinancingSettings().getPurchaser(),
                                    financedInvoice.getAmount()
                            );
                        });
            }

            stop  = !ids.hasNext();
        } while (!stop);
        log.info("{} invoices have been financed", financedInvoicesCount.get());
        log.info("Financing completed");
    }

}
