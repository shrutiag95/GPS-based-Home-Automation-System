import processing.serial.*;
Serial port;

 void setup()  {

   
      println(Serial.list());
    port = new Serial(this, "/dev/ttyUSB0", 9600);  // Open the port that the Arduino board is connected to, at 9600 baud

}
 void draw() {

  String onoroff[] = loadStrings("http://androcation.16mb.com/LEDstate.txt"); // Insert the location of your .txt file
  print(onoroff[0]);  // Prints whatever is in the file ("1" or "0")

  if (onoroff[0].equals("1") == true) {
    println(" - TELLING ARDUINO TO TURN LED ON");
    port.write('H'); // Send "H" over serial to set LED to HIGH

  } else {

    println(" - TELLING ARDUINO TO TURN LED OFF");
    port.write('L');  // Send "L" over serial to set LED to LOW
 }

  delay(7000); // Set your desired interval here, in milliseconds
 }

