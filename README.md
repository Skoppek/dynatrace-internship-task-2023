# Installation
## Using .jar file

1. Clone the repo

 ```
 git clone https://github.com/Skoppek/dynatrace-internship-task-2023.git
 ```

2. Run the application 

> using **java -jar** command
>  ```
>  java -jar target/backendTask-1.0.0.jar
>  ```

  **OR**  
  
>  using **mvn spring-boot:run** command
>  ```
>  mvn spring-boot:run
>  ```

## Using Docker
1. Pull the image

 ```
 docker pull skoppek/skop_intern_backend_task
 ```

2. Run the docker image

 ```
 docker run -p 8080:8080 skop_intern_backend_task
 ```

# Usage
## Swagger UI
To use application's Swagger UI interface use http://localhost:8080/swagger-ui/index.html endpoint.

# Endpoints
1. The average exchange rate for given currency code on a given date

```
/average-exchange-rate/code/{code}/date/{date}
```  
**{code}** - currency code in accordane with ISO 4217  
**{date}** - date in YYYY-MM-DD format  

2. Max and min average rate value for given currency code of last N <= 255 quotations

```
/max-min-average-value/code/{code}?quotations={quotations}
```  
**{code}** - currency code in accordane with ISO 4217  
**{quotations}** - number of quotations  

3. The major difference between the buy and ask rate for given currency code of last N <= 255 quotations

```
/average-exchange-rate/code/{code}/date/{date}
```  
**{code}** - currency code in accordane with ISO 4217  
**{quotations}** - number of quotations  

# Examples

* To query operation 1, run this command:
```
curl http://localhost:8080/average-exchange-rate/code/cad/date/2023-04-19
```
Expected result:
```json
{
    "averageExchangeRate":3.1472
}
```
* To query operation 2, run this command:
```
curl http://localhost:8080/max-min-average-value/code/usd?quotations=210
```
Example result (this result was taken on 2023-04-24 00:30):
```json
{
    "maxValue":5.0381, 
    "minValue":4.2006
}
```

* To query operation 3, run this command:
```
curl http://localhost:8080/buy-sell-difference/code/pln?quotations=255
```
Expected result:
Example result (this result was taken on 2023-04-24 00:30):
```json
{
    "difference":-0.0838
}
```
# Errors
* Wrong date or currency format or number of quotations beyond <1, 255> range will return one this responses:
```json
{
    "status": "BAD_REQUEST",
    "message": [
        "Date format should be: YYYY-MM-DD"
    ]
}
```
```json
{
    "status":"BAD_REQUEST",
    "message":[
        "Provide 3 letter currency code (ISO-4217)"
    ]
}
```
```json
{
    "status":"BAD_REQUEST",
    "message":[
        "must be less than or equal to 255"
    ]
}
```
```json
{
    "status":"BAD_REQUEST",
    "message":[
        "must be greater than or equal to 1"
    ]
}
```
* In case of no data being found the response will be:
```json
{
    "status":"NOT_FOUND",
    "message":[
        "No data found."
    ]
}
```
* If parameters ale not allowed by NBP API (e.g. year 2100) the respose will be:
```json
{
    "status":"BAD_REQUEST",
    "message":[
        "Wrong query parameters."
    ]
}
```

