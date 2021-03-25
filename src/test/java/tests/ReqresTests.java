package tests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static utils.FileUtils.readStringFromFile;

public class ReqresTests extends BaseTest {

    @Test
    void successSingleUserTest() {
        given()
                .when()
                .get("/api/users/2")
        .then()
                .log().body()
                .statusCode(200)
                .body("data.first_name", is("Janet"));
    }

    @Test
    void successCreateTest() {
        String data = readStringFromFile("./src/test/resources/name_data.txt");
        given()
                .contentType(ContentType.JSON)
                .body(data)
        .when()
                .post("/api/users")
        .then()
                .log().body()
                .statusCode(201)
                .body("name", is("morpheus"))
                .and()
                .body("id", is(notNullValue()));
    }

    @Test
    void singleNotFoundTest() {
        given()
                .when()
                .get("/api/unknown/23")
        .then()
                .log().body()
                .statusCode(404)
                .body(is("{}"));
    }

    @Test
    void delayedResponseSizeTests() {
        given()
                .when()
                .get("api/users?delay=3")
        .then()
                .log().body()
                .statusCode(200)
                .body("data", hasSize(6))
                .and()
                .body("data.first_name[0]", equalTo("George"));
    }

    @Test
    void deleteTest() {
        given()
                .when()
                .delete("/api/users/2")
        .then()
                .log().body()
                .statusCode(204);
    }
}
