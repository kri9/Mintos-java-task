Features:
Retrieve a list of accounts by clientId
Retrieve account transaction history with pagination (offset, limit)
Perform transfers between accounts, converting currency if needed
Balance validation (account balance must remain non-negative)
Integration with 2 external currency exchange APIs
Currency rates cached for 5 minutes using Caffeine
Global error handling via @ControllerAdvice
Unit & integration

This design ensures resilience:  
If the primary API is unavailable or returns an invalid response, the system automatically falls back to the secondary API to complete the transaction.

cURL Examples TO TEST

 Get client accounts

curl -X GET "http://localhost:8080/client/1/accounts" -H "Accept: application/json"


Get account transactions (offset):

curl -X GET "http://localhost:8080/account/1/transactions?offset=0&limit=10" -H "Accept: application/json"

Make a transfer (POST):

curl -X POST "http://localhost:8080/transaction" \
  -H "Content-Type: application/json" \
  -d '{
    "sourceAccount": 1,
    "destinationAccount": 2,
    "amount": 100.00,
    "description": "Payment"
}'
