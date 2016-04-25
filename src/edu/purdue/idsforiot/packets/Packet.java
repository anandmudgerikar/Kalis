package edu.purdue.idsforiot.packets;

import java.sql.Timestamp;

public abstract class Packet {

	private PacketTypes type;
	private long timestamp;
	private String src;
	private String dst;
	private String data;

	public Packet(PacketTypes type, String src, String dst, String data) {
		this.timestamp = new Timestamp(new java.util.Date().getTime()).getTime();
		this.type = type;
		this.src = src;
		this.data = data;
		this.dst = dst;
	}
	
	public Packet()
	{
		System.out.println("Creating empty packet");
	}

	
	public PacketTypes getType() {
		return type;
	}
	public void setType(PacketTypes type) {
		this.type = type;
	}
	public String getData() {
		return this.data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getSrc() {
		return this.src;
	}
	public void setSrcAddr(String src) {
		this.src = src;
	}
	public String getDst() {
		return this.dst;
	}
	public void setDstAddr(String dst) {
		this.dst = dst;
	}
	public long getTimestamp() {
		return this.timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	

	
	// FORMAT: type, timestamp, src, dst, data
	public String toCSV() {
		return this.getType() + "," + this.getTimestamp() + "," + this.getSrc() + "," + this.getDst() + "," + this.getData();
	}
	
	
	@Override
	public String toString() {
		return "Packet [src=" + this.getSrc() + ", dst=" + this.getDst() + ", data=" + this.getData() + ", TimeStamp=" + this.getTimestamp() + "]";
	}

}
