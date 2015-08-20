package org.milan.climax.lazyloader;

import java.util.List;

import org.apache.http.HttpResponse;
import org.milan.climax.R;
import org.milan.climax.MainActivity;
import org.milan.climax.UIhelper.UIHelper;
import org.milan.climax.db.MilanClimaxDataSource;
import org.milan.climax.model.Audio;
import org.milan.climax.model.Model;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.Toast;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class AudioLazyAdapter extends BaseAdapter {

	private List<Audio> data;
	private static LayoutInflater inflater = null;
	private int w;
	private Model model;
	private Audio audioValues;
	private TextView playAlbumDetail;
	private ImageView imageAlbumDetail, image1, image2, image3, addPlaylist;
	private LinearLayout imagelinearLayout1, imagelinearLayout2,
			imagelinearLayout3, linearLayout1, linearLayout2, linearLayout3,
			main_linear_layout;
	public View rootView, popUpView;
	private RelativeLayout relLayout, relativeLayout;
	private String profile_ip_value, category;
	private TextView text1, text2, text3;
	private android.app.Activity context;
	private MilanClimaxDataSource milanClimaxDataSource;
	private TableLayout tableLayout;
	private ListView list;

	public AudioLazyAdapter(final android.app.Activity context, int w,
			String profile_ip_value, List<Audio> d, String category,
			final LinearLayout main_linear_layout, RelativeLayout relativeLayout) {
		this.context = context;
		data = d;
		this.w = w;
		this.model = Model.getModelObj();
		this.relativeLayout = relativeLayout;
		
		this.profile_ip_value = profile_ip_value;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		popUpView = context.getLayoutInflater().inflate(
				R.layout.playsongdetail, null);
		playAlbumDetail = UIHelper.getTextView(popUpView,
				R.id.btnTitleInPlaySongDetail);

		imageAlbumDetail = UIHelper.getImageView(popUpView,
				R.id.imgAlbumDetInPlayDetail);

		addPlaylist = UIHelper.getImageView(popUpView,
				R.id.imgAddPlaylistInSongDetInPlaySongDetail);

		tableLayout = UIHelper.getTableLayoutView(popUpView,
				R.id.tblLayoutInPlaySongDetail);
		this.milanClimaxDataSource = model.getMilanClimaxDataSource();
		this.category = category;
		this.main_linear_layout = main_linear_layout;
		this.list = list;
		model.setPopUpMessageProfileList(new PopupWindow(main_linear_layout,
				LayoutParams.WRAP_CONTENT, model.getHeight(context)-model.pxToDp(75)));
		relativeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.setVisibility(View.INVISIBLE);
				try {
					model.getPopUpMessageProfileList().dismiss();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					model.setPopUpMessageProfileList(new PopupWindow(main_linear_layout,
							LayoutParams.WRAP_CONTENT, model.getHeight(context)-model.pxToDp(75)));
					e.printStackTrace();
				}
			}
		});
	}

	public int getCount() {
		if ((data.size() % 3) == 0)
			return data.size() / 3;
		else
			return (data.size() / 3) + 1;

	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View dynamicView = convertView;
		if (convertView == null)
			dynamicView = inflater.inflate(R.layout.item, null);
		text1 = UIHelper.getTextView(dynamicView, R.id.text1);
		text2 = UIHelper.getTextView(dynamicView, R.id.text2);
		text3 = UIHelper.getTextView(dynamicView, R.id.text3);

		image1 = UIHelper.getImageView(dynamicView, R.id.image1);
		image2 = UIHelper.getImageView(dynamicView, R.id.image2);
		image3 = UIHelper.getImageView(dynamicView, R.id.image3);

		imagelinearLayout1 = UIHelper.getLinearLayoutView(dynamicView,
				R.id.image_linear_layout1);
		imagelinearLayout2 = UIHelper.getLinearLayoutView(dynamicView,
				R.id.image_linear_layout2);
		imagelinearLayout3 = UIHelper.getLinearLayoutView(dynamicView,
				R.id.image_linear_layout3);

		linearLayout1 = UIHelper.getLinearLayoutView(dynamicView,
				R.id.linear_layout1);
		linearLayout2 = UIHelper.getLinearLayoutView(dynamicView,
				R.id.linear_layout2);
		linearLayout3 = UIHelper.getLinearLayoutView(dynamicView,
				R.id.linear_layout3);

		try {
			int i = (3 * position);
			if (i < data.size()) {
				imagelinearLayout1.setVisibility(View.VISIBLE);
				text1.setVisibility(View.VISIBLE);
				audioValues = data.get(i);
				final String firstLabel = audioValues.getLabel();
				final String firstThumbnail = audioValues.getThumbnail();
				final int songID1 = audioValues.getSong_id();
				final int albumID1 = audioValues.getAlbum_id();
				final int artistID1 = audioValues.getArtist_id();
				Log.i("Tag--->", "--------->" + i);
				createView(firstLabel, firstThumbnail, imagelinearLayout1,
						image1, text1);
				if (category.equalsIgnoreCase("song")) {
					linearLayout1.setOnLongClickListener(new OnLongClickListener() {

						@Override
						public boolean onLongClick(View v) {
							// TODO Auto-generated method stub
							
							HttpResponse httpResponse = model
									.postJsonValuesToServer(
											"http://" + profile_ip_value + "/jsonrpc",
											"{\"id\":1,\"jsonrpc\":\"2.0\",\"method\":\"Playlist.Add\",\"params\":{\"playlistid\":0,\"item\":{\"songid\":"
													+ songID1 + "}}}");
							Log.i("tag", "-------------->" + httpResponse);
							if (httpResponse != null) {
								Toast.makeText(context,
										"Song added to playlist", 1000).show();
							} else {
								model.setErrorMsgForToast(context);
							}
							return true;
						}
					});
				}
				linearLayout1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// openPopUp(firstLabel, firstThumbnail, linearLayout1);
						relativeLayout.setVisibility(View.VISIBLE);
						if (category.equalsIgnoreCase("album")) {
							openAlbumPopUp(firstLabel, firstThumbnail,
									albumID1, linearLayout1);
						} else if (category.equalsIgnoreCase("artist")) {
							openArtistPopUp(firstLabel, firstThumbnail,
									artistID1, linearLayout1);
						}
						if (category.equalsIgnoreCase("song")) {
							playSong(firstLabel, firstThumbnail, songID1,
									linearLayout1);
						}
					}
				});
			} else {
				imagelinearLayout1.setVisibility(View.GONE);
				text1.setVisibility(View.GONE);
			}

			int j = (3 * position) + 1;
			if (j < data.size()) {
				imagelinearLayout2.setVisibility(View.VISIBLE);
				text2.setVisibility(View.VISIBLE);
				audioValues = data.get(j);
				Log.i("Tag--->", "--------->" + j);
				final String secondLabel = audioValues.getLabel();
				final String secondThumbnail = audioValues.getThumbnail();
				final int songID2 = audioValues.getSong_id();
				final int albumID2 = audioValues.getAlbum_id();
				final int artistID2 = audioValues.getArtist_id();
				createView(secondLabel, secondThumbnail, imagelinearLayout2,
						image2, text2);
				if (category.equalsIgnoreCase("song")) {
					linearLayout2.setOnLongClickListener(new OnLongClickListener() {

						@Override
						
						public boolean onLongClick(View v) {
							// TODO Auto-generated method stub
							HttpResponse httpResponse = model
									.postJsonValuesToServer(
											"http://" + profile_ip_value + "/jsonrpc",
											"{\"id\":1,\"jsonrpc\":\"2.0\",\"method\":\"Playlist.Add\",\"params\":{\"playlistid\":0,\"item\":{\"songid\":"
													+ songID2 + "}}}");
							Log.i("tag", "-------------->" + httpResponse);
							if (httpResponse != null) {
								Toast.makeText(context,
										"Song added to playlist", 1000).show();
							} else {
								model.setErrorMsgForToast(context);
							}
							return true;
						}
					});
				}
				linearLayout2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						relativeLayout.setVisibility(View.VISIBLE);
						if (category.equalsIgnoreCase("album")) {
							openAlbumPopUp(secondLabel, secondThumbnail,
									albumID2, linearLayout1);
						} else if (category.equalsIgnoreCase("artist")) {
							openArtistPopUp(secondLabel, secondThumbnail,
									artistID2, linearLayout1);
						}
						if (category.equalsIgnoreCase("song")) {
							playSong(secondLabel, secondThumbnail, songID2,
									linearLayout1);
						}
					}
				});
			} else {
				imagelinearLayout2.setVisibility(View.GONE);
				text2.setVisibility(View.GONE);
			}
			int k = (3 * position) + 2;
			if (k < data.size()) {
				imagelinearLayout3.setVisibility(View.VISIBLE);
				text3.setVisibility(View.VISIBLE);
				audioValues = data.get(k);
				Log.i("Tag--->", "--------->" + k);
				final String thirdLabel = audioValues.getLabel();
				final String thirdThumbnail = audioValues.getThumbnail();
				final int songID3 = audioValues.getSong_id();
				final int albumID3 = audioValues.getAlbum_id();
				final int artistID3 = audioValues.getArtist_id();
				createView(thirdLabel, thirdThumbnail, imagelinearLayout3,
						image3, text3);
				if (category.equalsIgnoreCase("song")) {
					linearLayout3.setOnLongClickListener(new OnLongClickListener() {

						@Override
						public boolean onLongClick(View v) {
							// TODO Auto-generated method stub
							HttpResponse httpResponse = model
									.postJsonValuesToServer(
											"http://" + profile_ip_value + "/jsonrpc",
											"{\"id\":1,\"jsonrpc\":\"2.0\",\"method\":\"Playlist.Add\",\"params\":{\"playlistid\":0,\"item\":{\"songid\":"
													+ songID3 + "}}}");
							Log.i("tag", "-------------->" + httpResponse);
							if (httpResponse != null) {
								Toast.makeText(context,
										"Song added to playlist", 1000).show();
							} else {
								model.setErrorMsgForToast(context);
							}
							return true;
						}
					});
				}
				linearLayout3.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						relativeLayout.setVisibility(View.VISIBLE);
						if (category.equalsIgnoreCase("album")) {
							openAlbumPopUp(thirdLabel, thirdThumbnail,
									albumID3, linearLayout1);
						} else if (category.equalsIgnoreCase("artist")) {
							openArtistPopUp(thirdLabel, thirdThumbnail,
									artistID3, linearLayout1);
						}
						if (category.equalsIgnoreCase("song")) {
							
							playSong(thirdLabel, thirdThumbnail, songID3,
									linearLayout1);
						}

					}
				});
			} else {
				imagelinearLayout3.setVisibility(View.GONE);
				text3.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dynamicView;
	}

	public void createView(String label, String thumbnail,
			LinearLayout imageLinearLayout, ImageView image, TextView text) {

		Log.i("tag",
				Picasso.with(context)
						.load("http://" + profile_ip_value + "/image/"
								+ thumbnail.replace("%", "%25")).toString());
		Picasso.with(context)
				.load("http://" + profile_ip_value + "/image/"
						+ thumbnail.replace("%", "%25"))
				.resize(((w - model.pxToDp(30)) / 3), model.pxToDp(120))
				/*.error(R.drawable.album)*/.centerCrop().into(image);

		LinearLayout.LayoutParams linearLayout2Params = new LinearLayout.LayoutParams(
				((w - model.pxToDp(30)) / 3), model.pxToDp(120));
		linearLayout2Params.setMargins(model.pxToDp(5), 0, 0, 0);
		LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
				((w - model.pxToDp(30)) / 3), model.pxToDp(120));
		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
				((w - model.pxToDp(30)) / 3),
				LinearLayout.LayoutParams.WRAP_CONTENT);

		textParams.setMargins(model.pxToDp(5), 0, 0, 0);
		imageLinearLayout.setLayoutParams(linearLayout2Params);
		image.setLayoutParams(imageParams);
		image.setPadding(model.pxToDp(10), model.pxToDp(4), model.pxToDp(10),
				model.pxToDp(4));
		text.setLayoutParams(textParams);
		text.setText(label);
		text.setSingleLine(true);
		text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		text.setTextColor(Color.parseColor("#939393"));
		text.setBackgroundColor(Color.parseColor("#000000"));
		text.setHeight(model.pxToDp(30));
		text.setPadding(model.pxToDp(5), 0, model.pxToDp(5), 0);
		text.setGravity(Gravity.CENTER);
		//text.setTypeface(null, Typeface.BOLD);
	}

	public void openAlbumPopUp(final String label, final String thumbnail,
			final int albumId, final LinearLayout linear_layout) {
		Cursor cursor = milanClimaxDataSource.selectAudioSongDataWithAlbumId(
				(long) model.getCurrent_profile_id(), albumId);
		ImageView playAlbumImage = UIHelper.getImageView(popUpView,
				R.id.imgPlayInSongDetInPlaySongDetail);
		playAlbumImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MainActivity) model.getContext()).setAlpha(v);
				playAlbum(label, thumbnail, albumId, linear_layout);
			}
		});
		playAlbumDetail.setText(label);
		// model.getPopUpMessageProfileList().setAnimationStyle(R.style.PopupWindowAnimation);

		Picasso.with(context)
				.load("http://" + profile_ip_value + "/image/"
						+ thumbnail.replace("%", "%25"))
				.resize(((w - model.pxToDp(60)) / 3), model.pxToDp(100))
				/*.error(R.drawable.album)*/.centerCrop().into(imageAlbumDetail);
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
					model.pxToDp(150), TableRow.LayoutParams.WRAP_CONTENT);
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
			imageParams1.gravity = Gravity.CENTER_VERTICAL;
			tableRow.addView(textView, textParams);
			tableRow.addView(v, viewParams);
			tableRow.addView(textView1, textParams1);
			tableRow.addView(v1, viewParams1);
			tableRow.addView(imageView, imageParams1);

			final String Label = cursor.getString(7);
			final String thumbnail1 = cursor.getString(9);
			final int song_id = cursor.getInt(8);
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((MainActivity) model.getContext()).setAlpha(v);
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
					((MainActivity) model.getContext()).setAlpha(v);
					playSong(Label, thumbnail1, song_id, linearLayout1);
				}
			});
			tableLayout.addView(tableRow, tableRowParams);
		}

		
		addPlaylist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				((MainActivity) model.getContext()).setAlpha(v);
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
		
		try {
			model.getPopUpMessageProfileList().setContentView(popUpView);
			model.getPopUpMessageProfileList().setAnimationStyle(
					R.style.PopupWindow);
			model.getPopUpMessageProfileList().setWidth(w - model.pxToDp(20));
			popUpView.findViewById(R.id.closeInPlaySongDetail).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							relativeLayout.setVisibility(View.INVISIBLE);
							try {
								model.getPopUpMessageProfileList().dismiss();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								model.setPopUpMessageProfileList(new PopupWindow(main_linear_layout,
										LayoutParams.WRAP_CONTENT, model.getHeight(context)-model.pxToDp(75)));
								e.printStackTrace();
							}
						}
					});
			if(model.getPopUpMessageProfileList().isShowing()){
				model.getPopUpMessageProfileList().dismiss();
				
			}
			model.getPopUpMessageProfileList().showAtLocation(main_linear_layout,
					Gravity.CENTER, 0, model.pxToDp(15));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			model.setPopUpMessageProfileList(new PopupWindow(main_linear_layout,
					LayoutParams.WRAP_CONTENT, model.getHeight(context)-model.pxToDp(75)));
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void playSong(String label, String thumbnail, int song_id,
			LinearLayout linear_layout) {
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
			model.setErrorMsgForToast(context);
		}
	}

	public void playAlbum(String label, String thumbnail, int album_id,
			LinearLayout linear_layout) {
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
			model.setErrorMsgForToast(context);
		}
	}

	public void playArtist(String label, String thumbnail, int album_id,
			LinearLayout linear_layout) {
		HttpResponse httpResponse = model
				.postJsonValuesToServer(
						"http://" + profile_ip_value + "/jsonrpc",
						"{\"jsonrpc\" : \"2.0\", \"method\" : \"Player.Open\",\"params\" : {\"item\":{\"artistid\":"
								+ album_id + "}}, \"id\" : \"2\"}");
		Log.i("tag", "-------------->" + httpResponse);
		if (httpResponse != null) {
			if (model.getCurrentActivationStatus() == 1)
				model.getXMLFromService(
						"http://" + model.getCurrentGatewayIP()
								+ "/Milan/Drivers/Play_Pause/Play.py");
			((MainActivity) context).openRemoteAnimatedLoadImage();
		} else {
			model.setErrorMsgForToast(context);
		}
	}

	public void openArtistPopUp(final String label, final String thumbnail,
			final int artistId, final LinearLayout linear_layout) {

		Log.i("artistId", "-------------->" + artistId);
		Cursor cursor = milanClimaxDataSource.selectAudioSongDataWithArtistId(
				(long) model.getCurrent_profile_id(), artistId);
		ImageView playAlbumImage = UIHelper.getImageView(popUpView,
				R.id.imgPlayInSongDetInPlaySongDetail);
		
		playAlbumImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MainActivity) model.getContext()).setAlpha(v);
				playArtist(label, thumbnail, artistId, linear_layout);
			}
		});
		playAlbumDetail.setText(label);
		// model.getPopUpMessageProfileList().setAnimationStyle(R.style.PopupWindowAnimation);

		Picasso.with(context)
				.load("http://" + profile_ip_value + "/image/"
						+ thumbnail.replace("%", "%25"))
				.resize(((w - model.pxToDp(60)) / 3), model.pxToDp(100))
				/*.error(R.drawable.album)*/.centerCrop().into(imageAlbumDetail);
		tableLayout.removeAllViews();
		tableLayout.setPadding(0, model.pxToDp(20), 0, 0);
		while (cursor.moveToNext()) {
			TableRow tableRow = new TableRow(context);
			tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
			tableRow.setBackgroundColor(Color.parseColor("#000000"));
			TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(
					TableLayout.LayoutParams.MATCH_PARENT,
					TableLayout.LayoutParams.WRAP_CONTENT);

			tableRowParams.setMargins(model.pxToDp(10), 0, model.pxToDp(10), 0);
			tableRowParams.gravity = Gravity.CENTER_HORIZONTAL;
			tableRow.setPadding(0, model.pxToDp(5), 0, model.pxToDp(5));
			TextView textView = new TextView(context);
			TableRow.LayoutParams textParams = new TableRow.LayoutParams(
					model.pxToDp(150), TableRow.LayoutParams.WRAP_CONTENT);
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
			imageParams1.gravity = Gravity.CENTER_VERTICAL;
			tableRow.addView(textView, textParams);
			tableRow.addView(v, viewParams);
			tableRow.addView(textView1, textParams1);
			tableRow.addView(v1, viewParams1);
			tableRow.addView(imageView, imageParams1);

			final String Label = cursor.getString(7);
			final String thumbnail1 = cursor.getString(9);
			final int song_id = cursor.getInt(8);
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((MainActivity) model.getContext()).setAlpha(v);
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
					((MainActivity) model.getContext()).setAlpha(v);
					playSong(Label, thumbnail1, song_id, linearLayout1);
				}
			});
			tableLayout.addView(tableRow, tableRowParams);
		}

		
		popUpView.findViewById(R.id.closeInPlaySongDetail).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						((MainActivity) model.getContext()).setAlpha(v);
						relativeLayout.setVisibility(View.INVISIBLE);
						try {
							model.getPopUpMessageProfileList().dismiss();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							model.setPopUpMessageProfileList(new PopupWindow(main_linear_layout,
									LayoutParams.WRAP_CONTENT, model.getHeight(context)-model.pxToDp(75)));
							e.printStackTrace();
						}
					}
				});
		
		addPlaylist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MainActivity) model.getContext()).setAlpha(v);
				HttpResponse httpResponse = model
						.postJsonValuesToServer(
								"http://" + profile_ip_value + "/jsonrpc",
								"{\"id\":1,\"jsonrpc\":\"2.0\",\"method\":\"Playlist.Add\",\"params\":{\"playlistid\":0,\"item\":{\"artistid\":"
										+ artistId + "}}}");
				Log.i("tag", "-------------->" + httpResponse);
				if (httpResponse != null) {
					Toast.makeText(context, "Artist List added to playlist",
							1000).show();
				} else {
					model.setErrorMsgForToast(context);
				}
			}
		});
		
		try {
			model.getPopUpMessageProfileList().setContentView(popUpView);
			model.getPopUpMessageProfileList().setAnimationStyle(
					R.style.PopupWindow);
			model.getPopUpMessageProfileList().setWidth(w - model.pxToDp(20));
			if(model.getPopUpMessageProfileList().isShowing()){
				model.getPopUpMessageProfileList().dismiss();
			}
			model.getPopUpMessageProfileList().showAtLocation(main_linear_layout,
					Gravity.CENTER, 0, model.pxToDp(15));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			model.setPopUpMessageProfileList(new PopupWindow(main_linear_layout,
					LayoutParams.WRAP_CONTENT, model.getHeight(context)-model.pxToDp(75)));
			e.printStackTrace();
		}
	}

}