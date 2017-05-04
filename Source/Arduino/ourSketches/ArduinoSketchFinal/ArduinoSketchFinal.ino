#include <SPI.h>  
#include <Pixy.h>
#include <WiFi.h>
#include <dht.h>

// Senior Design Project - Power of Robotany
// Members: Jacob Kirkland, Nate Kuhn, Kevin Shelton
//
// This is the arduino sketch for our arduino uno. The sketch connects the arduino,
// then pulls the settings from the server. With the settings, it parses the current plant
// data from the server. With all of the needed info on the current plant, the system then
// activates the water solenoid/light relay or enters the water timing loop. If neither of 
// these things are flagged, the system will just report the sensor readings.

//Here we set our analog pin locations on the arduino
#define soil_apin A1
#define uv_apin A0
#define dht_apin A2
//and our digital pins for solenoid/relay
#define waterPin 4
#define LED 2

// This is the main Pixy object 
Pixy pixy;
// This is our dual sensor
dht DHT;

char ssid[] = "our network";	//Wi-Fi Connection info
char pass[] = "network password";
int keyIndex = 0, waterWait = 0, waterFlag = 0, lightFlag = 0, waterTime = 0;
char curURL[] = "localhost:8080/";
char * dataSet;
char server[] = "localhost"; //Set to server computer IP

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
  //printWifiStatus();
  Serial.println("\nStarting Connection to Server:");
  if (client.connect(server, 8080)) {
    Serial.println("Connected.");
    }
  
  //Activate our modes for water solenoid and light relay, then ensure they are off
  pinMode(waterPin, OUTPUT);
  pinMode(LED, OUTPUT);
  digitalWrite(waterPin, LOW);
  digitalWrite(LED, LOW);
  pixy.init();
}

