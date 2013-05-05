#include "sensor.h"
#include "myadc.h"
#include "p18f4450.h"

void readLineSensors(int* iL, int* iR, int* oL, int* oR) {
    *iR = getAD0();
    *iL = getAD1();
    *oR = getAD2();
    *oL = getAD3();
}

int readPROXDown() {
    return getAD8();
}

int readPROXUp() {
    return getAD9();
}

/*** COLOR SENSOR ***/
void conf_color(void) {
    TRISBbits.TRISB4 = 1;
    TRISBbits.TRISB5 = 1;
    TRISBbits.TRISB6 = 1;
    TRISBbits.TRISB7 = 1;
}

char readColorSensor(void) {

    if (PORTBbits.RB4 == 0 && PORTBbits.RB5 == 0 && PORTBbits.RB6 == 0 && PORTBbits.RB7 == 0) {//no color
        return 'K';
    } else if (PORTBbits.RB4 == 0 && PORTBbits.RB5 == 0 && PORTBbits.RB6 == 0 && PORTBbits.RB7 == 1) {//red
        return 'A';
    } else if (PORTBbits.RB4 == 0 && PORTBbits.RB5 == 0 && PORTBbits.RB6 == 1 && PORTBbits.RB7 == 0) {//orange
        return 'B';
    } else if (PORTBbits.RB4 == 0 && PORTBbits.RB5 == 0 && PORTBbits.RB6 == 1 && PORTBbits.RB7 == 1) {//yellow
        return 'C';
    } else if (PORTBbits.RB4 == 0 && PORTBbits.RB5 == 1 && PORTBbits.RB6 == 0 && PORTBbits.RB7 == 0) {//dark green
        return 'D';
    } else if (PORTBbits.RB4 == 0 && PORTBbits.RB5 == 1 && PORTBbits.RB6 == 0 && PORTBbits.RB7 == 1) {//light blue
        return 'E';
    } else if (PORTBbits.RB4 == 0 && PORTBbits.RB5 == 1 && PORTBbits.RB6 == 1 && PORTBbits.RB7 == 0) {//dark blue
        return 'F';
    } else if (PORTBbits.RB4 == 0 && PORTBbits.RB5 == 1 && PORTBbits.RB6 == 1 && PORTBbits.RB7 == 1) {//purple
        return 'G';
    } else if (PORTBbits.RB4 == 1 && PORTBbits.RB5 == 0 && PORTBbits.RB6 == 0 && PORTBbits.RB7 == 0) {//light green
        return 'H';
    } else if (PORTBbits.RB4 == 1 && PORTBbits.RB5 == 0 && PORTBbits.RB6 == 0 && PORTBbits.RB7 == 1) {//pink
        return 'I';
    } else if (PORTBbits.RB4 == 1 && PORTBbits.RB5 == 0 && PORTBbits.RB6 == 1 && PORTBbits.RB7 == 0) {// brown
        return 'J';
    }
    return -1;
}
/*** END: COLOR SENSOR ***/