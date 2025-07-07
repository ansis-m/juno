package lv.adaptivemedia.juno.service;

import lv.adaptivemedia.juno.dto.SalesDataDto;
import lv.adaptivemedia.juno.jooq.tables.records.SalesDataRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

import static lv.adaptivemedia.juno.jooq.Tables.SALES_DATA;

@Service
public class SalesDataService {

    private final DSLContext dslContext;
    private final Random random = new Random();

    private final List<String> trackingIds = List.of(
            "ABB001", "ABB002", "ABB003", "ABB004", "ABB005",
            "TBS001", "TBS002", "TBS003", "TBS004", "TBS005",
            "EKW001", "EKW002", "EKW003", "EKW004", "EKW005",
            "ZKL001", "ZKL002", "ZKL003", "ZKL004", "ZKL005",
            "OLN001", "OLN002", "OLN003", "OLN004", "OLN005"
    );
    private final List<String> products = List.of(
            "Wireless Earbuds", "Smartphone Case", "Bluetooth Speaker", "Laptop Stand", "Portable Charger",
            "USB-C Hub", "Fitness Tracker", "LED Desk Lamp", "Noise Cancelling Headphones", "Smartwatch", "Electric Toothbrush",
            "Gaming Mouse", "Mechanical Keyboard", "Webcam", "Streaming Microphone", "Reusable Water Bottle", "Yoga Mat", "Desk Organizer"
    );

    public SalesDataService(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    /**
     * Retrieves sales data for a specified date range (inclusive).
     *
     * @param fromDate the start date (inclusive)
     * @param toDate   the end date (inclusive)
     * @return a list of sales data records within the date range
     */
    public List<SalesDataDto> getSalesDataByDateRange(LocalDate fromDate, LocalDate toDate) {
        // Convert LocalDate to LocalDateTime to include the entire day
        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime = toDate.atTime(LocalTime.MAX);

        List<SalesDataRecord> result = dslContext
                .selectFrom(SALES_DATA)
                .where(SALES_DATA.VISIT_DATE.ge(fromDateTime)
                        .and(SALES_DATA.VISIT_DATE.le(toDateTime)))
                .fetchInto(SalesDataRecord.class);

        return result.stream()
                .map(salesData ->
                        new SalesDataDto(
                                salesData.getId(),
                                salesData.getTrackingId(),
                                salesData.getVisitDate(),
                                salesData.getSaleDate(),
                                salesData.getSalePrice(),
                                salesData.getProduct(),
                                salesData.getCommissionAmount()
                        ))
                .toList();
    }

    /**
     * Inserts a new sales data record with generated data.
     * The fields sale_date, sale_price, and commission_amount are either all set or all null,
     * with the choice being random each time.
     *
     * @return the ID of the inserted record
     */
    public String insertTransactionData(LocalDateTime transactionDateTime) {
        // Select a random tracking ID from the trackingIds list
        String trackingId = trackingIds.get(random.nextInt(trackingIds.size()));

        // Select a random product from the product list
        String product = products.get(random.nextInt(products.size()));

        // Randomly decide whether to set sale-related fields or leave them null
        boolean includeSaleData = random.nextBoolean();

        // Start building the insert statement
        var insertStep = dslContext
                .insertInto(SALES_DATA)
                .set(SALES_DATA.TRACKING_ID, trackingId)
                .set(SALES_DATA.VISIT_DATE, transactionDateTime)
                .set(SALES_DATA.PRODUCT, product);

        // Either set all sale-related fields or leave them all null
        if (includeSaleData) {
            BigDecimal salePrice = new BigDecimal(random.nextInt(1000) + 1);
            BigDecimal commissionAmount = salePrice.multiply(BigDecimal.valueOf(random.nextFloat(0, 0.1f)));
            insertStep = insertStep
                    .set(SALES_DATA.SALE_DATE, transactionDateTime)
                    .set(SALES_DATA.SALE_PRICE, salePrice)
                    .set(SALES_DATA.COMMISSION_AMOUNT, commissionAmount);
        }

        // Execute the insert and return the ID
        return insertStep
                .returning(SALES_DATA.TRACKING_ID)
                .fetchOne()
                .getTrackingId();
    }
}
