package edu.purdue.idsforiot.packets;

public class Packet {

	// packet structure for BlinkToRadio
	private int nodeid;
	private String data;
	private long timestamp;

	public Packet(String raw) {
		this(Integer.parseInt(raw.substring(30, 32)), raw.substring(33, 35),0); //timestamp is set to appropriate value during logging of data
	}
	public Packet(int nodeid, String data, long timestamp) {
		this.nodeid = nodeid;
		this.data = data;
		this.timestamp = timestamp;
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
	
	public long getTimeStamp() {
		return timestamp;
	}

	public void setTimeStamp(long timestamp ) {
		this.timestamp = timestamp;
	}

	
	public String toCSV() {
		return this.getNodeID() + "," + this.getData() + "," + this.getTimeStamp();
	}
	
	
	
	@Override
	public String toString() {
		return "Packet [nodeid=" + nodeid + ", data=" + data + "]";
	}

}
