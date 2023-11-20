package lu.crx.financing.entities;

import lombok.*;
import lu.crx.financing.unpersisted.Financing;

import javax.persistence.*;

/**
 * An {@link Invoice} financed by the {@link PurchaserFinancingSettings}
 */
@Entity
@Getter
@ToString(exclude = {"purchaserFinancingSettings"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancedInvoice {

    @EmbeddedId
    private FinancedInvoiceId id;

    @OneToOne(optional = false)
    @JoinColumn(name = "invoice_id", insertable = false, updatable = false)
    private Invoice invoice;

    @ManyToOne(optional = false)
    @JoinColumn(name = "purchaser_financing_settings_id", insertable = false, updatable = false)
    private PurchaserFinancingSettings purchaserFinancingSettings;

    @Basic(optional = false)
    private long amount;

    public FinancedInvoice(@NonNull Financing financing, long amout) {
        this.invoice = financing.invoice();
        this.purchaserFinancingSettings = financing.purchaserFinancingSettings();
        this.id = new FinancedInvoiceId(invoice.getId(), purchaserFinancingSettings.getId());
        this.amount = amout;
    }
}
