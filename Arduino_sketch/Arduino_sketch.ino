#include <ESP8266WiFi.h>

#define trigPin 16
#define echoPin 15

//Set web server port number to 80
WiFiServer server(80);

const char* ssid = "Tomer_Note8";
const char* password = "tomer0504";

// Variable to store the HTTP request
String header;

// Variable to store the status of WiFi
int wifiStatus = WL_IDLE_STATUS;

// Current time
unsigned long currentTime = millis();
// Previous time
unsigned long previousTime = 0; 
// Define timeout time in milliseconds (example: 2000ms = 2s)
const long timeoutTime = 2000;

void setup()
{
    Serial.begin(115200);
    pinMode(trigPin, OUTPUT);
    pinMode(echoPin, INPUT);

    //Connect to the WiFi network with SSID and password
    Serial.print("Connecting to ");
    //Serial.println(ssid);
    WiFi.begin(ssid, password);
    
    while (WiFi.status() != WL_CONNECTED)
    {  //Wait for connection
        delay(500);
        Serial.println("Waiting to connect...");
    }

    // Print local IP address and start web server
    Serial.println("");
    Serial.println("WiFi connected.");
    Serial.println("IP address: ");
    Serial.println(WiFi.localIP());
    server.begin();
    Serial.println("Server listening");
    wifiStatus = WL_IDLE_STATUS;
}

void loop()
{
    WiFiClient client = server.available();     // Listen for incoming clients

    // If a new client connects
    if (client) 
    {
        Serial.println("New Client.");          // print a message out in the serial port
        String currentLine = "";                // make a String to hold incoming data from the client
        currentTime = millis();
        previousTime = currentTime;
        while (client.connected() && currentTime - previousTime <= timeoutTime) 
        { // loop while the client's connected
            currentTime = millis();         
            if (client.available()) 
            {             // if there's bytes to read from the client,
                char c = client.read();             // read a byte, then
                Serial.write(c);                    // print it out the serial monitor
                header += c;
                if (c == '\n') 
                {                    
                    // if the byte is a newline character
                    // if the current line is blank, you got two newline characters in a row.
                    // that's the end of the client HTTP request, so send a response:
                    if (currentLine.length() == 0) 
                    {
                        // HTTP headers always start with a response code (e.g. HTTP/1.1 200 OK)
                        // and a content-type so the client knows what's coming, then a blank line:
                        client.println("HTTP/1.1 200 OK");
                        client.println("Content-type:text/html");
                        client.println("Connection: close");
                        client.println();

                        // Display the HTML web page
                        //client.println("<!DOCTYPE html><html>");
                        //client.println("<head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
                        //client.println("<link rel=\"icon\" href=\"data:,\">");
                        // CSS
                        //client.println("<style>html { font-family: Helvetica; display: inline-block; margin: 0px auto; text-align: center;}</style></head>");
                        
                        // Web Page Heading
                        //client.println("<body><h1>ESP8266 Web Server - Tomer&Ori</h1>");
                        
                        // Calculate the distance
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
                        client.println(String(distance));
                        
                        // Display current distance 
                        //client.println("<p>Distance: " + String(distance) + " cm</p>");
                        
                        //client.println("</body></html>");
                        
                        // The HTTP response ends with another blank line
                        client.println();
                        // Break out of the while loop
                        break;
                    } 
                    
                    else 
                    { 
                        // if you got a newline, then clear currentLine
                        currentLine = "";
                    }
                } 
                
                else if (c != '\r') 
                {  
                    // if you got anything else but a carriage return character,
                    currentLine += c;      // add it to the end of the currentLine
                }
            }
        }
        
        // Clear the header variable
        header = "";
        // Close the connection
        client.stop();
        Serial.println("Client disconnected.");
        Serial.println("");
    }
}
