package org.milan.climax;

import java.util.ArrayList;
import java.util.List;

import org.milan.climax.R;
import org.milan.climax.UIhelper.UIHelper;
import org.milan.climax.asynchronous.LoadRecentlyAddedView;
import org.milan.climax.db.MilanClimaxDataSource;
import org.milan.climax.model.Audio;
import org.milan.climax.model.Model;
import org.milan.climax.model.Video;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.JsonReader;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

@SuppressLint({ "NewApi", "ValidFragment" })
public class RecentlyAddedFragment extends Fragment {

	private Model model;
	private android.app.Activity context;
	private MilanClimaxDataSource milanClimaxDataSource;
	public View rootView;
	private int w;
	private SharedPreferences settings;
	private int current_profile_id;
	private TextView ArtistsClick, AlbumsClick, SongsClick;
	private String profile_ip_value;
	public View popUpView;
	private List<Audio> albumsArr, artistArr, songArr;
	private LinearLayout main_linear_layout;
	private ArrayList<Integer> intArr;
	private ArrayList<Integer> videoId = new ArrayList<Integer>();
	private ArrayList<String> videoDirector, videoFanArt, videoGenre,
			videoLabel, videoDescription, videoPlot, videoRunTime,
			videoTagLine, videoThumbnail, videoTitle, videoTrailer, videoYear,
			videoRating, videoStudio;
	private String director, fanart, genre, label, runtime, title, trailer,
			year, tagline, plot, description, thumbnail, rating, studio;
	private LinearLayout linearLayout, albumLinearLayout;
	private List<Video> movieArr;
	private DrawerLayout mDrawerLayout;
	private RelativeLayout relativeLayout, headerRelative;

	public RecentlyAddedFragment(DrawerLayout mDrawerLayout) {
		model = Model.getModelObj();
		this.context = model.getContext();
		this.mDrawerLayout = mDrawerLayout;
		this.milanClimaxDataSource = model.getMilanClimaxDataSource();
		settings = model.getContext().getSharedPreferences("PREF_NAME",
				Context.MODE_PRIVATE);
		current_profile_id = settings.getInt("Profile_id", 0);
		this.profile_ip_value = milanClimaxDataSource
				.CurrentProfileIp((long) current_profile_id);
		albumsArr = milanClimaxDataSource
				.selectRecentlyAddedAudioAlbumDataWithId((long) current_profile_id);
		movieArr = milanClimaxDataSource
				.selectRecentlyAddedVideoCursorDataWithId((long) current_profile_id);
		this.w = context.getWindowManager().getDefaultDisplay().getWidth();
		Log.i("LOGCAT", Integer.toString(current_profile_id));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_recently_added,
				container, false);
		headerRelative = UIHelper.getrRelativeLayout(rootView, R.id.relForHead);
		headerRelative.setOnTouchListener(new RelativeLayoutTouchListener((MainActivity) model.getContext() , model));
		relativeLayout = UIHelper.getrRelativeLayout(rootView, R.id.transparent_background);
		model.openMenusAndRemote(rootView, mDrawerLayout, context);
		main_linear_layout = UIHelper.getLinearLayoutView(rootView,
				R.id.relForLoadsAndIcons);
	
		
		artistArr = milanClimaxDataSource
				.selectAudioArtistDataWithId((long) current_profile_id);
		songArr = milanClimaxDataSource
				.selectAudioSongDataWithId((long) current_profile_id);
		
