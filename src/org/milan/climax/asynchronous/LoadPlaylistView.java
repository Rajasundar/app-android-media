package org.milan.climax.asynchronous;

import org.apache.http.HttpResponse;
import org.milan.climax.R;
import org.milan.climax.MainActivity;
import org.milan.climax.db.MilanClimaxDataSource;
import org.milan.climax.model.Model;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@SuppressLint("NewApi")
public class LoadPlaylistView extends AsyncTask<String, Void, Void> {

	private android.app.Activity context;
	private String profile_ip_value;
	private RelativeLayout relLayout;
	public int w;
	private ProgressDialog dialog;
	private Model model;
	private MilanClimaxDataSource milanClimaxDataSource;
	private SharedPreferences settings;
	private int current_profile_id;
	private TableLayout tblLayoutInPlaylist;
	private String current_profile_ip, currentFilePath;
	private int categoryMedia, position;
	private TextView textHeading;

	public LoadPlaylistView(int category, TableLayout tblLayoutInPlaylist,
			String profile_ip) {
		this.tblLayoutInPlaylist = tblLayoutInPlaylist;
		this.categoryMedia = category;
		this.current_profile_ip = profile_ip;
		this.model = Model.getModelObj();
		this.context = model.getContext();
		Log.i("Tag", "Profile----->" + current_profile_ip);
	}

	public LoadPlaylistView() {

	}

	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub

		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		// TODO Auto-generated method stub

		dialog.dismiss();
		try {
			JsonReader reader = model
					.getJsonValuesFromServer(
							"http://" + current_profile_ip + "/jsonrpc",
							"{\"id\":1,\"jsonrpc\":\"2.0\",\"method\":\"Playlist.GetItems\",\"params\":{\"playlistid\":"
									+ categoryMedia
									+ ", \"properties\":[\"albumid\",\"duration\"]}}");

			tblLayoutInPlaylist.removeAllViews();
			reader.beginObject();
			position = 0;
			int Duration = 0;
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (name.equalsIgnoreCase("result")) {
					reader.beginObject();
					while (reader.hasNext()) {
						String check = reader.nextName();
						Log.i("Json String", "---------------->" + check);
						if (check.equalsIgnoreCase("items")) {
							reader.beginArray();
							String label = "";
							while (reader.hasNext()) {

								reader.beginObject();
								TableRow tableRow = new TableRow(context);
								tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
								TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(
										TableLayout.LayoutParams.FILL_PARENT,
										model.pxToDp(60));
								tableLayoutParams.setMargins(0,
										model.pxToDp(5), 0, 0);
								ImageView imageView = new ImageView(context);
								TableRow.LayoutParams imagLayoutParams = new TableRow.LayoutParams(
										model.pxToDp(40), model.pxToDp(30));
								imagLayoutParams.setMargins(0, model.pxToDp(3),
										0, 0);
								imagLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
								imageView
										.setBackgroundResource(R.drawable.foldericon);

								TextView textView = new TextView(context);
								textView.setTextSize(
										TypedValue.COMPLEX_UNIT_SP, 14);
								textView.setPadding(model.pxToDp(5),
										model.pxToDp(10), model.pxToDp(20),
										model.pxToDp(5));
								textView.setTextColor(Color
										.parseColor("#ffffff"));
								textView.setSingleLine(true);
								TableRow.LayoutParams textLayoutParams = new TableRow.LayoutParams(
										model.pxToDp(160),
										TableRow.LayoutParams.WRAP_CONTENT);

								TextView textView1 = new TextView(context);
								textView1.setTextSize(
										TypedValue.COMPLEX_UNIT_SP, 14);
								textView1.setPadding(model.pxToDp(5),
										model.pxToDp(10), model.pxToDp(2),
										model.pxToDp(5));
								textView1.setTextColor(Color
										.parseColor("#ffffff"));

								TableRow.LayoutParams textLayoutParams1 = new TableRow.LayoutParams(
										TableRow.LayoutParams.WRAP_CONTENT,
										TableRow.LayoutParams.WRAP_CONTENT);

								textLayoutParams.gravity = Gravity.CENTER_VERTICAL;
								if (categoryMedia == 0)
									imageView
											.setBackgroundResource(R.drawable.musicicon);
								if (categoryMedia == 1)
									imageView
											.setBackgroundResource(R.drawable.videoicon);
								if (categoryMedia == 2)
									imageView
											.setBackgroundResource(R.drawable.pictureicon);
								while (reader.hasNext()) {

									// Log.i("Json String" ,
									// "---------------->"+check);
									String name1 = reader.nextName();
									
									String min = "";
									String sec = "";
									int f;
									int f1;
									if (name1.equalsIgnoreCase("duration")) {

										Duration = reader.nextInt();
										Log.i("Json String",
												"---------------->" + Duration);
										

									} else if (name1.equalsIgnoreCase("label")) {

										label = reader.nextString();
										textView.setText(label);
										f = Duration / 60;
										f1 = Duration % 60;
										min = Integer.toString(f);
										sec = Integer.toString(f1);
										if (sec.length() == 1)
											sec = "0" + sec;
										if (min.length() == 1)
											min = "0" + min;
										textView1.setText(min + ":" + sec);
										tableRow.addView(imageView,
												imagLayoutParams);
										tableRow.addView(textView,
												textLayoutParams);
										tableRow.addView(textView1,
												textLayoutParams1);
										tableRow.setId(position);
										position++;
									
										tableRow.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												// TODO Auto-generated method
												// stub

												String requestStr = "{\"jsonrpc\":\"2.0\",\"method\":\"Player.Open\",\"id\":1,\"params\":{\"item\":{\"playlistid\":"
														+ categoryMedia
														+ ",\"position\":"
														+ v.getId() + "}}}";
												Log.i("Tag", "=--->"
														+ requestStr);
												HttpResponse httpResponse = model
														.postJsonValuesToServer(
																"http://"
																		+ current_profile_ip
																		+ "/jsonrpc",
																requestStr);
												if (httpResponse != null) {
													if (model
															.getCurrentActivationStatus() == 1)
														model.postJsonValuesToServer(
																"http://"
																		+ model.getCurrentGatewayIP()
																		+ "/Milan/Drivers/Play_Pause/Play.py",
																"");
													((MainActivity) context)
															.openRemoteAnimatedLoadImage();
												} else {

												}
											}
										});

									} else {

										reader.skipValue();
									}

								}

								reader.endObject();
								tblLayoutInPlaylist.addView(tableRow,
										tableLayoutParams);

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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog = ProgressDialog.show(context, "Loading",
				"Doing stuff. Please wait...");
		dialog.setProgressStyle(R.style.ProgressBar);

	}

}
