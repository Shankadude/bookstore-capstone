import uuid
import pytest
import requests

BASE_URL = "https://demoqa.com"

@pytest.fixture(scope="module")
def test_user():
    """
    Dynamically creates a unique user on DemoQA Book Store API,
    generates a Bearer token, and yields credentials for the test session.
    """
    unique_username = f"qa_cap_{uuid.uuid4().hex[:8]}"
    password = "TestPass@123!"
    payload = {"userName": unique_username, "password": password}

    # 1. Register the user
    reg_res = requests.post(f"{BASE_URL}/Account/v1/User", json=payload, timeout=15)
    assert reg_res.status_code == 201, f"User creation failed: {reg_res.text}"
    user_id = reg_res.json().get("userID")

    # 2. Generate Bearer Token
    tok_res = requests.post(f"{BASE_URL}/Account/v1/GenerateToken", json=payload, timeout=15)
    assert tok_res.status_code == 200, f"Token generation failed: {tok_res.text}"
    token = tok_res.json().get("token")
    assert token is not None, "Token was null in response"

    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }

    yield {
        "user_id": user_id,
        "username": unique_username,
        "password": password,
        "headers": headers
    }

    # Teardown: Delete test user after module finishes
    requests.delete(f"{BASE_URL}/Account/v1/User/{user_id}", headers=headers, timeout=15)


def test_get_all_books_catalog():
    """TC-API-01: Verify GET /BookStore/v1/Books returns 200 OK and valid book schema."""
    response = requests.get(f"{BASE_URL}/BookStore/v1/Books", timeout=15)

    assert response.status_code == 200
    assert "application/json" in response.headers.get("Content-Type", "")
    assert response.elapsed.total_seconds() < 5.0, "API response exceeded 5s SLA"

    data = response.json()
    assert "books" in data and isinstance(data["books"], list)
    assert len(data["books"]) >= 8, "Expected at least 8 default books in catalog"

    first_book = data["books"][0]
    for required_key in ("isbn", "title", "author", "publisher", "pages"):
        assert required_key in first_book, f"Missing key '{required_key}' in book object"


def test_get_single_book_by_isbn():
    """TC-API-02: Verify GET /BookStore/v1/Book?ISBN=9781449325862 returns 'Git Pocket Guide'."""
    isbn = "9781449325862"
    response = requests.get(f"{BASE_URL}/BookStore/v1/Book", params={"ISBN": isbn}, timeout=15)

    assert response.status_code == 200
    book = response.json()
    assert book["isbn"] == isbn
    assert book["title"] == "Git Pocket Guide"
    assert book["author"] == "Richard E. Silverman"


def test_get_single_book_invalid_isbn():
    """TC-API-03: Verify GET /BookStore/v1/Book with non-existent ISBN returns 400 Bad Request."""
    response = requests.get(f"{BASE_URL}/BookStore/v1/Book", params={"ISBN": "0000000000000"}, timeout=15)

    assert response.status_code == 400
    body = response.json()
    assert "code" in body and "message" in body


def test_authorized_user_status(test_user):
    """TC-API-04: Verify POST /Account/v1/Authorized confirms user is authorized."""
    payload = {"userName": test_user["username"], "password": test_user["password"]}
    response = requests.post(f"{BASE_URL}/Account/v1/Authorized", json=payload, timeout=15)

    assert response.status_code == 200
    assert response.json() is True


def test_add_book_to_user_collection(test_user):
    """TC-API-05: Verify POST /BookStore/v1/Books adds a book ISBN to the authenticated user's profile."""
    isbn = "9781449325862"
    payload = {
        "userId": test_user["user_id"],
        "collectionOfIsbns": [{"isbn": isbn}]
    }
    response = requests.post(
        f"{BASE_URL}/BookStore/v1/Books",
        json=payload,
        headers=test_user["headers"],
        timeout=15
    )

    assert response.status_code == 201, f"Expected 201 Created, got {response.status_code}: {response.text}"
    data = response.json()
    assert data["books"][0]["isbn"] == isbn


def test_add_duplicate_book_rejected(test_user):
    """TC-API-06: Verify adding an already-present ISBN returns 400 Bad Request."""
    isbn = "9781449325862"
    payload = {
        "userId": test_user["user_id"],
        "collectionOfIsbns": [{"isbn": isbn}]
    }
    response = requests.post(
        f"{BASE_URL}/BookStore/v1/Books",
        json=payload,
        headers=test_user["headers"],
        timeout=15
    )

    assert response.status_code == 400
    assert "already present" in response.json().get("message", "").lower()


def test_add_book_unauthorized_rejected():
    """TC-API-07: Verify POST /BookStore/v1/Books without Bearer token returns 401 Unauthorized."""
    payload = {
        "userId": "invalid-user-id",
        "collectionOfIsbns": [{"isbn": "9781449325862"}]
    }
    response = requests.post(f"{BASE_URL}/BookStore/v1/Books", json=payload, timeout=15)

    assert response.status_code == 401
