const fs = require('fs');
const path = require('path');
const dotenv = require('dotenv');

//load environment variables
dotenv.config();

const apiKey = process.env.GOOGLE_MAPS_API_KEY;

//AndroidManifest.xml path
const manifestPath = path.join(__dirname, 'android', 'app', 'src', 'main', 'AndroidManifest.xml');

//read AndroidManifest.xml
let manifestContent = fs.readFileSync(manifestPath, 'utf8');

//replace Key
manifestContent = manifestContent.replace(/android:value="API_KEY"/, `android:value="${apiKey}"`);

//replace AndroidManifest.xml content
fs.writeFileSync(manifestPath, manifestContent);

console.log('Google Maps API Key has been set successfully.');
