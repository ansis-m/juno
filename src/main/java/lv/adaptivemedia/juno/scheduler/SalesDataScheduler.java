package lv.adaptivemedia.juno.scheduler;

import lv.adaptivemedia.juno.config.SalesDataSchedulerConfig;
import lv.adaptivemedia.juno.service.SalesDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
public class SalesDataScheduler {

    private static final Logger log = LoggerFactory.getLogger(SalesDataScheduler.class);
    private final SalesDataService salesDataService;
    private final SalesDataSchedulerConfig config;

    private LocalDateTime historicalDataStart = LocalDateTime.now().minusDays(10);
    private LocalDateTime currentDataStart = LocalDateTime.now();

    @Autowired
    public SalesDataScheduler(SalesDataService salesDataService, SalesDataSchedulerConfig config) {
        this.salesDataService = salesDataService;
        this.config = config;
    }

    /**
     * Scheduled task that inserts historical transaction data.
     * Starts and runs for a limited number of runs
     */
    @Scheduled(fixedRate = 500, timeUnit = TimeUnit.MILLISECONDS)
    public void insertHistoricalTransactionData() {
        while (historicalDataStart.isBefore(LocalDateTime.now())) {
            String trackingId = salesDataService.insertTransactionData(historicalDataStart);
            historicalDataStart = historicalDataStart.plusMinutes(5);
            log.info("Inserted historical transaction data with tracking ID: {} at {}", trackingId, historicalDataStart);
        }
    }

    /**
     * Scheduled task that inserts generated sales data.
     * Starts and runs indefinitely.
     */
    @Scheduled(fixedRate = 500, timeUnit = TimeUnit.MILLISECONDS)
    public void insertTransactionalData() {
        if (config.isSchedulerEnabled()) {
            while (currentDataStart.isBefore(LocalDateTime.now())) {
                String trackingId = salesDataService.insertTransactionData(currentDataStart);
                currentDataStart = currentDataStart.plusSeconds(5);
                log.info("Inserted generated transaction data with tracking ID: {} at {}", trackingId, currentDataStart);
            }
        } else {
            log.warn("Sales data scheduler is disabled");
        }
    }
}
