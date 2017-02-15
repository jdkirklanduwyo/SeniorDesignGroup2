//
   
#include <SPI.h>  
#include <Pixy.h>
#include <WiFi.h>
#include <dht.h>

#define soil_apin A1
#define uv_apin A0
#define dht_apin A2

// This is the main Pixy object 
Pixy pixy;
dht DHT;

//TODO:Need a way to set this
char ssid[] = "our network";
char pass[] = "network password";
int keyIndex = 0;
char server[] = "our ReST server";

int status = WL_IDLE_STATUS;

WiFiClient client;

void setup()
{
  Serial.begin(9600);
  Serial.print("Starting...\n");

  //Get first time run settings from ReST server
  //Setup Connection
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("WiFi shield not present");
    //If no shield, stop until a shield is attached and the arduino reset
    while(true);
  }

  while(status != WL_CONNECTED) {
    status = WiFi.begin(ssid, pass);
    //Current connection delay (10s)
    delay(10000);
  }

  Serial.println("Connected to WiFi Successfully!");
  printWifiStatus();

  Serial.println("\nStarting Connection to Server:");
  if (client.connect(server, 80)) {
    Serial.println("Connected.");
    //First time settings ReST Request
    //TODO: Setup request for our server
    client.println("http://localhost:8080/settings/1");
    
    while (client.available()) {
      char c = client.read();
      //TODO: Set solenoid timers from ReST return info
    }
  }
  
  pixy.init();
}

void loop()
{ 
  static int i = 0, avgLoops = 20;
  int j, c, soilAvg = 0, uvAvg = 0, dhtTemp = 0, dhtHum = 0;
  uint16_t blocks;
  char buf[32]; 

  //Small loop for helping with sensor noise
  for (c = 0; c < avgLoops; c++)
  {
    soilAvg = soilAvg + analogRead(soil_apin);
    uvAvg = uvAvg + analogRead(uv_apin);
    DHT.read11(dht_apin);
    dhtHum = dhtHum + DHT.humidity;
    dhtTemp = dhtTemp + DHT.temperature;
    delay (2000);
  }

  //TODO: Better still connected check/Reconnect loop
  //This just reconnects if disconnected
  while(status != WL_CONNECTED) {
    status = WiFi.begin(ssid, pass);
    //Current connection delay (1000s)
    delay(1000000);
  }

  //Send sensor readings
  // grab blocks!
  blocks = pixy.getBlocks();
  
  // If there are detect blocks, print them!
  if (blocks)
  {
    i++;
    
    // do this (print) every 50 frames because printing every
    // frame would bog down the Arduino
    if (i%50==0)
    {
      sprintf(buf, "Detected %d:\n", blocks);
      Serial.print(buf);
      for (j=0; j<blocks; j++)
      {
        sprintf(buf, "  block %d: ", j);
        Serial.print(buf); 
        pixy.blocks[j].print();
      }
    }
  }

  //TODO: Format the output into our ReST String
  Serial.println (soilAvg);
  Serial.println (uvAvg);
  Serial.println (dhtHum);
  Serial.println (dhtTemp);

  
  //Adjust settings from ReST server
  //TODO: Read from ReST again and make changes if needed
  //TODO: Setup request for our server
  client.println("http://localhost:8080/settings/1");

  //TODO: Perhaps move this so it checks for changes more often
  while (client.available()) {
    char c = client.read();
    //TODO: Set solenoid timers from ReST return info
  }
  //This is the delay for the reading:
  //6 hours - 21,600,000
  delay(21600000);
}

void printWifiStatus() {
  // print the SSID of the network you're attached to:
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());

  // print your WiFi shield's IP address:
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);

  // print the received signal strength:
  long rssi = WiFi.RSSI();
  Serial.print("signal strength (RSSI):");
  Serial.print(rssi);
  Serial.println(" dBm");
}

