package mari.ku.tests;

import mari.ku.config.UserConfig;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;

public class DemowebshopTests extends TestBase {

    UserConfig configs = ConfigFactory.create(UserConfig.class);

    @Test
    @DisplayName("Добавление отзыва авторизованным пользователем и проверка опубликованного отзыва (API + UI)")
    void addAndCheckReviewByAuthorizedUser() {

        //авторизуемся через API и получаем авторизационную куку
        String authorizationCookie =
                given()
                        .contentType("application/x-www-form-urlencoded")
                        .formParam("Email", configs.userLogin())
                        .formParam("Password", configs.userPassword())
                        .when()
                        .post("/login")
                        .then()
                        .log().all()
                        .statusCode(302)
                        .extract()
                        .cookie("NOPCOMMERCE.AUTH");

        //добавляем отзыв товару через API
        given()
                .cookie("NOPCOMMERCE.AUTH", authorizationCookie)
                .contentType("application/x-www-form-urlencoded")
                .formParam("AddProductReview.Title", reviewTitle)
                .formParam("AddProductReview.ReviewText", reviewText)
                .formParam("AddProductReview.Rating", uidRating)
                .formParam("add-review", "Submit+review")
                .when()
                .post("/productreviews/" + uidProduct)
                .then()
                .log().all()
                .statusCode(200);

        //передаем авторизационную куку в браузер, открываем главную страницу магазина, проверяем, что пользователь авторизован
        open("/Themes/DefaultClean/Content/images/logo.png");
        getWebDriver().manage().addCookie(new Cookie("NOPCOMMERCE.AUTH", authorizationCookie));
        open("");
        $(".account").shouldHave(text(configs.userLogin()));

        //проверяем оставленный отзыв
        open("/productreviews/" + uidProduct);
        $$(".review-title").findBy(text(reviewTitle)).parent()
                .sibling(0).shouldHave(text(reviewText))
                .sibling(0).$(".user").shouldHave(text(configs.userFirstName()));

    }
}
