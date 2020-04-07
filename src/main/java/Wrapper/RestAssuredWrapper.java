package Wrapper;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSpecification;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class RestAssuredWrapper {

    public static RequestSpecification requestSpecification;

    public RestAssuredWrapper() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri("http://localhost:3000");
        requestSpecBuilder.setContentType(ContentType.JSON);
        RequestSpecification build = requestSpecBuilder.build();
        requestSpecification = RestAssured.given().spec(build);
    }

    public static ResponseOptions<Response> performGetOperation(String url) throws URISyntaxException {
        return requestSpecification.get(new URI(url));
    }

    public static void performGetOperationsWithParams(Map<String, String> params, String url) {
        requestSpecification.pathParams(params);
        try {
            requestSpecification.get(new URI(url));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
