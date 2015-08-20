package org.milan.climax.model;

public class Audio {

   
	private String artistName;
	private int album_id;
	private int song_id;
	private int artist_id;
	
	public String getLabel() {
		return Label;
	}
	public void setLabel(String label) {
		Label = label;
	}
	private String thumbnail;
	private String Label;
	
	
	public void setArtistName(String string){
		this.artistName = string;
	}
	public String getArtistName(){
		return artistName;
	}
	public void setThumbnail(String string){
		this.thumbnail = string;
	}
	public String getThumbnail(){
		return thumbnail;
	}
	public int getAlbum_id() {
		return album_id;
	}
	public void setAlbum_id(int album_id) {
		this.album_id = album_id;
	}
	public int getSong_id() {
		return song_id;
	}
	public void setSong_id(int song_id) {
		this.song_id = song_id;
	}
	public int getArtist_id() {
		return artist_id;
	}
	public void setArtist_id(int artist_id) {
		this.artist_id = artist_id;
	}
	
	
	
}
