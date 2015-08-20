package org.milan.climax.model;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.milan.climax.R;
import org.milan.climax.MainActivity;
import org.milan.climax.UIhelper.UIHelper;
import org.milan.climax.asynchronous.HttpResponseData;
import org.milan.climax.asynchronous.RequestData;
import org.milan.climax.db.MilanClimaxDataSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.R.bool;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.JsonReader;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;

import com.squareup.picasso.Transformation;

@SuppressLint("NewApi")
public class Model implements Transformation {

	public MilanClimaxDataSource milanClimaxDataSource;
	private LayoutInflater inflaterForAll;
	private android.app.Activity contextforAll;
	private Integer profileCount;
	private DefaultHttpClient httpclient;
	private HttpPost httpPost;
	private StringEntity stringEntity;
	private HttpResponse httpResponse;
	private InputStream inputStream;
	private JsonReader JsonReader;
	private String current_profile_ip_value, currentRoomName;
	private int current_profile_id;
	public boolean popUpOPen;
	public PopupWindow popUpMessageProfileList;
	private static final String LOGCAT = null;
	private Model model;
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
	private String currentGatewayIP;
	private int currentActivationStatus;
	private ArrayList<String> videoDirector, videoFanArt, videoGenre,
			videoLabel, videoDescription, videoPlot, videoRunTime,
			videoTagLine, videoThumbnail, videoTitle, videoTrailer, videoYear,
			videoRating, videoStudio;
	private String director, fanart, genre, label, runtime, title, trailer,
			year, tagline, plot, description, thumbnail, rating, studio;
	private String whichConfig;
	private double screenSize;

	public double getScreenSize() {
		return screenSize;
	}

	public void setScreenSize(double screenSize) {
		this.screenSize = screenSize;
	}

	public PopupWindow getPopUpMessageProfileList() {
		return popUpMessageProfileList;
	}

	public void setPopUpMessageProfileList(PopupWindow popupWindow) {
		this.popUpMessageProfileList = popupWindow;
	}

	public boolean isPopUpOPen() {
		return popUpOPen;
	}

	public void setPopUpOPen(boolean popUpOPen) {
		this.popUpOPen = popUpOPen;
	}

	public Integer getProfileCount() {
		return profileCount;
	}

	public void setProfileCount(Integer profileCount) {
		this.profileCount = profileCount;
	}

	public static Model ModelObj;

	public Model() {

	}

	public static Model getModelObj() {
		if (ModelObj == null) {
			ModelObj = new Model();
		}
		return ModelObj;
	}

	public void setInflater(LayoutInflater inflater) {
		this.inflaterForAll = inflater;
	}

	public LayoutInflater getInflater() {
		return inflaterForAll;
	}

	public void setContext(android.app.Activity context) {
		this.contextforAll = context;
	}

	public android.app.Activity getContext() {
		return contextforAll;
	}

	public void setMilanClimax(MilanClimaxDataSource milanClimaxDataSource) {
		this.milanClimaxDataSource = milanClimaxDataSource;
	}

	public MilanClimaxDataSource getMilanClimaxDataSource() {
		return milanClimaxDataSource;
	}

	public int pxToDp(int px) {
		int x = (int) (px * Resources.getSystem().getDisplayMetrics().density);
		Log.i("tag", Integer.toString(x));
		return x;

	}

	public double pxToDoubleDp(double px) {
		double x = (double) (px * Resources.getSystem().getDisplayMetrics().density);
		return x;

	}

