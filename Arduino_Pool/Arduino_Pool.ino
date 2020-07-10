#include <ArduinoJson.h>
#include <ESP8266WiFi.h>
//Include the SSL client
#include <WiFiClientSecure.h>

#define trigPin 14
#define echoPin 15
#define ledPin 3

char ssid[] = "Tomer_Note8";        // your network SSID (name)
char password[] = "tomer0504";      // your network key

//the time we give the sensor to calibrate (10-60 secs according to the datasheet)
int calibrationTime = 10;
int pause = 5; 
int dis = 0;

void setup() {
  Serial.begin(115200);
    
  // Set WiFi to station mode and disconnect from an AP if it was Previously connected
  WiFi.disconnect();
  delay(100);

  // Attempt to connect to Wifi network:
  Serial.println();
  Serial.print("Connecting Wifi: ");
  Serial.println(ssid);
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println("");
  Serial.println("WiFi connected");
  Serial.print("IP address: ");
  IPAddress ip = WiFi.localIP();
  Serial.println(ip);
  delay(1000);
  
  // Initialize pins
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  pinMode(ledPin, OUTPUT);
  digitalWrite(ledPin, LOW);

  for(int i = 0; i < calibrationTime; i++){
      delay(1000);
   }
    
  delay(50);
}


void loop() {

  if (WiFi.status() == WL_CONNECTED) {
    
    int counter = 0;
    int newDis = 0;
    delay(500);

    for(int i = 0; i < calibrationTime; i++){
      dis = getDistance();
      delay(1000);
    }

    digitalWrite(ledPin, HIGH);
    
    while (true) {
      newDis = getDistance();

      if (newDis < (dis-5)){
        counter++;
        if (counter >= pause){
          sendDataToFirebase();
          counter = 0;
        }
      } else {
        counter = 0;
      }
      
      delay(1000);
    }
  }
}

int getDistance() {
  long duration; 
  int distance;
  digitalWrite(trigPin, LOW); 
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  duration = pulseIn(echoPin, HIGH);
                        
  //distance = (duration/2) / 29.1;
  distance = duration*0.034/2;
  Serial.println(String(distance));

  return distance;
}

void sendDataToFirebase() {
  //Add a client
  WiFiClientSecure client;
  client.setInsecure();

  char input[] = "{\"to\":\"/topics/ArduinoPool1\",\"data\":{\"title\":\"Child in danger!!!\",\"text\":\"Danger detected by ArduinoPool1!\",\"image\":\"https://i.imgur.com/yAWMpYT.png\",\"icon\":\"ic_chat\",\"color\":\"#FF0000\",\"sound\":\"siren\",\"android_channel_id\":\"2\",\"Sensor\":\"ArduinoPool1\"}}";

  Serial.println("Send data...");
  if (client.connect("https://fcm.googleapis.com", 443)) {
    Serial.println("Connected to the server..");
    client.println("POST /fcm/send HTTP/1.1");
    client.println("User-Agent: Arduino");
    client.println("Authorization: key=AAAAxA-ivVs:APA91bG2cVLCgqYKRGE_JpHHMzmhTW7GLUyANa-jmcQ23fjRaxKHe6vd00_aje7lMSJKakL82xonM1aHe6hxuGRHz0ehmUATDGlM_qlGQlUthbE1ZCqaP1X3viHYPHL0vXJZ7goRjIA1");
    client.println("Content-Type: application/json");
    client.println("Host: fcm.googleapis.com");
    client.print("Content-Length: ");
    client.println(strlen(input));
    client.print("\n");
    client.print(input);
  } else {
    Serial.println("Connection failed");
  }

  Serial.println("Data sent...Reading response..");
  delay(3000);
  while (client.available()) {
    char c = client.read();
    Serial.print(c);
  }
  Serial.println("Finished!");
  client.flush();
  client.stop();
}
