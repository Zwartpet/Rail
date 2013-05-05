//******************************************************************************
//
// Filename :       main.c
// Dependencies:    all
// Processor:       PIC18
// Compiler:        MCC18 v3.37 or higher
// Company:         ITopia
// Author:	    Casper Eekhof, John Zwarthoed
// Date:            2012-02-20
// Version:         1.0
//
//******************************************************************************

#include <p18f4550.h>
#include "xlcd/xlcd.h"
#include "pwm/pwm.h"
#include "usart/usart.h"
#include "myadc.h"
#include "sensor.h"
#include "constants.h"
#include "command.h"// stopt alle comand in een qeue 

//
//------------------------------------------------------------------------------
//

void task_PROX();
int carIsNear(void);
void takeNextLeft(void);
void takeNextRight(void);
void lineCorrection(void);
void displayOnStart(void);
void processData(void);
void updateLocation(char);
void pulse(int led);
void pulse_setup(void);
void adjust(void);

void pulse(int led) {
    if (led == 4) {
        PORTBbits.RB4 = ~PORTBbits.RB4;
    } else if (led == 6) {
        PORTBbits.RB6 = ~PORTBbits.RB6;
    }
}

void pulse_setup(void) {
    int i;
    TRISBbits.TRISB4 = 0; // Port is output
    PORTBbits.RB4 = 0;
    TRISBbits.TRISB6 = 0; // Port is output
    PORTBbits.RB6 = 0;
    while (i++ < 1000);
}

//To check which sensors are above the black tape we got to save the value's somewhere
int innerR = 0; //inner right sensor - - 1 -
int innerL = 0; //inner left sensor  - 1 - -
int outerR = 0; //outer right sensor - - - 1
int outerL = 0; //outer left sensor  1 - - -

int turned = 0; //We are using turned and turning to make sure we've made a turn
int turning = 0;

int onNode = 0; //We are using onNode so we can skip one at the time and no multiple skips

int drive = 0; //new command so we probably got to go somewhere, lets get ready to start the engine

//These integers will be used to save the directions we get from the server
int reservedR = 0; //intersection up ahead: to the right
int reservedL = 0; //intersection up ahead: to the left
int reservedW = 0; //intersection up ahead: we're not going left, we're not going right but we're going straight forward
int reservedS = 0;

char color;




//int drive = 0; //No need to drive yet, we've got to receive a task first
int completed = 1; //No task yet, so we are ready to receive one
char nextCommand; //Temporary location of the command we've got to execute
int xCounter = 0;
int commandCounter = 0;
int requireReboot = 0;
extern int timer;

void processData(void) {
    pulse(4);
    //        pulse();
    readLineSensors(&innerL, &innerR, &outerL, &outerR);

    // Get new message and handle it
    if (completed && queueLength) {
        nextCommand = dataQueue[0]; //get first command in command queue, and save it somewhere temporary
        shiftCommand(dataQueue, queueLength--); //shift the commands so we get a new command when we're finished with the current one
        completed = 0; //we've just got us a new command so we're not completed yet
        drive = 1;

        if (commandCounter == 16) {
            XLCDL2home();
        } else if (commandCounter == 32) {
            XLCDClear();
            XLCDL1home();
            commandCounter = 0;
        }
        commandCounter++;

        XLCDPut(nextCommand);
        switch (nextCommand) {
            case 'S':
                reservedS = 1;
                drive = 0; //Stop command received, no more driving :(
                //printQueue(dataQueue, queueLength);
                break;
            case 'R':
                reservedR = 1; //Command R received, we should turn right when its possible
                break;
            case 'L':
                reservedL = 1; //Command L received, we should turn left when its possible
                break;
            case '1':
                reservedW = 1; //Command 1 received, we should skip a node
                break;
        }
    }

    if (!carIsNear() && !completed && !reservedS) {
        drive = 1;
    } else {
        drive = 0;
    }
    // Drive
    if (drive) {
        //            pulse();
        //If we need to take the next left.
        readLineSensors(&innerL, &innerR, &outerL, &outerR);

        if (reservedL) {
            takeNextLeft();
        } else if (reservedR) { //If we need to take the next right.
            takeNextRight();
        }

        readLineSensors(&innerL, &innerR, &outerL, &outerR);

        // color =  readColorSensor();
        // Send the location to the server
        //    if (color != 'K') {
        //            updateLocation(color);
        //    }

        //        if (carIsNear()) {
        //            emergencyBreak();
        //        } else {
        //Line correction
        lineCorrection();
        //        }

        //         Reset after turn
        if (turning) { //if we started turning left or right
            if (innerR < BLACK && innerL > BLACK) { //stage 1 of the turn has been made
                turned = 1;
                turning = 0;
            } else if (innerR > BLACK && innerL < BLACK) { //stage 1 of the turn has been made
                turned = 1;
                turning = 0;
            }
        }

        readLineSensors(&innerL, &innerR, &outerL, &outerR);
        //readColor();

        //Reset after turn has been taken
        if ((reservedR || reservedL) && turned && innerL > BLACK && innerR > BLACK && outerL < BLACK && outerR < BLACK) { //we're right on track again, we're finished turning
            if (reservedR) {
                reservedR = 0;
                updateLocation('R');
            } else if (reservedL) {
                reservedL = 0;
                updateLocation('L');
            }
            turned = 0;
            completed = 1;
        }

        readLineSensors(&innerL, &innerR, &outerL, &outerR);
        // Counting nodes/stations
        if (!onNode && reservedW && outerL > BLACK && outerR > BLACK) {
            reservedW--;
            onNode = 1;

            // Wait for the node to pass. Otherwise the count would go up enormous
        } else if (onNode && outerL < BLACK && outerR < BLACK) {
            onNode = 0;

            // Send the server we've passed a node
            updateLocation((char) (reservedW + '0'));

            if (!reservedW) { // We've had all the stations, now we are done with this command.
                completed = 1;
            }
        }

        readLineSensors(&innerL, &innerR, &outerL, &outerR);

        // We've lost the track some how. Now stop.
        if (innerL < BLACK && innerR < BLACK && outerL < BLACK && outerR < BLACK) {
            drive = 0;
        }
    } else {
        LeftPWM(STOP, 0);
        RightPWM(STOP, 0);

        if (reservedS && xCounter++ > 5000) {

            updateLocation('s');

            // We do a complete software reset now
            innerR = 0;
            innerL = 0;
            outerR = 0;
            outerL = 0;

            reservedS = 0;
            reservedW = 0;
            reservedL = 0;
            reservedR = 0;

            drive = 0;
            timer = 0;
            onNode = 0;
            turned = 0;
            turning = 0;
            xCounter = 0;
            completed = 1;
            queueLength = 0;
            nextCommand = '\0';
            commandCounter = 0;


            requireReboot = 1;
            // Let the while in the main handle it.
            return;
        }
        //        if (reservedS && xCounter++ > 10000) {
        //            xCounter = 0;
        //            completed = 1;
        //            reservedS = 0;
        //            printQueue(dataQueue, queueLength);
        //        }
    }

    if (!reservedS) {
        // Get commands over bluetooth when there are no commands
        if (!queueLength) {
            while (!queueLength) {// Urgent need of commands
                adjust();
                get_Commands();
                //                pulse();
            }
        } else { // just waiting for server reply of SN.. (Location update)
            adjust();
            get_Commands();
        }
    }
    pulse(4);

}

