package org.milan.climax;

import java.util.List;

import org.milan.climax.R;
import org.milan.climax.UIhelper.UIHelper;
import org.milan.climax.asynchronous.LoadAudioView;
import org.milan.climax.db.MilanClimaxDataSource;
import org.milan.climax.model.Audio;
import org.milan.climax.model.Model;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint({ "NewApi", "ValidFragment" })
public class MusicFragment extends Fragment {

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
	private DrawerLayout mDrawerLayout;
	private RelativeLayout relativeLayout, headerRelative;
	
	public MusicFragment(DrawerLayout mDrawerLayout) {
		model = Model.getModelObj();
		this.context = model.getContext();
		this.milanClimaxDataSource = model.getMilanClimaxDataSource();
		settings = model.getContext().getSharedPreferences("PREF_NAME",
				Context.MODE_PRIVATE);
		this.mDrawerLayout = mDrawerLayout;
		current_profile_id = settings.getInt("Profile_id", 0);
		this.profile_ip_value = milanClimaxDataSource
				.CurrentProfileIp((long) current_profile_id);
		Log.i("LOGCAT", Integer.toString(current_profile_id));
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = context.getLayoutInflater().inflate(R.layout.music_fragment,
				container, false);
		headerRelative = UIHelper.getrRelativeLayout(rootView, R.id.relForHead);
		headerRelative.setOnTouchListener(new RelativeLayoutTouchListener((MainActivity) model.getContext() , model));
		model.openMenusAndRemote(rootView, mDrawerLayout, context);
		
		w = context.getWindowManager().getDefaultDisplay().getWidth();
		AlbumsClick = UIHelper.getTextView(rootView, R.id.albumsInAlbums);
		ArtistsClick = UIHelper.getTextView(rootView, R.id.artistsInAlbums);
		SongsClick = UIHelper.getTextView(rootView, R.id.songInAlbums);
		main_linear_layout = UIHelper.getLinearLayoutView(rootView,
				R.id.linearForAlbums);
		//
		// homePageView();
		relativeLayout = (RelativeLayout) rootView.findViewById(R.id.transparent_background);
		albumsArr = milanClimaxDataSource
				.selectAudioAlbumDataWithId((long) current_profile_id);
		artistArr = milanClimaxDataSource
				.selectAudioArtistDataWithId((long) current_profile_id);
		songArr = milanClimaxDataSource
				.selectAudioSongDataWithId((long) current_profile_id);

		AlbumsClick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MainActivity) model.getContext()).setAlpha(v);
				AlbumsClick.setTextColor(Color.parseColor("#000000"));
				SongsClick.setTextColor(Color.parseColor("#ffffff"));
				ArtistsClick.setTextColor(Color.parseColor("#ffffff"));
				new LoadAudioView(context, albumsArr, rootView,
						profile_ip_value, w, "album", main_linear_layout, relativeLayout)
						.execute();
				//

			}
		});

		SongsClick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MainActivity) model.getContext()).setAlpha(v);
				AlbumsClick.setTextColor(Color.parseColor("#ffffff"));
				ArtistsClick.setTextColor(Color.parseColor("#ffffff"));
				SongsClick.setTextColor(Color.parseColor("#000000"));
				new LoadAudioView(context, songArr, rootView, profile_ip_value,
						w, "song", main_linear_layout, relativeLayout).execute();
			}
		});

		ArtistsClick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MainActivity) model.getContext()).setAlpha(v);
				ArtistsClick.setTextColor(Color.parseColor("#000000"));
				AlbumsClick.setTextColor(Color.parseColor("#ffffff"));
				SongsClick.setTextColor(Color.parseColor("#ffffff"));
				new LoadAudioView(context, artistArr, rootView,
						profile_ip_value, w, "artist", main_linear_layout, relativeLayout)
						.execute();

			}
		});

		new LoadAudioView(context, albumsArr, rootView, profile_ip_value, w,
				"album", main_linear_layout, relativeLayout).execute();
		return rootView;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		Log.i("Music Fragment", "Asynchronous Task stopped");
		super.onStop();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		Log.i("Music Fragment", "Asynchronous Task stopped");
		super.onStop();
	}

}
