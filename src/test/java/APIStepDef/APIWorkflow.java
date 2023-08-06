package APIStepDef;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.it.Ma;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apiguardian.api.API;
import org.junit.Assert;
import utlis.APIConstants;
import utlis.APIPayloadConstants;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class APIWorkflow {
    RequestSpecification request;
    Response response;
    public static String employee_id;
    @Given("a request is prepared for creating an employee")
    public void a_request_is_prepared_for_creating_an_employee() {
        request = given().header(APIConstants.HEADER_CONTENT_TYPE_KEY,APIConstants.HEADER_CONTENT_TYPE_VALUE).
                header(APIConstants.HEADER_AUTHORIZATION_KEY, GenerateTokenStep.token)
                .body(APIPayloadConstants.createEmployeePayload());
    }

    @When("a POST call is made to create an employee")
    public void a_post_call_is_made_to_create_an_employee() {
        response = request.when().post(APIConstants.CREATE_EMPLOYEE_URI);
        response.prettyPrint();
    }

    @Then("the status code for creating an employee is {int}")
    public void the_status_code_for_creating_an_employee_is(int statusCode) {
        response.then().assertThat().statusCode(statusCode);
    }


    @Then("the employee created contains key {string} and value {string}")
    public void the_employee_created_contains_key_and_value(String key, String value) {
//        response.then().assertThat().body(key, equalTo(value));
//or
        String actual = response.jsonPath().getString(key);

        Assert.assertEquals(actual,value);
    }

    @Then("the employee id {string} is stored as a global variable")
    public void the_employee_id_is_stored_as_a_global_variable(String empId) {
        employee_id = response.jsonPath().getString(empId);
        System.out.println(employee_id);
    }

//    ----------------------------------------------------------------------
//    -----------------------------------------------------------------


    @Given("a request is prepared for retrieving an employee")
    public void a_request_is_prepared_for_retrieving_an_employee() {
        request=given().header(APIConstants.HEADER_CONTENT_TYPE_KEY,APIConstants.HEADER_CONTENT_TYPE_VALUE).header(APIConstants.HEADER_AUTHORIZATION_KEY,GenerateTokenStep.token)
                .queryParam("employee_id",employee_id);

    }
    @When("a GET call is made to retrieve the employee")
    public void a_get_call_is_made_to_retrieve_the_employee() {
       response=request.when().get(APIConstants.GET_ONE_EMPLOYEE_URI);
    }
    @Then("the status code for this employee is {int}")
    public void the_status_code_for_this_employee_is(int statuscde) {
        response.then().assertThat().statusCode(statuscde);

    }
    @Then("the employee id {string} matches the globally stored id")
    public void the_employee_id_matches_the_globally_stored_id(String emp_id) {
        String actual_empID = response.jsonPath().getString(emp_id);
        Assert.assertEquals(employee_id,actual_empID);
    }
    @Then("the employee information under the key {string} matches the data used to create the employee")
    public void the_employee_information_under_the_key_matches_the_data_used_to_create_the_employee(String employeeObject, io.cucumber.datatable.DataTable dataTable) {
        List<Map<String,String>> expectedData=dataTable.asMaps();
        String expectedFirstName=null;
        String expectedLastName=null;
        for(Map<String,String>map :expectedData){
//            extract all the values of expected data from the data table
             expectedFirstName = map.get("emp_firstname");
              expectedLastName = map.get("emp_lastname");
        }

//        extract all the values from the response
        String actualFirstname=response.jsonPath().getString("employee.emp_firstname");
      String  actualLastName=response.jsonPath().getString("employee.emp_lastname");


//        asseert them

        Assert.assertEquals(actualFirstname,expectedFirstName);
        Assert.assertEquals(actualLastName,expectedLastName);

    }

    @Given("the request is prepared to update the employee firstname to {string}")
    public void the_request_is_prepared_to_update_the_employee_firstname_to(String name) {
        request=given().header(APIConstants.HEADER_CONTENT_TYPE_KEY,APIConstants.HEADER_CONTENT_TYPE_VALUE).header(APIConstants.HEADER_AUTHORIZATION_KEY,GenerateTokenStep.token)
                .body(APIPayloadConstants.updateEmployeePartiallyPayload(employee_id,"emp_firstname",name));
    }
    @When("a PATCH call is made to update the employee")
    public void a_patch_call_is_made_to_update_the_employee() {
    response=request.patch(APIConstants.UPDATE_PARTIALLY_EMPLOYEE_URI);
    }
    @Then("the status code is {int}")
    public void the_status_code_is(Integer code) {
    response.then().assertThat().statusCode(code);
    response.prettyPrint();
    }
    @Then("the employee updated has the updated firstname {string}")
    public void the_employee_updated_has_the_updated_firstname(String name) {
        String actualName = response.jsonPath().getString("employee.emp_firstname");
        Assert.assertEquals(actualName,name);
    }

}
