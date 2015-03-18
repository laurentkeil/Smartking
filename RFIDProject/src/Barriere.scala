



/*
 * Copyright 2007 Phidgets Inc.  All rights reserved.
 */

import java.awt.Button;
import java.awt.Frame;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

import com.phidgets._
import com.phidgets.event._


class Barriere {
	var servo:AdvancedServoPhidget=null;
	val open:Boolean=false;
	val close:Boolean=false;
	
	def Barriere() = {
		servo= new AdvancedServoPhidget();
		/**
		 * Régler les paramètres du servo motor
		 * Set Min Position = 0; Set Max Position=100
		 */
		
		this.servo.addAttachListener(new AttachListener() {
			def attached(ae:AttachEvent ) = {
				System.out.println("attachment of " + ae);
				//if(this.open==true){this.ouverture();}
				//if(this.close==true){}
				System.out.println("L'erreur est : " + ae.getClass());
        config();
			}
		});
		this.servo.addDetachListener(new DetachListener() {
			def  detached(ae:DetachEvent) {
				System.out.println("detachment of " + ae);
				System.out.println("Veuillez le rebranchez");
				try {
					servo.setPosition(0, 20.0);
				} catch {
					// TODO Auto-generated catch block
          case e : PhidgetException =>{ e.printStackTrace();
					System.out.println("Problème quand on le branche");}
				}
			}
		});
		this.servo.addErrorListener(new ErrorListener() {
			def  error(ee:ErrorEvent ) {
				System.out.println("error event for " + ee);
			}
		});
		this.servo.addServoPositionChangeListener(new ServoPositionChangeListener()
		{
			def servoPositionChanged(oe:ServoPositionChangeEvent)
			{
				System.out.println(oe);
			}
		});
    
    config();
		
	}
	
  def config(){
    
    System.out.println("Configuration du servo motor");
    /*System.out.println("Serial: " + servo.getSerialNumber());
    System.out.println("Servos: " + servo.getMotorCount());*/
    System.out.println("waiting for AdvancedServo attachment...");
    servo.openAny();
    servo.waitForAttachment();
    //System.out.println("Ligne 70 : "+ servo.getVelocity(0) + " - Position : " + servo.getPosition(0));
    
    servo.openAny();
    servo.setPosition(0, 10.0);
    //System.out.println("Renew 70 : "+ servo.getVelocity(0) + " - Position : " + servo.getPosition(0));

    //this.speed()
    this.servo.setEngaged(0, true);
  }
  
  
  def ouverture():Unit ={
	
		System.out.println("Ouverture barrière...");
	    	this.servo.setPosition(0, 120.0);
      
		System.out.println("Barrière ouverte");
	}	
	
	def  fermeture() = {
		
		System.out.println("Fermeture barrière...");
	    	this.servo.setPosition(0, 20.0);	
       
		System.out.println("Barrière fermée");
	}
	
	def  close_system() ={
		
		this.servo.setEngaged(0, false);
		//this.servo.close();
		
	}
}


