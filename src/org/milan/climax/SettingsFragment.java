package org.milan.climax;

import java.util.ArrayList;

import org.milan.climax.R;
import org.milan.climax.UIhelper.UIHelper;
import org.milan.climax.asynchronous.LoadAudioView;
import org.milan.climax.asynchronous.LoadSettingsConfig;
import org.milan.climax.db.MilanClimaxDBHelper;
import org.milan.climax.db.MilanClimaxDataSource;
import org.milan.climax.model.Model;
import org.milan.climax.slider.NavDrawerItem;
import org.milan.climax.slider.ProfileDrawerListAdapter;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

@SuppressLint({ "ValidFragment", "NewApi" })
public class SettingsFragment extends Fragment implements OnClickListener {

	private View rootView, editFormView;
	private TableLayout tableLayout;
	private android.app.Activity context;
	private Model model;
	private MilanClimaxDataSource milanClimaxDataSource;
	private Button tabAudioButton, tabVideoButton;
	public EditText profSettRoomName, profSettGatewayIp, profSettMediaIp,
			profSettUsername, profSettPwd;
	private EditText roomName, mediaIp, gatewayIP, Username, Password,
			mediaPort, gatewayPort;
	private RelativeLayout relativeLayout;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerListProfile;
	private ArrayList<NavDrawerItem> navDrawerItems, navDrawerItemsProfile;
	private TypedArray navMenuIcons;
	ProfileDrawerListAdapter adapter1;
	private final int totalProfileCount = 20;

	public SettingsFragment(DrawerLayout mDrawerLayout, ListView mDrawerListProfiles, TypedArray navMenuIcons, ArrayList<NavDrawerItem> navDrawerItemsProfile) {
		model = Model.getModelObj();
		this.context = model.getContext();
		this.mDrawerLayout = mDrawerLayout;
		this.mDrawerListProfile = mDrawerListProfiles;
		this.milanClimaxDataSource = model.getMilanClimaxDataSource();
		this.navMenuIcons = navMenuIcons;
		this.navDrawerItemsProfile = navDrawerItemsProfile;
	}

	private LinearLayout linearLayout;
	private TextView submitSettingsProfile, btnBackInProfileSettings,
			btnResetInProfileSettings;
	private RelativeLayout headerRelative;

