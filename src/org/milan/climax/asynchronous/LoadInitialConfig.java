package org.milan.climax.asynchronous;

import java.util.ArrayList;

import org.milan.climax.db.MilanClimaxDataSource;
import org.milan.climax.model.Model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.JsonReader;
import android.util.Log;
import android.widget.EditText;

@SuppressLint("NewApi")
public class LoadInitialConfig extends AsyncTask<String, Void, Void> {

	private Long profileID;
	private static final String LOGCAT = null;
	private Model model;
	private MilanClimaxDataSource milanClimaxDataSource;
	public EditText profSettRoomName, profSettGatewayIp, profSettMediaIp,
			profSettUsername, profSettPwd;
	private String mediaIp;
	private JsonReader reader;

	private ArrayList<Integer> videoId = new ArrayList<Integer>(),
			songId = new ArrayList<Integer>(),
			Duration = new ArrayList<Integer>(),
			PlayCount = new ArrayList<Integer>();
	private ArrayList<String> Label = new ArrayList<String>(),
			Thumbnail = new ArrayList<String>(),
			Title = new ArrayList<String>(),
			artistId = new ArrayList<String>();
	private ArrayList<String> videoDirector, videoFanArt, videoGenre,
			videoLabel, videoDescription, videoPlot, videoRunTime,
			videoTagLine, videoThumbnail, videoTitle, videoTrailer, videoYear,
			videoRating, videoStudio;
	private String director, fanart, genre, label, runtime, title, trailer,
			year, tagline, plot, description, thumbnail, rating, studio;
	private String whichConfig;
private String RequestStr;
	public LoadInitialConfig(android.app.Activity activity, Long profileID,
			MilanClimaxDataSource milanClimaxDataSource, Model model,
			String MediaIp, String config) {
		this.profileID = profileID;
		this.milanClimaxDataSource = milanClimaxDataSource;
		this.model = model;
		this.mediaIp = MediaIp;
		this.whichConfig = config;
	}

	public LoadInitialConfig(android.app.Activity activity, Long profileID,
			MilanClimaxDataSource milanClimaxDataSource, Model model,
			String MediaIp, String config, String config2) {
		this.milanClimaxDataSource = milanClimaxDataSource;
		this.model = model;
		this.mediaIp = MediaIp;
		this.profileID = profileID;
		String audioRequestStr = "{\"id\":1,\"jsonrpc\":\"2.0\",\"method\":\"AudioLibrary.GetRecentlyAddedAlbums\",\"params\":{\"properties\":[\"albumlabel\",\"artistid\",\"type\",\"description\",\"thumbnail\",\"genre\",\"title\",\"artist\",\"rating\",\"year\"]}}";
		String videoRequestStr = "{\"id\":1,\"jsonrpc\":\"2.0\",\"method\":\"VideoLibrary.GetRecentlyAddedMovies\",\"params\":{\"properties\":[\"title\",\"director\",\"tagline\",\"fanart\",\"thumbnail\",\"genre\",\"setid\",\"rating\",\"year\",\"studio\",\"resume\",\"runtime\",\"plot\",\"trailer\"]}}";
		getAudioUpdateValues(mediaIp, audioRequestStr);
		getVideoMobileUpdateValues(mediaIp, videoRequestStr);
	}
	
	@Override
	protected Void doInBackground(String... Params) {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		/*
		 * dialog = ProgressDialog.show(context, "Loading",
		 * "Updating Library. Please wait...");
		 * dialog.setProgressStyle(R.style.ProgressBar);
		 */
	}

