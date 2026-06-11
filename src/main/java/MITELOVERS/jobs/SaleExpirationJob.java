package MITELOVERS.jobs;

import MITELOVERS.applicationservices.SaleExpirationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SaleExpirationJob {

    private final SaleExpirationService _expirationService;

    public SaleExpirationJob(SaleExpirationService expirationService) {

        _expirationService = expirationService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void run() {

        _expirationService.expireAllExpiredSales();
    }

}
