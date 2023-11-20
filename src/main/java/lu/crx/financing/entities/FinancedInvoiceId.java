package lu.crx.financing.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FinancedInvoiceId implements Serializable {

    @Column(name = "invoice_id")
    private long invoiceId;

    @Column(name = "purchaser_financing_settings_id")
    private long purchaserFinancingSettingsId;
}
