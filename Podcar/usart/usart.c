// Init usart at 112500 baud for connecting to Wt12

#include "usart.h"

#include <p18f4550.h>
#include "xlcd/xlcd.h"
#include "command.h"


#define mDataRdyUSART() PIR1bits.RCIF
#define mTxRdyUSART()   TXSTAbits.TRMT

void initUSART(void) {
    char c;
    // Transmit and recv mode
    TXSTA = 0b00100100; // TX enable BRGH=1
    //        76543210
    RCSTA = 0b10010000; // Single Character RX
    //         76543210
    // Baudrate config
    BAUDCON = 0b00001000; // BRG16 = 1
    //          76543210
    //   SPBRGH:SPBRG: =((FOSC/Desired Baud Rate)/4) – 1
    SPBRGH = 0x00;
    SPBRG = 0x67;
    c = RCREG; // read clear data
}//end InitializeUSART

void putcUSART(char c) {
    while (!mTxRdyUSART());
    TXREG = c;
}

void putStrUSART(char *str) {
    int i = 0;
    while (str[i]) putcUSART(str[i++]);
}

void putRomStrUSART(rom char *str) {
    // Opening sequence
    putcUSART((char) 27);
    while (*str) {
        putcUSART(*str);
        str++;
    }
    // Closing sequence
    putcUSART((char) 13);
}

//  getcUSART() wait until a valid char arrived in usart;
//

unsigned char getcUSART(void) {
    char c;

    if (RCSTAbits.OERR) { // in case of overrun error we should never see an overrun error, but if we do,
        RCSTAbits.CREN = 0; // reset the port
        c = RCREG;
        RCSTAbits.CREN = 1; // and keep going.
    } else {
        while (!mDataRdyUSART());

        c = RCREG;
    }
    //XLCDPut(c);
    // not necessary.  EUSART auto clears the flag when RCREG is cleared
    PIR1bits.RCIF = 0; // clear Flag

    return c;
}
//------------------------------------------------------------------------------

extern void pulse(int);
extern void lineCorrection(void);
extern void readLineSensors(int*, int*, int*, int*);
extern int innerL;
extern int innerR;
extern int outerL;
extern int outerR;

unsigned char dataQueue[10];
int queueLength = 0;
int timer = 0;

void get_Commands(void) {
    char c;
    int protocolRunning = 0;

    pulse(6);
    // Wait for incomming data and process when done handling data.
    while (mDataRdyUSART() || protocolRunning) {
        // Prevent an unusual uncessary long search for Bluetooth packets
        //        if (queueLength >= 3) {
        //            break;
        //        }

        c = getcUSART();

        //            readLineSensors(&innerL, &innerR, &outerL, &outerR);
        //            lineCorrection();

        if (c == 27) { // Received ascii ESC reset input buffer and start over
            continue;
        }
        if (c == 10 || c == 13) { // ascii NL or CR
            continue;
        }

        // The end of the procol has occured, now we can process the data.
        if (c == 'Z' && protocolRunning) {
            // Reset
            protocolRunning = 0;
            
            // Break out of the loop to process the data
            break;
        }
        if (protocolRunning) {
            dataQueue[queueLength++] = c;
            timer = 0;
        }
        if (c == '*') {
            protocolRunning = 1;
            timer = 0;
        }

        if (timer++ > 5000) {
            timer = 0;
            break;
        }
    }
    pulse(6);
}
