## Requirements to Run the Application

- **IDE**: Any IDE suitable for Java development (e.g., IntelliJ IDEA, Eclipse).
- **Java**: JDK 17 or higher must be configured on your machine.
- **Maven**: Maven 3.9.x or higher for dependencies and builds.

---

## Setup Instructions

1. Clone the repository.
2. Open the project in your IDE.
3. Configure your IDE to use JDK 17 or higher.
4. On the project root run (needed to generate Jooq classes):
   ```bash
   ./mvnw clean compile
   ```
5. Run the application from your IDE using the provided [JunoApplication](run://configuration/JunoApplication) run configuration.

> **Note**: This application uses an in-memory H2 database. All data will be lost after a restart due to non-persistent storage.

---

## Data & Usage

This application generates historical transaction data on startup for past 10 days up to now.<br>
Application generates real-time transaction data every 5 seconds.<br>
You can pause the data generation with the toggle API (`POST /sales-data/scheduler/toggle`).

---

## API Description & Usage

The application's APIs can be accessed via the following base URL after starting the application:
```
http://localhost:8081
```

Swagger URL
```
http://localhost:8081/swagger-ui/index.html
```

***Sales Data API***:

- `GET /sales-data`<br>

Retrieves sales data records within a specified date range (inclusive).

Required query parameters:
- `fromDate`: Start date in ISO format (yyyy-MM-dd)
- `toDate`: End date in ISO format (yyyy-MM-dd)

Example request:
```
GET /sales-data?fromDate=2023-01-01&toDate=2023-01-31
```

Example response:
```json
[
  {
    "id": 1,
    "trackingId": "ABB003",
    "visitDate": "2023-01-15T10:30:00",
    "saleDate": "2023-01-15T11:45:00",
    "salePrice": 99.99,
    "product": "Premium Widget",
    "commissionAmount": 10.00
  },
  {
    "id": 2,
    "trackingId": "ABB003",
    "visitDate": "2023-01-20T14:15:00",
    "saleDate": null,
    "salePrice": null,
    "product": "Deluxe Gadget",
    "commissionAmount": null
  }
]
```

- `POST /sales-data/scheduler/toggle`<br>

Toggles the sales data scheduler on/off and returns the new state.

Example response:
```json
true
```

The response is a boolean indicating whether the scheduler is enabled (`true`) or disabled (`false`) after toggling.

---

## JOOQ Code Generation

This project uses JOOQ's code generation capabilities to generate type-safe SQL queries based on the database schema. 
The code generation is configured in the `pom.xml` file and is automatically executed during the Maven build process.

The JOOQ code generator creates Java classes that represent the database schema, tables, columns, and other database objects. 
These classes are generated in the `target/generated-sources/jooq` directory under the package `lv.adaptivemedia.juno.jooq`.

If IDE fails to locate the classes, reload the Maven project.
