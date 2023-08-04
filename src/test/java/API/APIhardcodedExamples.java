package API;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class APIhardcodedExamples {
//setting the base uri
String baseURI = RestAssured.baseURI = "http://hrm.syntaxtechs.net/syntaxapi/api";

//token
    String token="Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2OTExMDY0NzQsImlzcyI6ImxvY2FsaG9zdCIsImV4cCI6MTY5MTE0OTY3NCwidXNlcklkIjoiNTcwNiJ9.2EIAZvsKX_gK-EJqlt1o-jZMfxpV7QldGHxXr4fuROs";

@Test
    public void createEmployee(){
//    prepare the request
    RequestSpecification request = given().header("Content-Type", "application/json").
            header("Authorization", token).body("{\n" +
                    "  \"emp_firstname\": \"ab\",\n" +
                    "  \"emp_lastname\": \"ra\",\n" +
                    "  \"emp_middle_name\": \" ca\",\n" +
                    "  \"emp_gender\":   \"M\",\n" +
                    "  \"emp_birthday\": \"2023-07-28\",\n" +
                    "  \"emp_status\": \"employeed\",\n" +
                    "  \"emp_job_title\": \"cloudengineer\"\n" +
                    "}");
//    send the request
    Response response = request.when().post("/createEmployee.php");

   response.prettyPrint();
//   extract the employeeid
    String employee_id = response.jsonPath().getString("Employee.employee_id");
    System.out.println(employee_id);
//assertions
//    the employee_status is employeed

    String employee_status= response.jsonPath().getString("Employee.emp_status");
    Assert.assertEquals("employeed",employee_status );

     }


}