void loop()
{ 
  static int avgLoops = 20;
  int i, j, c, train, health, id, folColor, soilAvg = 0, uvAvg = 0, dhtTemp = 0, dhtHum = 0;
  bool endServ = false;
  uint16_t blocks;
  char buf[32], pixyData[512], sensOut[128]; 
  
  //This just reconnects if disconnected
  //Future: Better still connected check/Reconnect loop
  while(status != WL_CONNECTED) {
    status = WiFi.begin(ssid, pass);
    //Current connection delay (1000s)
    delay(1000000);
  }
  
  //Settings ReST Request
  client.println("http://localhost:8080/settings");
  while (client.available()) 
  {
    // This reads the data from the server on what the settings should be
    dataSet = "";
    while (!endServ)
    {
      char r = client.read();
      dataSet += r; 

      // Parse one char at a time until endline
      if (r == '\n')
      {
        endServ = true;
      }
    }
    endServ = false;
	
	//char 31 is waitFlag, 49 is waterFlag and 67 is lightFlag
    if (dataSet[31] == "1")
    {
	    waterWait = 1;
	    continue;
    }
	  else if (dataSet[49] == "1")
	  {
	    waterFlag = 1;
	  }
	  else if (dataSet[67] == "1")
	  {
	    //If the light needs turned on, simply turn it on
	    digitalWrite(LED, HIGH);
	  }
	  else if (dataSet[67] == "0")
	  {
	    //and back off again when needed
	    digitalWrite(LED, LOW);
	  }
	  //Now we retrieve the current plant information
	  client.println("http://localhost:8080/current");
	  dataSet = "";
      while (!endServ)
      {
        char r = client.read();
        dataSet += r; 

        // Parse one char at a time until endline
        if (r == '\n')
        {
          endServ = true;
        }
      }
      endServ = false;
	
	  //This parses the int tokens we need from the data
	  char * splitStr;
	  splitStr = strtok (dataSet, " ':,");
	  for (i = 0; i < 15; i++)
	  {
	    splitStr = strtok (dataSet, " ':,");
	    if (i == 0)
	    {
	      //inTraining flag
	      train = atoi(splitStr);
	    }
	    else if (i == 10)
	    {
	      //Current plant health val
	      health = atoi(splitStr);
	    }
	    else if (i == 14)
	    {
		    //And our current plant id
	      id = atoi(splitStr);
	    }
	  }
	
  	//Then we use the plant id to retrieve a couple more values
  	sprintf(curURL, "http://localhost:8080/plant/%d", id);
   	client.println(curURL);
  	dataSet = "";
    while (!endServ)
    {
      char r = client.read();
      dataSet += r; 

      // Parse one char at a time until endline
      if (r == '\n')
      {
        endServ = true;
      }
    }
    endServ = false;
    splitStr = strtok (dataSet, " ':,");
  	for (i = 0; i < 15; i++)
  	{
	    if (i == 6)
	    {
	      //The foliage color signature
	      folColor = atoi(splitStr);
	    }
   	  else if (i == 14)
  	  {
	      //and the watering timer time
	      waterTime = atoi(splitStr);
	    }
	  }
  
  }

  //grab # of pixy blocks
  blocks = pixy.getBlocks();
  
  //Initialize our pixy X and Y arrays
  int blockXarr[blocks], blockYarr[blocks];
  for (i = 0; i < blocks; i++)
  {
    blockXarr[i] = 0;
	  blockYarr[i] = 0;
  }

  //Small loop for helping with sensor noise
  for (c = 0; c < avgLoops; c++)
  {
    soilAvg = soilAvg + analogRead(soil_apin);
    uvAvg = uvAvg + analogRead(uv_apin);
    DHT.read11(dht_apin);
    dhtHum = dhtHum + DHT.humidity;
    dhtTemp = dhtTemp + DHT.temperature;

	  //Here we gather the X and Y values for our pixy blocks matching the correct color
    if (blocks)
    {
	    i = 0;
      for (j=0; j<blocks; j++)
      {
		    if (pixy.blocks[j].signature == folColor)
		    {
		      blockXarr[i] = blockXarr[i] + pixy.blocks[j].width;
		      blockYarr[i] = blockYarr[i] + pixy.blocks[j].height;
		    }
	   	i++;
      }
    }
    delay (2000);
  }
  
  //This times our watering for a new plant or plant pot
  if (waterWait == 1)
  {
    //Alert the system arduino is waiting
    client.println("http://localhost:8080/settings/waiting/1");
	  delay(1000);
	
	  //And ask the server for an update
	  client.println("http://localhost:8080/settings");
	  while (waterWait == 1)
	  {
  	  //Once the system is queued and ready
  	  dataSet = "";
      while (!endServ)
      {
        char r = client.read();
        dataSet += r; 

        // Parse one char at a time until endline
        if (r == '\n')
        {
          endServ = true;
        }
      }
      endServ = false;
  	  if (dataSet[49] == "1")
  	  {
            //Open the solenoid once the app says go
  	        digitalWrite(waterPin, HIGH);
		
	    	//then check quickly until the app says stop
    		delay(200);
    		client.println("http://localhost:8080/settings");
    		dataSet = "";
            while (!endServ)
            {
              char r = client.read();
              dataSet += r; 

              // Parse one char at a time until endline
              if (r == '\n')
              {
              endServ = true;
              }
            }
            endServ = false;
    		while (dataSet[49] == "1")
    		{
    		  delay(200);
	    	  client.println("http://localhost:8080/settings");
	    	  dataSet = "";
              while (!endServ)
              {
                char r = client.read();
                dataSet += r; 

                // Parse one char at a time until endline
                if (r == '\n')
                {
                  endServ = true;
                }
              }
              endServ = false;
    		}
    		//then turn the water off
    		digitalWrite(waterPin, LOW);
    	  }
    	}
    }
  //If the plant needs watered, we now have the waterTime from our previous data retrieval
  if (waterFlag == 1)
  {
    digitalWrite(waterPin, HIGH);
	  delay(waterTime);
	  digitalWrite(waterPin, LOW);
  }
    
  //Now we calculate our averages for the sensors
  soilAvg = soilAvg / avgLoops;
  uvAvg = uvAvg / avgLoops;
  dhtHum = dhtHum / avgLoops;
  dhtTemp = dhtTemp / avgLoops;
  
  //And format our pixy data for outputting
  int goodBlocks = (sizeof(blockXarr)/sizeof(int));
  
  //We start with the number of matching color blocks
  sprintf(pixyData, "%d", goodBlocks);
  for (i = 0; i < goodBlocks; i++)
  {
    //followed by xy's for each block
    blockXarr[i] = blockXarr[i] / avgLoops;
	  blockYarr[i] = blockYarr[i] / avgLoops;
	  sprintf(pixyData, "%s %d%d", pixyData, blockXarr[i], blockYarr[i]);
  }
  
  //Send sensor readings
  //Plant update
  sprintf(sensOut, "%d %d %d %d %d %d", id, uvAvg, soilAvg, dhtHum, dhtTemp, health);
  sprintf(curURL, "http://localhost:8080/update/plant/%s", sensOut);
  client.println(curURL);
  
  //Current plant update
  sprintf(sensOut, "%s %d", sensOut, train);
  sprintf(curURL, "http://localhost:8080/update/current/%s", sensOut);
  client.println(curURL);
  
  //Pixy data update
  sprintf(curURL, "http://localhost:8080/pixy/currentBlocks/set/%s", pixyData);
  client.println(curURL);
  
  //Delay until next sensor reading
  //6 hours - 21,600,000
  delay(216000);
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

