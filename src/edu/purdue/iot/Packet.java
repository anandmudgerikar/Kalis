package edu.purdue.iot;

public class Packet {

	// packet structure for BlinkToRadio
	private int nodeid;
	private String data;

	public Packet(int nodeid, String data) {
		this.nodeid = nodeid;
		this.data = data;
	}

	public int getNodeID() {
		return nodeid;
	}

	public void setNodeID(int nodeid) {
		this.nodeid = nodeid;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	
	@Override
	public String toString() {
		return "Packet [nodeid=" + nodeid + ", data=" + data + "]";
	}

}
