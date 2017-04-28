int waterPin = 3; //TODO: Need actual pin

#define CH1 3 //Channel one for digital pin 3
#define LED 4 //Our "light". Digital pin 4

void setup() {
	pinMode(waterPin, OUTPUT);
	pinMode(CH1, OUTPUT);
	pinMode(LED, OUTPUT);
	digitalWrite(CH1, LOW);
	delay(600000); //Wait for 10 mins (600,000)
	digitalWrite(waterPin, HIGH);
	digitalWrite(CH1, HIGH);
	delay(5000); //5 Seconds ,watering time
	digitalWrite(waterPin, LOW)
}

void loop() {

}