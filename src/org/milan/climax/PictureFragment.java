package org.milan.climax;

import org.apache.http.HttpResponse;
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
import android.util.JsonReader;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@SuppressLint({ "NewApi", "ValidFragment" })
public class PictureFragment extends Fragment implements OnClickListener {
	private ImageView musicFolder, videoFolder, pictureFolder;
	private Model model;
	private android.app.Activity context;

	private MilanClimaxDataSource milanClimaxDataSource;
	public View rootView, genrePopUpView, directorPopUpView, studioPopUpView;
	private SharedPreferences settings;
	private int current_profile_id;
	private int w;
	private TableLayout tblLayoutInMoreMedia;
	private String current_profile_ip, categoryMedia, currentFilePath;
	private RelativeLayout headerRelative;
	public PictureFragment(String currentFilePath, String categoryMedia) {
		
		model = Model.getModelObj();
		this.context = model.getContext();
		this.categoryMedia = categoryMedia;
		this.currentFilePath = currentFilePath;
		this.milanClimaxDataSource = model.getMilanClimaxDataSource();
		settings = model.getContext().getSharedPreferences("PREF_NAME",
				Context.MODE_PRIVATE);
		current_profile_id = settings.getInt("Profile_id", 0);
		this.current_profile_ip = milanClimaxDataSource.CurrentProfileIp((long) current_profile_id);
		Log.i("LOGCAT", Integer.toString(current_profile_id));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.moremediathird, container, false);

		headerRelative = UIHelper.getrRelativeLayout(rootView, R.id.relForHead);
		headerRelative.setOnTouchListener(new RelativeLayoutTouchListener((MainActivity) model.getContext() , model));
		tblLayoutInMoreMedia = UIHelper.getTableLayoutView(rootView,
				R.id.tblLayoutInMoreMediaThird);

		getDirectoriesView(currentFilePath, categoryMedia);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	
			
	}
		
			
			public void getDirectoriesView(String filePath , String category){
				try {
					tblLayoutInMoreMedia.removeAllViews();
					JsonReader reader = model.getJsonValuesFromServer("http://"+current_profile_ip+"/jsonrpc", 
							"{\"jsonrpc\": \"2.0\", \"method\": \"Files.GetDirectory\", \"params\":{\"directory\":\""+filePath+"\"}, \"id\": 1}");
					reader.beginObject();
					while (reader.hasNext()) {
						String name = reader.nextName();
						if (name.equalsIgnoreCase("result")) {
							reader.beginObject();
							while (reader.hasNext()) {
								String check = reader.nextName();
								Log.i("Json String" , "---------------->"+check);
								if (check.equalsIgnoreCase("files")) {
									reader.beginArray();
									int i = 0;
									String label = "";
									String file = "";
									String fileType = "";
									while (reader.hasNext()) {
										i++;

										reader.beginObject();
										TableRow tableRow = new TableRow(context);
										TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(
												TableLayout.LayoutParams.FILL_PARENT, model.pxToDp(60));
										tableLayoutParams.setMargins(0, model.pxToDp(5), 0, 0);
										ImageView imageView = new ImageView(context);
										TableRow.LayoutParams imagLayoutParams = new TableRow.LayoutParams(
												model.pxToDp(40), model.pxToDp(30));
										imagLayoutParams.setMargins(0, model.pxToDp(3), 0, 0);
										imagLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
										imageView.setBackgroundResource(R.drawable.foldericon);
										
										TextView textView = new TextView(context);
										textView.setTextSize(TypedValue.COMPLEX_UNIT_SP , 13);
										textView.setTextColor(Color.parseColor("#ffffff"));
										
										TableRow.LayoutParams textLayoutParams = new TableRow.LayoutParams(
												TableRow.LayoutParams.WRAP_CONTENT,
												TableRow.LayoutParams.WRAP_CONTENT);
										textLayoutParams.gravity = Gravity.CENTER_VERTICAL;
										textLayoutParams.setMargins(0, model.pxToDp(5), 0, 0);
										while (reader.hasNext()) {	
											//Log.i("Json String" , "---------------->"+check);
											String name1 = reader.nextName();
											
											if (name1.equalsIgnoreCase("label")) {
												
												label = reader.nextString();
												textView.setText(label);
												
												 
											} else if (name1.equalsIgnoreCase("file")) {
												
												file = reader.nextString();
												String nameTest = reader.nextName();
												fileType = reader.nextString();
												if(fileType.equalsIgnoreCase("file")){
													if(category.equalsIgnoreCase("music"))
													imageView.setBackgroundResource(R.drawable.musicicon);
													if(category.equalsIgnoreCase("video"))
														imageView.setBackgroundResource(R.drawable.videoicon);
													if(category.equalsIgnoreCase("pictures"))
														imageView.setBackgroundResource(R.drawable.pictureicon);
												}
												final String finalStringFileType = fileType;
												Log.i("tag", "----------->"+fileType);
												final String typePlay = category;
												final String sendFilePath = file;
												tableRow.setOnClickListener(new OnClickListener() {
													
													@Override
													public void onClick(View v) {
														// TODO Auto-generated method stub
														if(finalStringFileType.equalsIgnoreCase("directory")){
															Fragment newFragment = new PictureFragment(sendFilePath , typePlay);
															FragmentTransaction transaction = getFragmentManager().beginTransaction();

															// Replace whatever is in the fragment_container view with this fragment,
															// and add the transaction to the back stack
															transaction.add(R.id.frame_container, newFragment);
															transaction.addToBackStack(null);

															// Commit the transaction
															transaction.commit();
														}else if(finalStringFileType.equalsIgnoreCase("file"))
														getDirectoriesView(sendFilePath, typePlay);
													}
												});	
												
												tableRow.setOnLongClickListener(new OnLongClickListener() {
													
													@Override
													public boolean onLongClick(View v) {
														// TODO Auto-generated method stub
														String requestStr = "{\"jsonrpc\":\"2.0\",\"method\":\"Player.Open\",\"id\":1,\"params\":{\"item\":{\"path\":\""+sendFilePath+"\"}}}";
														Log.i("Long-click" , "=--->"+requestStr);
														HttpResponse httpResponse = model.postJsonValuesToServer("http://"+current_profile_ip+"/jsonrpc", requestStr);
														if (httpResponse != null) {
															if(model.getCurrentActivationStatus() == 1)
																model.postJsonValuesToServer(
																		"http://" + model.getCurrentGatewayIP()
																				+ "/Milan/Drivers/Play_Pause/Play.py", "");
															((MainActivity) context).openRemoteAnimatedLoadImage();
														} else {

														}
														return true;
													}
												});
												
												 
											}else {
												
												reader.skipValue();
											}
											
										}
										
										tableRow.addView(imageView, imagLayoutParams);
										tableRow.addView(textView, textLayoutParams);
										tblLayoutInMoreMedia.addView(tableRow , tableLayoutParams);
										
										
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
					} catch (Exception e) {
						// TODO Auto-generated catch block
						model.setErrorMsgForToast(context);
						e.printStackTrace();
					}
			}


	
}
