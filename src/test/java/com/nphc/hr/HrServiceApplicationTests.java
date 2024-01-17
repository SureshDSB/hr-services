package com.nphc.hr;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest(classes = HrServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class HrServiceApplicationTests {

    private RequestSpecification spec;


    @Value("${server.servlet.context-path}")
    private String contextPath;

    @LocalServerPort
    private int port;


    @BeforeEach
    void setup() {
        this.spec = new RequestSpecBuilder().build();
    }




    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/reset.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/user-data.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void testGet() {
        RestAssured.given(this.spec).accept("application/json").header("Content-Type", "application/json")
                .pathParam("id", "e0001").when().port(this.port)
                .get(this.contextPath + "users/{id}")
                .then().assertThat().statusCode(is(200));
    }




    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/reset.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/user-data.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void testCreateUser() {
        RestAssured.given(this.spec).accept("application/json").header("Content-Type", "application/json")
                .port(this.port)
                .body("{\n" +
                        "        \"id\": \"e101\",\n" +
                        "        \"login\": \"emp1\",\n" +
                        "        \"name\": \"name\",\n" +
                        "        \"salary\": 32.22,\n" +
                        "        \"startDate\": \"2024-01-11\"\n" +
                        "    }")
                .post(this.contextPath + "users")
                .then().assertThat().statusCode(is(201));
    }




    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/reset.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/user-data.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void testUpdateUser() {
        RestAssured.given(this.spec).accept("application/json").header("Content-Type", "application/json")
                .pathParam("id", "e0001").when().port(this.port)
                .body("{\n" +
                        "        \"id\": \"e0001\",\n" +
                        "        \"login\": \"Updateemp1\",\n" +
                        "        \"name\": \"updateName1\",\n" +
                        "        \"salary\": 32.22,\n" +
                        "        \"startDate\": \"2024-01-16\"\n" +
                        "    }")
                .put(this.contextPath + "users/{id}")
                .then().assertThat().statusCode(is(200));
    }




    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/reset.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/user-data.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void testPartialUpdateUser() {
        RestAssured.given(this.spec).accept("application/json").header("Content-Type", "application/json")
                .pathParam("id", "e0001").when().port(this.port)
                .body("{\n" +
                        "        \"salary\": 3223432.22,\n" +
                        "        \"startDate\": \"2024-01-11\"\n" +
                        "    }")
                .patch(this.contextPath + "users/{id}")
                .then().assertThat().statusCode(is(200));
    }


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/reset.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/user-data.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void testDeleteUser() {
        RestAssured.given(this.spec).accept("application/json").header("Content-Type", "application/json")
                .pathParam("id", "e0001").when().port(this.port)
                .delete(this.contextPath + "users/{id}")
                .then().assertThat().statusCode(is(200));
    }


    @Test
    void testFileUpload() {
        RestAssured.given(this.spec).accept("application/json").header("Content-Type", "multipart/form-data")
                .when().port(this.port)
                .multiPart(new File("src/test/resources/init/user-data.csv"))
                .post(this.contextPath + "users/upload")
                .then().assertThat().statusCode(is(200));
    }


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/reset.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/user-data12.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void testFetch() {
        RestAssured.given(this.spec).accept("application/json").header("Content-Type", "application/json")
                .queryParam("minSalary", "1000.00")
                .queryParam("maxSalary", "8000.00")
                .queryParam("limit", "10")
                .when().port(this.port)
                .get(this.contextPath + "users")
                .then().assertThat().statusCode(is(200));
    }


}
