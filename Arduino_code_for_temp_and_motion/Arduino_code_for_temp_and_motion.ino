/*
The purpose of this code is to take temperature and motion readings from an Arduino 
and send them to a computer via XBee shield with XBee for the Arduino and a dongle 
with XBee to a serial port. The data will then be uploaded into a website using
JavaScript. The temperature sensor used is an LM61 and the motion sensor used is a 
PIR motion sensor from SparkFun.com.

The temperature sensor is connected so Vs is connected to the 5V pin on the Arduino,
the ground is connected to the the GND pin and the Vo is connected to A0. The motion
sensor is connected so Vs is connected to the 5V pin on the Arduino, the ground is 
connected to the the GND pin, and the Vo is connected to a pullup resistor which is 
connected to A1.

This project is for an assignment given by Prof. Dave Johnson in Networking 
Technologies.

Created By: George Dederich
Date Created: 2013-10-29
*/

static float temperature=0;
static int timer=0;

  void setup(){
    Serial.begin(9600); //Setting up serial connection between the Arduino                         
  }                     //and the computer.
  
  void loop(){
   
      for (int i=0; i<20; i++){               //Averaging temperature to get a more accurate
        temperature=temperature+analogRead(0); //temperature reading.
      }
      
      temperature=temperature/20.0;           //Dividing the reading by amount of times averaged
      temperature=((temperature*(5000.0/1024.0))-600.0)/10; //Change to C
      temperature=(temperature*1.8)+32; //Change temperature from C to F
      Serial.print("Temperature = ");
      Serial.println(temperature);       //Sends temperature in C to serial port
      
      if (analogRead(1) != 0){               //Motion sensor is connected to pin 1, sends a 0 when no motion is detected
        timer=timer+1;        //Counts to a set value to determine whether the room is occupied, 10 mins will be set at 600
        if (timer < 11){
          Serial.println(timer); //Sends timer values if determining if room is occupied
        }
        if (timer > 10){             
          Serial.println("Room is unoccupied"); //Sends alert that the room is unoccupied
        }
      }
       else {
         timer=0;
         Serial.println("Room is occupied"); //Sends alert that the room is occupied when movement is sensed
       }
      
      

      delay(1000);                   //Delay the loop by 1 second
      temperature = 0;          
    };

