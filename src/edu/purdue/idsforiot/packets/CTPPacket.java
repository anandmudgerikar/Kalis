package edu.purdue.idsforiot.packets;

public class CTPPacket extends ZigBeePacket {

	// link layer headers
	private String frameType;
	private int rssi; // check if modlink quality is same as RSSI value

	// packet structure for CPT
	private String thl;
	private String origin;
	private String seqno;
	private String collectid;

	public CTPPacket(String[] rawparts) {
		this(rawparts[2], rawparts[3], rawparts[6], Integer.parseInt(rawparts[13]), rawparts[12].split(" "));
		this.setTimestamp(Long.parseLong(rawparts[1]));
	}
	
	public CTPPacket(String src, String dest, String frameType, int rssi, String[] payload) {
		//we do not care about application data so we just send null string
		super(payload[8], dest, "");
		
		this.frameType = frameType;
		this.rssi = rssi;
		this.thl = payload[1];
		this.origin = payload[4] + payload[5];
		this.seqno = payload[6];
		this.collectid = payload[7];
	}

	
	
	public String getTHL() {
		return thl;
	}
	public void setTHL(String thl) {
		this.thl = thl;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getSeqNo() {
		return seqno;
	}
	public void setSeqNo(String seqno) {
		this.seqno = seqno;
	}
	public String getCollectID() {
		return collectid;
	}
	public void setCollectID(String collectid) {
		this.collectid = collectid;
	}
	public String getFrameType() {
		return frameType;
	}
	public void setFrameType(String frameType) {
		this.frameType = frameType;
	}
	public int getRSSI() {
		return rssi;
	}
	public void setRSSI(int rssi) {
		this.rssi = rssi;
	}


	@Override
	public String toString() {
		return "ZigBee CTP " + super.toString();
	}
	
	@Override
	public String toCSV() {
		return super.toCSV() + "," + this.getTHL() + "," + this.getOrigin() + "," + this.getSeqNo() + "," + this.getCollectID();
	}

}
