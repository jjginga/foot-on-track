from garminconnect import Garmin
import datetime

#auth
username = "username"
password = "password"
client = Garmin(username, password)
client.login()

#perio
start_date = datetime.datetime(2023, 1, 1)
end_date = datetime.datetime(2024, 6, 24)

#filter running activities
activities = client.get_activities_by_date(start_date, end_date)
running_activities = [activity for activity in activities if activity['activityType']['typeKey'] == 'running']

#export
for activity in running_activities:
    gpx_data = client.download_activity(activity['activityId'], dl_fmt=client.ActivityDownloadFormat.GPX)
    with open(f"{activity['activityId']}.gpx", "wb") as f:
        f.write(gpx_data)
