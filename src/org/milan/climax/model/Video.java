package org.milan.climax.model;

public class Video {

   
	private String genreName;
	private int album_id;
	private int movie_id;
	private String director_name;
	private String thumbnail;
	private String Label;
	private String rating;
	private String tagline;
	private String description;
	private String fanArt;
	private String year;
	private int duration;
	
	
	public String getLabel() {
		return Label;
	}
	public void setLabel(String label) {
		this.Label = label;
	}
	public void setGenreName(String string){
		this.genreName = string;
	}
	public String getGenreName(){
		return genreName;
	}
	public void setThumbnail(String string){
		this.thumbnail = string;
	}
	public String getThumbnail(){
		return thumbnail;
	}
	
	public int getMovie_id() {
		return movie_id;
	}
	public void setMovie_id(int movie_id) {
		this.movie_id = movie_id;
	}
	public int getAlbum_id() {
		return album_id;
	}
	public void setAlbum_id(int album_id) {
		this.album_id = album_id;
	}
	public String getDirector_name() {
		return director_name;
	}
	public void setDirector_name(String director_name) {
		this.director_name = director_name;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getTagline() {
		return tagline;
	}
	public void setTagline(String tagline) {
		this.tagline = tagline;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFanArt() {
		return fanArt;
	}
	public void setFanArt(String fanArt) {
		this.fanArt = fanArt;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	
	
}
