package lu.crx.financing.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.crx.financing.entities.*;
import lu.crx.financing.repository.FinancedInvoiceRepository;
import lu.crx.financing.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeedingService {

    private final EntityManager entityManager;

    private final InvoiceRepository invoiceRepository;

    private final FinancedInvoiceRepository financedInvoiceRepository;

    private Creditor creditor1;
    private Creditor creditor2;
    private Creditor creditor3;

    private Debtor debtor1;
    private Debtor debtor2;
    private Debtor debtor3;

    private Purchaser purchaser1;
    private Purchaser purchaser2;
    private Purchaser purchaser3;

    @Transactional
    public void seedPerformanceTestPaidInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            invoices.addAll(this.seedInvoices());
        }

        List<FinancedInvoice> financedInvoices = new ArrayList<>();
        invoices
                .stream()
                .filter(invoice -> {
                    final int index = invoices.indexOf(invoice);
                    return index % 75 != 0;
                })
                .forEach(invoice -> {
                    PurchaserFinancingSettings purchaserFinancingSettings =
                            purchaser1.getPurchaserFinancingSettings().stream().toList().get(0);
                    invoice.setFinanced(true);

                    financedInvoices.add(
                            FinancedInvoice.builder()
                                    .purchaserFinancingSettings(purchaserFinancingSettings)
                                    .invoice(invoice)
                                    .id(new FinancedInvoiceId(invoice.getId(), purchaserFinancingSettings.getId()))
                                    .build()
                    );
                });
        invoiceRepository.saveAll(invoices);
        financedInvoiceRepository.saveAll(financedInvoices);
    }

    @Transactional
    public void seedMasterData() {
        log.info("Seeding master data");

        // creditors
        creditor1 = Creditor.builder()
                .name("Coffee Beans LLC")
                .maxFinancingRateInBps(5)
                .build();
        entityManager.persist(creditor1);

        creditor2 = Creditor.builder()
                .name("Home Brew")
                .maxFinancingRateInBps(3)
                .build();
        entityManager.persist(creditor2);

        creditor3 = Creditor.builder()
                .name("Beanstalk")
                .maxFinancingRateInBps(2)
                .build();
        entityManager.persist(creditor3);

        // debtors
        debtor1 = Debtor.builder()
                .name("Chocolate Factory")
                .build();
        entityManager.persist(debtor1);

        debtor2 = Debtor.builder()
                .name("Sweets Inc")
                .build();
        entityManager.persist(debtor2);

        debtor3 = Debtor.builder()
                .name("ChocoLoco")
                .build();
        entityManager.persist(debtor3);

        // purchasers
        purchaser1 = Purchaser.builder()
                .name("RichBank")
                .minimumFinancingTermInDays(10)
                .purchaserFinancingSetting(PurchaserFinancingSettings.builder()
                        .creditor(creditor1)
                        .annualRateInBps(50)
                        .build())
                .purchaserFinancingSetting(PurchaserFinancingSettings.builder()
                        .creditor(creditor2)
                        .annualRateInBps(60)
                        .build())
                .purchaserFinancingSetting(PurchaserFinancingSettings.builder()
                        .creditor(creditor3)
                        .annualRateInBps(30)
                        .build())
                .build();

        purchaser2 = Purchaser.builder()
                .name("FatBank")
                .minimumFinancingTermInDays(12)
                .purchaserFinancingSetting(PurchaserFinancingSettings.builder()
                        .creditor(creditor1)
                        .annualRateInBps(40)
                        .build())
                .purchaserFinancingSetting(PurchaserFinancingSettings.builder()
                        .creditor(creditor2)
                        .annualRateInBps(80)
                        .build())
                .purchaserFinancingSetting(PurchaserFinancingSettings.builder()
                        .creditor(creditor3)
                        .annualRateInBps(25)
                        .build())
                .build();

        purchaser3 = Purchaser.builder()
                .name("MegaBank")
                .minimumFinancingTermInDays(8)
                .purchaserFinancingSetting(PurchaserFinancingSettings.builder()
                        .creditor(creditor1)
                        .annualRateInBps(30)
                        .build())
                .purchaserFinancingSetting(PurchaserFinancingSettings.builder()
                        .creditor(creditor2)
                        .annualRateInBps(50)
                        .build())
                .purchaserFinancingSetting(PurchaserFinancingSettings.builder()
                        .creditor(creditor3)
                        .annualRateInBps(45)
                        .build())
                .build();

        for (Purchaser purchaser : List.of(purchaser1, purchaser2, purchaser3)) {
            purchaser.getPurchaserFinancingSettings().forEach(setting -> setting.setPurchaser(purchaser));
            entityManager.persist(purchaser);
        }
    }

    @Transactional
    public List<Invoice> seedInvoices() {
        return List.of(
                invoiceRepository.save(Invoice.builder()
                        .creditor(creditor1)
                        .debtor(debtor1)
                        .valueInCents(200000)
                        .maturityDate(LocalDate.now().plusDays(52))
                        .build()),

                invoiceRepository.save(Invoice.builder()
                        .creditor(creditor1)
                        .debtor(debtor2)
                        .valueInCents(800000)
                        .maturityDate(LocalDate.now().plusDays(33))
                        .build()),

                invoiceRepository.save(Invoice.builder()
                        .creditor(creditor1)
                        .debtor(debtor3)
                        .valueInCents(600000)
                        .maturityDate(LocalDate.now().plusDays(43))
                        .build()),

                invoiceRepository.save(Invoice.builder()
                        .creditor(creditor1)
                        .debtor(debtor1)
                        .valueInCents(500000)
                        .maturityDate(LocalDate.now().plusDays(80))
                        .build()),

                invoiceRepository.save(Invoice.builder()
                        .creditor(creditor1)
                        .debtor(debtor2)
                        .valueInCents(6000000)
                        .maturityDate(LocalDate.now().plusDays(5))
                        .build()),

                invoiceRepository.save(Invoice.builder()
                        .creditor(creditor2)
                        .debtor(debtor3)
                        .valueInCents(500000)
                        .maturityDate(LocalDate.now().plusDays(10))
                        .build()),

                invoiceRepository.save(Invoice.builder()
                        .creditor(creditor2)
                        .debtor(debtor1)
                        .valueInCents(800000)
                        .maturityDate(LocalDate.now().plusDays(15))
                        .build()),

                invoiceRepository.save(Invoice.builder()
                        .creditor(creditor2)
                        .debtor(debtor2)
                        .valueInCents(9000000)
                        .maturityDate(LocalDate.now().plusDays(30))
                        .build()),

                invoiceRepository.save(Invoice.builder()
                        .creditor(creditor2)
                        .debtor(debtor3)
                        .valueInCents(450000)
                        .maturityDate(LocalDate.now().plusDays(32))
                        .build()),

                invoiceRepository.save(Invoice.builder()
                        .creditor(creditor2)
                        .debtor(debtor1)
                        .valueInCents(800000)
                        .maturityDate(LocalDate.now().plusDays(11))
                        .build()),

                invoiceRepository.save(Invoice.builder()
                        .creditor(creditor3)
                        .debtor(debtor2)
                        .valueInCents(3000000)
                        .maturityDate(LocalDate.now().plusDays(10))
                        .build()),

                invoiceRepository.save(Invoice.builder()
                        .creditor(creditor3)
                        .debtor(debtor3)
                        .valueInCents(5000000)
                        .maturityDate(LocalDate.now().plusDays(14))
                        .build()),

                invoiceRepository.save(Invoice.builder()
                        .creditor(creditor3)
                        .debtor(debtor1)
                        .valueInCents(9000000)
                        .maturityDate(LocalDate.now().plusDays(23))
                        .build()),

                invoiceRepository.save(Invoice.builder()
                        .creditor(creditor3)
                        .debtor(debtor2)
                        .valueInCents(800000)
                        .maturityDate(LocalDate.now().plusDays(18))
                        .build()),

                invoiceRepository.save(Invoice.builder()
                        .creditor(creditor3)
                        .debtor(debtor3)
                        .valueInCents(9000000)
                        .maturityDate(LocalDate.now().plusDays(50))
                        .build())
        );

    }

    @Transactional
    public void clearDatabase() {
        log.info("Clearing database data. This may take several minutes...");
        List.of(
                FinancedInvoice.class,
                Invoice.class,
                PurchaserFinancingSettings.class,
                Purchaser.class,
                Debtor.class,
                Creditor.class,
                Invoice.class
        ).forEach(entityClass -> entityManager
                .createQuery(String.format("delete from %s", entityClass.getName()))
                .executeUpdate()
        );
    }

}
