# DDD-CQRS-EVENT_SOURCING

# user-management
1. user.command.api this is write side of api
2. user.core is command models
3. user.query.api is to handle user queries

This project uses 
1. axon-server
2. axon framework
3. mongo db for user management, read write db 
   1. User by axon server to maintain domain events
4. mysql db bank management

All of the above services are deployed on docker below are the steps to run them in docker
1. Create network in docker SpringBankNet so that all the services can communicate on same network
    - **_docker network create --attachable -d overlay SpringBankNet_**
2. Run axon server in docker and attach it to created network (pull image and run container)
    - **_docker run -d --name axon-server -p 8024:8024 -p 8124:8124 --network SpringBankNet --restart always axoniq/axonserver:latest_**
3. Run mongo on docker
    - **_docker run -it -d --name mongo-container -p 27017:27017 --network SpringBankNet --restart always -v mongo_data_container:/data/db mongo:latest_**
4. Run mysql on docker
    - **_docker run -it -d --name mysql-container -p 3306:3306 --network SpringBankNet -e MYSQL_ROOT_PASSWORD=springBankPassword --restart always -v mysql_data_container:/var/lib/mysql mysql:latest_**
5. Create user command and query microservices in docker-compose
6. Create bank command and query microservices in docker-compose
7. Create api-gateway service in docker-compose
8. You will have to register first user with non security user.command.api 
9. To run the docker compose up you will have to build the image of all the microservices manually or put them in your remote docker hub 
   a.   docker build -t api-gateway .
   b.   docker build -t user-oauth2 .
   c.   docker build -t user-cmd-api .
   d.   docker build -t  user-query-api . 
   e.   docker build -t bankacc-cmd-api .
   f.   docker build -t bankacc-query-api .


# user.oauth2.0

User can create access_token using 
POST http://localhost:8083/oauth/token along with grant type password, username and password


# Securing the user command , user query, bank command and bank query with spring security Oauth2.0
1. Adding security dependency 
2. Using **_@EnableResourceServer @EnableGlobalMethodSecurity_**
3. Annotating write side with @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
4. Annotating read side with @PreAuthorize("hasAuthority('READ_PRIVILEGE')")

# API gate-way
1. Define endpoints and regex to endpoints
3. Use given post man collection