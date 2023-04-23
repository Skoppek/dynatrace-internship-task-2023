# Installation
## Using .jar file

1. Clone the repo

> ```git clone https://github.com/Skoppek/dynatrace-internship-task-2023.git```

2. Run the application 

> using **java -jar** command
>  ```java -jar target/backendTask-1.0.0.jar```

  **OR**  
  
>  using **mvn spring-boot:run** command
>  ```mvn spring-boot:run```

## Using Docker
1. Pull the image

> ```docker pull skoppek/skop_intern_backend_task```

2. Run the docker image

> ```docker run -p 8080:8080 skop_intern_backend_task```

# Usage
## Swagger UI
To use application's Swagger UI interface use http://localhost:8080/swagger-ui/index.html endpoint.

# Endpoints
- Average exchange rate for given currency code on a given date

```/average-exchange-rate/code/{code}/date/{date}```
--{code} - currency code in accordane with ISO 4217
--{date} - date in YYYY-MM-DD format

- Max and min average rate value for given currency code of last N <= 255 quotations

```/max-min-average-value/code/{code}?quotations={quotations}```
--{code} - currency code in accordane with ISO 4217
--{quotations} - number of quotations

- the major difference between the buy and ask rate for given currency code of last N <= 255 quotations

```/average-exchange-rate/code/{code}/date/{date}```
--{code} - currency code in accordane with ISO 4217
--{quotations} - number of quotations

