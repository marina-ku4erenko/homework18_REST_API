package mari.ku.tests;

import com.github.javafaker.Faker;

public class TestData {

    Faker faker = new Faker();

    String uidProduct = String.valueOf(faker.number().numberBetween(1, 40));
    String uidRating = String.valueOf(faker.number().numberBetween(1, 5));

    String reviewTitle = "Cool product " + uidProduct;
    String reviewText = "I like this product " + uidProduct;

}
