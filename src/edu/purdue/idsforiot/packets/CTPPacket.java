package edu.purdue.idsforiot.packets;

import java.sql.Timestamp;

public class CTPPacket extends Packet {

	// packet structure for CPT
	private String thl;
	private String origin;
	private String seqno;
	private String collectid;

	public CTPPacket(String raw) {
		this(Integer.parseInt(raw.substring(30, 32)), raw.substring(33, 35), new Timestamp(new java.util.Date().getTime()).getTime(),
			 raw.substring(8, 16), raw.substring(32, 48), raw.substring(48, 56), raw.substring(56, 64));
	}
	public CTPPacket(int nodeid, String data, long timestamp, String thl, String origin, String seqno, String collectid) {
		super(nodeid, data, timestamp);
		this.thl = thl;
		this.origin = origin;
		this.seqno = seqno;
		this.collectid = collectid;
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
