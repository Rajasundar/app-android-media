package org.milan.climax.asynchronous;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.milan.climax.R;
import org.milan.climax.MainActivity;
import org.milan.climax.UIhelper.UIHelper;
import org.milan.climax.db.MilanClimaxDataSource;
import org.milan.climax.lazyloader.RecentlyAddedAudioLazyAdapter;
import org.milan.climax.lazyloader.RecentlyAddedVideoLazyAdapter;
import org.milan.climax.model.Audio;
import org.milan.climax.model.Model;
import org.milan.climax.model.Video;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.meetme.android.horizontallistview.HorizontalListView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class LoadRecentlyAddedView extends AsyncTask<String, Void, Void>
		implements Transformation {

	private android.app.Activity context;
	public View rootView, popUpView, popUpView1;
	private String profile_ip_value;
	public int w;
	private ProgressDialog dialog;
	private Model model;
	private MilanClimaxDataSource milanClimaxDataSource;
	private List<Audio> audioArr;
	private List<Video> videoArr;
	private String category;
	private LinearLayout main_linear_layout;
	private TableLayout tableLayout;
	private TextView playAlbumDetail, playMovieDetail;
	private ImageView imageAlbumDetail, addPlaylist;;
	private RelativeLayout relativeLayout;

	public LoadRecentlyAddedView(android.app.Activity activity,
			List<Audio> viewArr, List<Video> videoArr, View root,
			String profile_ip_value, int width, String category,
			final LinearLayout main_linear_layout, final RelativeLayout relativeLayout) {
		this.context = activity;
		this.rootView = root;
		this.w = width;
		this.model = Model.getModelObj();
		this.milanClimaxDataSource = model.getMilanClimaxDataSource();
		this.category = category;
		this.audioArr = viewArr;
		this.videoArr = videoArr;
		this.profile_ip_value = profile_ip_value;
		this.main_linear_layout = main_linear_layout;
		this.relativeLayout = relativeLayout;
		popUpView = context.getLayoutInflater().inflate(
				R.layout.playsongdetail, null);
		popUpView1 = context.getLayoutInflater().inflate(
				R.layout.playmoviedetail, null);
		
		playAlbumDetail = UIHelper.getTextView(popUpView,
				R.id.btnTitleInPlaySongDetail);

		imageAlbumDetail = UIHelper.getImageView(popUpView,
				R.id.imgAlbumDetInPlayDetail);

		addPlaylist = UIHelper.getImageView(popUpView,
				R.id.imgAddPlaylistInSongDetInPlaySongDetail);

		tableLayout = UIHelper.getTableLayoutView(popUpView,
				R.id.tblLayoutInPlaySongDetail);

		playMovieDetail = UIHelper.getTextView(popUpView1,
				R.id.btnTitleInPlayMovieDetail);
		
		model.setPopUpMessageProfileList(new PopupWindow(main_linear_layout,
				LayoutParams.WRAP_CONTENT, model.getHeight(context)-model.pxToDp(100)));
		relativeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setVisibility(View.INVISIBLE);
				try {
					model.getPopUpMessageProfileList().dismiss();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					model.setPopUpMessageProfileList(new PopupWindow(main_linear_layout,
							LayoutParams.WRAP_CONTENT, model.getHeight(context)-model.pxToDp(100)));
					e.printStackTrace();
				}
			}
		});

	}

	public LoadRecentlyAddedView() {

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
		HorizontalListView listMovies = (HorizontalListView) rootView
				.findViewById(R.id.list_movies);
		HorizontalListView listAlbum = (HorizontalListView) rootView
				.findViewById(R.id.list_albums);

		listAlbum.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				Audio audioValues = audioArr.get(position);
				final String firstLabel = audioValues.getLabel();
				final String firstThumbnail = audioValues.getThumbnail();
				final int songID1 = audioValues.getSong_id();
				final int albumID1 = audioValues.getAlbum_id();
				final int artistID1 = audioValues.getArtist_id();
				openAlbumPopUp(firstLabel, firstThumbnail, albumID1);

			}
		});

		listMovies.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Video videoValues = videoArr.get(position);
				final String firstLabel = videoValues.getLabel();
				final String firstThumbnail = videoValues.getThumbnail();
				final String firstDirectorName = videoValues.getDirector_name();
				final String firstDescription = videoValues.getDescription();
				final String firstFanArt = videoValues.getFanArt();
				final String firstGenre = videoValues.getGenreName();
				final String firstRating = videoValues.getRating();
				final String firstTagline = videoValues.getTagline();
				final String firstyear = videoValues.getYear();
				final int firstDuration = videoValues.getDuration();
				final int movieID1 = videoValues.getMovie_id();
				openMoviePopUp(firstLabel, firstThumbnail, firstDirectorName,
						firstDescription, firstFanArt, firstGenre, firstRating,
						firstTagline, firstyear, firstDuration, movieID1);
			}

		});

		RecentlyAddedAudioLazyAdapter audioAdapter = new RecentlyAddedAudioLazyAdapter(
				context, w, profile_ip_value, audioArr, category,
				main_linear_layout);
		listAlbum.setAdapter(audioAdapter);
		RecentlyAddedVideoLazyAdapter videoAdapter = new RecentlyAddedVideoLazyAdapter(
				context, w, profile_ip_value, videoArr, category,
				main_linear_layout);
		listMovies.setAdapter(videoAdapter);

	}

	public void openAlbumPopUp(final String label, final String thumbnail,
			final int albumId) {
		Cursor cursor = milanClimaxDataSource.selectAudioSongDataWithAlbumId(
				(long) model.getCurrent_profile_id(), albumId);
		ImageView playAlbumImage = UIHelper.getImageView(popUpView,
				R.id.imgPlayInSongDetInPlaySongDetail);
		playAlbumImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				playAlbum(label, thumbnail, albumId);
			}
		});
		playAlbumDetail.setText(label);
			
		// model.getPopUpMessageProfileList().setAnimationStyle(R.style.PopupWindowAnimation);

		Picasso.with(context)
				.load("http://" + profile_ip_value + "/image/"
						+ thumbnail.replace("%", "%25"))
				.resize(((w - model.pxToDp(60)) / 3), model.pxToDp(100))
				.error(R.drawable.album).centerCrop().into(imageAlbumDetail);
		tableLayout.removeAllViews();
		tableLayout.setPadding(0, model.pxToDp(20), 0, 0);
		while (cursor.moveToNext()) {
			TableRow tableRow = new TableRow(context);
			tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
			tableRow.setBackgroundColor(Color.parseColor("#000000"));
			TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(
					TableLayout.LayoutParams.MATCH_PARENT,
					TableLayout.LayoutParams.WRAP_CONTENT);
			tableRowParams.gravity = Gravity.CENTER_HORIZONTAL;
			tableRowParams.setMargins(model.pxToDp(10), 0, model.pxToDp(10), 0);

			tableRow.setPadding(0, model.pxToDp(5), 0, model.pxToDp(5));
			TextView textView = new TextView(context);
			TableRow.LayoutParams textParams = new TableRow.LayoutParams(
					model.pxToDp(160), TableRow.LayoutParams.WRAP_CONTENT);
			textView.setText(cursor.getPosition() + 1 + ". "
					+ cursor.getString(7));
			textView.setSingleLine(true);
			textView.setPadding(model.pxToDp(10), 0, 0, 0);
			textView.setGravity(Gravity.CENTER_VERTICAL);
			textView.setTextColor(Color.parseColor("#B5B4B4"));
			View v = new View(context);
			TableRow.LayoutParams viewParams = new TableRow.LayoutParams(
					(int) model.pxToDoubleDp(0.3),
					TableRow.LayoutParams.FILL_PARENT);
			v.setBackgroundColor(Color.parseColor("#c0c0c0"));
			TextView textView1 = new TextView(context);
			TableRow.LayoutParams textParams1 = new TableRow.LayoutParams(
					model.pxToDp(40), TableRow.LayoutParams.WRAP_CONTENT);
			textParams1.setMargins(model.pxToDp(10), 0, model.pxToDp(10), 0);
			int f = (cursor.getInt(4)) / 60;
			int f1 = (cursor.getInt(4)) % 60;
			String min = Integer.toString(f);
			String sec = Integer.toString(f1);
			if (sec.length() == 1)
				sec = "0" + sec;
			if (min.length() == 1)
				min = "0" + min;
			textView1.setText(min + ":" + sec);
			textView1.setSingleLine(true);
			textView1.setGravity(Gravity.CENTER_VERTICAL);
			textView1.setTextColor(Color.parseColor("#B5B4B4"));
			View v1 = new View(context);
			TableRow.LayoutParams viewParams1 = new TableRow.LayoutParams(
					(int) 0.5, TableRow.LayoutParams.FILL_PARENT);
			v1.setBackgroundColor(Color.parseColor("#c0c0c0"));
			ImageView imageView = new ImageView(context);
			imageView.setBackgroundResource(R.drawable.addbtn);
			TableRow.LayoutParams imageParams1 = new TableRow.LayoutParams(
					model.pxToDp(25), model.pxToDp(25));
			imageParams1.setMargins(0, 0, model.pxToDp(3), 0);
			imageParams1.gravity = Gravity.CENTER_VERTICAL;
			tableRow.addView(textView, textParams);
			tableRow.addView(v, viewParams);
			tableRow.addView(textView1, textParams1);
			tableRow.addView(v1, viewParams1);
			tableRow.addView(imageView, imageParams1);

			final String Label = cursor.getString(7);
			final String thumbnail1 = cursor.getString(9);
			final int song_id = cursor.getInt(8);
			addPlaylist.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					HttpResponse httpResponse = model
							.postJsonValuesToServer(
									"http://" + profile_ip_value + "/jsonrpc",
									"{\"id\":1,\"jsonrpc\":\"2.0\",\"method\":\"Playlist.Add\",\"params\":{\"playlistid\":0,\"item\":{\"albumid\":"
											+ albumId + "}}}");
					Log.i("tag", "-------------->" + httpResponse);
					if (httpResponse != null) {
						Toast.makeText(context, "Album List added to playlist",
								1000).show();
					} else {
						model.setErrorMsgForToast(context);
					}
				}
			});
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					HttpResponse httpResponse = model
							.postJsonValuesToServer(
									"http://" + profile_ip_value + "/jsonrpc",
									"{\"id\":1,\"jsonrpc\":\"2.0\",\"method\":\"Playlist.Add\",\"params\":{\"playlistid\":0,\"item\":{\"songid\":"
											+ song_id + "}}}");
					Log.i("tag", "-------------->" + httpResponse);
					if (httpResponse != null) {
						Toast.makeText(context,
								"Song added to playlist", 1000).show();
					} else {
						model.setErrorMsgForToast(context);
					}
				}
			});
			tableRow.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					playSong(Label, thumbnail1, song_id);
				}
			});
			tableLayout.addView(tableRow, tableRowParams);
		}
		model.setPopUpMessageProfileList(new PopupWindow(main_linear_layout,
				LayoutParams.WRAP_CONTENT, model.getHeight(context)-model.pxToDp(100)));
		model.getPopUpMessageProfileList().setContentView(popUpView);
		model.getPopUpMessageProfileList().setAnimationStyle(
				R.style.PopupWindow);
		popUpView.findViewById(R.id.closeInPlaySongDetail).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						relativeLayout.setVisibility(View.INVISIBLE);
						model.closePopup(model.getPopUpMessageProfileList());
					}
				});
		model.getPopUpMessageProfileList().setWidth(w - model.pxToDp(20));
		try {
			if(model.getPopUpMessageProfileList().isShowing()){
				model.getPopUpMessageProfileList().dismiss();
				relativeLayout.setVisibility(View.VISIBLE);
				model.getPopUpMessageProfileList().showAtLocation(main_linear_layout,
						Gravity.CENTER, 0, model.pxToDp(10));
			}else{
			model.getPopUpMessageProfileList().showAtLocation(main_linear_layout,
					Gravity.CENTER, 0, model.pxToDp(10));
			relativeLayout.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void playSong(String label, String thumbnail, int song_id) {
		HttpResponse httpResponse = model
				.postJsonValuesToServer(
						"http://" + profile_ip_value + "/jsonrpc",
						"{\"jsonrpc\" : \"2.0\", \"method\" : \"Player.Open\",\"params\" : {\"item\":{\"songid\":"
								+ song_id + "}}, \"id\" : \"2\"}");
		Log.i("tag", "-------------->" + httpResponse);
		if (httpResponse != null) {
			if (model.getCurrentActivationStatus() == 1)
				model.getXMLFromService(
						"http://" + model.getCurrentGatewayIP()
								+ "/Milan/Drivers/Play_Pause/Play.py");
			((MainActivity) context).openRemoteAnimatedLoadImage();
		} else {

		}
	}

	public void playAlbum(String label, String thumbnail, int album_id) {
		HttpResponse httpResponse = model
				.postJsonValuesToServer(
						"http://" + profile_ip_value + "/jsonrpc",
						"{\"jsonrpc\" : \"2.0\", \"method\" : \"Player.Open\",\"params\" : {\"item\":{\"albumid\":"
								+ album_id + "}}, \"id\" : \"2\"}");
		Log.i("tag", "-------------->" + httpResponse);
		if (httpResponse != null) {
			if (model.getCurrentActivationStatus() == 1)
				model.getXMLFromService(
						"http://" + model.getCurrentGatewayIP()
								+ "/Milan/Drivers/Play_Pause/Play.py");
			((MainActivity) context).openRemoteAnimatedLoadImage();
		} else {

		}
	}

	public void openMoviePopUp(final String label, final String thumbnail,
			final String director, final String description,
			final String fanArt, final String genre, final String rating,
			final String tagline, final String year, int duration,
			final int movieID) {
		String hours = String.valueOf(duration / 3600);
		if (hours.length() < 2) {
			hours = "0" + hours;
		}
		final String finlHours = hours;
		String minutes = String.valueOf((duration % 3600) / 60);
		if (minutes.length() < 2) {
			minutes = "0" + minutes;
		}
		final String finlMinutes = minutes;
		UIHelper.getTextView(popUpView1, R.id.txtValDirectorInMovieDet)
				.setText(director.replace(",", "/"));
		UIHelper.getTextView(popUpView1, R.id.txtValGenreInMovieDet).setText(
				genre.replace(",", "/"));
		UIHelper.getTextView(popUpView1, R.id.txtValYearInMovieDet).setText(
				year);
		UIHelper.getTextView(popUpView1, R.id.txtValDurationInMovieDet)
				.setText(finlHours + ":" + finlMinutes);
		UIHelper.getTextView(popUpView1, R.id.txtValTagLineInMovieDet).setText(
				tagline);
		UIHelper.getTextView(popUpView1, R.id.txtValDescriptionInMovieDet)
				.setText(description);
		UIHelper.getRatingBar(popUpView1, R.id.ratingBar).setRating(
				Float.parseFloat(rating) / 2);
		ImageView playMovie = UIHelper.getImageView(popUpView1,
				R.id.play_movie_button);
		model.setPopUpMessageProfileList(new PopupWindow(main_linear_layout,
				LayoutParams.WRAP_CONTENT, model.getHeight(context)-model.pxToDp(100)));
		playMovie.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				playMovie(label, thumbnail, movieID);
			}
		});
		model.getPopUpMessageProfileList().setContentView(popUpView1);
		model.getPopUpMessageProfileList().setAnimationStyle(
				R.style.PopupWindow);
		playMovieDetail.setText(label);
		// Bitmap bitmap = transform(image);

		Bitmap bmp = null;
		RelativeLayout relative_layout = (RelativeLayout) popUpView1
				.findViewById(R.id.relForMovieDetail);
		try {

			new LoadBitmapImage("http://" + profile_ip_value + "/image/"
					+ fanArt.replace("%", "%25"), model, relative_layout)
					.execute();
			/*
			 * bmp = getBitmapFromURL("http://" + profile_ip_value + "/image/" +
			 * fanArt.replace("%", "%25")); Bitmap bmp2 = transform(bmp);
			 * BitmapDrawable bmp3 = new BitmapDrawable(bmp2);
			 * 
			 * relativeLayout.setBackgroundDrawable(bmp3);
			 */
		} catch (OutOfMemoryError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			relative_layout.setBackgroundResource(R.drawable.moviebackbg);
		}
		Log.i("Image---->",
				"http://" + profile_ip_value + "/image/"
						+ fanArt.replace("%", "%25"));
		if (bmp == null) {
			relative_layout.setBackgroundResource(R.drawable.moviebackbg);
		}

		model.getPopUpMessageProfileList().setWidth(w - model.pxToDp(20));
		if(model.getPopUpMessageProfileList().isShowing()){
			model.getPopUpMessageProfileList().dismiss();
			relativeLayout.setVisibility(View.VISIBLE);
			model.getPopUpMessageProfileList().showAtLocation(main_linear_layout,
					Gravity.CENTER, 0, model.pxToDp(10));
		}else{
		
			relativeLayout.setVisibility(View.VISIBLE);
		model.getPopUpMessageProfileList().showAtLocation(main_linear_layout,
				Gravity.CENTER, 0, model.pxToDp(10));}
		popUpView1.findViewById(R.id.closeInMovieDetail).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						relativeLayout.setVisibility(View.INVISIBLE);
						model.closePopup(model.getPopUpMessageProfileList());
					}
				});
	}

	@SuppressWarnings("deprecation")
	public void playMovie(String label, String thumbnail, int movie_id) {
		HttpResponse httpResponse = model
				.postJsonValuesToServer(
						"http://" + profile_ip_value + "/jsonrpc",
						"{\"jsonrpc\" : \"2.0\", \"method\" : \"Player.Open\",\"params\" : {\"item\":{\"movieid\":"
								+ movie_id + "}}, \"id\" : \"2\"}");
		Log.i("tag", "-------------->" + httpResponse);
		if (httpResponse != null) {
			if (model.getCurrentActivationStatus() == 1)
				model.getXMLFromService(
						"http://" + model.getCurrentGatewayIP()
								+ "/Milan/Drivers/Play_Pause/Play.py");
			((MainActivity) context).openRemoteAnimatedLoadImage();
		} else {

		}
	}

	@Override
	public String key() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bitmap transform(Bitmap source) {
		int size = Math.min(source.getWidth(), source.getHeight());
		int x = (source.getWidth() - size) / 2;
		int y = (source.getHeight() - size) / 2;
		Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
		if (result != source) {
			source.recycle();
		}
		return result;
	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog = ProgressDialog.show(context, "Loading",
				"Doing stuff. Please wait...");
		dialog.setProgressStyle(R.style.ProgressBar);

	}

}
