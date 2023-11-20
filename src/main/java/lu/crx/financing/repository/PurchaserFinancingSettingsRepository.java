package lu.crx.financing.repository;

import lu.crx.financing.entities.PurchaserFinancingSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaserFinancingSettingsRepository extends JpaRepository<PurchaserFinancingSettings, Long> {

    @Query("""
            select invoice, purchaserFinancingSettings 
            from PurchaserFinancingSettings purchaserFinancingSettings
            join purchaserFinancingSettings.creditor.invoices invoice
            join fetch purchaserFinancingSettings.purchaser purchaser
            where invoice.id in :ids
            and function('datediff', 'day', current_date, invoice.maturityDate) >= purchaser.minimumFinancingTermInDays
            and purchaserFinancingSettings.annualRateInBps * 60 / 360 <= invoice.creditor.maxFinancingRateInBps
            """)

    List<Object[]> findFinancingsForInvoicesById(List<Long> ids);
}
