package org.milan.climax;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.milan.climax.R;
import org.milan.climax.UIhelper.UIHelper;
import org.milan.climax.asynchronous.LoadInitialConfig;
import org.milan.climax.db.MilanClimaxDataSource;
import org.milan.climax.model.Model;
import org.milan.climax.slider.NavDrawerItem;
import org.milan.climax.slider.NavDrawerListAdapter;
import org.milan.climax.slider.ProfileDrawerListAdapter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.JsonReader;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.devadvance.circularseekbar.CircularSeekBar;
import com.devadvance.circularseekbar.CircularSeekBar.OnCircularSeekBarChangeListener;
import com.squareup.picasso.Picasso;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnClickListener {

	private final String LOGCAT = "MainActivity";
	private final int totalProfileCount = 20;
	SharedPreferences settings, milanNavSettings;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList, mDrawerListProfile;
	private MilanClimaxDataSource milanClimaxDataSource;
	private CharSequence mTitle;
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private RelativeLayout relLayout, relForRemoteInRemote;
	private PopupWindow nowPlayingPlayer, amplifierWindow;
	private Runnable r1;
	private Handler handler1 = new Handler();
	private int count = 0;
	private int setMaximum = 0;
	private int playerid = 5;
	private String milanNavRoomName;

	private ArrayList<NavDrawerItem> navDrawerItems, navDrawerItemsProfile;
	private NavDrawerListAdapter adapter;
	ProfileDrawerListAdapter adapter1;
	public Model model;
	private ImageView openRemote, closeRemote, backgroundSource,
			pictureImgsource, remoteNext, remotePrevious, remotePlay,
			remotePause, playView, pauseView, muteIcon, ampIcon, stopPlayer,
			shuffleView, repeatOffView, repeatOnView, rightIcon, leftIcon,
			infoIcon, rightClickIcon, keyIcon, keyControlIcon, fullScreenIcon;
	private int w, current_profile_id;
	private SeekBar volumeSeekBar;
	private CircularSeekBar playerSeekBar;
	private TextView contentName, totalSec, seekSec;
	private View popUpView, popUpView1, popUpView2;
	private ArrayList<String> pNameAL, pManuAL, pModelAL, pRoomAL, pStatusAL,
			profiles;
	private ImageView navIcon, upIcon, downIcon, selectIcon, backIcon,
			homeIcon, ccIcon;
	private PopupWindow navigationWindow;
	private LinearLayout musicLinear, videoLinear, pictureLinear, tvLinear;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
		addProfiles();
		//detector = new SimpleGestureFilter(this, this);
		openRemote.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				openRemoteAnimatedLoadImage();
				return true;
			}
		});
		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		mDrawerListProfile = (ListView) findViewById(R.id.list_slidermenu1);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
				findViewById(R.id.list_slidermenu1));
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		// Movies
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		// Music
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));
		// Pictures
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, 2)));
		// Playlist
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, 2)));
		// More Media
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1)));

		// Remote
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons
				.getResourceId(6, -1)));

		// Settings
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons
				.getResourceId(7, 2)));

		int j = 0;
		for (int i = 0; i < profiles.size(); i++) {
			if (i % 5 == 0) {
				j = 0;
			} else
				j++;
			navDrawerItemsProfile.add(new NavDrawerItem(profiles.get(i),
					navMenuIcons.getResourceId(j, 2)));
		}
		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		mDrawerListProfile
				.setOnItemClickListener(new SlideMenuClickListener1());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		adapter1 = new ProfileDrawerListAdapter(getApplicationContext(),
				navDrawerItemsProfile);

		mDrawerList.setAdapter(adapter);
		mDrawerListProfile.setAdapter(adapter1);

		// enabling action bar app icon and behaving it as toggle button

		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setHomeButtonEnabled(false);
		getActionBar().hide();

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}

	private class SlideMenuClickListener1 implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayViewProfile(position + 1);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
		// toggle nav drawer on selecting action bar app icon/title

	}

	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		menu.clear();
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;

		switch (position) {
		case 0:
			fragment = new RecentlyAddedFragment(mDrawerLayout);
			model.setPopUpMessageProfileList(null);
			break;
		case 1:
			model.setPopUpMessageProfileList(null);
			fragment = new MovieFragment(mDrawerLayout);
			break;
		case 2:
			model.setPopUpMessageProfileList(null);
			fragment = new MusicFragment(mDrawerLayout);
			break;
		case 3:
			model.setPopUpMessageProfileList(null);
			fragment = new MoreMediaSecondFragment("pictures", mDrawerLayout);
			break;
		case 4:
			model.setPopUpMessageProfileList(null);
			fragment = new PlayListFragment(mDrawerLayout);
			break;
		case 5:
			model.setPopUpMessageProfileList(null);
			fragment = new MoreMediaFragment(mDrawerLayout);
			break;
		case 6:
			openRemoteAnimatedLoadImage();
			// fragment = new RemoteFragment();
			break;
		case 7:
			model.setPopUpMessageProfileList(null);
			fragment = new SettingsFragment(mDrawerLayout, mDrawerListProfile,
					navMenuIcons, navDrawerItemsProfile);
			break;

		default:
			Log.i("MainMethod", "--->Default Fragment");
			fragment = new RecentlyAddedFragment(mDrawerLayout);
			break;
		}

		if (fragment != null) {
			android.app.FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment)
					.addToBackStack(null).commit();

			// update selected item and title, then close the drawer
			// mDrawerList.setItemChecked(position, true);
			// mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);

		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	private void displayViewProfile(int position) {
		// update the main content by replacing fragments
		Fragment fragment = new ProfileFragment(position, mDrawerLayout);

		if (fragment != null) {
			android.app.FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			// mDrawerListProfile.setItemChecked(position, true);
			// mDrawerListProfile.setSelection(position);
			mDrawerLayout.closeDrawer(mDrawerListProfile);
		}
		// error in creating fragment
	}

	@Override
	public void onBackPressed() {

		if (nowPlayingPlayer != null) {
			/*
			 * Log.i("Pop-up-menu", model.getPopUpMessageProfileList()
			 * .isShowing() + "s");
			 */
			if (nowPlayingPlayer.isShowing()) {

				if (amplifierWindow.isShowing()) {
					amplifierWindow.dismiss();
				} else {
					nowPlayingPlayer.dismiss();
				}
				count = 0;
			} else if (model.getPopUpMessageProfileList() != null) {
				if (model.getPopUpMessageProfileList().isShowing() == true) {
					Log.i("Pop-up-menu", model.getPopUpMessageProfileList()
							.isShowing() + "s");
					model.closePopup(model.getPopUpMessageProfileList());

				} else {
					android.app.FragmentManager fm = getFragmentManager();
					Log.i("MainActivity",
							"popping backstack" + fm.getBackStackEntryCount());
					if (fm.getBackStackEntryCount() > 1) {
						fm.popBackStack();
					} else {
						Log.i("MainActivity",
								"nothing on backstack, calling super");
						finish();
						super.onBackPressed();
					}
				}
			} else {
				android.app.FragmentManager fm = getFragmentManager();
				Log.i("MainActivity",
						"popping backstack" + fm.getBackStackEntryCount());
				if (fm.getBackStackEntryCount() > 1) {
					fm.popBackStack();
				} else {
					Log.i("MainActivity", "nothing on backstack, calling super");
					finish();
					super.onBackPressed();
				}
			}
		} else if (model.getPopUpMessageProfileList() != null) {
			Log.i("Pop-up-menu", model.getPopUpMessageProfileList().isShowing()
					+ "s");
			if (model.getPopUpMessageProfileList().isShowing() == true) {
				Log.i("Pop-up-menu", model.getPopUpMessageProfileList()
						.isShowing() + "s");
				model.closePopup(model.getPopUpMessageProfileList());

			} else {
				android.app.FragmentManager fm = getFragmentManager();
				Log.i("MainActivity",
						"popping backstack" + fm.getBackStackEntryCount());
				if (fm.getBackStackEntryCount() > 1) {
					fm.popBackStack();
				} else {
					Log.i("MainActivity", "nothing on backstack, calling super");
					finish();
					super.onBackPressed();
				}
			}
		} else {
			android.app.FragmentManager fm = getFragmentManager();
			Log.i("MainActivity",
					"popping backstack" + fm.getBackStackEntryCount());
			if (fm.getBackStackEntryCount() > 1) {
				fm.popBackStack();
			} else {
				Log.i("MainActivity", "nothing on backstack, calling super");
				finish();
				super.onBackPressed();
			}
		}

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		// mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		// mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

	@Override
	protected void onStart() {

		super.onStart();
		Log.i(LOGCAT, "Onstart");

	}

	@Override
	protected void onRestart() {

		super.onRestart();
		Log.i(LOGCAT, "OnRestart");
	}

	@Override
	protected void onResume() {

		super.onResume();
		Log.i(LOGCAT, "OnResume");
	}

	@Override
	protected void onStop() {

		super.onStop();
		Log.i(LOGCAT, "OnStop");
	}

	public void init() {

		if (milanClimaxDataSource == null) {
			milanClimaxDataSource = new MilanClimaxDataSource(this);
			milanClimaxDataSource.insertData();
		}

		w = this.getWindowManager().getDefaultDisplay().getWidth();
		BugSenseHandler.initAndStartSession(MainActivity.this, "e59b894a");
		settings = this.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
		try {
			Bundle bundle = getIntent().getExtras();
			if (bundle != null) {
				milanNavRoomName = bundle.getString("Room Name");
				if (milanNavRoomName.isEmpty() != true) {
					current_profile_id = milanClimaxDataSource
							.MilanNavRoomName(milanNavRoomName);
					Log.i("Main", "------------>" + milanNavRoomName);
					Editor editor = settings.edit();
					editor.putInt("Profile_id", current_profile_id);
					editor.commit();
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		current_profile_id = settings.getInt("Profile_id", 0);
		Log.i("Current-Profile", "------------>" + current_profile_id);
		setModel();
		Log.i("Network Existence--->", "----->" + model.isOnline(this));
		if (model.isOnline(this) == true)
			new LoadInitialConfig(this, (long) current_profile_id,
					milanClimaxDataSource, model,
					model.getCurrent_profile_ip_value(), "initial", "initial")
					.execute();
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		getAllViews();
		popUpView1 = this.getLayoutInflater().inflate(R.layout.amplifier, null);
		popUpView2 = this.getLayoutInflater()
				.inflate(R.layout.navigation, null);
		amplifierWindow = new PopupWindow(relLayout, LayoutParams.FILL_PARENT,
				model.pxToDp(250));
		navigationWindow = new PopupWindow(relLayout, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT - model.pxToDp(40));
		//openRemote.setOnClickListener(this);
		mTitle = getTitle();
		// load slide menu items
		addProfiles();

	}

	public void addProfiles() {
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		profiles = new ArrayList<String>();
		navDrawerItemsProfile = new ArrayList<NavDrawerItem>();
		String RoomName = "";
		for (int i = 1; i < totalProfileCount + 1; i++) {
			RoomName = milanClimaxDataSource.CurrentProfileRoomName((long) i);
			Log.i("Room-name", "---------->" + RoomName);
			if (RoomName == null)
				profiles.add("Profile" + i);
			else {
				RoomName = RoomName.substring(0, 1).toUpperCase()
						+ RoomName.substring(1);
				profiles.add(RoomName);
			}

		}
	}

	public void setModel() {

		model.setInflater(getLayoutInflater());
		model.setContext(this);
		model.setProfileCount(totalProfileCount);
		model.setMilanClimax(milanClimaxDataSource);
		model.setCurrent_profile_id(current_profile_id);
		model.setCurrent_profile_ip_value(milanClimaxDataSource
				.CurrentProfileIp((long) current_profile_id));
		model.setCurrentGatewayIP(milanClimaxDataSource
				.CurrentGatewayIp((long) current_profile_id));
		model.setCurrentActivationStatus(milanClimaxDataSource
				.CurrentActivationStatus((long) current_profile_id));
		model.setScreenSize(model.getScreenSizeInInches(this));
		model.setCustomActionBar(current_profile_id);

	}

	public void getAllViews() {
		openRemote = UIHelper.getImageView(
				this.findViewById(android.R.id.content).getRootView(),
				R.id.cntrlBtn);
		popUpView = this.getLayoutInflater().inflate(R.layout.remote, null);
		closeRemote = UIHelper.getImageView(popUpView, R.id.closeInRemote);
		backgroundSource = UIHelper.getImageView(popUpView, R.id.bgsource);
		pictureImgsource = UIHelper.getImageView(popUpView,
				R.id.pictureimgsource);
		relForRemoteInRemote = UIHelper.getrRelativeLayout(popUpView,
				R.id.relForRemoteInRemote);
		playerSeekBar = (CircularSeekBar) popUpView
				.findViewById(R.id.circularSeekBar1);
		remotePrevious = UIHelper.getImageView(popUpView, R.id.prevInRemote);
		volumeSeekBar = UIHelper.getSeekBar(popUpView, R.id.volumeSeekBar);
		remoteNext = UIHelper.getImageView(popUpView, R.id.nxtInRemote);
		remotePlay = UIHelper.getImageView(popUpView, R.id.playInRemote);
		remotePause = UIHelper.getImageView(popUpView, R.id.pauseInRemote);
		playView = UIHelper.getImageView(popUpView, R.id.playInRemote);
		pauseView = UIHelper.getImageView(popUpView, R.id.pauseInRemote);
		stopPlayer = UIHelper.getImageView(popUpView, R.id.stopInRemote);
		contentName = UIHelper.getTextView(popUpView, R.id.playStatusInRemote);
		muteIcon = UIHelper.getImageView(popUpView, R.id.muteVolume);
		ampIcon = UIHelper.getImageView(popUpView, R.id.ampInRemote);
		totalSec = UIHelper.getTextView(popUpView, R.id.total_seconds);
		seekSec = UIHelper.getTextView(popUpView, R.id.seek_seconds);
		shuffleView = UIHelper.getImageView(popUpView, R.id.shuffle);
		repeatOffView = UIHelper.getImageView(popUpView, R.id.repeat_off);
		repeatOnView = UIHelper.getImageView(popUpView, R.id.repeat_on);
		navIcon = UIHelper.getImageView(popUpView, R.id.open_navigation);

	}

	public MainActivity() {
		model = Model.getModelObj();

		// TODO Auto-generated constructor stub
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.cntrlBtn) {
			openRemoteAnimatedLoadImage();

		}
	}

	public void openRemoteAnimatedLoadImage() {
		// if(current_profile_id != 0){
		count++;

		if (count == 1) {
			RelativeLayout main_relative_layout = (RelativeLayout) findViewById(R.id.main_relative_layout);
			relForRemoteInRemote.setOnTouchListener(new RelativeLayoutTouchListener(this));
			double screenSize = model.getScreenSize();
			nowPlayingPlayer = new PopupWindow(relLayout,
					LayoutParams.FILL_PARENT, model.getHeight(this)
							- model.pxToDp(85));
			nowPlayingPlayer.setBackgroundDrawable(new BitmapDrawable());
			nowPlayingPlayer.setContentView(popUpView);
			nowPlayingPlayer.setAnimationStyle(R.style.PopupWindowAnimation);
			nowPlayingPlayer.showAtLocation(main_relative_layout,
					Gravity.NO_GRAVITY, 0, model.pxToDp(62));

			popUpView.setPadding(model.pxToDp(10), 0, model.pxToDp(10), 0);
			popUpView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (amplifierWindow.isShowing()) {
						amplifierWindow.dismiss();
					}
					return true;
				}
			});

			final String profile_ip_value = model.getCurrent_profile_ip_value();
			final android.app.Activity context = this;
			JsonReader initialReader = model
					.getJsonValuesFromServer(
							"http://" + profile_ip_value + "/jsonrpc",
							"{\"id\": \"1\" ,\"jsonrpc\": \"2.0\", \"method\": \"Player.GetActivePlayers\"}");

			if (profile_ip_value != null) {
				if (initialReader != null) {
					r1 = new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								playerSeekBar.setProgress(0);
								JsonReader reader1 = model
										.getJsonValuesFromServer(
												"http://" + profile_ip_value
														+ "/jsonrpc",
												"{\"id\": \"1\" ,\"jsonrpc\": \"2.0\", \"method\": \"Player.GetActivePlayers\"}");

								reader1.beginObject();
								while (reader1.hasNext()) {
									String name = reader1.nextName();

									Log.i(LOGCAT, name);
									if (name.equalsIgnoreCase("result")) {
										reader1.beginArray();
										while (reader1.hasNext()) {
											reader1.beginObject();

											while (reader1.hasNext()) {
												name = reader1.nextName();
												if (name.equalsIgnoreCase("playerid")) {
													playerid = reader1
															.nextInt();
													Log.i("PlayerId",
															"------------->"
																	+ playerid);

												} else {
													reader1.skipValue();
												}
											}
											reader1.endObject();
										}
										reader1.endArray();

									} else {
										reader1.skipValue();
									}

								}
								reader1.endObject();

								JsonReader reader = model
										.getJsonValuesFromServer(
												"http://" + profile_ip_value
														+ "/jsonrpc",
												"{\"jsonrpc\":\"2.0\",\"method\":\"Player.GetItem\",\"id\":\"1\",\"params\":{\"playerid\":"
														+ playerid
														+ ",\"properties\":[\"thumbnail\"]}}");
								Log.i("Showing", "--------->" + count);
								reader.beginObject();
								while (reader.hasNext()) {
									String name = reader.nextName();

									Log.i(LOGCAT, name);
									if (name.equalsIgnoreCase("result")) {
										reader.beginObject();
										while (reader.hasNext()) {
											name = reader.nextName();
											if (name.equalsIgnoreCase("item")) {
												reader.beginObject();
												while (reader.hasNext()) {
													name = reader.nextName();
													Log.i(LOGCAT, name);
													if (name.equalsIgnoreCase("label")) {
														Log.i(LOGCAT, name);
														String label = reader
																.nextString();
														contentName
																.setText(label);

													} else if (name
															.equalsIgnoreCase("thumbnail")) {

														String thumbnail = reader
																.nextString();
														if ((playerid == 0)
																|| (playerid == 1)) {

															backgroundSource
																	.setVisibility(View.VISIBLE);
															pictureImgsource
																	.setVisibility(View.GONE);
															if (thumbnail
																	.equalsIgnoreCase("")) {
																Drawable image = model
																		.getContext()
																		.getResources()
																		.getDrawable(
																				R.drawable.albumart);
																backgroundSource
																		.setImageDrawable(image);
															} else {
																Picasso.with(
																		context)
																		.load("http://"
																				+ profile_ip_value
																				+ "/image/"
																				+ thumbnail
																						.replace(
																								"%",
																								"%25"))
																		.resize(w,
																				w)
																		.centerCrop()
																		.into(backgroundSource);
															}
														} else if (playerid == 2) {

															backgroundSource
																	.setVisibility(View.GONE);
															pictureImgsource
																	.setVisibility(View.VISIBLE);
															if (thumbnail
																	.equalsIgnoreCase("")) {
																Drawable image = model
																		.getContext()
																		.getResources()
																		.getDrawable(
																				R.drawable.albumart);
																backgroundSource
																		.setImageDrawable(image);
															} else {
																Picasso.with(
																		context)
																		.load("http://"
																				+ profile_ip_value
																				+ "/image/"
																				+ thumbnail
																						.replace(
																								"%",
																								"%25"))
																		.resize(w,
																				w)
																		.centerCrop()
																		.into(pictureImgsource);
															}
														}

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

									} else {
										reader.skipValue();
									}

								}

								if ((playerid == 0) || (playerid == 1)) {
									playerSeekBar.setVisibility(View.VISIBLE);
									volumeSeekBar.setVisibility(View.VISIBLE);
									muteIcon.setVisibility(View.VISIBLE);
									ampIcon.setVisibility(View.VISIBLE);
									seekSec.setVisibility(View.VISIBLE);
									totalSec.setVisibility(View.VISIBLE);
									JsonReader reader3 = model
											.getJsonValuesFromServer(
													"http://"
															+ profile_ip_value
															+ "/jsonrpc",
													"{\"jsonrpc\":\"2.0\",\"method\":\"XBMC.GetInfoLabels\",\"id\":\"1\",\"params\":{\"labels\":[\"ListItem.Thumb\",\"Player.Time\",\"Player.Duration\"]}}");

									reader3.beginObject();
									while (reader3.hasNext()) {
										String name = reader3.nextName();

										Log.i(LOGCAT, name);
										if (name.equalsIgnoreCase("result")) {
											reader3.beginObject();
											while (reader3.hasNext()) {

												name = reader3.nextName();
												if (name.equalsIgnoreCase("Player.Duration")) {

													String contentDuration = reader3
															.nextString();
													if (contentDuration
															.equalsIgnoreCase("")) {
														totalSec.setText("");
														contentName
																.setText("Now Playing :");
														Drawable image = getResources()
																.getDrawable(
																		R.drawable.albumart);
														backgroundSource
																.setImageDrawable(image);

													} else {
														String[] s = contentDuration
																.split(":");
														for (int i = 0; i < s.length; i++) {
															if (i == 0)
																setMaximum = (Integer
																		.parseInt(s[i])) * 60;
															else if (i == 1)
																setMaximum = setMaximum
																		+ (Integer
																				.parseInt(s[i]));

														}

														Log.i("Tag",
																"---------->"
																		+ setMaximum);
														playerSeekBar
																.setMax(setMaximum);

														totalSec.setText(contentDuration);
													}

												} else if (name
														.equalsIgnoreCase("Player.Time")) {

													int setProgress = 0;
													String contentDuration = reader3
															.nextString();
													if (contentDuration
															.equalsIgnoreCase("")) {
														seekSec.setText("");
													} else {
														String[] s = contentDuration
																.split(":");

														for (int i = 0; i < s.length; i++) {
															if (i == 0)
																setProgress = (Integer
																		.parseInt(s[i])) * 60;
															else if (i == 1)
																setProgress = setProgress
																		+ (Integer
																				.parseInt(s[i]));

														}
														Log.i("Tag",
																"---------->"
																		+ setProgress);
														playerSeekBar
																.setProgress(setProgress);
														seekSec.setText(contentDuration);
													}
												} else {
													reader3.skipValue();
												}
											}
											reader3.endObject();

										} else {
											reader3.skipValue();
										}

									}
								} else if (playerid == 2) {
									playerSeekBar.setVisibility(View.GONE);
									volumeSeekBar.setVisibility(View.GONE);
									muteIcon.setVisibility(View.GONE);
									ampIcon.setVisibility(View.GONE);
									seekSec.setVisibility(View.GONE);
									totalSec.setVisibility(View.GONE);

								}

								JsonReader reader11 = model
										.getJsonValuesFromServer(
												"http://" + profile_ip_value
														+ "/jsonrpc",
												"{\"jsonrpc\":\"2.0\",\"method\":\"Player.GetProperties\",\"id\":1,\"params\":{\"playerid\":"
														+ playerid
														+ ",\"properties\":[\"speed\" , \"repeat\", \"shuffled\",\"position\"]}}");

								reader11.beginObject();
								while (reader11.hasNext()) {
									String name = reader11.nextName();

									Log.i(LOGCAT, name);
									if (name.equalsIgnoreCase("result")) {
										reader11.beginObject();
										while (reader11.hasNext()) {

											name = reader11.nextName();
											if (name.equalsIgnoreCase("speed")) {

												int speed = reader11.nextInt();
												if (speed == 0) {
													playView.setVisibility(View.VISIBLE);
													pauseView
															.setVisibility(View.GONE);
												} else {
													playView.setVisibility(View.GONE);
													pauseView
															.setVisibility(View.VISIBLE);
												}

											} else if (name
													.equalsIgnoreCase("shuffled")) {

												boolean b = reader11
														.nextBoolean();
												if (b == true)
													shuffleView
															.setImageResource(R.drawable.shuffle_on);
												else
													shuffleView
															.setImageResource(R.drawable.shuffle_off);

											} else if (name
													.equalsIgnoreCase("repeat")) {

												String b = reader11
														.nextString();
												if (b.equalsIgnoreCase("all")) {
													repeatOnView
															.setVisibility(View.VISIBLE);
													repeatOffView
															.setVisibility(View.GONE);
												} else if (b
														.equalsIgnoreCase("one")) {
													repeatOnView
															.setVisibility(View.VISIBLE);
													repeatOffView
															.setVisibility(View.GONE);
												} else if (b
														.equalsIgnoreCase("off")) {
													repeatOnView
															.setVisibility(View.GONE);
													repeatOffView
															.setVisibility(View.VISIBLE);
												}

											} else {
												reader11.skipValue();
											}
										}
										reader11.endObject();

									} else {
										reader11.skipValue();
									}

								}

								JsonReader reader12 = model
										.getJsonValuesFromServer(
												"http://" + profile_ip_value
														+ "/jsonrpc",
												"{\"jsonrpc\":\"2.0\",\"method\":\"Application.GetProperties\",\"id\":\"1\",\"params\":{\"properties\":[\"volume\",\"muted\"]}}");

								reader12.beginObject();
								while (reader12.hasNext()) {
									String name = reader12.nextName();

									Log.i(LOGCAT, name);
									if (name.equalsIgnoreCase("result")) {
										reader12.beginObject();
										while (reader12.hasNext()) {

											name = reader12.nextName();
											if (name.equalsIgnoreCase("volume")) {

												int vol = reader12.nextInt();
												volumeSeekBar.setProgress(vol);

											} else if (name
													.equalsIgnoreCase("muted")) {

												boolean b = reader12
														.nextBoolean();
												if (b == true)
													muteIcon.setImageResource(R.drawable.mute);
												else
													muteIcon.setImageResource(R.drawable.unmute);

											} else {
												reader12.skipValue();
											}
										}
										reader12.endObject();

									} else {
										reader12.skipValue();
									}

								}

								handler1.postDelayed(r1, 500);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};

					handler1.post(r1);

					playerSeekBar
							.setOnSeekBarChangeListener(new OnCircularSeekBarChangeListener() {

								@Override
								public void onStopTrackingTouch(
										CircularSeekBar seekBar) {
									amplifierWindow.dismiss();
									int value = 0;
									Log.i("Main",
											"---->" + seekBar.getProgress());
									if (setMaximum != 0) {
										value = seekBar.getProgress() * 100
												/ setMaximum;
									} else {
										value = 0;
									}
									model.postJsonValuesToServer(
											"http://" + profile_ip_value
													+ "/jsonrpc",
											"{\"jsonrpc\":\"2.0\",\"method\":\"Player.Seek\",\"id\":\"1\",\"params\":{\"playerid\":"
													+ playerid
													+ ", \"value\":"
													+ value + "}}");

								}

								@Override
								public void onStartTrackingTouch(
										CircularSeekBar seekBar) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onProgressChanged(
										CircularSeekBar circularSeekBar,
										int progress, boolean fromUser) {
									// TODO Auto-generated method stub

								}
							});

					volumeSeekBar
							.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

								@Override
								public void onStopTrackingTouch(SeekBar seekBar) {
									// TODO Auto-generated method stub
									amplifierWindow.dismiss();
									int value = seekBar.getProgress();
									model.postJsonValuesToServer(
											"http://" + profile_ip_value
													+ "/jsonrpc",
											"{\"jsonrpc\":\"2.0\",\"method\":\"Application.SetVolume\",\"id\":\"1\",\"params\":{\"volume\":"
													+ value + "}}}");

								}

								@Override
								public void onStartTrackingTouch(SeekBar seekBar) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onProgressChanged(SeekBar seekBar,
										int progress, boolean fromUser) {
									// TODO Auto-generated method stub
									if (fromUser == false) {

									}
								}
							});

					remotePrevious.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							setAlpha(v);
							amplifierWindow.dismiss();
							model.postJsonValuesToServer(
									"http://" + profile_ip_value + "/jsonrpc",
									"{\"jsonrpc\":\"2.0\",\"method\":\"Player.GoTo\",\"id\":1,\"params\":{\"playerid\":"
											+ playerid
											+ ",\"to\":\"previous\"}}");

						}
					});

					muteIcon.setOnTouchListener(new OnTouchListener() {

						@Override
						public boolean onTouch(View v, MotionEvent event) {
							setAlpha(v);
							try {
								amplifierWindow.dismiss();
								JsonReader reader = model
										.getJsonValuesFromServer(
												"http://" + profile_ip_value
														+ "/jsonrpc",
												"{\"jsonrpc\":\"2.0\",\"method\":\"Application.SetMute\",\"id\":1,\"params\":{\"mute\":\"toggle\"}}");
								reader.beginObject();
								while (reader.hasNext()) {
									String name = reader.nextName();

									Log.i(LOGCAT, name);
									if (name.equalsIgnoreCase("result")) {
										boolean b = reader.nextBoolean();
										if (b == true)
											muteIcon.setImageResource(R.drawable.mute);
										else
											muteIcon.setImageResource(R.drawable.unmute);
									} else {
										reader.skipValue();
									}

								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return false;
						}
					});

					remoteNext.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							setAlpha(v);
							amplifierWindow.dismiss();
							model.postJsonValuesToServer(
									"http://" + profile_ip_value + "/jsonrpc",
									"{\"jsonrpc\":\"2.0\",\"method\":\"Player.GoTo\",\"id\":1,\"params\":{\"playerid\":"
											+ playerid + ",\"to\":\"next\"}}");

						}
					});

					remotePlay.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							setAlpha(v);
							amplifierWindow.dismiss();
							try {

								HttpResponse httpResponse = model
										.postJsonValuesToServer(
												"http://" + profile_ip_value
														+ "/jsonrpc",
												"{\"jsonrpc\":\"2.0\",\"method\":\"Player.PlayPause\",\"id\":\"1\",\"params\":{\"playerid\":"
														+ playerid
														+ ",\"play\":\"toggle\"}}");
								if (model.getCurrentActivationStatus() == 1){
									Log.i("url" , "http://"
													+ model.getCurrentGatewayIP()
													+ "/Milan/Drivers/Play_Pause/Play.py");
									model.getXMLFromService(
											"http://"
													+ model.getCurrentGatewayIP()
													+ "/Milan/Drivers/Play_Pause/Play.py");
								}
								if (httpResponse != null) {
									playView.setVisibility(View.GONE);
									pauseView.setVisibility(View.VISIBLE);
								} else {
									Toast.makeText(context,
											"No Response from Milan Media",
											1000).show();
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});

					remotePause.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							setAlpha(v);
							amplifierWindow.dismiss();
							try {

								HttpResponse httpResponse = model
										.postJsonValuesToServer(
												"http://" + profile_ip_value
														+ "/jsonrpc",
												"{\"jsonrpc\":\"2.0\",\"method\":\"Player.PlayPause\",\"id\":\"1\",\"params\":{\"playerid\":"
														+ playerid
														+ ",\"play\":\"toggle\"}}");
								Log.i("Main",
										"http://"
												+ model.getCurrentGatewayIP()
												+ "/Milan/Drivers/Play_Pause/Pause.py");
								if (model.getCurrentActivationStatus() == 1){
									model.getXMLFromService(
											"http://"
													+ model.getCurrentGatewayIP()
													+ "/Milan/Drivers/Play_Pause/Pause.py");
									Log.i("url" , "http://"
											+ model.getCurrentGatewayIP()
											+ "/Milan/Drivers/Play_Pause/Play.py");}
								if (httpResponse != null) {
									playView.setVisibility(View.VISIBLE);
									pauseView.setVisibility(View.GONE);
								} else {
									Toast.makeText(context,
											"No Response from Milan Media",
											1000).show();
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});

					shuffleView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							setAlpha(v);
							model.postJsonValuesToServer(
									"http://" + profile_ip_value + "/jsonrpc",
									"{ \"jsonrpc\" : \"2.0\", \"method\" : \"Player.SetShuffle\",\"params\" : {\"playerid\":0,\"shuffle\":\"toggle\"}, \"id\" : \"2\" }");

						}
					});

					repeatOnView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							setAlpha(v);
							model.postJsonValuesToServer(
									"http://" + profile_ip_value + "/jsonrpc",
									"{ \"jsonrpc\" : \"2.0\", \"method\" : \"Player.SetRepeat\",\"params\" : { \"playerid\":0,\"repeat\":\"off\"}, \"id\" : \"2\" }");
						}
					});

					repeatOffView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							setAlpha(v);
							model.postJsonValuesToServer(
									"http://" + profile_ip_value + "/jsonrpc",
									"{ \"jsonrpc\" : \"2.0\", \"method\" : \"Player.SetRepeat\",\"params\" : { \"playerid\":0,\"repeat\":\"cycle\"}, \"id\" : \"2\" }");

						}
					});

					stopPlayer.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							setAlpha(v);
							amplifierWindow.dismiss();
							try {

								HttpResponse httpResponse = model
										.postJsonValuesToServer(
												"http://" + profile_ip_value
														+ "/jsonrpc",
												"{\"jsonrpc\":\"2.0\",\"method\":\"Player.Stop\",\"id\":1,\"params\":{\"playerid\":"
														+ playerid + "}}");
								if (httpResponse != null) {
									playerSeekBar.setProgress(0);
									playView.setVisibility(View.VISIBLE);
									pauseView
											.setVisibility(View.GONE);
								} else {
									Toast.makeText(context,
											"No Response from Milan Media",
											1000).show();
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});

				} else {
					playerSeekBar.setProgress(0);
				}

				closeRemote.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						amplifierWindow.dismiss();
						navigationWindow.dismiss();
						setAlpha(v);
						if (nowPlayingPlayer != null) {
							if (nowPlayingPlayer.isShowing()) {
								Log.i("Showing", "--------->" + count);
								handler1.removeCallbacks(r1);
								nowPlayingPlayer.dismiss();
								count = 0;
							} else {

							}
						} else {

						}
					}
				});

				ampIcon.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						amplifierWindow.dismiss();
						getVolume();
						openAmpPopup();
						setAlpha(v);

					}
				});
				navIcon.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						setAlpha(v);
						RelativeLayout main_relative_layout = (RelativeLayout) findViewById(R.id.main_relative_layout);
						upIcon = UIHelper
								.getImageView(popUpView2, R.id.up_icon);
						downIcon = UIHelper.getImageView(popUpView2,
								R.id.down_icon);
						musicLinear = UIHelper.getLinearLayoutView(popUpView2,
								R.id.music_linear);
						videoLinear = UIHelper.getLinearLayoutView(popUpView2,
								R.id.video_linear);
						pictureLinear = UIHelper.getLinearLayoutView(
								popUpView2, R.id.image_linear);
						tvLinear = UIHelper.getLinearLayoutView(popUpView2,
								R.id.tv_linear);
						rightIcon = UIHelper.getImageView(popUpView2,
								R.id.right_icon);
						leftIcon = UIHelper.getImageView(popUpView2,
								R.id.left_icon);
						infoIcon = UIHelper.getImageView(popUpView2,
								R.id.info_icon);
						rightClickIcon = UIHelper.getImageView(popUpView2,
								R.id.right_click_icon);
						keyIcon = UIHelper.getImageView(popUpView2,
								R.id.key_icon);
						keyControlIcon = UIHelper.getImageView(popUpView2,
								R.id.control_icon);
						fullScreenIcon = UIHelper.getImageView(popUpView2,
								R.id.full_screen_icon);
						selectIcon = UIHelper.getImageView(popUpView2,
								R.id.select_icon);
						backIcon = UIHelper.getImageView(popUpView2,
								R.id.back_icon);
						homeIcon = UIHelper.getImageView(popUpView2,
								R.id.home_icon);
						ccIcon = UIHelper.getImageView(popUpView2,
								R.id.closed_captions);

						upIcon.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								setAlpha(v);
								HttpResponse httpResponse = model
										.postJsonValuesToServer(
												"http://" + profile_ip_value
														+ "/jsonrpc",
												"{\"jsonrpc\": \"2.0\", \"method\": \"Input.Up\", \"id\": 1}");
								if (httpResponse != null) {

								} else {
								}

							}
						});
						downIcon.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								setAlpha(v);
								HttpResponse httpResponse = model
										.postJsonValuesToServer(
												"http://" + profile_ip_value
														+ "/jsonrpc",
												"{\"jsonrpc\": \"2.0\", \"method\": \"Input.Down\", \"id\": 1}");
								if (httpResponse != null) {

								} else {
								}

							}
						});
						musicLinear.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								setAlpha(v);
								HttpResponse httpResponse = model
										.postJsonValuesToServer(
												"http://" + profile_ip_value
														+ "/jsonrpc",
												"{\"jsonrpc\": \"2.0\", \"method\": \"GUI.ActivateWindow\", \"params\": { \"window\": \"music\" }, \"id\": 1 }");

								if (httpResponse != null) {

								} else {
								}

							}
						});
						videoLinear.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								setAlpha(v);
								HttpResponse httpResponse = model
										.postJsonValuesToServer(
												"http://" + profile_ip_value
														+ "/jsonrpc",
												"{\"jsonrpc\": \"2.0\", \"method\": \"GUI.ActivateWindow\", \"params\": { \"window\": \"video\" }, \"id\": 1 }");
								if (httpResponse != null) {

								} else {
								}

							}
						});
						pictureLinear.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								setAlpha(v);
								HttpResponse httpResponse = model
										.postJsonValuesToServer(
												"http://" + profile_ip_value
														+ "/jsonrpc",
												"{\"jsonrpc\": \"2.0\", \"method\": \"GUI.ActivateWindow\", \"params\": { \"window\": \"pictures\" }, \"id\": 1 }");
								if (httpResponse != null) {

								} else {
								}

							}
						});
						tvLinear.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								setAlpha(v);
								HttpResponse httpResponse = model
										.postJsonValuesToServer(
												"http://" + profile_ip_value
														+ "/jsonrpc",
												"{\"jsonrpc\": \"2.0\", \"method\": \"GUI.ActivateWindow\", \"params\": { \"window\": \"tv\" }, \"id\": 1 }");
								if (httpResponse != null) {

								} else {
								}

							}
						});
						rightIcon.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								setAlpha(v);
								HttpResponse httpResponse = model
										.postJsonValuesToServer(
												"http://" + profile_ip_value
														+ "/jsonrpc",
												"{\"jsonrpc\": \"2.0\", \"method\": \"Input.Right\", \"id\": 1}");
								if (httpResponse != null) {

								} else {
								}

							}
						});
						leftIcon.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								setAlpha(v);
								HttpResponse httpResponse = model
										.postJsonValuesToServer(
												"http://" + profile_ip_value
														+ "/jsonrpc",
												"{\"jsonrpc\": \"2.0\", \"method\": \"Input.Left\", \"id\": 1}");
								if (httpResponse != null) {

								} else {
								}

							}
						});
						infoIcon.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								setAlpha(v);
								HttpResponse httpResponse = model
										.postJsonValuesToServer(
												"http://" + profile_ip_value
														+ "/jsonrpc",
												"{\"jsonrpc\": \"2.0\", \"method\": \"Input.Info\", \"id\": 1}");
								if (httpResponse != null) {

								} else {
								}

							}
						});
						rightClickIcon
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										setAlpha(v);
										HttpResponse httpResponse = model
												.postJsonValuesToServer(
														"http://"
																+ profile_ip_value
																+ "/jsonrpc",
														"{\"jsonrpc\": \"2.0\", \"method\": \"Input.Right\", \"id\": 1}");
										if (httpResponse != null) {

										} else {
										}

									}
								});
						fullScreenIcon
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										setAlpha(v);
										HttpResponse httpResponse = model
												.postJsonValuesToServer(
														"http://"
																+ profile_ip_value
																+ "/jsonrpc",
														"{\"jsonrpc\": \"2.0\", \"method\": \"GUI.SetFullscreen\", \"params\": {\"fullscreen\": \"toggle\"}, \"id\": \"1\"}");
										if (httpResponse != null) {

										} else {
										}

									}
								});
						keyControlIcon
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										setAlpha(v);
										HttpResponse httpResponse = model
												.postJsonValuesToServer(
														"http://"
																+ profile_ip_value
																+ "/jsonrpc",
														"{\"jsonrpc\": \"2.0\", \"method\": \"Input.ShowOSD\", \"id\": 1}");
										if (httpResponse != null) {

										} else {
										}

									}
								});
						keyIcon.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								setAlpha(v);
								HttpResponse httpResponse = model
										.postJsonValuesToServer(
												"http://" + profile_ip_value
														+ "/jsonrpc",
												"{\"jsonrpc\": \"2.0\", \"method\": \"Input.SendText\", \"id\": 1}");
								if (httpResponse != null) {

								} else {
								}

							}
						});
						selectIcon.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								setAlpha(v);
								HttpResponse httpResponse = model
										.postJsonValuesToServer(
												"http://" + profile_ip_value
														+ "/jsonrpc",
												"{\"jsonrpc\": \"2.0\", \"method\": \"Input.Select\", \"id\": 1}");
								if (httpResponse != null) {

								} else {
								}

							}
						});
						backIcon.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								setAlpha(v);
								HttpResponse httpResponse = model
										.postJsonValuesToServer(
												"http://" + profile_ip_value
														+ "/jsonrpc",
												"{\"jsonrpc\": \"2.0\", \"method\": \"Input.Back\", \"id\": 1}");
								if (httpResponse != null) {

								} else {
								}

							}
						});
						homeIcon.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								setAlpha(v);
								HttpResponse httpResponse = model
										.postJsonValuesToServer(
												"http://" + profile_ip_value
														+ "/jsonrpc",
												"{\"jsonrpc\": \"2.0\", \"method\": \"Input.Home\", \"id\": 1}");
								if (httpResponse != null) {

								} else {
								}

							}
						});
						ccIcon.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								setAlpha(v);
								final List<String> list2 = Arrays.asList("On",
										"Off");
								CharSequence[] cs2 = list2
										.toArray(new CharSequence[list2.size()]);
								AlertDialog.Builder builder2 = new AlertDialog.Builder(
										model.getContext(),
										AlertDialog.THEME_DEVICE_DEFAULT_DARK);
								builder2.setTitle("Subtitle").setItems(cs2,
										new DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface dialog,
													int which) {
												HttpResponse httpResponse = null;
												if (list2.get(which)
														.equalsIgnoreCase("On")) {
													httpResponse = model
															.postJsonValuesToServer(
																	"http://"
																			+ profile_ip_value
																			+ "/jsonrpc",
																	"{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"Player.SetSubtitle\",\"params\":{\"playerid\":1,\"subtitle\":\"on\"}}");
												} else if (list2
														.get(which)
														.equalsIgnoreCase("Off")) {
													httpResponse = model
															.postJsonValuesToServer(
																	"http://"
																			+ profile_ip_value
																			+ "/jsonrpc",
																	"{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"Player.SetSubtitle\",\"params\":{\"playerid\":1,\"subtitle\":\"off\"}}");
												}

											}
										});
								builder2.create().show();

							}
						});
						popUpView2.setPadding(model.pxToDp(10), 0,
								model.pxToDp(5), 0);
						ImageView closeAmpPop = UIHelper.getImageView(
								popUpView2, R.id.clos_amp_pop);
						closeAmpPop.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {
								// TODO Auto-generated method stub
								navigationWindow.dismiss();
								return false;
							}
						});

						double screenSize = model.getScreenSize();

						navigationWindow.setContentView(popUpView2);
						navigationWindow
								.setAnimationStyle(R.style.PopupWindowAnimation);
						if (navigationWindow.isShowing()) {
							navigationWindow.dismiss();
						} else {
							navigationWindow.showAtLocation(
									main_relative_layout, Gravity.NO_GRAVITY,
									model.pxToDp(50), model.pxToDp(60));
						}
					}
				});
			}
			mDrawerLayout.closeDrawer(Gravity.LEFT);

		} else {
			Log.i("Showing", "--------->" + count);
		}
		// }
	}

	public void getVolume() {
		try {
			Log.i("myLog", "getVolume");
			pNameAL = new ArrayList<String>();
			pManuAL = new ArrayList<String>();
			pModelAL = new ArrayList<String>();
			pRoomAL = new ArrayList<String>();
			pStatusAL = new ArrayList<String>();
			String urlStr = null;
			Document doc = null;

			urlStr = ("http://" + model.getCurrentGatewayIP() + "/Infomap/Service/common.php?name=volume");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream is = model.getXMLFromService(urlStr);
			doc = db.parse(is);
			doc.getDocumentElement().normalize();
			NodeList parentNode = doc.getElementsByTagName("Device");
			Log.i("mylog", "parent count--->" + parentNode.getLength());
			for (int index = 0; index < parentNode.getLength(); index++) {
				Node item = parentNode.item(index);
				NodeList childNode = item.getChildNodes();
				Log.i("mylog", "child node--->" + childNode.getLength());

				for (int child = 0; child < childNode.getLength(); child++) {
					Node childItem = childNode.item(child);
					String nodeName = childItem.getNodeName();

					if (nodeName.equalsIgnoreCase("Name")) {
						Node node = childItem.getFirstChild();
						if (node != null) {
							String namePop = childItem.getFirstChild()
									.getNodeValue();
							pNameAL.add(namePop);
						} else {
							pNameAL.add("");
						}
					} else if (nodeName.equalsIgnoreCase("Manufacturer")) {
						Node node = childItem.getFirstChild();
						if (node != null) {
							String manuPop = childItem.getFirstChild()
									.getNodeValue();
							pManuAL.add(manuPop);
						} else {
							pManuAL.add("");
						}
					} else if (nodeName.equalsIgnoreCase("Model")) {
						Node node = childItem.getFirstChild();
						if (node != null) {
							String modelPop = childItem.getFirstChild()
									.getNodeValue();
							pModelAL.add(modelPop);
						} else {
							pModelAL.add("");
						}
					} else if (nodeName.equalsIgnoreCase("Room")) {
						Node node = childItem.getFirstChild();
						if (node != null) {
							String roomPop = childItem.getFirstChild()
									.getNodeValue();
							pRoomAL.add(roomPop);
						} else {
							pRoomAL.add("");
						}
					} else if (nodeName.equalsIgnoreCase("Status")) {
						Node node = childItem.getFirstChild();
						if (node != null) {
							String statusPop = childItem.getFirstChild()
									.getNodeValue();
							pStatusAL.add(statusPop);
						} else {
							pStatusAL.add("");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void openAmpPopup() {
		RelativeLayout main_relative_layout = (RelativeLayout) findViewById(R.id.main_relative_layout);

		popUpView1.setPadding(model.pxToDp(10), 0, model.pxToDp(5), 0);
		TableLayout tableLayout = (TableLayout) popUpView1
				.findViewById(R.id.tlTable01);
		ImageView closeAmpPop = UIHelper.getImageView(popUpView1,
				R.id.clos_amp_pop);
		closeAmpPop.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				amplifierWindow.dismiss();
				return false;
			}
		});
		tableLayout.removeAllViews();
		tableLayout.setStretchAllColumns(true);
		if (pRoomAL.size() > 0) {
			for (int i = 0; i < pRoomAL.size(); i++) {
				final int j = i;
				TableRow tableRow1 = new TableRow(this);
				tableRow1.setPadding(0, 0, model.pxToDp(1), model.pxToDp(1));
				TableRow tableRow = new TableRow(this);
				tableRow.setPadding(model.pxToDp(1), 0, model.pxToDp(1),
						model.pxToDp(1));
				TextView textView = new TextView(this);
				TableRow.LayoutParams textParams1 = new TableRow.LayoutParams(
						TableRow.LayoutParams.FILL_PARENT,
						TableRow.LayoutParams.WRAP_CONTENT);
				textParams1.span = 5;
				textParams1.setMargins(0, 0, 0, model.pxToDp(10));
				TableRow.LayoutParams textParams2 = new TableRow.LayoutParams(
						TableRow.LayoutParams.FILL_PARENT,
						TableRow.LayoutParams.WRAP_CONTENT);
				textParams2.span = 5;
				textParams2.setMargins(0, 0, 0, model.pxToDp(10));
				TableRow.LayoutParams textParams = new TableRow.LayoutParams(0,
						model.pxToDp(60));
				textParams.setMargins(model.pxToDp(1), 0, 0, model.pxToDp(10));
				textView.setGravity(Gravity.CENTER_VERTICAL);
				textView.setTextColor(Color.parseColor("#ffffff"));
				textView.setPadding(model.pxToDp(5), model.pxToDp(5),
						model.pxToDp(5), model.pxToDp(5));
				textView.setText(pRoomAL.get(i));
				textView.setTypeface(null, Typeface.BOLD);
				TableRow tableRow2 = new TableRow(this);
				tableRow2.setPadding(0, 0, model.pxToDp(1), model.pxToDp(1));
				TextView textView4 = new TextView(this);
				textView4.setText(pManuAL.get(i) + " - " + pModelAL.get(i));
				textView4.setTextColor(Color.parseColor("#ffffff"));
				ImageView onButton = new ImageView(this);
				onButton.setImageResource(R.drawable.volon);
				onButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (pNameAL.get(j).equalsIgnoreCase("TV"))
							amplifierVolume(pNameAL.get(j), pRoomAL.get(j),
									pModelAL.get(j), "On_cmd", pManuAL.get(j));
						else
							amplifierVolume(pNameAL.get(j), pRoomAL.get(j),
									pModelAL.get(j), "cmd_on", pManuAL.get(j));
					}
				});
				ImageView offButton = new ImageView(this);
				offButton.setImageResource(R.drawable.voloff);
				offButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (pNameAL.get(j).equalsIgnoreCase("TV"))
							amplifierVolume(pNameAL.get(j), pRoomAL.get(j),
									pModelAL.get(j), "OFF", pManuAL.get(j));
						else
							amplifierVolume(pNameAL.get(j), pRoomAL.get(j),
									pModelAL.get(j), "off", pManuAL.get(j));
					}
				});
				ImageView imageView = new ImageView(this);
				imageView.setPadding(model.pxToDp(5), model.pxToDp(5),
						model.pxToDp(5), model.pxToDp(5));
				imageView.setImageResource(R.drawable.volmute);

				ImageView imageView1 = new ImageView(this);
				imageView1.setPadding(model.pxToDp(5), model.pxToDp(5),
						model.pxToDp(5), model.pxToDp(5));
				imageView1.setImageResource(R.drawable.volminus);

				ImageView imageView2 = new ImageView(this);
				imageView2.setPadding(model.pxToDp(5), model.pxToDp(5),
						model.pxToDp(5), model.pxToDp(5));
				imageView2.setImageResource(R.drawable.volplus);
				if (pStatusAL.get(i).equalsIgnoreCase("Enabled")) {
					imageView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							amplifierVolume(pNameAL.get(j), pRoomAL.get(j),
									pModelAL.get(j), "Mute", pManuAL.get(j));
						}
					});
					imageView1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							amplifierVolume(pNameAL.get(j), pRoomAL.get(j),
									pModelAL.get(j), "Vol_down", pManuAL.get(j));
						}
					});
					imageView2.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							amplifierVolume(pNameAL.get(j), pRoomAL.get(j),
									pModelAL.get(j), "Vol_up", pManuAL.get(j));
						}
					});
				} else {
				}
				textParams1.setMargins(model.pxToDp(5), 0, 0, 0);
				textParams2.setMargins(model.pxToDp(10), 0, 0, 0);
				tableRow1.addView(textView, textParams1);
				tableRow2.addView(textView4, textParams2);
				tableRow.addView(onButton, textParams);
				tableRow.addView(offButton, textParams);
				tableRow.addView(imageView, textParams);
				tableRow.addView(imageView1, textParams);
				tableRow.addView(imageView2, textParams);
				tableLayout.addView(tableRow1);
				tableLayout.addView(tableRow2);
				tableLayout.addView(tableRow);

			}
			double screenSize = model.getScreenSize();

			amplifierWindow.setContentView(popUpView1);
			amplifierWindow.setAnimationStyle(R.style.PopupWindowAnimation);
			if (amplifierWindow.isShowing()) {
				amplifierWindow.dismiss();
			} else {
				amplifierWindow.showAtLocation(main_relative_layout,
						Gravity.NO_GRAVITY, 0,
						model.getHeight(this) - model.pxToDp(305));
			}
		} else {

			Toast.makeText(this, "Connection Unavailable", 1000).show();

		}

	}

	public void setAlpha(final View v) {
		v.setAlpha(0.5f);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				v.setAlpha(1f);

			}
		}, 500);
	}

	public void amplifierVolume(final String option, final String room,
			final String model1, final String cmd, final String manufacturer) {
		final String gatewayIP = model.getCurrentGatewayIP();
		new Thread(new Runnable() {
			public void run() {
				try {
					Log.i("myLog", "Amp/Tv src-->" + cmd);
					String url = String.format("http://" + gatewayIP
							+ "/Infomap/IP2IR_Driver/IR.php?Room=%s&dev="
							+ option + "&Model=%s&cmd=%s&Manu=%s",
							Uri.encode(room), Uri.encode(model1),
							Uri.encode(cmd), Uri.encode(manufacturer));
					Log.i("myLog", "Amp/Tv link-->" + url);
					HttpClient httpclient = new DefaultHttpClient();
					httpclient.execute(new HttpGet(url));
				} catch (Exception e) {
					Log.i("My log", "Network exception");
				}
			}
		}).start();
	}

	private class RelativeLayoutTouchListener implements OnTouchListener {

	    static final String logTag = "ActivitySwipeDetector";
	    private Activity activity;
	    static final int MIN_DISTANCE = 100;// TODO change this runtime based on screen resolution. for 1920x1080 is to small the 100 distance
	    private float downX, downY, upX, upY;

	    // private MainActivity mMainActivity;

	    public RelativeLayoutTouchListener(MainActivity mainActivity) {
	        activity = mainActivity;
	    }

	    public void onRightToLeftSwipe() {
	    }

	    public void onLeftToRightSwipe() {
	    }

	    public void onTopToBottomSwipe() {
	    }

	    public void onBottomToTopSwipe() {
	    	amplifierWindow.dismiss();
			navigationWindow.dismiss();
			if (nowPlayingPlayer != null) {
				if (nowPlayingPlayer.isShowing()) {
					Log.i("Showing", "--------->" + count);
					handler1.removeCallbacks(r1);
					nowPlayingPlayer.dismiss();
					count = 0;
				} else {

				}
			} else {

			}
	    }

	    public boolean onTouch(View v, MotionEvent event) {
	        switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN: {
	            downX = event.getX();
	            downY = event.getY();
	            return true;
	        }
	        case MotionEvent.ACTION_UP: {
	            upX = event.getX();
	            upY = event.getY();

	            float deltaX = downX - upX;
	            float deltaY = downY - upY;

	            // swipe horizontal?
	            if (Math.abs(deltaX) > MIN_DISTANCE) {
	                // left or right
	                if (deltaX < 0) {
	                    this.onLeftToRightSwipe();
	                    return true;
	                }
	                if (deltaX > 0) {
	                    this.onRightToLeftSwipe();
	                    return true;
	                }
	            } else {
	                Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long horizontally, need at least " + MIN_DISTANCE);
	                // return false; // We don't consume the event
	            }

	            // swipe vertical?
	            if (Math.abs(deltaY) > MIN_DISTANCE) {
	                // top or down
	                if (deltaY < 0) {
	                    this.onTopToBottomSwipe();
	                    return true;
	                }
	                if (deltaY > 0) {
	                    this.onBottomToTopSwipe();
	                	return true;
	                }
	            } else {
	                Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long vertically, need at least " + MIN_DISTANCE);
	                // return false; // We don't consume the event
	            }

	            return false; // no swipe horizontally and no swipe vertically
	        }// case MotionEvent.ACTION_UP:
	        }
	        return false;
	    }

	}
}
