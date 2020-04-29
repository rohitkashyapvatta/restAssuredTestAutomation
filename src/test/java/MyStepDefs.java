import Address.Address;
import Wrapper.RestAssuredWrapperv2;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import model.Location;
import model.Posts;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsNot;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;

public class MyStepDefs {
    private static ResponseOptions<Response> responseOptions;
    private static String accessToken;

    @Given("^I navigate to the \"([^\"]*)\" location$")
    public void iNavigateToTheLocation(String urlLocation) {
        RestAssuredWrapperv2 restAssuredWrapperv2 = new RestAssuredWrapperv2(urlLocation, "GET", accessToken);
        responseOptions = restAssuredWrapperv2.ExecuteAPI();
    }

    @And("I look for post with id \"([^\"]*)\" with author name as \"([^\"]*)\"$")
    public void iLookForPostWithId(String postId, String authorName) {
        org.hamcrest.MatcherAssert.assertThat("", responseOptions.getBody().jsonPath().get("author").equals(authorName));
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
        Posts[] listOfPosts = responseOptions.getBody().as(Posts[].class);
        boolean isDataPresent = Arrays.stream(listOfPosts).anyMatch(post -> post.getAuthor().equals(authorName));
        assertThat(authorName + " is not present", isDataPresent);
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
        RestAssuredWrapperv2 restAssuredWrapperv2 = new RestAssuredWrapperv2(url, "DELETE", accessToken);
        responseOptions = restAssuredWrapperv2.ExecuteWithPathParams(pathParams);
    }

    @And("I perform the GET request \"([^\"]*)\" with following data$")
    public void iPerformTheGETRequestWithFollowingData(String url, Map<String, String> postDetails) {
        HashMap<String, String> pathParams = new HashMap<>();
        postDetails.forEach(pathParams::put);
        RestAssuredWrapperv2 restAssuredWrapperv2 = new RestAssuredWrapperv2(url, "GET", accessToken);
        responseOptions = restAssuredWrapperv2.ExecuteWithPathParams(pathParams);
    }

    @Then("the author name \"([^\"]*)\" is not present in the list of authors$")
    public void theAuthorNameIsNotPresentInTheListOfAuthors(String authorName) {
        assertThat(responseOptions.getBody().jsonPath().get("author"), IsNot.not(authorName));
    }

    @Given("I perform the post request \"([^\"]*)\" with following body data$")
    public void iPerformThePostRequestWithFollowingBodyData(String pathLocation, Map<String, String> apiBodyDetails) {
        HashMap<String, String> body = new HashMap<>();
        apiBodyDetails.forEach(body::put);
        RestAssuredWrapperv2 restAssuredWrapperv2 = new RestAssuredWrapperv2(pathLocation, "POST", accessToken);
        responseOptions = restAssuredWrapperv2.ExecuteWithBody(body);
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
        RestAssuredWrapperv2 restAssuredWrapperv2 = new RestAssuredWrapperv2(pathLocation, "PUT", accessToken);
        responseOptions = restAssuredWrapperv2.ExecuteWithBodyAndPathParams(body, pathParams);
    }

    @Then("the title \"([^\"]*)\" is present in the list of posts$")
    public void theTitleIsPresentInTheListOfPosts(String titleName) {
        assertThat(titleName + "is not present", responseOptions.getBody().jsonPath().get("title").equals(titleName));
    }

    @Given("^I logged into the application \"([^\"]*)\" with following credentials$")
    public void iLoggedIntoTheApplicationWithFollowingCredentials(String url, Map<String, String> loginCredentials) {

        HashMap<String, String> loginPayload = new HashMap<>();
        loginCredentials.forEach(loginPayload::put);
        RestAssuredWrapperv2 restAssuredWrapperv2 = new RestAssuredWrapperv2(url, "POST", null);
        accessToken = restAssuredWrapperv2.Authenticate(loginPayload);
    }

    @And("I perform the GET request \"([^\"]*)\" with following query data$")
    public void iPerformTheGETRequestWithFollowingQueryData(String url, Map<String, String> queryParams) {
        Map<String, String> queryParamsPayload = new HashMap<>();
        queryParams.forEach(queryParamsPayload::put);
        RestAssuredWrapperv2 restAssuredWrapperv2 = new RestAssuredWrapperv2(url, "GET", accessToken);
        responseOptions = restAssuredWrapperv2.ExecuteWithQueryParams(queryParamsPayload);
    }

    @Then("the \"([^\"]*)\" location with address \"([^\"]*)\" is present$")
    public void theLocationWithAddressIsPresent(String addressType, String streetName) {
        Location[] locations = responseOptions.getBody().as(Location[].class);
        Address expectedAddress = Objects.requireNonNull(Arrays.stream(locations)
                .findFirst()
                .orElse(null))
                .getAddress()
                .stream()
                .filter(address -> address.getType().equals(addressType))
                .findFirst().
                        orElse(null);
        assertThat(streetName + " is not present in the response", Objects.requireNonNull(expectedAddress).getStreet().equals(streetName));
    }

    @Then("the json schema should match with the response$")
    public void theJsonSchemaShouldMatchWithTheResponse() {
        String s = responseOptions.getBody().asString();
        assertThat(s, JsonSchemaValidator.matchesJsonSchemaInClasspath("posts.json"));
    }

    @And("I read the json file")
    public void iReadTheJsonFile() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject body = (JSONObject) jsonParser.parse(
                new FileReader("src/jsonFiles/post.json"));
        body.put("id", 55);
        String s = body.toJSONString();
    }
}
