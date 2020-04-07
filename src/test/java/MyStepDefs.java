import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsNot;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Wrapper.RestAssuredWrapper.*;
import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;

public class MyStepDefs {
    private static ResponseOptions<Response> responseOptions;

    @Given("^I navigate to the \"([^\"]*)\" location$")
    public void iNavigateToTheLocation(String urlLocation) {
        try {
            responseOptions = performGetOperation(urlLocation);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @And("I look for post with id \"([^\"]*)\" with author name as \"([^\"]*)\"$")
    public void iLookForPostWithId(String postId, String authorName) {
        assertThat("", responseOptions.getBody().jsonPath().get("author").equals(authorName));
    }

    @And("I look for post with author as \"([^\"]*)\"$")
    public void iLookForPostWithAuthorAs(String authorName) {
        with().queryParam("author", authorName)
                .when()
                .get("http://localhost:3000/posts/")
                .then()
                .assertThat().body(CoreMatchers.containsString("2"));
    }

    @Then("the author name \"([^\"]*)\" is present in the list of authors$")
    public void theAuthorNameIsRetrieved(final String authorName) {
        List<String> listOfAuthors = responseOptions.getBody().jsonPath().get("author");
        assertThat(authorName + " is not present", listOfAuthors.contains(authorName));
    }

    @Given("I perform the post request \"([^\"]*)\" with following data$")
    public void iPerformThePostRequestWithFollowingData(String pathLocation, Map<String, String> listOfThings) {
        HashMap<String, String> body = new HashMap<>();
        HashMap<String, String> params = new HashMap<>();
        int size = listOfThings.size();
        listOfThings.entrySet().stream().limit(size - 1).forEach(e -> body.put(e.getKey(), e.getValue()));
        listOfThings.entrySet().stream().skip(1).forEach(e -> params.put(e.getKey(), e.getValue()));
        responseOptions = performPostOperationsWithParamsAndBody(pathLocation, body, params);
    }

    @Then("the response body should have name as \"([^\"]*)\"$")
    public void theResponseBodyShouldHaveNameAs(String name) {
        String actualName = responseOptions.getBody().jsonPath().get("name");
        assertThat("", actualName.equals(name));
    }

    @When("I perform the DELETE request \"([^\"]*)\" with following data$")
    public void iPerformTheDELETERequestWithFollowingData(String url, Map<String, String> postDetails) {
        HashMap<String, String> pathParams = new HashMap<>();
        postDetails.forEach(pathParams::put);
        responseOptions = performDeleteOperationsWithParams(url, pathParams);
    }

    @And("I perform the GET request \"([^\"]*)\" with following data$")
    public void iPerformTheGETRequestWithFollowingData(String url, Map<String, String> postDetails) {
        HashMap<String, String> pathParams = new HashMap<>();
        postDetails.forEach(pathParams::put);
        responseOptions = performGetOperationsWithParams(url, pathParams);
    }

    @Then("the author name \"([^\"]*)\" is not present in the list of authors$")
    public void theAuthorNameIsNotPresentInTheListOfAuthors(String authorName) {
        assertThat(responseOptions.getBody().jsonPath().get("author"), IsNot.not(authorName));
    }

    @Given("I perform the post request \"([^\"]*)\" with following body data$")
    public void iPerformThePostRequestWithFollowingBodyData(String pathLocation, Map<String, String> apiBodyDetails) {
        HashMap<String, String> body = new HashMap<>();
        apiBodyDetails.forEach(body::put);
        responseOptions = performPostOperationsWithBody(pathLocation, body);
    }

    @When("I perform the PUT request \"([^\"]*)\" with following data$")
    public void iPerformTheRequestWithFollowingData(String pathLocation, Map<String, String> apiBodyDetails) {
        HashMap<String, String> body = new HashMap<>();
        apiBodyDetails.forEach(body::put);
        HashMap<String, String> pathParams = new HashMap<>();
        for (Map.Entry<String, String> entry : apiBodyDetails.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.equals("id")) {
                pathParams.put(key, value);
            }
            break;
        }
        responseOptions = performPutOperationsWithParamsAndBody(pathLocation, pathParams, body);
    }

    @Then("the title \"([^\"]*)\" is present in the list of posts$")
    public void theTitleIsPresentInTheListOfPosts(String titleName) {
        assertThat(titleName + "is not present", responseOptions.getBody().jsonPath().get("title").equals(titleName));
    }
}
