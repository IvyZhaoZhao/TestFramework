package com.test.selenium;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class GoogleSuggest {
    public static void main(String[] args) throws Exception {
        // The Firefox driver supports javascript
        WebDriver driver = new FirefoxDriver();

        // Go to the Google Suggest home page
        driver.get("http://www.google.com/webhp?complete=1&hl=en");

        // Enter the query string "Cheese"
        WebElement query = driver.findElement(By.name("q"));
        query.sendKeys("Cheese");

        // Sleep until the div we want is visible or 5 seconds is over
        long end = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < end) {

            //findElement would throw a NoSuchElement exception if the element is not present. Using findElements is a better approach
            ArrayList<WebElement> resultsDiv = (ArrayList<WebElement>) driver.findElements(By.className("sbsb_a"));
            // If results have been returned, the results are displayed in a drop down.
            if (resultsDiv.size()>0) {
              break;
            }
        }

        // And now list the suggestions
        List<WebElement> allSuggestions = driver.findElements(By.xpath("//div[@class='sbqs_c']"));

        for (WebElement suggestion : allSuggestions) {
            System.out.println(suggestion.getText());
        }

        driver.quit();
    }
}


