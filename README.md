# Online Bookstore Full-Stack QA Capstone Project

![Full-Stack QA Capstone CI](https://github.com/shashankdattu/bookstore-capstone/actions/workflows/main.yml/badge.svg)

A complete, production-grade Quality Assurance capstone repository demonstrating end-to-end testing practices across the entire software delivery lifecycle: **Requirements & Risk Analysis**, **Manual Test Authoring & Jira Defect Tracking**, **REST API Contract & Negative Testing**, **Java Selenium Page Object Model UI Automation**, and **CI/CD Integration (Jenkins & GitHub Actions)**.

* **Target Application:** DemoQA Book Store Web UI (`https://demoqa.com/books`)
* **Target API:** DemoQA Swagger REST API (`https://demoqa.com/swagger/`)

---

## Repository Architecture

```text
bookstore-capstone/
├── .github/
│   └── workflows/
│       └── main.yml                  # GitHub Actions CI workflow (Python + Java pipeline)
├── docs/
│   ├── test_plan.md                  # Test strategy, scope boundaries, and risk matrix
│   └── project_report.md             # Formal QA final evaluation and root cause analysis
├── manual_testing/
│   ├── test_cases.csv                # 10 manual test cases (functional, negative, boundary)
│   └── defect_reports/
│       ├── defect_001.md             # DEF-001 (OBQ-4): Search whitespace sanitization bug
│       └── defect_002.md             # DEF-002 (OBQ-5): Pagination view state reset bug
├── api_testing/
│   ├── bookstore_postman_collection.json # Exported Postman collection with pm.test() assertions
│   ├── requirements.txt              # Pytest and Requests dependency versions
│   ├── reports/
│   │   └── api-results.xml           # Generated JUnit XML test report from Pytest
│   └── tests/
│       ├── __init__.py
│       └── test_bookstore_api.py     # 7 automated REST API tests (lifecycle fixture, negative checks)
├── ui_automation/
│   ├── pom.xml                       # Maven build configuration (Selenium 4.25, TestNG 7.10)
│   └── src/test/
│       ├── java/com/bookstore/
│       │   ├── pages/
│       │   │   ├── LoginPage.java    # Page Object for /login with explicit waits
│       │   │   └── BookStorePage.java# Page Object for /books with JS-assisted actions
│       │   └── tests/
│       │       └── BookStoreUiTests.java # TestNG test suite with @DataProvider (7 executions)
│       └── resources/
│           ├── config.properties     # Application endpoints and timeout parameters
│           └── testng.xml            # TestNG suite runner XML configuration
├── ci_cd/
│   └── Jenkinsfile                   # Declarative multi-stage Jenkins CI pipeline
├── .gitignore                        # Git exclusion rules for Python, Java, IDEs, and OS artifacts
└── README.md                         # Project documentation and reproduction instructions


