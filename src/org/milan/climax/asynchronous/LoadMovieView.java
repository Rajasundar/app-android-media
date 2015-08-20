package org.milan.climax.asynchronous;

import java.util.List;

import org.milan.climax.R;
import org.milan.climax.db.MilanClimaxDataSource;
import org.milan.climax.lazyloader.VideoLazyAdapter;
import org.milan.climax.model.Model;
import org.milan.climax.model.Video;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class LoadMovieView extends AsyncTask<String, Void, Void> {

	private android.app.Activity context;
	public View rootView, popUpView;
	private String profile_ip_value;
	public int w;
	private ProgressDialog dialog;
	private Model model;
	private MilanClimaxDataSource milanClimaxDataSource;
	private List<Video> viewArr;
	private String category;
	private LinearLayout main_linear_layout;
	private RelativeLayout relativeLayout;

	public LoadMovieView(android.app.Activity activity, List<Video> viewArr,
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

	public LoadMovieView() {

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
		VideoLazyAdapter adapter = new VideoLazyAdapter(context, w,
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
