import Wrapper.RestAssuredWrapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import org.hamcrest.CoreMatchers;

import java.net.URISyntaxException;
import java.util.List;

import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;

public class MyStepDefs {
    private static ResponseOptions<Response> responseOptions;

    @Given("^I navigate to the \"([^\"]*)\" location$")
    public void iNavigateToTheLocation(String urlLocation) {
        try {
            responseOptions = RestAssuredWrapper.performGetOperation(urlLocation);
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
}
