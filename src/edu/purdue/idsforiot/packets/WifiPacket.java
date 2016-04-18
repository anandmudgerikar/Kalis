package edu.purdue.idsforiot.packets;

public class WifiPacket {

	private String timestamp;
	private String source_ip;
	private String dest_ip;
	
	//we can add more fields if required later
	public void setTimeStamp(String timestamp ) {
		this.timestamp = timestamp;
	}
	
	public void setSourceIp(String source_ip ) {
		this.source_ip = source_ip;
	}
	
	public void setDestIP(String dest_ip ) {
		this.dest_ip = dest_ip;
	}
	
	public String getTimeStamp()
	{
		return this.timestamp;
	}
	
	public String getSourceIp()
	{
		return this.source_ip;
	}
	
	public String getDestIp()
	{
		return this.dest_ip;
	}
	
	public String toCSV() {
		return this.getTimeStamp() + "," + this.getSourceIp() + "," + this.getDestIp();
	}
	
	@Override
	public String toString() {
		return "Wifi Packet [TimeStamp=" + timestamp + ", source ip =" + source_ip + ", destination ip ="+ dest_ip +"]";
	}
}
