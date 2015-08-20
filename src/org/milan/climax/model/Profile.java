package org.milan.climax.model;

public class Profile {

	private long id;
	private String room_name;
	private String profile_name;
	private String media_ip;
	private String media_gateway;
	private String username;
	private String password;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getProfile_name() {
		return profile_name;
	}
	public void setProfile_name(String profile_name) {
		this.profile_name = profile_name;
	}
	public String getRoom_name() {
		return room_name;
	}
	public void setRoom_name(String room_name) {
		this.room_name = room_name;
	}
	public String getMedia_ip() {
		return media_ip;
	}
	public void setMedia_ip(String media_ip) {
		this.media_ip = media_ip;
	}
	public String getMedia_gateway() {
		return media_gateway;
	}
	public void setMedia_gateway(String media_gateway) {
		this.media_gateway = media_gateway;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
