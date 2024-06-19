import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class KiwiTest {
    //@RepeatedTest(10)
    @Test
    void itShouldOpenMainPage() throws InterruptedException {
        open("https://www.kiwi.com/en");
        Cookie cookie = new Cookie("cookie_consent", "agreed");
        WebDriverRunner.getWebDriver().manage().addCookie(cookie);
        refresh();

        $(byAttribute("data-test","LandingSearchButton")).click();
        Thread.sleep(10000);
        $(byAttribute("data-test", "PictureCard")).click();


        SelenideElement wrapper = $(byAttribute("data-test", "ResultCardWrapper"));
        wrapper.shouldBe(Condition.visible);

        /*SelenideElement wrapper = $(byAttribute("data-test","ResultCardWrapper"))
                .waitUntil(Condition.appears, 15000);*/

        String price = wrapper.find("strong[class*='PriceText']").shouldNotBe(Condition.empty).getText();
        wrapper.click();
        $("div[data-test='ModalFooter'] div[class*=FooterPriceWrapper]")
                .shouldHave(Condition.text(price));
    }

    @Test
    void itShouldFindNomad() throws InterruptedException {
        open("https://www.kiwi.com/nomad");
        Cookie cookie = new Cookie("cookie_consent", "agreed");
        WebDriverRunner.getWebDriver().manage().addCookie(cookie);
        refresh();

        $(byText("Show popular journeys")).click();

        $$("div[class*=ExampleItemstyled]")
                .shouldHave(sizeGreaterThan(1))
                .find(Condition.text("Asia"))
                .click();

        $(byAttribute("data-test", "BookingButton"))
                .wait(Condition.appears, 15000)
                .click();

        $("div.ReservationBill-item-price")
                .wait(Condition.appears, 15000)
                .shouldNotBe(Condition.empty);
    }

    @Test
    void changeCurrency() throws InterruptedException {
        open("https://www.kiwi.com/en/");
        Cookie cookie = new Cookie("cookie_consent", "agreed");
        WebDriverRunner.getWebDriver().manage().addCookie(cookie);
        refresh();

        $(byAttribute("data-test", "Currency-Open")).click();
        $(byAttribute("data-test", "Currency-Item-jpy")).click();
        $(byAttribute("data-test", "Currency")).shouldHave(Condition.text("JPY"));

        $$(byAttribute("data-test","SearchField-input"))
                .get(1)
                .val("Tokyo");

        $(byAttribute("data-test", "PlacePickerRowCheckbox")).click();

        $(byText("Search")).click();
        SelenideElement wrapper = $(byAttribute("data-test", "ResultCardWrapper"));

        wrapper.find("strong[class*='PriceText']").shouldHave(Condition.text("€"));
    }


    @AfterEach
    void quitDriver() {
        WebDriverRunner.getWebDriver().quit();
    }

}
