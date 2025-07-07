package lv.adaptivemedia.juno.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lv.adaptivemedia.juno.config.SalesDataSchedulerConfig;
import lv.adaptivemedia.juno.dto.SalesDataDto;
import lv.adaptivemedia.juno.service.SalesDataService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping()
@Tag(name = "Sales Data", description = "API for retrieving sales data")
public class SalesDataController {

    private final SalesDataService salesDataService;
    private final SalesDataSchedulerConfig schedulerConfig;

    public SalesDataController(SalesDataService salesDataService, SalesDataSchedulerConfig schedulerConfig) {
        this.salesDataService = salesDataService;
        this.schedulerConfig = schedulerConfig;
    }

    /**
     * Endpoint to retrieve sales data within a specified date range.
     *
     * @param fromDate the start date (inclusive) in ISO format (yyyy-MM-dd)
     * @param toDate   the end date (inclusive) in ISO format (yyyy-MM-dd)
     * @return a list of sales data records within the date range
     */
    @Operation(
            summary = "Get sales data by date range",
            description = "Retrieves sales data records within the specified date range (inclusive)"
    )
    @GetMapping("/sales-data")
    public ResponseEntity<List<SalesDataDto>> getSalesData(
            @Parameter(description = "Start date (inclusive) in ISO format (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @Parameter(description = "End date (inclusive) in ISO format (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        List<SalesDataDto> salesData = salesDataService.getSalesDataByDateRange(fromDate, toDate);
        return ResponseEntity.ok(salesData);
    }

    /**
     * Endpoint to toggle the sales data scheduler on/off.
     *
     * @return the current state of the scheduler after toggling
     */
    @Operation(
            summary = "Toggle sales data scheduler",
            description = "Toggles the sales data scheduler on/off and returns the new state"
    )
    @PostMapping("/sales-data/scheduler/toggle")
    public ResponseEntity<Boolean> toggleScheduler() {
        schedulerConfig.toggleScheduler();
        boolean isEnabled = schedulerConfig.isSchedulerEnabled();
        return ResponseEntity.ok(isEnabled);
    }
}
