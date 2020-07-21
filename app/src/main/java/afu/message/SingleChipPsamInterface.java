package afu.message;

import afu.util.DeviceInfo;

/**
 * Created by liu_rui on 2020/1/11.
 */
interface SingleChipPsamInterface {

	void setUartWriteMessage(byte[] bytes);

	void threadStartMessage();

	void threadStopMessage();

	void threadOffMessage();

	void uartStopMessage();

	void messageInspect(byte[] by);
}
