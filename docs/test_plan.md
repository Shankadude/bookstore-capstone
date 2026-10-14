# Test Plan: Online Bookstore Full-Stack QA Capstone

## 1. Introduction & Project Overview
The objective of this project is to perform comprehensive Full-Stack Quality Assurance on the Online Bookstore web application and REST API (https://demoqa.com/books). Testing covers functional manual verification, REST API contract and negative payload testing, automated UI regression testing using Selenium WebDriver (Page Object Model), and Continuous Integration via Jenkins.

## 2. Testing Scope
### In-Scope
* **User Authentication (OBQ-1):** New user registration, login token generation, valid/invalid UI login, and logout flow.
* **Book Catalog & Search (OBQ-2):** Listing all available books, real-time search filtering by title/author/publisher, and viewing individual book details (ISBN, page count).
* **Collection / Cart Management (OBQ-3):** Adding books to a user's collection via API/UI, preventing duplicate book entries, and removing books from the collection.

### Out-of-Scope
* Third-party live credit card payment processing (mocked/excluded in sandbox environment).
* Load testing exceeding 100 concurrent virtual users (restricted by public sandbox rate limits).
* Cross-browser testing on Safari/iOS (testing is standardized on Google Chrome latest stable).

## 3. Testing Objectives
1. Verify 100% of core user stories meet their defined Acceptance Criteria.
2. Validate backend REST API endpoints (/Account/v1/* and /BookStore/v1/*) for status codes, JSON schema integrity, and response times under 1000ms.
3. Build a maintainable, zero-flakiness Selenium WebDriver + TestNG UI automation suite using the Page Object Model (POM) and Explicit Waits.
4. Automate test execution and JUnit XML reporting inside a CI/CD pipeline.

## 4. Risk Assessment & Mitigation Matrix
| Risk Category | Specific Threat Scenario | Likelihood | Impact | Mitigation Strategy |
| :--- | :--- | :--- | :--- | :--- |
| **Technical / Security** | Weak password validation or unauthorized access to /BookStore/v1/Books endpoints. | Medium | Critical | Execute negative API tests without Bearer tokens (expect 401 Unauthorized) and validate password complexity regex. |
| **Technical / UI Timing** | Dynamic DOM rendering and third-party ad iframes intercepting Selenium clicks (ElementClickInterceptedException). | High | Major | Use WebDriverWait (ExpectedConditions) and JavaScript executor scrolling inside Page Objects. |
| **Technical / Data** | Duplicate book ISBNs added to a user's collection causing state corruption. | Medium | Major | Verify POST /BookStore/v1/Books returns 400 Bad Request when adding an already-present ISBN. |
| **Environment** | Public sandbox experiences high latency or resets test user accounts periodically. | Medium | Moderate | Dynamically create or verify test user precondition in the API test fixture before running suite assertions. |

## 5. Test Environment & Tools
* **Issue Tracking & Management:** Atlassian Jira Cloud
* **API Testing:** Postman & Python 3 (pytest, equests)
* **UI Automation:** Java 17, Selenium WebDriver 4.25, TestNG 7.10, Maven
* **CI/CD & Version Control:** Git, GitHub, Jenkins Pipeline, GitHub Actions

## 6. Entry and Exit Criteria
* **Entry Criteria:** Target application URL and Swagger API endpoints return HTTP 200 OK; test environment dependencies installed.
* **Exit Criteria:** 100% of manual and automated test cases executed; all discovered defects logged in Jira with reproduction steps; Final Project Report completed.
