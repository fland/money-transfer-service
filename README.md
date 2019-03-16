# money-transfer-service

## Building
Build service using command `mvn clean package`

## Running
Execute service using command `java -jar money-service-1.0-SNAPSHOT.jar`

Configurable environment variable - `SERVER_PORT`, defaults to `8080`

## Usage

Retrieve account:
```http request
GET http://localhost:8080/api/account/12344
Accept: */*
Cache-Control: no-cache
```

Create account:
```http request
POST http://localhost:8080/api/account
Accept: */*
Cache-Control: no-cache
Content-Type: application/json

{"number": 12344, "balance": 100.0, "name":  "account1"}
```

Start transactions processing:
```http request
GET http://localhost:8080/api/processing/start
Accept: */*
Cache-Control: no-cache
```

Stop transactions processing:
```http request
GET http://localhost:8080/api/processing/stop
Accept: */*
Cache-Control: no-cache
```

Create transaction:
```http request
POST http://localhost:8080/api/transfer
Accept: */*
Cache-Control: no-cache
Content-Type: application/json

{"sourceAccount": 12344, "destinationAccount": 12345, "amount": 17.10}
```

Retrieve transaction status:
```http request
GET http://localhost:8080/api/transfer/8d358004-7495-4a3f-85d1-124a84ca7783
Accept: */*
Cache-Control: no-cache
```

Retrieve transactions per account:
```http request
GET http://localhost:8080/api/transfer/account/12345
Accept: */*
Cache-Control: no-cache
```