package org.milan.climax;

import java.io.File;

import org.milan.climax.R;
import org.milan.climax.asynchronous.LoadAudioView;
import org.milan.climax.asynchronous.LoadInitialConfig;
import org.milan.climax.db.MilanClimaxDataSource;
import org.milan.climax.model.Model;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint({ "NewApi", "ValidFragment", "CommitTransaction", "CommitPrefEdits" })
public class ProfileFragment extends Fragment {

	private SharedPreferences settings;
	private Model model;
	private View rootView;
	private MilanClimaxDataSource milanClimaxDataSource;
	private DrawerLayout mDrawerLayout;
	
	public ProfileFragment(int currentProfile, DrawerLayout mDrawerLayout) {

		model = Model.getModelObj();
		this.milanClimaxDataSource = model.getMilanClimaxDataSource();
		settings = model.getContext().getSharedPreferences("PREF_NAME",
				Context.MODE_PRIVATE);
		this.mDrawerLayout = mDrawerLayout;
		/*
		 * try { trimCache(model.getContext()); } catch (Exception e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		new LoadAudioView().cancel(true);
		Editor editor = settings.edit();
		editor.putInt("Profile_id", currentProfile);
		editor.commit();
		model.setCustomActionBar(currentProfile);
		model.setCurrent_profile_id(currentProfile);
		model.setCurrent_profile_ip_value(milanClimaxDataSource.CurrentProfileIp((long) currentProfile));
		model.setCurrentGatewayIP(milanClimaxDataSource.CurrentGatewayIp((long) currentProfile));
		new LoadInitialConfig(model.getContext(), (long) currentProfile, milanClimaxDataSource, model, model.getCurrent_profile_ip_value(), "initial", "initial").execute();

	}

	public static void trimCache(Context context) {
		try {
			File dir = context.getCacheDir();
			if (dir != null && dir.isDirectory()) {
				deleteDir(dir);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		// rootView =
		// model.getContext().getLayoutInflater().inflate(R.layout.profile_edit,
		// container, false);
		Fragment newFragment = new RecentlyAddedFragment(mDrawerLayout);
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();

		// Replace whatever is in the fragment_container view with this
		// fragment,
		// and add the transaction to the back stack
		transaction.add(R.id.frame_container, newFragment);

		// Commit the transaction
		transaction.commit();
		// Fragment fragment = new SettingsFragment();
		/*
		 * FragmentTransaction transaction = getChildFragmentManager()
		 * .beginTransaction(); transaction .add(R.id.ParentLayoutInProfile,
		 * fragment) .commit();
		 */
		return rootView;
	}
}
