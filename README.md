# grubbank
Grub Bank (or Food bank) is a standalone java application to store your favourite food recipes, update or delete them. It can also be used to search recipes based on some search criteria. 
### Step by step guide to run the application - 

Pre-requisite : Java 11, Client like POSTMAN or browser.

Steps :
1. Open your terminal
2. On your terminal navigate to a desired location on your machine where you want to place the project (eg : cd Documents)
3. Here execute the command : git clone https://github.com/priyankamuniraju/grubbank.git
4. Navigate to the project(grubbank) folder (eg: cd grubbank)
5. Run the command : ./gradlew bootrun
6. The application starts on port 8080 by default.
7. Optional Step : If you currently have another application running on port 8080. The default port can be changed to any desired port number. For that 
a.)Go to the file - /grubbank/src/main/resources/application.properties  
b.)And add the following line :server.port=8081
c.)Save the changes and repeat step 5
8. Now that the application is up and running, the application can be used through clients like POSTMAN or a browser by hitting the REST endpoints described here : https://github.com/priyankamuniraju/grubbank/wiki 
