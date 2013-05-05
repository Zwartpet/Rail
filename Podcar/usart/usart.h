#ifndef __USART_H
#define __USART_H

extern void initUSART(void) ;
extern unsigned char getcUSART(void) ;

void putcUSART(char c); 
void putStrUSART(char *str) ;
void get_Commands(void);
void putRomStrUSART(rom char *str);
#endif