/*
The purpose of this code is to take temperature and motion readings from an Arduino 
and send them to a computer via XBee shield with XBee for the Arduino and a dongle 
with XBee to a serial port. The data will then be uploaded into a website using
JavaScript. The temperature sensor used is an LM61.

This project is for an assignment given by Prof. Dave Johnson in Networking 
Technologies.

Created By: George Dederich
Date Created: 2013-10-29
*/

static float temperature=0;

  void setup(){
    Serial.begin(9600); //Setting up serial connection between the Arduino                         
  }                     //and the computer.
  
  void loop(){
    
    //if (Serial.available() >0) { //Telling the program to proceed if there is
                                 //serial connection available.
     
      for (int i=0; i<100; i++){               //Averaging temperature to get a more accurate
        temperature=temperature+analogRead(0); //temperature reading.
      }
      
      temperature=temperature/100.0; //Dividing the reading by amount of times averaged
      temperature=((temperature*(5000.0/1024.0))-600.0)/10;
      Serial.println(temperature);     //Sends temperature in C to serial port
      delay(1000);                   //Delay the loop by 1 second
      temperature = 0;          
    };

