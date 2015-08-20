package org.milan.climax;

import org.milan.climax.R;
import org.milan.climax.UIhelper.UIHelper;
import org.milan.climax.db.MilanClimaxDataSource;
import org.milan.climax.model.Model;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

@SuppressLint({ "NewApi", "ValidFragment" })
public class PlayListFragment extends Fragment implements OnClickListener {
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
	public PlayListFragment(DrawerLayout mDrawerLayout) {
		model = Model.getModelObj();
		this.context = model.getContext();
		this.mDrawerLayout = mDrawerLayout;
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
		View rootView = inflater.inflate(R.layout.playlist, container, false);
		headerRelative = UIHelper.getrRelativeLayout(rootView, R.id.relForHead);
		headerRelative.setOnTouchListener(new RelativeLayoutTouchListener((MainActivity) model.getContext() , model));
		model.openMenusAndRemote(rootView, mDrawerLayout, context);
		musicFolder = UIHelper.getTableRowView(rootView,
				R.id.imgMusicFolderInMoreMedia);
		videoFolder = UIHelper.getTableRowView(rootView,
				R.id.imgVideoFolderInMoreMedia);
		tblLayoutInMoreMedia = UIHelper.getTableLayoutView(rootView,
				R.id.tblLayoutInMoreMedia);

		musicFolder.setOnClickListener(this);
		videoFolder.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	
			if (v.getId() == R.id.imgMusicFolderInMoreMedia) {
				Fragment newFragment = new PlaylistSecondFragment(0 , mDrawerLayout);
				FragmentTransaction transaction = getFragmentManager().beginTransaction();

				// Replace whatever is in the fragment_container view with this fragment,
				// and add the transaction to the back stack
				transaction.replace(R.id.frame_container, newFragment);
				transaction.addToBackStack(null);

				// Commit the transaction
				transaction.commit();
			}else if (v.getId() == R.id.imgVideoFolderInMoreMedia) {
				Fragment newFragment = new PlaylistSecondFragment(1 , mDrawerLayout);
				FragmentTransaction transaction = getFragmentManager().beginTransaction();

				// Replace whatever is in the fragment_container view with this fragment,
				// and add the transaction to the back stack
				transaction.replace(R.id.frame_container, newFragment);
				transaction.addToBackStack(null);

				// Commit the transaction
				transaction.commit();
			}else if (v.getId() == R.id.imgPictureFolderInMoreMedia) {
				Fragment newFragment = new PlaylistSecondFragment(2 , mDrawerLayout);
				FragmentTransaction transaction = getFragmentManager().beginTransaction();

				// Replace whatever is in the fragment_container view with this fragment,
				// and add the transaction to the back stack
				transaction.replace(R.id.frame_container, newFragment);
				transaction.addToBackStack(null);

				// Commit the transaction
				transaction.commit();
			}
	}
			
			


	
}
