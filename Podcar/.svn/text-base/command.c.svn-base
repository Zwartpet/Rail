#include "command.h"

#include "xlcd/xlcd.h"

void shiftCommand(unsigned char *commands, int nCommands) {
    int i = 0;
    for (i = 0; i < nCommands; i++) {
        commands[i] = commands[i + 1];
    }
}

extern void printQueue(unsigned char *commands, int nCommands){
    int i = 0;
    XLCDClear();
    XLCDPut('Q');
    for(i = 0; i < nCommands; i++){
        XLCDPut(commands[i]);
    }
}