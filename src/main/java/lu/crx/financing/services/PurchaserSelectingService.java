package lu.crx.financing.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.crx.financing.entities.Invoice;
import lu.crx.financing.entities.PurchaserFinancingSettings;
import lu.crx.financing.repository.PurchaserFinancingSettingsRepository;
import lu.crx.financing.unpersisted.Financing;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class PurchaserSelectingService {

    private final PurchaserFinancingSettingsRepository purchaserFinancingSettingsRepository;

    public Map<Invoice, List<Financing>> getEligiblePurchasers(List<Long> ids) {
        return purchaserFinancingSettingsRepository
                .findFinancingsForInvoicesById(ids)
                .stream()
                .map(tuple -> new Financing((Invoice) tuple[0],(PurchaserFinancingSettings) tuple[1]))
                .collect(Collectors.groupingBy(Financing::invoice));
    }
}
