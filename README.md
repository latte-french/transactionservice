# Revolut Backend Task

A project which:
- implements transaction service for money transfers between the accounts 
- implements RESTful API methods for managing users, accounts and transfers
- saves all data into the embedded database
- is a standalone application

## How to build and launch ##

`mvn clean install` to compile the code and run tests.

The resulting **transaction-service-1.0-SNAPSHOT.jar** can be found in the directory:

`\transactionservice\target\`

You can launch it via command 

`java -jar transactionservice\target\transaction-service-1.0-SNAPSHOT.jar`

The Spark application starts on http://localhost:8080

PUT and POST methods require a json as a parameter.
 

## RESTful API examples: ##

**GET account with id = 4000123412341234**

`curl http://localhost:8080/accounts/4000123412341234`

*Output:*

Account(id=4000123412341234, balance=23.56, currency=RUB)

**GET all accounts**

`curl http://localhost:8080/accounts`

*Output:*

[Account(id=4000123412341234, balance=23.56, currency=RUB), Account(id=4000123412341235, balance=5.8, currency=USD), Account(id=4000123412341236, balance=102.13, currency=EUR)]

**POST a new account which will belong to the user with id = 2**

`curl -H "Content-Type: application/json" -X POST -d "{\"balance\":\"5\",\"currency\":\"RUB\",\"userId\":\"2\"}" http://localhost:8080/accounts`

*Output:*

Account(id=4000123412341237, balance=5.0, currency=RUB)

**PUT an account with id = 4000123412341234, updating the balance and currency**

You can also update just balance or just currency

`curl -H "Content-Type: application/json" -X PUT -d "{\"balance\":\"5\",\"currency\":\"EUR\"}" http://localhost:8080/accounts/4000123412341234`

*Output:*

Account(id=4000123412341234, balance=5.0, currency=EUR)

**DELETE account with id = 4000123412341234**

`curl -X DELETE http://localhost:8080/accounts/4000123412341234`

*Output:*

Account deleted

**GET user with id = 1**

`curl http://localhost:8080/users/1`

*Output:*

User(id=1, firstName=Alex, lastName=Smith)

**GET all users**

`curl http://localhost:8080/accounts/4000123412341234`

*Output:*

[User(id=1, firstName=Alex, lastName=Smith), User(id=2, firstName=Clint, lastName=Eastwood), User(id=3, firstName=Peter, lastName=Pan)]

**GET all accounts of the user with id = 2**

`curl http://localhost:8080/users/2/accounts`

*Output:*

[Account(id=4000123412341235, balance=5.8, currency=USD), Account(id=4000123412341236, balance=102.13, currency=EUR), Account(id=4000123412341237, balance=5.0, currency=RUB)]

**POST a new user**

`curl -H "Content-Type: application/json" -X POST -d "{\"firstName\":\"Maria\",\"lastName\":\"Teresa\"}" http://localhost:8080/users`

*Output:*

User(id=4, firstName=Maria, lastName=Teresa)

**PUT a user with id = 4, updating his first and last names**

You can also change the first or the last name separately

`curl -H "Content-Type: application/json" -X PUT -d "{\"firstName\":\"Julia\",\"lastName\":\"Roberts\"}" http://localhost:8080/users/4`

*Output:*

User(id=4, firstName=Julia, lastName=Roberts)

**DELETE a user with id = 2**

Be careful, it will also delete all his accounts, to imitate a real banking system

`curl -X DELETE http://localhost:8080/users/2`

*Output:*

User deleted

**POST a transfer, from account with id = 4000123412341234 to account with id = 4000123412341235**

In this example we transfer 5 conventional units (in the currency of the fist account). In this case, it's 5.0 RUB. It will be converted into currency of the second account (to USD).

`curl -H "Content-Type: application/json" -X POST -d "{\"from\":\"4000123412341234\",\"to\":\"4000123412341235\",\"money\":\"5\"}" http://localhost:8080/transfers`

*Output:*

Transfer(accountFromId=4000123412341234, accountToId=4000123412341235, sumToTransfer=5.0, sumTransferred=0.0755, transferredAt=2019-08-25 18:56:19.238)

**GET all processed transfers**

`curl http://localhost:8080/transfers`

*Output:*

[Transfer(accountFromId=4000123412341234, accountToId=4000123412341235, sumToTransfer=5.0, sumTransferred=0.0755, transferredAt=2019-08-25 18:56:19.238), Transfer(accountFromId=4000123412341234, accountToId=4000123412341235, sumToTransfer=5.0, sumTransferred=0.0755, transferredAt=2019-08-25 19:02:38.944), Transfer(accountFromId=4000123412341234, accountToId=4000123412341236, sumToTransfer=8.0, sumTransferred=0.1088, transferredAt=2019-08-25 19:02:50.752)]

## Exceptions ##

Examples of implemented exceptions:
1) **"No accounts exist in the database"** - if you're trying to do sth with accounts when the 'accounts' database table is empty
2) **"Account with id 5 doesn't exist"** - *no comments*
3) **"No users exist in the database"** - if you're trying to do sth with users when the 'users' database table is empty
4) **"User with id 7 doesn't exist"** - *no comments*
5) **"No accounts belong to the user with id = 9"** - *no comments*
6) **"Account with id 4000123412341235 can't transfer 10000 RUB, the balance is only 5 RUB"** - when the balance of the first account isn't enough for transferring money
7) **"No transfers exist in the database"** - if you're trying to request transfers when the 'transfers' database table is empty
