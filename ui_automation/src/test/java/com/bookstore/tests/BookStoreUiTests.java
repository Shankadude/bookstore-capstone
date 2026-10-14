package com.bookstore.tests;

import com.bookstore.pages.BookStorePage;
import com.bookstore.pages.LoginPage;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class BookStoreUiTests {
    private WebDriver driver;
    private LoginPage loginPage;
    private BookStorePage bookStorePage;

    private final String booksUrl = "https://demoqa.com/books";
    private final String loginUrl = "https://demoqa.com/login";

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        // EAGER strategy waits for DOMContentLoaded without hanging on slow third-party ad networks
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        loginPage = new LoginPage(driver);
        bookStorePage = new BookStorePage(driver);
    }

    @Test(priority = 1, description = "TC-UI-01: Verify Book Store catalog loads default tech books")
    public void testCatalogLoadsDefaultBooks() {
        bookStorePage.open(booksUrl);
        int count = bookStorePage.getDisplayedBookCount();
        Assert.assertTrue(count >= 8, "Expected at least 8 books in catalog, but found: " + count);
    }

    @DataProvider(name = "bookSearchQueries")
    public Object[][] getSearchData() {
        return new Object[][] {
            {"Git Pocket", 1, "Git Pocket Guide"},
            {"JavaScript", 4, "Learning JavaScript Design Patterns"},
            {"NonExistentBookXYZ999", 0, ""}
        };
    }

    @Test(priority = 2, dataProvider = "bookSearchQueries", description = "TC-UI-02: Data-driven search filter test")
    public void testSearchFilterScenarios(String query, int expectedCount, String expectedFirstTitle) {
        bookStorePage.open(booksUrl);
        bookStorePage.searchForBook(query);

        int actualCount = bookStorePage.getDisplayedBookCount();
        Assert.assertEquals(actualCount, expectedCount, "Book count mismatch for query: " + query);

        if (expectedCount > 0) {
            Assert.assertEquals(bookStorePage.getFirstBookTitle(), expectedFirstTitle);
        }
    }

    @Test(priority = 3, description = "TC-UI-03: Verify clicking a book opens the Book Details view with ISBN")
    public void testNavigateToBookDetailsPage() {
        bookStorePage.open(booksUrl);
        bookStorePage.clickBookByTitle("Git Pocket Guide");

        String actualIsbn = bookStorePage.getDisplayedIsbnOnDetailsPage();
        Assert.assertEquals(actualIsbn, "9781449325862", "Book details ISBN did not match expected value.");
    }

    @Test(priority = 4, description = "TC-UI-04: Verify invalid login credentials trigger error message")
    public void testInvalidLoginDisplaysErrorBanner() {
        loginPage.open(loginUrl);
        loginPage.enterUsername("invalid_capstone_user_99");
        loginPage.enterPassword("WrongPass@123");
        loginPage.clickLoginButton();

        String error = loginPage.getInvalidLoginErrorMessage();
        Assert.assertEquals(error, "Invalid username or password!", "Unexpected error banner text.");
    }

    @Test(priority = 5, description = "TC-UI-05: Verify submitting empty login form triggers validation state")
    public void testEmptyLoginValidation() {
        loginPage.open(loginUrl);
        loginPage.clickLoginButton();

        Assert.assertTrue(loginPage.isUsernameFieldMarkedInvalid(),
            "Expected username field to have 'is-invalid' validation class when submitted empty.");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
