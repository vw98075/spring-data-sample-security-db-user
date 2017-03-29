## Spring Data Sample With Security


This is the fourth installment of the Spring Data Rest sample series. In the previous installment, we added a security feature to protect RESTful end point access. In this installment, we replace the in-memory user setting with a database user setting. Again, the user data can be obtained by submitting a GET request to localhost:8080/accounts. In a real world application, the URL shall not be exposed to the outside of the world. You likely notice the passwords are encrypted. 
All end-points of this application can be found on the Swagger page http://localhost:8080/swagger-ui.html. 
 

Spring Data Sample series:

 * [First installment](https://github.com/vw98075/spring-data-sample): a single entity
 * [Second installment](https://github.com/vw98075/spring-data-sample2): related multiple entities
 * [Third installment](https://github.com/vw98075/spring-data-sample-security): secure API access with an in-memory user setup
 * Fourth installment: secure API access with a database user setup
 * Fifth installment: secure API access with JWT, a sessionless approach


