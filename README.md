# DDD-CQRS-EVENT_SOURCING

# user-management
1. user.command.api this is write side of api
2. user.core is command models
3. user.query.api is to handle user queries

This project uses 
1. axon-server
2. axon framework
3. mongo db to for writing db and axon-framework uses publish domain events
4. mysql db 

All of the above services are deployed on docker below are the steps to run them in docker
1. Create network in docker SpringBankNet
    - **_docker network create --attachable -d overlay SpringBankNet_**
2. Run axon server in docker and attach it to created network (pull image and run container)
    - **_docker run -d --name axon-server -p 8024:8024 -p 8124:8124 --network SpringBankNet --restart always axoniq/axonserver:latest_**
3. Run mongo on docker
    - **_docker run -it -d --name mongo-container -p 27017:27017 --network SpringBankNet --restart always -v mongo_data_container:/data/db mongo:latest_**
4. Run mysql on docker
    - **_docker run -it -d --name mysql-container -p 3306:3306 --network SpringBankNet -e MYSQL_ROOT_PASSWORD=springBankPassword --restart always -v mysql_data_container:/var/lib/mysql mysql:latest_**
5. Create user command and query microservice 


# user.oauth2.0

User can create access_token using 
POST http://localhost:8084/oauth/token along with grant type password, username and password


# Securing the user command and user query with spring security 
1. Adding security dependency 
2. Using **_@EnableResourceServer @EnableGlobalMethodSecurity_**
3. Annotating write side with @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
4. Annotating read side with @PreAuthorize("hasAuthority('READ_PRIVILEGE')")