		new LoadRecentlyAddedView(context, albumsArr, movieArr, rootView, profile_ip_value, w,
				"album", main_linear_layout, relativeLayout).execute();
		return rootView;
	}

	public void getRecentlyAddedVideoMobileUpdateValues(
			String profile_ip_value, String RequestStr) {

		videoId = new ArrayList<Integer>();
		try {

			JsonReader reader = model.getJsonValuesFromServer("http://"
					+ profile_ip_value + "/jsonrpc", RequestStr);
			
			Log.i("LOGCAT", "Video_Mobile");
			if(reader != null){
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
			videoId = new ArrayList<Integer>();
			
			int movie_id;

			
			while (reader.hasNext()) {
				String name = reader.nextName();

				if (name.equalsIgnoreCase("result")) {
					
					
					reader.beginObject();
					while (reader.hasNext()) {

						String name1 = reader.nextName();
						String type = "movies";
						if (name1.equalsIgnoreCase(type)) {
							TextView text = null;
							LinearLayout.LayoutParams textParams = null;
							reader.beginArray();
							while (reader.hasNext()) {
								reader.beginObject();
								while (reader.hasNext()) {
									director = fanart = studio = rating = genre = label = plot = runtime = title = trailer = year = tagline = description = "";
									String check = reader.nextName();
									LinearLayout childLinearLayout = new LinearLayout(context);
									childLinearLayout.setOrientation(LinearLayout.VERTICAL);
									childLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
									LinearLayout.LayoutParams childLinearLayoutParams = new LayoutParams(
											LinearLayout.LayoutParams.FILL_PARENT,
											LinearLayout.LayoutParams.WRAP_CONTENT);
									if (check.equalsIgnoreCase("cast")) {
										reader.beginArray();
										while (reader.hasNext()) {
											reader.beginObject();
											while (reader.hasNext()) {
												String name2 = reader
														.nextName();
												if (name2
														.equalsIgnoreCase("name")) {
													Log.i("LOGCAT",
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
										if (director.equalsIgnoreCase("")) {

											// customData.setDirector("");
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
										// customData.setStudio(studio);
										if (studio.equalsIgnoreCase("")) {

											// customData.setStudio("");
										}
										reader.endArray();
									} else if (check
											.equalsIgnoreCase("movieid")) {
										movie_id = reader.nextInt();
										videoId.add(movie_id);
										// customData.setMovieID(movie_id);
									} else if (check.equalsIgnoreCase("rating")) {
										rating = reader.nextString();
										// customData.setRating(rating);
									} else if (check.equalsIgnoreCase("fanart")) {
										fanart = reader.nextString();
										// customData.setFanart(fanart);
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
										// customData.setGenre(genre);
										if (genre.equalsIgnoreCase("")) {
											// customData.setGenre("");
										}
										reader.endArray();

									} else if (check.equalsIgnoreCase("label")) {
										label = reader.nextString();
										text = new TextView(context);
										textParams = new LinearLayout.LayoutParams(
												model.pxToDp(90),
												LinearLayout.LayoutParams.WRAP_CONTENT);
										textParams.setMargins(model.pxToDp(5), 0, 0, 0);
										text.setLayoutParams(textParams);
										text.setText(label);
										text.setSingleLine(true);
										text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
										text.setTextColor(Color.parseColor("#939393"));
										text.setBackgroundColor(Color.parseColor("#000000"));
										text.setHeight(model.pxToDp(30));
										text.setPadding(model.pxToDp(5), 0, model.pxToDp(5), 0);
										text.setGravity(Gravity.CENTER);
										text.setTypeface(null, Typeface.BOLD);
										
										// customData.setLabel(label);
									} else if (check
											.equalsIgnoreCase("description")) {
										description = reader.nextString();
										// customData.setDescription(description);
									} else if (check.equalsIgnoreCase("plot")) {
										plot = reader.nextString();
										// customData.setPlot(plot);
									} else if (check
											.equalsIgnoreCase("runtime")) {
										runtime = reader.nextString();
										// customData.setRuntime(runtime);
									} else if (check
											.equalsIgnoreCase("tagline")) {
										tagline = reader.nextString();
										// customData.setTagline(tagline);
									} else if (check
											.equalsIgnoreCase("thumbnail")) {
										thumbnail = reader.nextString();
										LinearLayout imageLinearLayout = new LinearLayout(
												context);
										imageLinearLayout
												.setOrientation(LinearLayout.VERTICAL);
										LinearLayout.LayoutParams imageLinearLayoutParams = new LayoutParams(
												model.pxToDp(95),
												model.pxToDp(105));
										imageLinearLayout
												.setBackgroundResource(R.drawable.musicart);
										imageLinearLayoutParams.setMargins(
												model.pxToDp(5), 0, 0, 0);
										ImageView image = new ImageView(context);
										image.setPadding(model.pxToDp(10),
												model.pxToDp(1),
												model.pxToDp(10),
												model.pxToDp(1));
										Log.i("tag",
												Picasso.with(context)
														.load("http://" + profile_ip_value + "/image/"
																+ thumbnail.replace("%", "%25")).toString());
										Picasso.with(context)
												.load("http://" + profile_ip_value + "/image/"
														+ thumbnail.replace("%", "%25"))
												.resize(model.pxToDp(95), model.pxToDp(105))
												.error(R.drawable.album).centerCrop().into(image);
										imageLinearLayout.addView(image);
										childLinearLayout.addView(imageLinearLayout , imageLinearLayoutParams);
										// customData.setThumbnail(thumbnail);
										
										childLinearLayout.addView(text , textParams);
										childLinearLayout.setOnClickListener(new OnClickListener() {
											
											@Override
											public void onClick(View v) {
												// TODO Auto-generated method stub
												
											}
										});
										linearLayout.addView(childLinearLayout , childLinearLayoutParams);
										
									} else if (check.equalsIgnoreCase("title")) {

										title = reader.nextString();
										Log.i("Title-Name", "---->" + title);
										// customData.setTitle(title);
									} else if (check
											.equalsIgnoreCase("trailer")) {
										trailer = reader.nextString();
										// customData.setTrailer(trailer);
									} else if (check.equalsIgnoreCase("year")) {
										year = reader.nextString();
										// customData.setYear(year);
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
				
					/*
					 * customDatas.add(new CustomData("thumbnail", "director",
					 * "genre", "fanart", "studio", "rating", "label", "plot",
					 * "runtime", "title" , "trailer", "year", "tagline",
					 * "description"));
					 */
				} else {
					reader.skipValue();
				}

			}
			reader.endObject();
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			model.setErrorMsgForToast(context);
			e.printStackTrace();
		}

	}
	
	
	public void getAudioUpdateValues(String profile_ip_value,
			String audioRequestArr) {
		try {
			// avoids a ConcurrentModificationException

			JsonReader reader = model.getJsonValuesFromServer("http://" + profile_ip_value
					+ "/jsonrpc", audioRequestArr);
			Log.i("Tag" , "-------->"+reader.toString());
			if(reader != null){
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
							while (reader.hasNext()) {

								reader.beginObject();
								int albumid;
								String artist_name = "";
								String artist_id = "";
								thumbnail = "";
								String title = "";
								String label = "";
								TextView text = null;
								LinearLayout.LayoutParams textParams = null;
								while (reader.hasNext()) {
									
									
									name = reader.nextName();
									LinearLayout childLinearLayout = new LinearLayout(context);
									childLinearLayout.setOrientation(LinearLayout.VERTICAL);
									childLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
									LinearLayout.LayoutParams childLinearLayoutParams = new LayoutParams(
											LinearLayout.LayoutParams.FILL_PARENT,
											LinearLayout.LayoutParams.WRAP_CONTENT);
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
										}

										reader.endArray();
									} else if (name
											.equalsIgnoreCase("thumbnail")) {
										thumbnail = reader.nextString();
										LinearLayout imageLinearLayout = new LinearLayout(
												context);
										imageLinearLayout
												.setOrientation(LinearLayout.VERTICAL);
										LinearLayout.LayoutParams imageLinearLayoutParams = new LayoutParams(
												model.pxToDp(95),
												model.pxToDp(105));
										imageLinearLayout
												.setBackgroundResource(R.drawable.musicart);
										imageLinearLayoutParams.setMargins(
												model.pxToDp(5), 0, 0, 0);
										ImageView image = new ImageView(context);
										image.setPadding(model.pxToDp(10),
												model.pxToDp(1),
												model.pxToDp(10),
												model.pxToDp(1));
										Log.i("tag",
												Picasso.with(context)
														.load("http://" + profile_ip_value + "/image/"
																+ thumbnail.replace("%", "%25")).toString());
										Picasso.with(context)
												.load("http://" + profile_ip_value + "/image/"
														+ thumbnail.replace("%", "%25"))
												.resize(model.pxToDp(95), model.pxToDp(105))
												.error(R.drawable.album).centerCrop().into(image);
										imageLinearLayout.addView(image);
										childLinearLayout.addView(imageLinearLayout , imageLinearLayoutParams);
										// customData.setThumbnail(thumbnail);
										
										childLinearLayout.addView(text , textParams);
										childLinearLayout.setOnClickListener(new OnClickListener() {
											
											@Override
											public void onClick(View v) {
												// TODO Auto-generated method stub
												
											}
										});
										albumLinearLayout.addView(childLinearLayout , childLinearLayoutParams);
									} else if (name.equalsIgnoreCase("title")) {
										title = reader.nextString();
									} else if (name.equalsIgnoreCase("label")) {
										label = reader.nextString();
										text = new TextView(context);
										textParams = new LinearLayout.LayoutParams(
												model.pxToDp(90),
												LinearLayout.LayoutParams.WRAP_CONTENT);
										textParams.setMargins(model.pxToDp(5), 0, 0, 0);
										text.setLayoutParams(textParams);
										text.setText(label);
										text.setSingleLine(true);
										text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
										text.setTextColor(Color.parseColor("#939393"));
										text.setBackgroundColor(Color.parseColor("#000000"));
										text.setHeight(model.pxToDp(30));
										text.setPadding(model.pxToDp(5), 0, model.pxToDp(5), 0);
										text.setGravity(Gravity.CENTER);
										text.setTypeface(null, Typeface.BOLD);
										
									} else {
										reader.skipValue();
									}
								}

								reader.endObject();
								// }
							}
							reader.endArray();
						} else {
							reader.skipValue();
						}
					}

					reader.endObject();
				} else {
					reader.skipValue();
				}
			}
			reader.endObject();
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			model.setErrorMsgForToast(context);
			e.printStackTrace();
		}
	}

}
