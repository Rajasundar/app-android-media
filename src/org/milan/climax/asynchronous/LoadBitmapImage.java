package org.milan.climax.asynchronous;

import org.milan.climax.R;
import org.milan.climax.model.Model;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.widget.RelativeLayout;

public class LoadBitmapImage extends AsyncTask<String, Void, Void> {

	private android.app.Activity context;
	private RelativeLayout relativeLayout;
	private Model model;
	private String bmp;
	private BitmapDrawable bmp3;
	private Bitmap newBmp, bmp2;

	public LoadBitmapImage(String bmp, Model model,
			RelativeLayout relativeLayout) {
		this.model = Model.getModelObj();
		this.bmp = bmp;
		this.relativeLayout = relativeLayout;

	}

	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		try {
			newBmp = model.getBitmapFromURL(bmp);
			bmp2 = model.transform(newBmp);
			bmp3 = new BitmapDrawable(bmp2);
		} catch (OutOfMemoryError e) {
			// TODO Auto-generated catch block
			relativeLayout.setBackgroundResource(R.drawable.moviebackbg);
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			relativeLayout.setBackgroundResource(R.drawable.moviebackbg);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		try {
			relativeLayout.setBackgroundDrawable(bmp3);
			if (bmp3 == null) {
				relativeLayout.setBackgroundResource(R.drawable.moviebackbg);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();

	}

}