	public void getAudioUpdateValues(String profile_ip_value,
			String audioRequestArr) {
		try {
			// avoids a ConcurrentModificationException

			reader = model.getJsonValuesFromServer("http://" + profile_ip_value
					+ "/jsonrpc", audioRequestArr);
			reader.beginObject();
			ArrayList<Integer> albumId = new ArrayList<Integer>();
			ArrayList<String> artistName = new ArrayList<String>();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (name.equalsIgnoreCase("result")) {
					reader.beginObject();
					while (reader.hasNext()) {
						String check = reader.nextName();

						if (check.equalsIgnoreCase("albums")) {
							reader.beginArray();
							int i = 0;
							while (reader.hasNext()) {
								i++;

								reader.beginObject();
								int albumid;
								String artist_name = "";
								String artist_id = "";
								thumbnail = "";
								String title = "";
								String label = "";

								while (reader.hasNext()) {

									name = reader.nextName();
									if (name.equalsIgnoreCase("albumid")) {
										albumid = reader.nextInt();
										albumId.add(albumid);
									} else if (name.equalsIgnoreCase("artist")) {
										reader.beginArray();

										while (reader.hasNext()) {
											if (artist_name
													.equalsIgnoreCase("")) {
												artist_name = reader
														.nextString();
											} else {
												artist_name = artist_name + ","
														+ reader.nextString();
											}
										}
										artistName.add(artist_name);
										if (artist_name.equalsIgnoreCase("")) {
											Log.i(LOGCAT, "empty");
											artistId.add("");
										}
										reader.endArray();
									} else if (name
											.equalsIgnoreCase("artistid")) {
										reader.beginArray();
										if (!artist_name.equalsIgnoreCase("")) {
											while (reader.hasNext()) {
												if (artist_id
														.equalsIgnoreCase("")) {
													artist_id = reader
															.nextString();
												} else {
													artist_id = artist_id
															+ ","
															+ reader.nextString();
												}
											}
											artistId.add(artist_id);
										}

										reader.endArray();
									} else if (name
											.equalsIgnoreCase("thumbnail")) {
										thumbnail = reader.nextString();
										Thumbnail.add(thumbnail);
									} else if (name.equalsIgnoreCase("title")) {
										title = reader.nextString();
										Title.add(title);
									} else if (name.equalsIgnoreCase("label")) {
										label = reader.nextString();
										Label.add(label);
									} else {
										reader.skipValue();
									}
								}

								reader.endObject();
								// }
							}
							Log.i(LOGCAT, Integer.toString(artistName.size()));
							Log.i(LOGCAT, Integer.toString(artistId.size()));
							Log.i(LOGCAT, Integer.toString(i));
							reader.endArray();
						} else {
							reader.skipValue();
						}
					}

					reader.endObject();
					
					milanClimaxDataSource.bulkInsertRecentlyAddedAudioData(profileID,
							albumId, artistId, artistName, mediaIp, Label,
							Thumbnail, Title);
				} else {
					reader.skipValue();
				}
			}
			reader.endObject();
			
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						throw new Exception();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}, 1000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getAudioSongUpdateValues(String profile_ip_value,
			String audioRequestArr) {
		try {
			// avoids a ConcurrentModificationException

			reader = model.getJsonValuesFromServer("http://" + profile_ip_value
					+ "/jsonrpc", audioRequestArr);
			reader.beginObject();
			ArrayList<Integer> albumId = new ArrayList<Integer>();
			ArrayList<String> artistId = new ArrayList<String>();
			ArrayList<String> artistName = new ArrayList<String>();
			ArrayList<String> Label = new ArrayList<String>();
			ArrayList<Integer> songId = new ArrayList<Integer>();
			ArrayList<String> Thumbnail = new ArrayList<String>();
			ArrayList<Integer> PlayCount = new ArrayList<Integer>();
			ArrayList<Integer> Duration = new ArrayList<Integer>();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (name.equalsIgnoreCase("result")) {
					reader.beginObject();
					while (reader.hasNext()) {
						String check = reader.nextName();

						if (check.equalsIgnoreCase("songs")) {
							Log.i("Songs", "Value-->songs");
							reader.beginArray();
							int i = 0;
							while (reader.hasNext()) {
								i++;

								reader.beginObject();
								int albumid;
								int songid;
								String artist_name = "";
								String artist_id = "";
								int play_count;
								int duration;
								thumbnail = "";
								String label = "";

								while (reader.hasNext()) {

									name = reader.nextName();
									if (name.equalsIgnoreCase("albumid")) {
										albumid = reader.nextInt();
										albumId.add(albumid);
									} else if (name.equalsIgnoreCase("artist")) {
										reader.beginArray();

										while (reader.hasNext()) {
											if (artist_name
													.equalsIgnoreCase("")) {
												artist_name = reader
														.nextString();
											} else {
												artist_name = artist_name + ","
														+ reader.nextString();
											}
										}
										artistName.add(artist_name);
										if (artist_name.equalsIgnoreCase("")) {
											Log.i(LOGCAT, "empty");
											artistId.add("");
										}
										reader.endArray();
									} else if (name
											.equalsIgnoreCase("artistid")) {
										reader.beginArray();
										if (!artist_name.equalsIgnoreCase("")) {
											while (reader.hasNext()) {
												if (artist_id
														.equalsIgnoreCase("")) {
													artist_id = reader
															.nextString();
												} else {
													artist_id = artist_id
															+ ","
															+ reader.nextString();
												}
											}
											artistId.add(artist_id);
										}

										reader.endArray();
									} else if (name
											.equalsIgnoreCase("thumbnail")) {
										thumbnail = reader.nextString();
										Thumbnail.add(thumbnail);
									} else if (name.equalsIgnoreCase("songid")) {
										songid = reader.nextInt();
										songId.add(songid);
									} else if (name
											.equalsIgnoreCase("duration")) {
										duration = reader.nextInt();
										Duration.add(duration);
									} else if (name
											.equalsIgnoreCase("playcount")) {
										play_count = reader.nextInt();
										PlayCount.add(play_count);
									} else if (name.equalsIgnoreCase("label")) {
										label = reader.nextString();
										Label.add(label);
									} else {
										reader.skipValue();
									}
								}

								reader.endObject();
								// }
							}
							Log.i(LOGCAT, Integer.toString(artistName.size()));
							Log.i(LOGCAT, Integer.toString(artistId.size()));
							Log.i(LOGCAT, Integer.toString(i));
							reader.endArray();
						} else {
							reader.skipValue();
						}
					}

					reader.endObject();
					milanClimaxDataSource.bulkInsertAudioSongsData(profileID,
							albumId, artistId, artistName, mediaIp, Label,
							Thumbnail, Duration, PlayCount, songId);
				} else {
					reader.skipValue();
				}
			}
			reader.endObject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getAudioArtistUpdateValues(String profile_ip_value,
			String audioRequestArr) {
		try {
			// avoids a ConcurrentModificationException

			reader = model.getJsonValuesFromServer("http://" + profile_ip_value
					+ "/jsonrpc", audioRequestArr);
			reader.beginObject();
			String type = "artists";
			new ArrayList<Integer>();
			ArrayList<Integer> artistId = new ArrayList<Integer>();
			ArrayList<String> artistName = new ArrayList<String>();
			ArrayList<String> LabelArtist = new ArrayList<String>();
			ArrayList<String> ThumbnailArtist = new ArrayList<String>();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (name.equalsIgnoreCase("result")) {
					reader.beginObject();
					while (reader.hasNext()) {
						String check = reader.nextName();

						if (check.equalsIgnoreCase(type)) {
							reader.beginArray();
							int i = 0;
							while (reader.hasNext()) {
								i++;

								reader.beginObject();
								int artistid;
								String artist_name = "";
								thumbnail = "";
								String label = "";

								while (reader.hasNext()) {

									name = reader.nextName();
									if (name.equalsIgnoreCase("artistid")) {
										artistid = reader.nextInt();
										artistId.add(artistid);
									} else if (name.equalsIgnoreCase("artist")) {
										artist_name = reader.nextString();
										artistName.add(artist_name);
									} else if (name
											.equalsIgnoreCase("thumbnail")) {
										thumbnail = reader.nextString();
										ThumbnailArtist.add(thumbnail);
									} else if (name.equalsIgnoreCase("label")) {
										label = reader.nextString();
										LabelArtist.add(label);
									} else {
										reader.skipValue();
									}
								}

								reader.endObject();
								// }
							}
							Log.i(LOGCAT, Integer.toString(artistName.size()));
							Log.i(LOGCAT, Integer.toString(artistId.size()));
							Log.i(LOGCAT, Integer.toString(i));
							reader.endArray();
						} else {
							reader.skipValue();
						}
					}

					reader.endObject();
					milanClimaxDataSource.bulkInsertAudioArtistsData(profileID,
							artistId, artistName, mediaIp, LabelArtist,
							ThumbnailArtist);
				} else {
					reader.skipValue();
				}
			}
			reader.endObject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getVideoMobileUpdateValues(String profile_ip_value,
			String RequestStr) {

		Log.i("Start", "----->started");

		try {

			reader = model.getJsonValuesFromServer("http://" + profile_ip_value
					+ "/jsonrpc", RequestStr);
			Log.i(LOGCAT, "Video_Mobile");
			reader.beginObject();
			videoDirector = new ArrayList<String>();
			videoDescription = new ArrayList<String>();
			videoRating = new ArrayList<String>();
			videoThumbnail = new ArrayList<String>();
			videoFanArt = new ArrayList<String>();
			videoGenre = new ArrayList<String>();
			videoLabel = new ArrayList<String>();
			videoPlot = new ArrayList<String>();
			videoRunTime = new ArrayList<String>();
			videoTagLine = new ArrayList<String>();
			videoTitle = new ArrayList<String>();
			videoTrailer = new ArrayList<String>();
			videoYear = new ArrayList<String>();
			videoStudio = new ArrayList<String>();
			videoId = new ArrayList<Integer>();
			director = fanart = studio = rating = genre = label = plot = runtime = title = trailer = year = tagline = description = "";
			int movie_id;
			while (reader.hasNext()) {
				String name = reader.nextName();

				Log.i(LOGCAT, name);
				if (name.equalsIgnoreCase("result")) {
					reader.beginObject();
					while (reader.hasNext()) {

						String name1 = reader.nextName();
						String type = "movies";
						if (name1.equalsIgnoreCase(type)) {
							reader.beginArray();
							while (reader.hasNext()) {
								reader.beginObject();
								while (reader.hasNext()) {
									genre = "";
									director = "";
									studio = "";
									String check = reader.nextName();
									if (check.equalsIgnoreCase("cast")) {
										reader.beginArray();
										while (reader.hasNext()) {
											reader.beginObject();
											while (reader.hasNext()) {
												String name2 = reader
														.nextName();
												if (name2
														.equalsIgnoreCase("name")) {
													Log.i(LOGCAT,
															reader.nextString());
												} else {
													reader.skipValue();
												}
											}
											reader.endObject();
										}
										reader.endArray();
									} else if (check
											.equalsIgnoreCase("director")) {
										reader.beginArray();

										while (reader.hasNext()) {
											if (director.equalsIgnoreCase("")) {
												director = reader.nextString();
											} else {
												director = director + ","
														+ reader.nextString();
											}
										}
										videoDirector.add(director);
										if (director.equalsIgnoreCase("")) {
											Log.i(LOGCAT, "empty");
											videoDirector.add("");
										}
										reader.endArray();
									} else if (check.equalsIgnoreCase("studio")) {
										reader.beginArray();

										while (reader.hasNext()) {
											if (studio.equalsIgnoreCase("")) {
												studio = reader.nextString();
											} else {
												studio = studio + ","
														+ reader.nextString();
											}
										}
										videoStudio.add(studio);
										if (studio.equalsIgnoreCase("")) {
											Log.i(LOGCAT, "empty");
											videoStudio.add("");
										}
										reader.endArray();
									} else if (check
											.equalsIgnoreCase("movieid")) {
										movie_id = reader.nextInt();
										videoId.add(movie_id);
									} else if (check.equalsIgnoreCase("rating")) {
										rating = reader.nextString();
										videoRating.add(rating);
									} else if (check.equalsIgnoreCase("fanart")) {
										fanart = reader.nextString();
										videoFanArt.add(fanart);
									} else if (check.equalsIgnoreCase("genre")) {

										reader.beginArray();

										while (reader.hasNext()) {
											if (genre.equalsIgnoreCase("")) {
												genre = reader.nextString();
											} else {
												genre = genre + ","
														+ reader.nextString();
											}
										}
										videoGenre.add(genre);
										if (genre.equalsIgnoreCase("")) {
											Log.i(LOGCAT, "empty");
											videoGenre.add("");
										}
										reader.endArray();

									} else if (check.equalsIgnoreCase("label")) {
										label = reader.nextString();
										videoLabel.add(label);
									} else if (check
											.equalsIgnoreCase("description")) {
										description = reader.nextString();
										videoDescription.add(description);
									} else if (check.equalsIgnoreCase("plot")) {
										plot = reader.nextString();
										videoPlot.add(plot);
									} else if (check
											.equalsIgnoreCase("runtime")) {
										runtime = reader.nextString();
										videoRunTime.add(runtime);
									} else if (check
											.equalsIgnoreCase("tagline")) {
										tagline = reader.nextString();
										videoTagLine.add(tagline);
									} else if (check
											.equalsIgnoreCase("thumbnail")) {
										thumbnail = reader.nextString();
										videoThumbnail.add(thumbnail);
									} else if (check.equalsIgnoreCase("title")) {

										title = reader.nextString();
										Log.i("Title-Name", "---->" + title);
										videoTitle.add(title);
									} else if (check
											.equalsIgnoreCase("trailer")) {
										trailer = reader.nextString();
										videoTrailer.add(trailer);
									} else if (check.equalsIgnoreCase("year")) {
										year = reader.nextString();
										videoYear.add(year);
									} else {
										reader.skipValue();
									}

								}
								reader.endObject();
							}
							reader.endArray();
						} else {
							reader.skipValue();
						}
					}
					reader.endObject();
					milanClimaxDataSource.bulkInsertRecentlyAddedMovieData(profileID,
							videoId, videoDirector, videoFanArt, videoGenre,
							videoLabel, videoDescription, videoPlot, mediaIp,
							videoRunTime, videoRating, videoTagLine,
							videoThumbnail, videoTitle, videoTrailer,
							videoYear, videoStudio);

				} else {
					reader.skipValue();
				}

			}
			reader.endObject();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

}
