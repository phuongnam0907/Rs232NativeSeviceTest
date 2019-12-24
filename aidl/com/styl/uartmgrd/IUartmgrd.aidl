package com.styl.uartmgrd;

interface IUartmgrd {
    int setSerialPortParams(int baudrate,int dataBits,int stopBits,char parity);
    int open(String portName);
    void close(int fd);
    byte[] read_data(int bufsize, int timeout);
    int read(out byte[] buf, int bufsize);
    int select(int timeOut);
    int setBlock(int blockmode);
    int write_data(in byte[] buf, int writesize);
}