void updateLocation(char what) {
    //     Opening sequence
    putcUSART((char) 27);
    adjust();
    //     Message/Packet
    putcUSART(what);

    //     Closing sequence
    putcUSART((char) 13);
}

void lineCorrection(void) {
    if (drive) {
        if ((innerL < BLACK || innerR < BLACK) && !turning) {
            if (innerL > innerR) {
                LeftPWM(SLOW, 0);
                RightPWM(MED, 0);
            } else if (innerR > innerL) {
                LeftPWM(MED, 0);
                RightPWM(SLOW, 0);
            }
        }
        if (innerL > BLACK && innerR > BLACK && !turning) {
            if (reservedL || reservedR) {
                LeftPWM(MEDL, 0);
                RightPWM(MEDL, 0);
            } else {
                LeftPWM(MED, 0);
                RightPWM(MED, 0);
            }
        }
    }
}

void takeNextLeft(void) {
    if (outerL > BLACK && reservedL) {
        LeftPWM(SLOW, 0);
        RightPWM(MEDH, 0);
        turning = 1;
    }
}

void takeNextRight(void) {
    if (outerR > BLACK && reservedR) {
        LeftPWM(MEDH, 0);
        RightPWM(SLOW, 0);
        turning = 1;
    }
}

int carIsNear(void) {
    long long sensVal;
    int max = 799;

    sensVal = readPROXDown();

    if (sensVal < max) {
        return 1;
    }
    return 0;
}

void adjust(void) {
    if (drive) {
        if (turning) {
            if (reservedL) {
                LeftPWM(SLOW, 0);
                RightPWM(MEDH, 0);
            } else if (reservedR) {
                LeftPWM(MEDH, 0);
                RightPWM(SLOW, 0);
            }
        } else {
            readLineSensors(&innerL, &innerR, &outerL, &outerR);
            lineCorrection();
        }
    }
}

void displayOnStart(void) {
    rom char * line1 = "Project RAILCAB";
    rom char * line2 = "     Team 3";
    XLCDL1home();
    XLCDPutRomString(line1);
    XLCDL2home();
    XLCDPutRomString(line2);
    //     Delay 1000 ms
}




//******************************************************************************
//** Reset vector mapping **
//******************************************************************************
extern void _startup(void); // See c018i.c in your C18 compiler dir
// Vector moved hid bootloader in place
#pragma code _RESET_INTERRUPT_VECTOR = 0x001000

void _reset(void) {

    _asm goto _startup _endasm
}
// end reset vector code
#pragma code

//******************************************************************************
//  main
//******************************************************************************

void main(void) {
    //    conf_color();
    ADC_init(); // Should be done first
    PWM_init(); // Beware pwm ports go together with ANALOG in
    initUSART();
    XLCDInit(); // initialize the LCD module
    XLCDClear();
    //    displayOnStart();

    pulse_setup();
    while (1) {
        processData();
        if (requireReboot) {
            requireReboot = 0;
            continue;
        }
        get_Commands();
    }
}