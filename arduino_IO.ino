const int ledPin = 13;
 
int incomingByte;
      
 
void setup() {
  // initialize serial communication:
  Serial.begin(9600);

  // initialize the LED pin as an output:
  pinMode(ledPin, OUTPUT);

}
 

void loop() {

  // see if there's incoming serial data:
  if (Serial.available() > 0) {
 
   
    incomingByte = Serial.read();
  
  
    if (incomingByte == 'H') {
      digitalWrite(ledPin, HIGH);
    }
    
    if (incomingByte == 'L') {
      digitalWrite(ledPin, LOW);
    }
  }
}
