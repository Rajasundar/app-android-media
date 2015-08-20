package org.milan.climax.asynchronous;

import java.util.List;

import org.milan.climax.R;
import org.milan.climax.db.MilanClimaxDataSource;
import org.milan.climax.lazyloader.AudioLazyAdapter;
import org.milan.climax.model.Audio;
import org.milan.climax.model.Model;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class LoadAudioView extends AsyncTask<String, Void, Void> {

	private android.app.Activity context;
	public View rootView, popUpView;
	private String profile_ip_value;
	public int w;
	private ProgressDialog dialog;
	private Model model;
	private MilanClimaxDataSource milanClimaxDataSource;
	private List<Audio> viewArr;
	private String category;
	private LinearLayout main_linear_layout;
	private RelativeLayout relativeLayout;

	public LoadAudioView(android.app.Activity activity, List<Audio> viewArr,
			View root, String profile_ip_value, int width, String category,
			LinearLayout main_linear_layout, RelativeLayout relativeLayout) {
		this.context = activity;
		this.rootView = root;
		this.w = width;
		this.model = Model.getModelObj();
		this.milanClimaxDataSource = model.getMilanClimaxDataSource();
		this.category = category;
		this.viewArr = viewArr;
		this.profile_ip_value = profile_ip_value;
		this.main_linear_layout = main_linear_layout;
		this.relativeLayout = relativeLayout;
	}

	public LoadAudioView() {

	}

	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub

		return null;
	}

	@Override
	protected void onPostExecute(Void v) {

		dialog.hide();
		dialog.dismiss();
		ListView list = (ListView) rootView.findViewById(R.id.list);
		AudioLazyAdapter adapter = new AudioLazyAdapter(context, w,
				profile_ip_value, viewArr, category, main_linear_layout,
				relativeLayout);
		list.setAdapter(adapter);

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog = new ProgressDialog(context);
		dialog.setMessage("Loading");
		dialog.setCancelable(true);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.show();
	}

}
