# Defect Report: DEF-002

* **Jira Issue Key:** OBQ-5 (Bug)
* **Linked Test Case:** TC-010
* **Summary:** [Catalog UI] Pagination row-size preference resets to default (10 rows) after viewing a book details page
* **Severity:** Trivial / Usability
* **Priority:** P4 (Low)
* **Status:** Open
* **Environment:** Production/Staging Sandbox (https://demoqa.com/books) | Windows 11 | Chrome 130.0

## Description
When a shopper changes the catalog table pagination dropdown from 10 rows to 5 rows, clicks on a book to inspect its details, and clicks **Back To Book Store**, the component state is destroyed and resets back to 10 rows.

## Steps to Reproduce
1. Navigate to https://demoqa.com/books.
2. Scroll to the bottom pagination bar and change the rows-per-page <select> dropdown (ria-label="rows per page") to 5 rows.
3. Click on the book title "Git Pocket Guide".
4. Click the **Back To Book Store** button (id="addNewRecordButton").
5. Check the pagination dropdown value.

## Expected Result
The catalog table retains the user's selected 5 rows view state during session navigation.

## Actual Result
The pagination dropdown resets to 10 rows.