	public JsonReader getJsonValuesFromServer(String url, String postJsonString) {

		JsonReader jsonReader = null;
		try {
			jsonReader = new RequestData().execute(url , postJsonString).get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		return jsonReader;
	}

	public HttpResponse postJsonValuesToServer(String url, String postJsonString) {

		HttpResponse httpResponse = null;
		try {
			httpResponse = new HttpResponseData().execute(url, postJsonString).get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		return httpResponse;
	}

	public InputStream getXMLFromService(String url) {

		InputStream is = null;
		try {
			httpclient = new DefaultHttpClient();
			httpResponse = httpclient.execute(new HttpGet(url));
			HttpEntity httpEntity = httpResponse.getEntity();

			if (httpEntity != null) {
				is = httpEntity.getContent();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return is;
	}

	public void openMenusAndRemote(View v, final DrawerLayout mDrawerLayout,
			final android.app.Activity context) {
		TextView profileName = UIHelper.getTextView(v, R.id.txtForRooms);
		View milanNavButton = UIHelper.getView(v, R.id.milanOS);

		milanNavButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean appInstallStatus = appInstalledOrNot("com.org.milan_os2_mobile");
				if (appInstallStatus) {
					try {
						confirmDialog();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else {
					InstallAppConfirmDialog();
				}
			}
		});
		String RoomName = milanClimaxDataSource
				.CurrentProfileRoomName((long) current_profile_id);
		Log.i("Room-name", "---------->" + RoomName);

		if (current_profile_id == 0)
			profileName.setText("Select Profile");
		else {
			if (RoomName == null)
				profileName.setText("Profile" + current_profile_id);
			else {
				RoomName = RoomName.substring(0, 1).toUpperCase()
						+ RoomName.substring(1);
				profileName.setText(RoomName);
			}

		}
		ImageView openRemote = UIHelper.getImageView(v, R.id.cntrlBtn);
		ImageView openMenu = UIHelper.getImageView(v, R.id.arrow);
		openRemote.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((MainActivity) context).openRemoteAnimatedLoadImage();
			}
		});
		openMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Boolean f = mDrawerLayout.isDrawerOpen(Gravity.LEFT);
				if (f == false)
					mDrawerLayout.openDrawer(Gravity.LEFT);
				else if (f == true)
					mDrawerLayout.closeDrawer(Gravity.LEFT);
			}
		});
		profileName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Boolean f = mDrawerLayout.isDrawerOpen(Gravity.RIGHT);
				if (f == false)
					mDrawerLayout.openDrawer(Gravity.RIGHT);
				else if (f == true)
					mDrawerLayout.closeDrawer(Gravity.RIGHT);
			}
		});
	}

	public void confirmDialog() {
		/*
		 * AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		 * builder
		 * .setMessage("Are you sure you want to switch to Milan application?")
		 * .setCancelable(false) .setPositiveButton("Yes", new
		 * DialogInterface.OnClickListener() { public void
		 * onClick(DialogInterface dialog, int id) {
		 * 
		 * } }) .setNegativeButton("No", new DialogInterface.OnClickListener() {
		 * public void onClick(DialogInterface dialog, int id) {
		 * dialog.dismiss(); } }); AlertDialog alert = builder.create();
		 * alert.show(); WindowManager.LayoutParams lp = new
		 * WindowManager.LayoutParams();
		 * lp.copyFrom(alert.getWindow().getAttributes());
		 * alert.getWindow().setAttributes(lp); return alert;
		 */
		Intent LaunchIntent = getContext().getPackageManager()
				.getLaunchIntentForPackage("com.org.milan_os2_mobile");
		getContext().startActivity(LaunchIntent);
	}

	public AlertDialog InstallAppConfirmDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setMessage("Install Milan Universal to control milan")
				.setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						String url = "https://play.google.com/store/apps/details?id=com.org.milan_os2_mobile&hl=en";
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(url));
						i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						getContext().startActivity(i);
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(alert.getWindow().getAttributes());
		alert.getWindow().setAttributes(lp);
		return alert;
	}

	public DisplayMetrics getDisplay() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		return displaymetrics;

	}

	public double getScreenSizeInInches(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
		double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
		double screenInches = Math.sqrt(x + y);
		return screenInches;
	}

	private boolean appInstalledOrNot(String uri) {
		PackageManager pm = getContext().getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(uri, pm.GET_ACTIVITIES);
			app_installed = true;
		} catch (Exception e) {
			app_installed = false;
		}
		return app_installed;
	}

	public int getHeight(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		int height = context.getWindowManager().getDefaultDisplay().getHeight();
		return height;
	}

	public Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setCustomActionBar(int profile_id) {
		/*
		 * final ActionBar actionBar = contextforAll.getActionBar();
		 * actionBar.setCustomView(R.layout.action_bar);
		 * actionBar.setDisplayShowTitleEnabled(false);
		 * actionBar.setDisplayShowHomeEnabled(false);
		 * actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
		 * actionBar.setDisplayShowCustomEnabled(true);
		 * actionBar.setIcon(R.drawable.head); Button b = (Button)
		 * actionBar.getCustomView().findViewById( R.id.openProfileRight); if
		 * (profile_id == 0) b.setText("Select Profile"); else
		 * b.setText("Profile" + profile_id);
		 */
	}

	public String getCurrent_profile_ip_value() {
		return current_profile_ip_value;
	}

	public void setCurrent_profile_ip_value(String current_profile_ip_value) {
		this.current_profile_ip_value = current_profile_ip_value;
	}

	public int getCurrent_profile_id() {
		return current_profile_id;
	}

	public void setCurrent_profile_id(int current_profile_id) {
		this.current_profile_id = current_profile_id;
	}

	public void closePopup(PopupWindow popWindow) {
		this.setPopUpMessageProfileList(null);
		//if (popWindow != null) {
			if (popWindow.isShowing()) {
				popWindow.dismiss();
			}
		//}
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
					milanClimaxDataSource.bulkInsertRecentlyAddedMovieData(
							(long) model.getCurrent_profile_id(), videoId,
							videoDirector, videoFanArt, videoGenre, videoLabel,
							videoDescription, videoPlot, mediaIp, videoRunTime,
							videoRating, videoTagLine, videoThumbnail,
							videoTitle, videoTrailer, videoYear, videoStudio);

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

	public void openPopUpScreen(double screenSize, RelativeLayout relLayout) {
		if (screenSize <= 4.3)
			model.setPopUpMessageProfileList(new PopupWindow(relLayout,
					LayoutParams.WRAP_CONTENT, model.pxToDp(450)));
		else
			model.setPopUpMessageProfileList(new PopupWindow(relLayout,
					LayoutParams.WRAP_CONTENT, model.pxToDp(500)));
	}

	/**
	 * @return the currentGatewayIP
	 */
	public String getCurrentGatewayIP() {
		return currentGatewayIP;
	}

	/**
	 * @param currentGatewayIP
	 *            the currentGatewayIP to set
	 */
	public void setCurrentGatewayIP(String currentGatewayIP) {
		this.currentGatewayIP = currentGatewayIP;
	}

	/**
	 * @return the currentActivationStatus
	 */
	public int getCurrentActivationStatus() {
		return currentActivationStatus;
	}

	/**
	 * @param currentActivationStatus
	 *            the currentActivationStatus to set
	 */
	public void setCurrentActivationStatus(int currentActivationStatus) {
		this.currentActivationStatus = currentActivationStatus;
	}

	/**
	 * @return the currentRoomName
	 */
	public String getCurrentRoomName() {
		return currentRoomName;
	}

	/**
	 * @param currentRoomName
	 *            the currentRoomName to set
	 */
	public void setCurrentRoomName(String currentRoomName) {
		this.currentRoomName = currentRoomName;
	}

	public void setErrorMsgForToast(Activity context) {
		Toast.makeText(context, "Switch on the Milan Media", 1000).show();
	}

	@Override
	public String key() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bitmap transform(Bitmap source) {
		int size = Math.min(source.getWidth(), source.getHeight());
		int x = (source.getWidth() - size) / 2;
		int y = (source.getHeight() - size) / 2;
		Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
		if (result != source) {
			source.recycle();
		}
		return result;
	}

	
	public boolean isOnline(Context context) { 
		boolean exists = false;

		try {
			String[] currentIP = milanClimaxDataSource.CurrentGatewayIp((long) getCurrent_profile_id()).split(":");
		    SocketAddress sockaddr = new InetSocketAddress(currentIP[0] , 100);
		    // Create an unbound socket
		    Socket sock = new Socket();

		    // This method will block no more than timeoutMs.
		    // If the timeout occurs, SocketTimeoutException is thrown.
		    int timeoutMs = 2000;   // 2 seconds
		    sock.connect(sockaddr, timeoutMs);
		    exists = true;
		}catch(Exception e){
		}
		return exists;
	}

}
