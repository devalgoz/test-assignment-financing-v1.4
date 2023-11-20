package lu.crx.financing.unpersisted;

import lombok.NonNull;
import lu.crx.financing.entities.Invoice;
import lu.crx.financing.entities.PurchaserFinancingSettings;

public record Financing(@NonNull Invoice invoice, @NonNull PurchaserFinancingSettings purchaserFinancingSettings) {
}
