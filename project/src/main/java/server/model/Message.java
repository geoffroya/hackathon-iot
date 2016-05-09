package server.model;

import org.rapidoid.http.Req;

public class Message {

	public String id;
	public long timestamp;
	public int sensorType;
	public long value;
	public Req req;

	public Message(String id, long timestamp, int sensorType, long value, Req req) {
		this.id = id;
		this.timestamp = timestamp;
		this.sensorType = sensorType;
		this.value = value;
		this.req = req;
	}

	public Message(long timestamp, int sensorType, long value, Req req) {
		this.timestamp = timestamp;
		this.sensorType = sensorType;
		this.value = value;
		this.req = req;
	}
	
	

}
