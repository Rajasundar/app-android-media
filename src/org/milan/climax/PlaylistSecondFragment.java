package org.milan.climax;

import org.milan.climax.R;
import org.milan.climax.UIhelper.UIHelper;
import org.milan.climax.asynchronous.LoadPlaylistView;
import org.milan.climax.db.MilanClimaxDataSource;
import org.milan.climax.model.Model;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

@SuppressLint({ "NewApi", "ValidFragment" })
public class PlaylistSecondFragment extends Fragment {
	private ImageView musicFolder, videoFolder, pictureFolder;
	private Model model;
	private android.app.Activity context;

	private MilanClimaxDataSource milanClimaxDataSource;
	public View rootView, genrePopUpView, directorPopUpView, studioPopUpView;
	private SharedPreferences settings;
	private int current_profile_id;
	private int w;
	private TableLayout tblLayoutInMoreMedia;
	private String current_profile_ip, currentFilePath;
	private int categoryMedia;
	private TextView textHeading;
	private DrawerLayout mDrawerLayout;
	private RelativeLayout headerRelative;
	public PlaylistSecondFragment(int categoryMedia, DrawerLayout mDrawerLayout) {

		model = Model.getModelObj();
		this.context = model.getContext();
		this.categoryMedia = categoryMedia;
		this.mDrawerLayout = mDrawerLayout;
		this.milanClimaxDataSource = model.getMilanClimaxDataSource();
		settings = model.getContext().getSharedPreferences("PREF_NAME",
				Context.MODE_PRIVATE);
		current_profile_id = settings.getInt("Profile_id", 0);
		this.current_profile_ip = milanClimaxDataSource
				.CurrentProfileIp((long) current_profile_id);
		Log.i("LOGCAT", Integer.toString(current_profile_id));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.playlistsecond, container,
				false);
		headerRelative = UIHelper.getrRelativeLayout(rootView, R.id.relForHead);
		headerRelative.setOnTouchListener(new RelativeLayoutTouchListener((MainActivity) model.getContext() , model));
		model.openMenusAndRemote(rootView, mDrawerLayout, context);
		tblLayoutInMoreMedia = UIHelper.getTableLayoutView(rootView,
				R.id.tblLayoutInMoreMediaThird);
		textHeading = UIHelper.getTextView(rootView,
				R.id.txtMoreMediaTitleInMoreMedia);
		/* textHeading.setText(currentFilePath); */
		if (categoryMedia == 0)
			textHeading.setText("Music");
		if (categoryMedia == 1)
			textHeading.setText("Video");
		if (categoryMedia == 2)
			textHeading.setText("Picture");
		getDirectoriesView(categoryMedia);
		return rootView;
	}

	public void getDirectoriesView(int category) {
		new LoadPlaylistView(categoryMedia, tblLayoutInMoreMedia,
				current_profile_ip).execute();
	}

}
