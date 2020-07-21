package com.ljsw.tjbankpad.baggingin.activity.entity;

import java.io.Serializable;
import java.util.List;

public class JsonRootBean implements Serializable {

	private int undoSum;
	private List<String> undoPacketNumList;
	private List<UndoPacketList> undoPacketList;
	private int sum;

	public int getUndoSum() {
		return undoSum;
	}

	public void setUndoSum(int undoSum) {
		this.undoSum = undoSum;
	}

	public List<String> getUndoPacketNumList() {
		return undoPacketNumList;
	}

	public void setUndoPacketNumList(List<String> undoPacketNumList) {
		this.undoPacketNumList = undoPacketNumList;
	}

	public List<UndoPacketList> getUndoPacketList() {
		return undoPacketList;
	}

	public void setUndoPacketList(List<UndoPacketList> undoPacketList) {
		this.undoPacketList = undoPacketList;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

}
