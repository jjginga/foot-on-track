import requests
import time
from datetime import datetime

#base URL of the API
base_url = 'http://192.168.1.15'
running_session_url = f'{base_url}:8091/running-sessions'

# function to get the current time in ISO 8601 format
def get_current_time_iso():
    return datetime.utcnow().isoformat()

#function to authenticate and get token and userID
def login(username, password):
    url = f'{base_url}:8090/auth/login'
    headers = {'Content-Type': 'application/json'}
    data = {
        "username": username,
        "password": password
    }
    response = requests.post(url, json=data, headers=headers)
    if response.status_code == 200:
        response_data = response.json()
        return response_data.get('token'), response_data.get('userId')
    else:
        return None, None

#function to start a running session
def start_session(token, user_id):
    url = f'{running_session_url}/start'
    headers = {
        'Content-Type': 'application/json',
        'Authorization': f'Bearer {token}'
    }
    data = {
        "userId": user_id,
        "time": get_current_time_iso()
    }
    response = requests.post(url, json=data, headers=headers)
    return response.status_code, response.json() if response.content and response.status_code==200 else {}

#function to update a running session
def update_session(token, session_id):
    url = f'{running_session_url}/{session_id}/update'
    headers = {
        'Content-Type': 'application/json',
        'Authorization': f'Bearer {token}'
    }
    data = {
        "latitude": 40.7128,
        "longitude": -74.0060,
        "timestamp": get_current_time_iso()
    }
    response = requests.post(url, json=data, headers=headers)
    return response.status_code

#function to stop a running session
def stop_session(token, session_id):
    url = f'{running_session_url}/{session_id}/stop'
    headers = {
        'Content-Type': 'application/json',
        'Authorization': f'Bearer {token}'
    }
    data = {
        "time": get_current_time_iso()
    }
    response = requests.post(url, json=data, headers=headers)
    return response.status_code

#function to pause a running session
def pause_session(token, session_id):
    url = f'{running_session_url}/{session_id}/pause'
    headers = {
        'Content-Type': 'application/json',
        'Authorization': f'Bearer {token}'
    }
    response = requests.post(url, headers=headers)
    return response.status_code

#function to resume a running session
def resume_session(token, session_id):
    url = f'{running_session_url}/{session_id}/resume'
    headers = {
        'Content-Type': 'application/json',
        'Authorization': f'Bearer {token}'
    }
    response = requests.post(url, headers=headers)
    return response.status_code

#authenticate and get token and userID
token, user_id = login("batman", "gotham")
if not token or not user_id:
    print("Authentication failed")
    exit()

#initial data
timestamp_start = int(time.time())


#valid session start
status_code, response_data = start_session(token, user_id)
session_id = response_data.get('id') if status_code == 200 else None
print(f'Valid session start: Expected 200, Got {status_code}')
#valid session stop
status_code = stop_session(token, session_id)
print(f'Stop session: Expected 200, Got {status_code}')

#invalid session start
status_code, response_data = start_session(token, user_id)
session_id = response_data.get('id') if status_code == 200 else None
status_code, response_data = start_session(token, user_id)
print(f'Start session again: Expected 409, Got {status_code}')
status_code = stop_session(token, session_id)

#valid session update
status_code, response_data = start_session(token, user_id)
session_id = response_data.get('id') if status_code == 200 else None
status_code = update_session(token, session_id)
print(f'Update with session started: Expected 200, Got {status_code}')
status_code = stop_session(token, session_id)

#update without started session
status_code = update_session(token, session_id)
print(f'Update without starting session: Expected 409, Got {status_code}')

#valid pause
status_code, response_data = start_session(token, user_id)
session_id = response_data.get('id') if status_code == 200 else None
status_code = pause_session(token, session_id)
print(f'Pause started session: Expected 200, Got {status_code}')
status_code = stop_session(token, session_id)

#pause without starting session
status_code = pause_session(token, session_id)
print(f'Pause without starting session: Expected 409, Got {status_code}')

#valid resume
status_code, response_data = start_session(token, user_id)
session_id = response_data.get('id') if status_code == 200 else None
status_code = pause_session(token, session_id)
status_code = resume_session(token, session_id)
print(f'Resume with paused session: Expected 200, Got {status_code}')
#resume session that is not paused
status_code = resume_session(token, session_id)
print(f'Resume without starting session: Expected 409, Got {status_code}')
status_code = stop_session(token, session_id)

#resume without starting session
status_code = resume_session(token, session_id)
print(f'Resume without pausing session: Expected 409, Got {status_code}')

#stop without starting session
status_code = stop_session(token, session_id)
print(f'Stop without starting session: Expected 409, Got {status_code}')

#stoped paused session
status_code, response_data = start_session(token, user_id)
session_id = response_data.get('id') if status_code == 200 else None
status_code = pause_session(token, session_id)
status_code = stop_session(token, session_id)
print(f'Stop paused session: Expected 200, Got {status_code}')
