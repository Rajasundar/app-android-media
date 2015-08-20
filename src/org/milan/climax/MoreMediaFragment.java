package org.milan.climax;

import java.io.IOException;
import java.util.ArrayList;

import org.milan.climax.R;
import org.milan.climax.UIhelper.UIHelper;
import org.milan.climax.db.MilanClimaxDataSource;
import org.milan.climax.model.Model;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.JsonReader;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@SuppressLint({ "NewApi", "ValidFragment" })
public class MoreMediaFragment extends Fragment implements OnClickListener {
	private TableRow musicFolder, videoFolder, pictureFolder;
	private Model model;
	private android.app.Activity context;

	private MilanClimaxDataSource milanClimaxDataSource;
	public View rootView, genrePopUpView, directorPopUpView, studioPopUpView;
	private SharedPreferences settings;
	private int current_profile_id;
	private int w;
	private TableLayout tblLayoutInMoreMedia;
	private String current_profile_ip;
	private DrawerLayout mDrawerLayout;
	private RelativeLayout headerRelative;

	public MoreMediaFragment(DrawerLayout mDrawerLayout) {
		
		model = Model.getModelObj();
		this.context = model.getContext();
		this.milanClimaxDataSource = model.getMilanClimaxDataSource();
		settings = model.getContext().getSharedPreferences("PREF_NAME",
				Context.MODE_PRIVATE);
		this.mDrawerLayout = mDrawerLayout;
		current_profile_id = settings.getInt("Profile_id", 0);
		this.current_profile_ip = milanClimaxDataSource
				.CurrentProfileIp((long) current_profile_id);
		Log.i("LOGCAT", Integer.toString(current_profile_id));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.moremedia, container, false);
		headerRelative = UIHelper.getrRelativeLayout(rootView, R.id.relForHead);
		headerRelative.setOnTouchListener(new RelativeLayoutTouchListener((MainActivity) model.getContext() , model));
		model.openMenusAndRemote(rootView, mDrawerLayout, context);
		musicFolder = UIHelper.getTableRowView(rootView,
				R.id.imgMusicFolderInMoreMedia);
		videoFolder = UIHelper.getTableRowView(rootView,
				R.id.imgVideoFolderInMoreMedia);
		pictureFolder = UIHelper.getTableRowView(rootView,
				R.id.imgPictureFolderInMoreMedia);
		tblLayoutInMoreMedia = UIHelper.getTableLayoutView(rootView,
				R.id.tblLayoutInMoreMedia);

