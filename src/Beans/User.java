package Beans;

public class User {
	private String name;
	private String ip;
	private int port;
	private long heartbeatTime;
	
	public User(String name, String ip, int port, long heartbeatTime) {
		super();
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.heartbeatTime = heartbeatTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public long getHeartbeatTime() {
		return heartbeatTime;
	}

	public void setHeartbeatTime(long heartbeatTime) {
		this.heartbeatTime = heartbeatTime;
	}
}
