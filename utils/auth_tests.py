import requests
import random
import string
import json

#base URL of the API
base_url = 'http://192.168.1.15:8090/auth'

#function to generate a random username
def generate_username():
    return 'user_' + ''.join(random.choices(string.ascii_lowercase + string.digits, k=8))

#function to generate a random email
def generate_email():
    return ''.join(random.choices(string.ascii_lowercase + string.digits, k=8)) + '@example.com'

#function to register a user
def register_user(username, email):
    url = f'{base_url}/register'
    headers = {'Content-Type': 'application/json'}
    data = {
        "username": username,
        "password": "password123",
        "email": email,
        "firstName": "First",
        "lastName": "Last"
    }
    response = requests.post(url, json=data, headers=headers)
    return response.status_code

#function to login a user
def login_user(username, password):
    url = f'{base_url}/login'
    headers = {'Content-Type': 'application/json'}
    data = {
        "username": username,
        "password": password
    }
    response = requests.post(url, json=data, headers=headers)
    try:
        response_data = response.json() if response.content else {}
    except json.JSONDecodeError:
        response_data = {}
    return response.status_code, response_data

#initial data
username_valid = generate_username()
email_valid = generate_email()

#valid registration test
status_code = register_user(username_valid, email_valid)
print(f'Valid registration test: Expected 200, Got {status_code}')

#registration with repeated username test
status_code = register_user(username_valid, generate_email())
print(f'Registration with repeated username test: Expected 403, Got {status_code}')

#registration with repeated email test
status_code = register_user(generate_username(), email_valid)
print(f'Registration with repeated email test: Expected 403, Got {status_code}')

#registration with empty JSON test
status_code = requests.post(f'{base_url}/register', json={}, headers={'Content-Type': 'application/json'}).status_code
print(f'Registration with empty JSON test: Expected 403, Got {status_code}')

#valid login test
status_code, response_data = login_user(username_valid, "password123")
print(f'Valid login test: Expected 200, Got {status_code}')
if status_code == 200:
    token = response_data.get('token')
else:
    token = None

#login with wrong username test
status_code, _ = login_user("wrong_username", "password123")
print(f'Login with wrong username test: Expected 401, Got {status_code}')

#login with wrong password test
status_code, _ = login_user(username_valid, "wrong_password")
print(f'Login with wrong password test: Expected 401, Got {status_code}')

#login with empty request test
status_code = requests.post(f'{base_url}/login', json={}, headers={'Content-Type': 'application/json'}).status_code
print(f'Login with empty request test: Expected 401, Got {status_code}')