		musicFolder.setOnClickListener(this);
		videoFolder.setOnClickListener(this);
		pictureFolder.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v.getId() == R.id.imgMusicFolderInMoreMedia) {
			Fragment newFragment = new MoreMediaSecondFragment("music" , mDrawerLayout);
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();

			// Replace whatever is in the fragment_container view with this
			// fragment,
			// and add the transaction to the back stack
			transaction.replace(R.id.frame_container, newFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
		} else if (v.getId() == R.id.imgVideoFolderInMoreMedia) {
			Fragment newFragment = new MoreMediaSecondFragment("video" , mDrawerLayout);
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();

			// Replace whatever is in the fragment_container view with this
			// fragment,
			// and add the transaction to the back stack
			transaction.replace(R.id.frame_container, newFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
		} else if (v.getId() == R.id.imgPictureFolderInMoreMedia) {
			Fragment newFragment = new MoreMediaSecondFragment("pictures" , mDrawerLayout);
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();

			// Replace whatever is in the fragment_container view with this
			// fragment,
			// and add the transaction to the back stack
			transaction.replace(R.id.frame_container, newFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
		}
	}

	public void getSourceView(String category) {
		try {
			tblLayoutInMoreMedia.removeAllViews();
			JsonReader reader = model
					.getJsonValuesFromServer(
							"http://" + current_profile_ip + "/jsonrpc",
							"{\"jsonrpc\": \"2.0\", \"method\": \"Files.GetSources\", \"params\":{\"media\":\""
									+ category + "\"}, \"id\": 1}");
			reader.beginObject();
			ArrayList<String> fileArr = new ArrayList<String>();
			ArrayList<String> labelArr = new ArrayList<String>();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (name.equalsIgnoreCase("result")) {
					reader.beginObject();
					while (reader.hasNext()) {
						String check = reader.nextName();
						Log.i("Json String", "---------------->" + check);
						if (check.equalsIgnoreCase("sources")) {
							reader.beginArray();
							int i = 0;

							while (reader.hasNext()) {
								i++;

								reader.beginObject();
								TableRow tableRow = new TableRow(context);
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
								textView.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										Log.i("Tag", "----->ccc");
									}
								});
								TableRow.LayoutParams textLayoutParams = new TableRow.LayoutParams(
										TableRow.LayoutParams.WRAP_CONTENT,
										TableRow.LayoutParams.WRAP_CONTENT);
								textLayoutParams.gravity = Gravity.CENTER_VERTICAL;
								textLayoutParams.setMargins(0, model.pxToDp(5),
										0, 0);
								while (reader.hasNext()) {
									// Log.i("Json String" ,
									// "---------------->"+check);
									String name1 = reader.nextName();
									String label = "";
									String file = "";
									if (name1.equalsIgnoreCase("label")) {

										label = reader.nextString();
										textView.setText(label);
										textView.setTextSize(
												TypedValue.COMPLEX_UNIT_SP, 11);
										textView.setTextColor(Color
												.parseColor("#ffffff"));
										labelArr.add(label);

									} else if (name1.equalsIgnoreCase("file")) {

										file = reader.nextString();
										fileArr.add(file);
										final String sendFilePath = file;
										final String typePlay = category;
										textView.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												// TODO Auto-generated method
												// stub
												getDirectoriesView(
														sendFilePath, typePlay);
											}
										});

									} else {
										reader.skipValue();
									}

								}

								tableRow.addView(imageView, imagLayoutParams);
								tableRow.addView(textView, textLayoutParams);
								tblLayoutInMoreMedia.addView(tableRow,
										tableLayoutParams);

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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getDirectoriesView(String filePath, String category) {
		try {
			tblLayoutInMoreMedia.removeAllViews();
			JsonReader reader = model
					.getJsonValuesFromServer(
							"http://" + current_profile_ip + "/jsonrpc",
							"{\"jsonrpc\": \"2.0\", \"method\": \"Files.GetDirectory\", \"params\":{\"directory\":\""
									+ filePath + "\"}, \"id\": 1}");
			reader.beginObject();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (name.equalsIgnoreCase("result")) {
					reader.beginObject();
					while (reader.hasNext()) {
						String check = reader.nextName();
						Log.i("Json String", "---------------->" + check);
						if (check.equalsIgnoreCase("files")) {
							reader.beginArray();
							String label = "";
							String file = "";
							String fileType = "";
							while (reader.hasNext()) {

								reader.beginObject();
								TableRow tableRow = new TableRow(context);
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
								textView.setPadding(model.pxToDp(5), model.pxToDp(10), model.pxToDp(2), model.pxToDp(5));
								textView.setTextSize(
										TypedValue.COMPLEX_UNIT_SP, 14);
								textView.setTextColor(Color
										.parseColor("#ffffff"));

								TableRow.LayoutParams textLayoutParams = new TableRow.LayoutParams(
										TableRow.LayoutParams.WRAP_CONTENT,
										TableRow.LayoutParams.WRAP_CONTENT);
								textLayoutParams.gravity = Gravity.CENTER_VERTICAL;
								while (reader.hasNext()) {
									// Log.i("Json String" ,
									// "---------------->"+check);
									String name1 = reader.nextName();

									if (name1.equalsIgnoreCase("label")) {

										label = reader.nextString();
										textView.setText(label);

									} else if (name1
											.equalsIgnoreCase("filetype")) {

										fileType = reader.nextString();

										if (fileType.equalsIgnoreCase("file")) {

											if (category
													.equalsIgnoreCase("music"))
												imageView
														.setBackgroundResource(R.drawable.musicicon);
											if (category
													.equalsIgnoreCase("video"))
												imageView
														.setBackgroundResource(R.drawable.videoicon);
											if(category.equalsIgnoreCase("pictures"))
												imageView.setBackgroundResource(R.drawable.pictureicon);
										} else {

										}

									} else if (name1.equalsIgnoreCase("file")) {

										file = reader.nextString();
										final String typePlay = category;
										final String sendFilePath = file;
										textView.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												// TODO Auto-generated method
												// stub
												getDirectoriesView(
														sendFilePath, typePlay);
											}
										});

									} else {

										reader.skipValue();
									}

								}

								tableRow.addView(imageView, imagLayoutParams);
								tableRow.addView(textView, textLayoutParams);
								tblLayoutInMoreMedia.addView(tableRow,
										tableLayoutParams);

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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
