void ADC_init();
int getAD(char adcselector);

#define  AD0SELECTOR 0x03  // 0000 11 AN0
#define  AD1SELECTOR 0x07  // 0001 11 AN1
#define  AD2SELECTOR 0x0B  // 0010 11 AN2
#define  AD3SELECTOR 0x0F  // 0011 11 AN3
#define  AD8SELECTOR 0x23  // 1000 11 AN8
#define  AD9SELECTOR 0x27  // 1001 11 AN9

#define  getAD0() getAD(AD0SELECTOR)
#define  getAD1() getAD(AD1SELECTOR)
#define  getAD2() getAD(AD2SELECTOR)
#define  getAD3() getAD(AD3SELECTOR)
#define  getAD8() getAD(AD8SELECTOR);
#define  getAD9() getAD(AD9SELECTOR);

