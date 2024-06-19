import requests
import csv
import glob
from datetime import datetime

# Base URL of the API
base_url = 'http://10.0.16.174'

# Function for login and get the JWT token
def login(username, password):
    url = f'{base_url}:8090/auth/login'
    headers = {'Content-Type': 'application/json'}
    data = {
        "username": username,
        "password": password
    }
    response = requests.post(url, json=data, headers=headers)
    response_data = response.json()
    return {
        'token': response_data['token'],
        'userId': response_data['userId']
    }

# Function to start a running session
def start_session(token, user_id, start_time):
    url = f'{base_url}:8091/running-sessions/start'
    headers = {
        'accept': '*/*',
        'Content-Type': 'application/json',
        'Authorization': f'Bearer {token}'
    }
    data = {
        "userId": user_id,
        "time": start_time
    }
    #print(f"URL: {url}")
    #print(f"Headers: {headers}")
    #print(f"Data: {data}")
    response = requests.post(url, json=data, headers=headers)
    response_data = response.json()
    return response_data['id']

# Function to update the running session with location points
def update_location(token, session_id, latitude, longitude, timestamp):
    url = f'{base_url}:8091/running-sessions/{session_id}/update'
    headers = {
        'accept': '*/*',
        'Content-Type': 'application/json',
        'Authorization': f'Bearer {token}'
    }
    data = {
        "latitude": float(latitude),
        "longitude": float(longitude),
        "timestamp": timestamp
    }
    response = requests.post(url, json=data, headers=headers)
    #print(f"Response body: {response.text}")
    return response.status_code

# Function to pause the running session
def pause_session(token, session_id):
    url = f'{base_url}:8091/running-sessions/{session_id}/pause'
    headers = {
        'accept': '*/*',
        'Authorization': f'Bearer {token}'
    }
    response = requests.post(url, headers=headers)
    return response.status_code

# Function to resume the running session
def resume_session(token, session_id):
    url = f'{base_url}:8091/running-sessions/{session_id}/resume'
    headers = {
        'accept': '*/*',
        'Authorization': f'Bearer {token}'
    }
    response = requests.post(url, headers=headers)
    return response.status_code

# Function to stop the running session
def stop_session(token, session_id, end_time):
    url = f'{base_url}:8091/running-sessions/{session_id}/stop'
    headers = {
        'accept': '*/*',
        'Authorization': f'Bearer {token}',
        'Content-Type': 'application/json'
    }
    data = {
        "time": end_time
    }
    response = requests.post(url, json=data, headers=headers)
    return response.status_code

# Function to simulate the run from a CSV file
def simulate_run_from_csv(token, user_id, csv_file):
    with open(csv_file, 'r') as csvfile:
        fieldnames = ['latitude', 'longitude', 'timestamp']
        reader = csv.DictReader(csvfile, fieldnames=fieldnames)
        next(reader)  # Skip the header row
        rows = list(reader)

        # Get the start time from the first row of the CSV
        start_time = rows[0]['timestamp']
        session_id = start_session(token, user_id, start_time)
        print(f"Session started with ID: {session_id}")

        for row in rows:
            status_code = update_location(token, session_id, row['latitude'], row['longitude'], row['timestamp'])
            if status_code != 200:
                print(f"Failed to update location: {row}")

        pause_status_code = pause_session(token, session_id)
        if pause_status_code == 200:
            print(f"Session {session_id} paused successfully.")
        else:
            print(f"Failed to pause session {session_id}.")

        resume_status_code = resume_session(token, session_id)
        if resume_status_code == 200:
            print(f"Session {session_id} resumed successfully.")
        else:
            print(f"Failed to resume session {session_id}.")

        end_time = rows[-1]['timestamp']
        stop_status_code = stop_session(token, session_id, end_time)
        if stop_status_code == 200:
            print(f"Session {session_id} stopped successfully.")
        else:
            print(f"Failed to stop session {session_id}.")

# Login credentials
username = "salvor_hardin"
password = "foundation"

# Get the JWT token and userId
credentials = login(username, password)
token = credentials['token']
user_id = credentials['userId']

# Simulate runs from all CSV files in the directory
csv_files = glob.glob('data/*.csv')

for csv_file in csv_files:
    simulate_run_from_csv(token, user_id, csv_file)
    print(f"Simulated run for {csv_file}")
