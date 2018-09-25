## City Finder

Endpoint: `suggestions?q=London&lat=40.222&long=-40.333`

### Preparation

Download the following files

http://download.geonames.org/export/dump/CA.zip
http://download.geonames.org/export/dump/US.zip

and extract them to

`src/main/resources/CA/CA.txt`
`src/main/resources/US/US.txt`

### Starting server

`java -Dserver.port=8080 -jar build/libs/cityFinder-0.1.0.jar`
