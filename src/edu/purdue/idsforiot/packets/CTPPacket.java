package edu.purdue.idsforiot.packets;

import java.sql.Timestamp;

public class CTPPacket extends Packet {

	//link layer headers
	private String FrameType;
	private String SrcAddr;
	private String DestAddr;
	private String RSSIValue; //check if modlink quality is same as RSSI value
		
	// packet structure for CPT
	private String thl;
	private String origin;
	private String seqno;
	private String collectid;

	public CTPPacket(String FrameType, String SrcAddr, String DestAddr, String RSSIValue, String[] payload) 
	{
		super(Integer.parseInt(payload[8]),"",new Timestamp(new java.util.Date().getTime()).getTime()); //we do not care about application data so we just send null string
		this.FrameType = FrameType;
		this.SrcAddr = SrcAddr;
		this.DestAddr = DestAddr;
		this.RSSIValue = RSSIValue;
		
		this.thl = payload[1];
		this.origin = payload[4]+payload[5];
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
	
	
	

	@Override
	public String toCSV() {
		return super.toCSV() + "," + this.getTHL() + "," + this.getOrigin() + "," + this.getSeqNo() + "," + this.getCollectID();
	}
	

}