	@SuppressLint("ResourceAsColor")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = context.getLayoutInflater().inflate(R.layout.profile_edit,
				container, false);
		headerRelative = UIHelper.getrRelativeLayout(rootView, R.id.relForHead);
		headerRelative.setOnTouchListener(new RelativeLayoutTouchListener((MainActivity) model.getContext() , model));
		relativeLayout = (RelativeLayout) rootView
				.findViewById(R.id.linearForTitleInProfileSettings);
		model.openMenusAndRemote(rootView, mDrawerLayout, context);
		new LoadAudioView().cancel(true);
		loadViewDynamically();
		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		linearLayout.removeAllViews();
		editFormView = context.getLayoutInflater().inflate(
				R.layout.form_edit_profile, null);
		linearLayout.addView(editFormView);
	}

	public void loadViewDynamically() {
		linearLayout = (LinearLayout) rootView
				.findViewById(R.id.relMainInSettings);
		tableLayout = (TableLayout) rootView
				.findViewById(R.id.tblLayoutInSettings);
		TableRow tableRow = new TableRow(context);
		TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(
				model.pxToDp(230), model.pxToDp(40));
		tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
		TextView textView = new TextView(context);
		textView.setText("Profile Name");
		textView.setTextColor(Color.parseColor("#75B543"));
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		textView.setTextAppearance(context, R.style.boldText);
		TextView textView1 = new TextView(context);
		textView1.setText("Edit");
		textView1.setTextColor(Color.parseColor("#ffffff"));
		textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		textView1.setTextAppearance(context, R.style.boldText);
		textView1.setGravity(Gravity.CENTER_VERTICAL);
		textView1.setGravity(Gravity.RIGHT);
		tableRow.addView(textView, new TableRow.LayoutParams(model.pxToDp(140),
				model.pxToDp(30)));
		tableRow.addView(textView1, new TableRow.LayoutParams(model.pxToDp(80),
				model.pxToDp(30)));
		tableLayout.removeAllViews();
		tableLayout.addView(tableRow, tableRowParams);
		TableRow[] tableRows = new TableRow[model.getProfileCount() + 1];
		final TextView[] textView2 = new TextView[model.getProfileCount() + 1];
		ImageView[] imageView = new ImageView[model.getProfileCount() + 1];
		for (int i = 1; i < model.getProfileCount() + 1; i++) {

			tableRows[i] = new TableRow(context);
			tableRows[i].setGravity(Gravity.CENTER_HORIZONTAL);
			textView2[i] = new TextView(context);
			
			String RoomName = milanClimaxDataSource.CurrentProfileRoomName((long) i);
			Log.i("Room-name" , "---------->"+RoomName);
			if(RoomName == null)
				textView2[i].setText("Profile"+i);
			else{
				RoomName = RoomName.substring(0,1).toUpperCase() + RoomName.substring(1).toLowerCase();
				textView2[i].setText(RoomName);
			}
			textView2[i].setTextColor(Color.parseColor("#ffffff"));
			textView2[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			textView2[i].setSingleLine(true);
			final int k = i;
			textView2[i].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Fragment fragment = new ProfileFragment(k, mDrawerLayout);

					textView2[k].setTextColor(Color.parseColor("#75b543"));
					if (fragment != null) {
						android.app.FragmentManager fragmentManager = getFragmentManager();
						fragmentManager.beginTransaction()
								.replace(R.id.frame_container, fragment).commit();

					}
				}
			});
			imageView[i] = new ImageView(context);
			imageView[i].setImageResource(android.R.drawable.ic_menu_edit);
			final android.app.Activity inflater12 = context;
			final int j = i;
			imageView[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					linearLayout.removeAllViews();
					relativeLayout.setVisibility(View.GONE);
					editFormView = inflater12.getLayoutInflater().inflate(
							R.layout.form_edit_profile, null);

					UIHelper.displayText(editFormView, R.id.txtProfileSettings,
							"Edit Profile" + j);

					submitSettingsProfile = UIHelper.getTextView(editFormView,
							R.id.btnSaveInProfileSettings);
					btnBackInProfileSettings = UIHelper.getTextView(
							editFormView, R.id.btnBackInProfileSettings);
					btnResetInProfileSettings = UIHelper.getTextView(
							editFormView, R.id.btnResetInProfileSettings);
					final EditText roomName = UIHelper.getEditText(
							editFormView, R.id.edit_text_room_name);
					EditText mediaIpSample = UIHelper.getEditText(editFormView,
							R.id.edit_text_media_ip);
					EditText mediaPort = UIHelper.getEditText(editFormView,
							R.id.edit_text_media_port);

					final String mediaIP = mediaIpSample.getText() + ":"
							+ mediaPort.getText();
					Log.i("Main", "---------->" + mediaIpSample.getText());
					final EditText gatewayIP = UIHelper.getEditText(
							editFormView, R.id.edit_text_gateway_ip);
					final EditText gatewayPort = UIHelper.getEditText(
							editFormView, R.id.edit_text_gateway_port);
					final EditText Username = UIHelper.getEditText(
							editFormView, R.id.edit_text_username);
					final EditText Password = UIHelper.getEditText(
							editFormView, R.id.edit_text_password);

					setValuesToForm(j);

					btnBackInProfileSettings
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									btnBackInProfileSettings.setTextColor(Color.parseColor("#75B543"));
									Fragment fragment = fragment = new SettingsFragment(mDrawerLayout, mDrawerListProfile, navMenuIcons, navDrawerItemsProfile);
									
									FragmentTransaction transaction = getFragmentManager()
											.beginTransaction();
									transaction.add(R.id.ParentLayoutInProfile,
											fragment).commit();
								}
							});
					btnResetInProfileSettings
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									btnResetInProfileSettings.setTextColor(Color.parseColor("#75B543"));
									new Handler().postDelayed(new Runnable() {

										@Override
										public void run() {

											btnResetInProfileSettings.setTextColor(Color.parseColor("#ffffff"));

										}
									}, 500);
									UIHelper.displayText(editFormView,
											R.id.edit_text_room_name, "");
									UIHelper.displayText(editFormView,
											R.id.edit_text_media_ip, "");
									UIHelper.displayText(editFormView,
											R.id.edit_text_gateway_ip, "");
									UIHelper.displayText(editFormView,
											R.id.edit_text_username, "");
									UIHelper.displayText(editFormView,
											R.id.edit_text_password, "");
								}
							});

					/*
					 * tabAudioButton = (Button) editFormView
					 * .findViewById(R.id.button_audio_tab_update);
					 * tabAudioButton.setOnClickListener(new OnClickListener() {
					 * 
					 * @Override public void onClick(View v) { // TODO
					 * Auto-generated method stub
					 * 
					 * } }); tabVideoButton = (Button) editFormView
					 * .findViewById(R.id.button_video_tab_update);
					 * tabVideoButton.setOnClickListener(new OnClickListener() {
					 * 
					 * @Override public void onClick(View v) { // TODO
					 * Auto-generated method stub
					 * 
					 * } });
					 */
					submitSettingsProfile
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									submitSettingsProfile.setTextColor(Color.parseColor("#75B543"));
									final EditText roomName = UIHelper
											.getEditText(editFormView,
													R.id.edit_text_room_name);
									EditText mediaIpSample = UIHelper
											.getEditText(editFormView,
													R.id.edit_text_media_ip);
									EditText mediaPort = UIHelper.getEditText(
											editFormView,
											R.id.edit_text_media_port);

									String mediaIP = mediaIpSample.getText()
											+ ":" + mediaPort.getText();
									Log.i("Main",
											"---------->"
													+ mediaIpSample.getText());
									EditText gatewayIpSample = UIHelper
											.getEditText(editFormView,
													R.id.edit_text_gateway_ip);
									EditText gatewayPort = UIHelper
											.getEditText(editFormView,
													R.id.edit_text_gateway_port);
									String gatewayIP = gatewayIpSample
											.getText()
											+ ":"
											+ gatewayPort.getText();
									EditText Username = UIHelper.getEditText(
											editFormView,
											R.id.edit_text_username);
									EditText Password = UIHelper.getEditText(
											editFormView,
											R.id.edit_text_password);
									ToggleButton toggleButton = (ToggleButton) editFormView
											.findViewById(R.id.edit_text_status);
									Boolean buttonStatus = toggleButton
											.isChecked();
									int onOff = 0;
									if (buttonStatus == true)
										onOff = 1;
									if(!roomName.getText()
													.toString().isEmpty() && !mediaIpSample.getText().toString().isEmpty() && !mediaPort.getText().toString().isEmpty()){
									new LoadSettingsConfig(context, (long) j,
											milanClimaxDataSource, model,
											mediaIP, "audio").execute();
									new LoadSettingsConfig(context, (long) j,
											milanClimaxDataSource, model,
											mediaIP, "video").execute();
									milanClimaxDataSource.updateProfileData(
											(long) j, roomName.getText()
													.toString(), mediaIP,
											onOff, gatewayIP, Username
													.getText().toString(),
											Password.getText().toString());
									navDrawerItemsProfile.clear();
									int j = 0;
									ArrayList<String> profiles = new ArrayList<String>();
									navDrawerItemsProfile = new ArrayList<NavDrawerItem>();
									String RoomName = "";
									for (int i = 1; i < totalProfileCount + 1; i++) {
										RoomName = milanClimaxDataSource.CurrentProfileRoomName((long) i);
										Log.i("Room-name" , "---------->"+RoomName);
										if(RoomName == null)
											profiles.add("Profile"+i);
										else{
											RoomName = RoomName.substring(0,1).toUpperCase() + RoomName.substring(1).toLowerCase();
											profiles.add(RoomName);
										}
										

									}
									for (int i = 0; i < profiles.size(); i++) {
										if (i % 5 == 0) {
											j = 0;
										} else
											j++;
										navDrawerItemsProfile.add(new NavDrawerItem(profiles.get(i),
												navMenuIcons.getResourceId(j, 2)));
									}
									
									adapter1 = new ProfileDrawerListAdapter(context,
											navDrawerItemsProfile);
									mDrawerListProfile.setAdapter(adapter1);
									Fragment fragment = new SettingsFragment(mDrawerLayout, mDrawerListProfile, navMenuIcons, navDrawerItemsProfile);
									
									FragmentTransaction transaction = getFragmentManager()
											.beginTransaction();
									transaction.add(R.id.ParentLayoutInProfile,
											fragment).commit();
										
									}else{
										Toast.makeText(context, "All * fields should not be Empty", 1000).show();
										
									}
									
								}
							});
					linearLayout.addView(editFormView);

				}
			});
			TableRow.LayoutParams params = new TableRow.LayoutParams(
					TableRow.LayoutParams.WRAP_CONTENT,
					TableRow.LayoutParams.WRAP_CONTENT);
			params.setMargins(model.pxToDp(40), 0, 0, 0);
			tableRows[i].addView(
					textView2[i],
					new TableRow.LayoutParams(model.pxToDp(140), model
							.pxToDp(30)));
			tableRows[i].addView(imageView[i], params);
			tableRowParams.setMargins(0, model.pxToDp(10), 0, 0);
			tableLayout.addView(tableRows[i], tableRowParams);
		}
	}

	public void setValuesToForm(long SelectID) {

		Log.i("LOGCAT", "Open form for profile" + SelectID);
		Cursor ProfileValues = milanClimaxDataSource
				.selectProfileCursorDataWithId(SelectID);
		roomName = UIHelper.getEditText(editFormView, R.id.edit_text_room_name);
		EditText mediaIpSample = UIHelper.getEditText(editFormView,
				R.id.edit_text_media_ip);
		EditText mediaPort = UIHelper.getEditText(editFormView,
				R.id.edit_text_media_port);
		final String mediaIP = mediaIpSample.getText().toString() + ":"
				+ mediaPort.getText().toString();
		Log.i("Main", "---------->" + mediaIP);
		mediaIp = UIHelper.getEditText(editFormView, R.id.edit_text_media_ip);
		gatewayIP = UIHelper.getEditText(editFormView,
				R.id.edit_text_gateway_ip);
		mediaPort = UIHelper.getEditText(editFormView,
				R.id.edit_text_media_port);
		gatewayPort = UIHelper.getEditText(editFormView,
				R.id.edit_text_gateway_port);

		Username = UIHelper.getEditText(editFormView, R.id.edit_text_username);
		Password = UIHelper.getEditText(editFormView, R.id.edit_text_password);
		ToggleButton toggleButton = (ToggleButton) editFormView
				.findViewById(R.id.edit_text_status);
		roomName.setText("dgdf");
		if (ProfileValues.getCount() > 0) {
			while (ProfileValues.moveToNext()) {
				roomName.setText(ProfileValues.getString(ProfileValues
						.getColumnIndex(MilanClimaxDBHelper.COLUMN_PROFILE_ROOM)));
				String[] mediaSetIpPort = ProfileValues
						.getString(
								ProfileValues
										.getColumnIndex(MilanClimaxDBHelper.COLUMN_PROFILE_MEDIA_IP))
						.split(":");
				if (mediaSetIpPort.length != 0) {
					try {
						if(mediaSetIpPort[0].length()!=0)
						mediaIp.setText(mediaSetIpPort[0]);
						if(mediaSetIpPort[1].length()!=0)
						mediaPort.setText(mediaSetIpPort[1]);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				String[] gatewaySetIpPort = ProfileValues
						.getString(
								ProfileValues
										.getColumnIndex(MilanClimaxDBHelper.COLUMN_PROFILE_MEDIA_GATEWY))
						.split(":");
				if (gatewaySetIpPort.length != 0) {
					try {
						if(gatewaySetIpPort[0].length()!=0)
						gatewayIP.setText(gatewaySetIpPort[0]);
						if(gatewaySetIpPort[1].length()!=0)
						gatewayPort.setText(gatewaySetIpPort[1]);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				int status = ProfileValues
						.getInt(ProfileValues
								.getColumnIndex(MilanClimaxDBHelper.COLUMN_PRFOILE_ACTIVATION));
				if (status == 0)
					toggleButton.setChecked(false);
				else
					toggleButton.setChecked(true);
				
				Username.setText(ProfileValues.getString(ProfileValues
						.getColumnIndex(MilanClimaxDBHelper.COLUMN_PROFILE_USERNAME)));
				Password.setText(ProfileValues.getString(ProfileValues
						.getColumnIndex(MilanClimaxDBHelper.COLUMN_PROFILE_PASSWORD)));
			}
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		new LoadSettingsConfig().cancel(true);
		Log.i("Settings Fragment", "Asynchronous Task stopped");
		super.onStop();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		new LoadSettingsConfig().cancel(true);
		Log.i("Settings Fragment", "Asynchronous Task stopped");
		super.onStop();
	}

}
