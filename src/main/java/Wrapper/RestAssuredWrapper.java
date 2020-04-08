package Wrapper;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
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

    public static ResponseOptions<Response> performGetOperationWithBearerToken(String url, String token) throws URISyntaxException {
        requestSpecification.header(new Header("Authorization", "Bearer " + token));
        return requestSpecification.get(new URI(url));
    }

    public static ResponseOptions<Response> performGetOperationWithBody(String url, Map<String, String> body) throws URISyntaxException {
        requestSpecification.body(body);
        return requestSpecification.get(new URI(url));
    }

    public static ResponseOptions<Response> performGetOperationsWithParams(String url, Map<String, String> params) {
        requestSpecification.pathParams(params);
        return requestSpecification.get(url);
    }

    public static ResponseOptions<Response> performGetOperationsWithQueryParams(String url, Map<String, String> queryParams, String token) {
        requestSpecification.header(new Header("Authorization", "Bearer " + token));
        requestSpecification.queryParams(queryParams);
        return requestSpecification.get(url);
    }

    public static ResponseOptions<Response> performPostOperationsWithParamsAndBody(String url, Map<String, String> body, Map<String, String> pathParams) {
        requestSpecification.pathParams(pathParams);
        requestSpecification.body(body);
        return requestSpecification.post(url);
    }

    public static ResponseOptions<Response> performPostOperationsWithBody(String url, Map<String, String> body) {
        requestSpecification.body(body);
        return requestSpecification.post(url);
    }

    public static ResponseOptions<Response> performDeleteOperationsWithParams(String url, Map<String, String> pathParams) {
        requestSpecification.pathParams(pathParams);
        return requestSpecification.delete(url);
    }

    public static ResponseOptions<Response> performPutOperationsWithParamsAndBody(String url, Map<String, String> pathParams, Map<String, String> body) {
        requestSpecification.pathParams(pathParams);
        requestSpecification.body(body);
        return requestSpecification.put(url);
    }
}
