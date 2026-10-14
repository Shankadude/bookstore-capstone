package com.bookstore.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class BookStorePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Element Locators
    private final By searchBox = By.id("searchBox");
    private final By bookTitleLinks = By.cssSelector(".action-buttons a");
    private final By isbnValueLabel = By.cssSelector("#ISBN-wrapper #userName-value");

    public BookStorePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void open(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
    }

    public void searchForBook(String query) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
        input.clear();
        input.sendKeys(query);
    }

    public int getDisplayedBookCount() {
        List<WebElement> books = driver.findElements(bookTitleLinks);
        return books.size();
    }

    public String getFirstBookTitle() {
        WebElement firstBook = wait.until(ExpectedConditions.visibilityOfElementLocated(bookTitleLinks));
        return firstBook.getText().trim();
    }

    public void clickBookByTitle(String title) {
        By bookLink = By.linkText(title);
        WebElement link = wait.until(ExpectedConditions.presenceOfElementLocated(bookLink));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", link);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
    }

    public String getDisplayedIsbnOnDetailsPage() {
        WebElement isbn = wait.until(ExpectedConditions.visibilityOfElementLocated(isbnValueLabel));
        return isbn.getText().trim();
    }
}
