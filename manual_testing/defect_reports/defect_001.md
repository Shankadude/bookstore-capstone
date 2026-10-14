# Defect Report: DEF-001

* **Jira Issue Key:** OBQ-4 (Bug)
* **Linked Test Case:** TC-006
* **Summary:** [Search] Book catalog search input fails to trim leading/trailing whitespace, returning 'No rows found' for valid titles
* **Severity:** Minor
* **Priority:** P3 (Medium)
* **Status:** Open
* **Environment:** Production/Staging Sandbox (https://demoqa.com/books) | Windows 11 | Chrome 130.0

## Description
When a user copies and pastes a book title into the catalog search box (#searchBox) that includes a leading or trailing space (e.g., " Git Pocket Guide "), the client-side filter performs a strict literal substring check without calling .trim() on the query string. As a result, a valid book title returns "No rows found".

## Steps to Reproduce
1. Navigate to https://demoqa.com/books.
2. Click into the Search input field (id="searchBox").
3. Enter "Git Pocket Guide " (with one trailing space at the end).
4. Observe the filtered book table results.

## Expected Result
The search filter should sanitize and trim leading/trailing whitespace from user input and display the matching record (Git Pocket Guide by Richard E. Silverman).

## Actual Result
The table collapses and displays "No rows found".
