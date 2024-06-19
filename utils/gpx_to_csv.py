import xml.etree.ElementTree as ET
import csv
import glob
import os

def gpx_to_csv(gpx_file, csv_file):
    tree = ET.parse(gpx_file)
    root = tree.getroot()

    # Namespace
    ns = {'default': 'http://www.topografix.com/GPX/1/1'}

    with open(csv_file, 'w', newline='') as csvfile:
        fieldnames = ['latitude', 'longitude', 'timestamp']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)

        writer.writeheader()

        for trkpt in root.findall('.//default:trkpt', ns):
            latitude = trkpt.attrib['lat']
            longitude = trkpt.attrib['lon']
            timestamp = trkpt.find('default:time', ns).text

            writer.writerow({'latitude': latitude, 'longitude': longitude, 'timestamp': timestamp})

# Convert all GPX files in the directory to CSV
gpx_files = glob.glob('data/*.gpx')

for gpx_file in gpx_files:
    csv_file = gpx_file.replace('.gpx', '.csv')
    gpx_to_csv(gpx_file, csv_file)
    print(f"Converted {gpx_file} to {csv_file}")
