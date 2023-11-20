package lu.crx.financing.test;

import lu.crx.financing.entities.Invoice;
import lu.crx.financing.entities.PurchaserFinancingSettings;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;

import static lu.crx.financing.util.Constants.DAYS_PER_MONTH;
import static lu.crx.financing.util.Constants.DAYS_PER_YEAR;

public class FinancingTest {

    protected boolean isPurchaserEligible(final Invoice invoice, final PurchaserFinancingSettings purchaserFinancingSettings) {
        if (!invoice.getCreditor().equals(purchaserFinancingSettings.getCreditor())) {
            return false;
        }

        final int maxFinancingRateInBps = invoice.getCreditor().getMaxFinancingRateInBps();
        final int annualRateInBps = purchaserFinancingSettings.getAnnualRateInBps();
        final LocalDate now = LocalDate.now();
        final int minimumFinancingTermInDays = purchaserFinancingSettings.getPurchaser().getMinimumFinancingTermInDays();
        final long numberOfDaysUntilMaturityDate = getDureationBetwwenDatesInDays(now, invoice.getMaturityDate());
        return annualRateInBps * DAYS_PER_MONTH / DAYS_PER_YEAR <= maxFinancingRateInBps &&
                numberOfDaysUntilMaturityDate >= minimumFinancingTermInDays;
    }

    private long getDureationBetwwenDatesInDays(final LocalDate date1, final LocalDate date2) {
        return Duration.between(date1.atStartOfDay(ZoneId.systemDefault()), date2.atStartOfDay(ZoneId.systemDefault())).toDays();
    }
}
