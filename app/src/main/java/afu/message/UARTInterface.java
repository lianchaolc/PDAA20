package afu.message;

/**
 * Created by liu_rui on 2019/11/27.
 */

public interface UARTInterface {

	void uartStartMessage(String uartFileName, int uartBaud);

	void uartWriteMessage(byte[] bytes);

	void threadStartMessage();

	void threadStopMessage();

	void threadOffMessage();

	void uartStopMessage();
}
