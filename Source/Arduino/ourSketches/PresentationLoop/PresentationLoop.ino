#define waterPin 4 
#define LED 2 //Our "light". Digital pin 2

void setup() {
	pinMode(waterPin, OUTPUT);
	pinMode(LED, OUTPUT);
	digitalWrite(waterPin, LOW);
  digitalWrite(LED, LOW);
	delay(6000); //Wait for 10 mins (600,000)
}

void loop() {
  digitalWrite(LED, HIGH);
  delay(2000);
  digitalWrite(waterPin, HIGH);
  delay(5000);
  digitalWrite(waterPin, LOW);
  delay(10000000000);
}
