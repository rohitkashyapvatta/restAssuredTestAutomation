package Wrapper;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class RestAssuredWrapperv2 {
    private RequestSpecBuilder builder = new RequestSpecBuilder();
    private String url;
    private String actionType;

    public RestAssuredWrapperv2(String uri, String actionType, String token) {
        this.url = "http://localhost:3000" + uri;
        this.actionType = actionType;
        if (token != null) {
            builder.addHeader("Authorization", "Bearer " + token);
        }
    }

    public ResponseOptions<Response> ExecuteAPI() {
        RequestSpecification requestSpecification = builder.build();
        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON);
        request.spec(requestSpecification);

        if (this.actionType.equalsIgnoreCase("GET")) {
            return request.get(this.url);
        } else if (this.actionType.equalsIgnoreCase("POST")) {
            return request.post(this.url);
        } else if (this.actionType.equalsIgnoreCase("DELETE")) {
            return request.delete(this.url);
        } else if (this.actionType.equalsIgnoreCase("PUT")) {
            return request.put(this.url);
        }
        return null;
    }

    public String Authenticate(Map<String, String> body) {
        builder.setBody(body);
        return ExecuteAPI().getBody().jsonPath().get("accessToken");
    }

    public ResponseOptions<Response> ExecuteWithQueryParams(Map<String, String> queryParams) {
        builder.addQueryParams(queryParams);
        return ExecuteAPI();
    }

    public ResponseOptions<Response> ExecuteWithPathParams(Map<String, String> pathParams) {
        builder.addPathParams(pathParams);
        return ExecuteAPI();
    }

    public ResponseOptions<Response> ExecuteWithBodyAndPathParams(Map<String, String> body, Map<String, String> pathParams) {
        builder.addPathParams(pathParams);
        builder.setBody(body);
        return ExecuteAPI();
    }

    public ResponseOptions<Response> ExecuteWithBody(Map<String, String> body) {
        builder.setBody(body);
        return ExecuteAPI();
    }
}
