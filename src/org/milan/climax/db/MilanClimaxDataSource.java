package org.milan.climax.db;

import java.util.ArrayList;
import java.util.List;

import org.milan.climax.model.Audio;
import org.milan.climax.model.Profile;
import org.milan.climax.model.Video;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class MilanClimaxDataSource {

	public static final String LOGTAG = "MILANCLIMAXDATASOURCE";

	SQLiteOpenHelper opSqLiteOpenHelper;
	SQLiteDatabase database;
	ContentValues contentValues;
	Profile profile;
	SQLiteStatement statement;

	public MilanClimaxDataSource(Context context) {
		opSqLiteOpenHelper = new MilanClimaxDBHelper(context);
		database = opSqLiteOpenHelper.getWritableDatabase();
	}

	public void open() {
		Log.i(LOGTAG, "Database opened");

	}

	// Profile Table CRUD Operation

	public List<Profile> selectProfileDataWithId(Long id) {
		List<Profile> ProfileValues = new ArrayList<Profile>();
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ MilanClimaxDBHelper.PROFILE_TABLE_NAME
				+ " WHERE profile_id = " + id, null);
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Profile profile = new Profile();
				profile.setId(cursor.getLong(cursor
						.getColumnIndex(MilanClimaxDBHelper.COLUMN_PROFILE_ID)));
				profile.setRoom_name(cursor.getString(cursor
						.getColumnIndex(MilanClimaxDBHelper.COLUMN_PROFILE_NAME)));
				profile.setRoom_name(cursor.getString(cursor
						.getColumnIndex(MilanClimaxDBHelper.COLUMN_PROFILE_ROOM)));
				profile.setMedia_ip(cursor.getString(cursor
						.getColumnIndex(MilanClimaxDBHelper.COLUMN_PROFILE_MEDIA_IP)));
				profile.setMedia_gateway(cursor.getString(cursor
						.getColumnIndex(MilanClimaxDBHelper.COLUMN_PROFILE_MEDIA_GATEWY)));
				profile.setUsername(cursor.getString(cursor
						.getColumnIndex(MilanClimaxDBHelper.COLUMN_PROFILE_USERNAME)));
				profile.setPassword(cursor.getString(cursor
						.getColumnIndex(MilanClimaxDBHelper.COLUMN_PROFILE_PASSWORD)));
				ProfileValues.add(profile);
			}
		}
		return ProfileValues;
	}

	public String CurrentProfileIp(Long id) {
		List<Profile> ProfileValues = new ArrayList<Profile>();
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ MilanClimaxDBHelper.PROFILE_TABLE_NAME
				+ " WHERE profile_id = " + id, null);
		String profile_media_ip = "";
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			profile_media_ip = cursor.getString(3);
		}
		return profile_media_ip;
	}
	
	public String CurrentProfileRoomName(Long id) {
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ MilanClimaxDBHelper.PROFILE_TABLE_NAME
				+ " WHERE profile_id = " + id, null);
		String roomName = "";
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			roomName = cursor.getString(2);
		}
		return roomName;
	}
	
	
	public int MilanNavRoomName(String name) {
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ MilanClimaxDBHelper.PROFILE_TABLE_NAME
				+ " WHERE " +MilanClimaxDBHelper.COLUMN_PROFILE_ROOM + " = 'Living Room'", null);
		int id = 0;
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			id = cursor.getInt(0);
		}
		return id;
	}
	
	
	
	public String CurrentGatewayIp(Long id) {
		List<Profile> ProfileValues = new ArrayList<Profile>();
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ MilanClimaxDBHelper.PROFILE_TABLE_NAME
				+ " WHERE profile_id = " + id, null);
		String gatewayIp = "";
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			gatewayIp = cursor.getString(5);
		}
		return gatewayIp;
	}
	
	public int CurrentActivationStatus(Long id) {
		List<Profile> ProfileValues = new ArrayList<Profile>();
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ MilanClimaxDBHelper.PROFILE_TABLE_NAME
				+ " WHERE profile_id = " + id, null);
		int activation = 0;
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			activation = cursor.getInt(4);
		}
		return activation;
	}

	public Cursor selectProfileCursorDataWithId(Long id) {
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ MilanClimaxDBHelper.PROFILE_TABLE_NAME
				+ " WHERE profile_id = " + id, null);
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");

		return cursor;
	}

	public void updateProfileData(Long id, String RoomName, String MediaIp, int status,
			String GatewayIp, String Username, String Password) {
		contentValues = new ContentValues();
		// contentValues.put(, value);
		contentValues.put(MilanClimaxDBHelper.COLUMN_PROFILE_ROOM, RoomName);
		contentValues.put(MilanClimaxDBHelper.COLUMN_PROFILE_MEDIA_GATEWY,
				GatewayIp);
		contentValues.put(MilanClimaxDBHelper.COLUMN_PROFILE_MEDIA_IP, MediaIp);
		contentValues.put(MilanClimaxDBHelper.COLUMN_PRFOILE_ACTIVATION, status);
		contentValues
				.put(MilanClimaxDBHelper.COLUMN_PROFILE_USERNAME, Username);
		contentValues
				.put(MilanClimaxDBHelper.COLUMN_PROFILE_PASSWORD, Password);
		database.update(MilanClimaxDBHelper.PROFILE_TABLE_NAME, contentValues,
				"profile_id =" + id, null);
	}

	public void insertData() {
		String query = "SELECT MAX(profile_id) AS max_id FROM "
				+ MilanClimaxDBHelper.PROFILE_TABLE_NAME;
		Cursor cursor = database.rawQuery(query, null);
		cursor.moveToFirst();
		int c = 0;
		c = cursor.getInt(0);
		if (c < 20) {
			contentValues = new ContentValues(); // contentValues.put(, value);
			contentValues.put(MilanClimaxDBHelper.COLUMN_PROFILE_USERNAME, "");
			contentValues.put(MilanClimaxDBHelper.COLUMN_PROFILE_MEDIA_IP, ":");
			contentValues.put(MilanClimaxDBHelper.COLUMN_PROFILE_MEDIA_GATEWY, ":");
			for (int i = 0; i < 20; i++) {
				database.insert(MilanClimaxDBHelper.PROFILE_TABLE_NAME, null,
						contentValues);
			}
		}
		cursor.close();

	}

	public void bulkInsertAudioData(Long selectId, ArrayList<Integer> albumId,
			ArrayList<String> artistid, ArrayList<String> artistName,
			String profile_ip, ArrayList<String> Label,
			ArrayList<String> Thumbnail, ArrayList<String> Title) {
		database.execSQL("DELETE FROM " + MilanClimaxDBHelper.AUDIO_TABLE_NAME
				+ " WHERE " + MilanClimaxDBHelper.COLUMN_PROFILE_ID + " = "
				+ selectId);
		String sqlQuery = "INSERT INTO " + MilanClimaxDBHelper.AUDIO_TABLE_NAME
				+ " (" + MilanClimaxDBHelper.COLUMN_PROFILE_ID + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_ALBUM_ID + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_ARTIST_ID + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_ARTIST_NAME + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_PROFILE_IP + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_LABEL + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_THUMBNAIL + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_TITLE + ")"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
		statement = database.compileStatement(sqlQuery);
		database.beginTransaction();
		int size = albumId.size();
		Log.i("mylog", "video size:" + size);

		for (int i = 0; i < size; i++) {
			statement.clearBindings();
			statement.bindLong(1, selectId);
			statement.bindLong(2, albumId.get(i));
			statement.bindString(3, artistid.get(i));
			statement.bindString(4, artistName.get(i));
			/* statement.bindString(5, Duration.get(i)); */
			statement.bindString(5, profile_ip);
			statement.bindString(6, Label.get(i));
			statement.bindString(7, Thumbnail.get(i));
			statement.bindString(8, Title.get(i));
			statement.execute();
		}
		try {
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
		Log.i("mylog", "video save end");
	}
	
	
	public void bulkInsertRecentlyAddedAudioData(Long selectId, ArrayList<Integer> albumId,
			ArrayList<String> artistid, ArrayList<String> artistName,
			String profile_ip, ArrayList<String> Label,
			ArrayList<String> Thumbnail, ArrayList<String> Title) {
		database.execSQL("DELETE FROM " + MilanClimaxDBHelper.RECENTLY_ADDED_AUDIO_TABLE_NAME);
		String sqlQuery = "INSERT INTO " + MilanClimaxDBHelper.RECENTLY_ADDED_AUDIO_TABLE_NAME
				+ " (" + MilanClimaxDBHelper.COLUMN_PROFILE_ID + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_ALBUM_ID + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_ARTIST_ID + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_ARTIST_NAME + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_PROFILE_IP + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_LABEL + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_THUMBNAIL + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_TITLE + ")"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
		statement = database.compileStatement(sqlQuery);
		database.beginTransaction();
		int size = albumId.size();
		Log.i("mylog", "video size:" + size);

		for (int i = 0; i < size; i++) {
			statement.clearBindings();
			statement.bindLong(1, selectId);
			statement.bindLong(2, albumId.get(i));
			statement.bindString(3, artistid.get(i));
			statement.bindString(4, artistName.get(i));
			/* statement.bindString(5, Duration.get(i)); */
			statement.bindString(5, profile_ip);
			statement.bindString(6, Label.get(i));
			statement.bindString(7, Thumbnail.get(i));
			statement.bindString(8, Title.get(i));
			statement.execute();
		}
		try {
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
		Log.i("mylog", "video save end");
	}

	public void bulkInsertMovieData(Long selectId, ArrayList<Integer> videoId,
			ArrayList<String> videoDirector, ArrayList<String> videoFanArt,
			ArrayList<String> videoGenre, ArrayList<String> videoLabel,
			ArrayList<String> videoDescription, ArrayList<String> videoPlot,
			String profile_ip, ArrayList<String> videoRunTime,
			ArrayList<String> videoRating, ArrayList<String> videoTagLine,
			ArrayList<String> videoThumbnail, ArrayList<String> videoTitle,
			ArrayList<String> videoTrailer, ArrayList<String> videoYear,
			ArrayList<String> videoStudio) {
		database.execSQL("DELETE FROM " + MilanClimaxDBHelper.VIDEO_TABLE_NAME
				+ " WHERE " + MilanClimaxDBHelper.COLUMN_PROFILE_ID + " = "
				+ selectId);
		Log.i("mylog", "ID------>:" +selectId);
		String sqlQuery = "INSERT INTO " + MilanClimaxDBHelper.VIDEO_TABLE_NAME
				+ " (" + MilanClimaxDBHelper.COLUMN_PROFILE_ID + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_ID + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_DIRECTOR + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_FANART + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_GENRE + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_LABEL + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_PLOT + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_PROFILE_IP + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_RUNTIME + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_RATING + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_TAGLINE + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_THUMBNAIL + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_TITLE + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_TRAILER + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_YEAR + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_STUDIO + ")"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?);";
		statement = database.compileStatement(sqlQuery);
		database.beginTransaction();
		int size = videoId.size();
		Log.i("mylog", "video size:" + size);
		Log.i("mylog", "video size:" + videoDirector.size());
		for (int i = 0; i < size; i++) {
			statement.clearBindings();
			statement.bindLong(1, selectId);
			statement.bindLong(2, videoId.get(i));
			statement.bindString(3, videoDirector.get(i));
			statement.bindString(4, videoFanArt.get(i));
			statement.bindString(5, videoGenre.get(i));
			statement.bindString(6, videoLabel.get(i));
			statement.bindString(7, videoPlot.get(i));
			statement.bindString(8, profile_ip);
			statement.bindString(9, videoRunTime.get(i));
			statement.bindString(10, videoRating.get(i));
			statement.bindString(11, videoTagLine.get(i));
			statement.bindString(12, videoThumbnail.get(i));
			statement.bindString(13, videoTitle.get(i));
			statement.bindString(14, videoTrailer.get(i));
			statement.bindString(15, videoYear.get(i));
			statement.bindString(16, videoStudio.get(i));
			/* statement.bindString(5, Duration.get(i)); */
			statement.execute();
		}
		try {
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
		Log.i("mylog", "video save end");
	}

	public void bulkInsertRecentlyAddedMovieData(Long selectId,
			ArrayList<Integer> videoId, ArrayList<String> videoDirector,
			ArrayList<String> videoFanArt, ArrayList<String> videoGenre,
			ArrayList<String> videoLabel, ArrayList<String> videoDescription,
			ArrayList<String> videoPlot, String profile_ip,
			ArrayList<String> videoRunTime, ArrayList<String> videoRating,
			ArrayList<String> videoTagLine, ArrayList<String> videoThumbnail,
			ArrayList<String> videoTitle, ArrayList<String> videoTrailer,
			ArrayList<String> videoYear, ArrayList<String> videoStudio) {
		database.execSQL("DELETE FROM "
				+ MilanClimaxDBHelper.RECENTLY_ADDED_VIDEO_TABLE_NAME);
		String sqlQuery = "INSERT INTO "
				+ MilanClimaxDBHelper.RECENTLY_ADDED_VIDEO_TABLE_NAME + " ("
				+ MilanClimaxDBHelper.COLUMN_PROFILE_ID + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_ID + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_DIRECTOR + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_FANART + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_GENRE + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_LABEL + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_PLOT + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_PROFILE_IP + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_RUNTIME + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_RATING + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_TAGLINE + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_THUMBNAIL + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_TITLE + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_TRAILER + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_YEAR + ","
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_STUDIO + ")"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?);";
		statement = database.compileStatement(sqlQuery);
		database.beginTransaction();
		int size = videoId.size();
		Log.i("mylog", "video size:" + size);
		Log.i("mylog", "video size:" + videoDirector.size());
		for (int i = 0; i < size; i++) {
			statement.clearBindings();
			statement.bindLong(1, selectId);
			statement.bindLong(2, videoId.get(i));
			statement.bindString(3, videoDirector.get(i));
			statement.bindString(4, videoFanArt.get(i));
			statement.bindString(5, videoGenre.get(i));
			statement.bindString(6, videoLabel.get(i));
			statement.bindString(7, videoPlot.get(i));
			statement.bindString(8, profile_ip);
			statement.bindString(9, videoRunTime.get(i));
			statement.bindString(10, videoRating.get(i));
			statement.bindString(11, videoTagLine.get(i));
			statement.bindString(12, videoThumbnail.get(i));
			statement.bindString(13, videoTitle.get(i));
			statement.bindString(14, videoTrailer.get(i));
			statement.bindString(15, videoYear.get(i));
			statement.bindString(16, videoStudio.get(i));
			/* statement.bindString(5, Duration.get(i)); */
			statement.execute();
		}
		try {
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
		Log.i("mylog", "video save end");
	}

	public void bulkInsertAudioSongsData(Long selectId,
			ArrayList<Integer> albumId, ArrayList<String> artistid,
			ArrayList<String> artistName, String profile_ip,
			ArrayList<String> Label, ArrayList<String> Thumbnail,
			ArrayList<Integer> Duration, ArrayList<Integer> PlayCount,
			ArrayList<Integer> songId) {
		database.execSQL("DELETE FROM "
				+ MilanClimaxDBHelper.AUDIO_SONG_TABLE_NAME + " WHERE "
				+ MilanClimaxDBHelper.COLUMN_PROFILE_ID + " = " + selectId);
		Log.i("mylog", "ID------>:" +selectId);
		String sqlQuery = "INSERT INTO "
				+ MilanClimaxDBHelper.AUDIO_SONG_TABLE_NAME + " ("
				+ MilanClimaxDBHelper.COLUMN_PROFILE_ID + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_ALBUM_ID + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_ARTIST_ID + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_ARTIST_NAME + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_PROFILE_IP + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_SONG_LABEL + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_SONG_THUMBNAIL + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_SONG_DURATION + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_SONG_PLAYCOUNT + ","
				+ MilanClimaxDBHelper.COLUMN_SONG_ID + ")"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ? , ? ,?);";
		statement = database.compileStatement(sqlQuery);
		database.beginTransaction();
		int size = albumId.size();
		Log.i("mylog", "Song size:" + songId.size());
		for (int i = 0; i < size; i++) {
			statement.clearBindings();
			statement.bindLong(1, selectId);
			statement.bindLong(2, albumId.get(i));
			statement.bindString(3, artistid.get(i));
			statement.bindString(4, artistName.get(i));
			/* statement.bindString(5, Duration.get(i)); */
			statement.bindString(5, profile_ip);
			statement.bindString(6, Label.get(i));
			statement.bindString(7, Thumbnail.get(i));
			statement.bindLong(8, Duration.get(i));
			statement.bindLong(9, PlayCount.get(i));
			statement.bindLong(10, songId.get(i));
			statement.execute();
		}
		try {
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
		Log.i("mylog", "audio save end");
	}

	public void bulkInsertAudioArtistsData(Long selectId,
			ArrayList<Integer> artistId, ArrayList<String> artistName,
			String profile_ip, ArrayList<String> Label,
			ArrayList<String> Thumbnail) {
		database.execSQL("DELETE FROM "
				+ MilanClimaxDBHelper.AUDIO_ARTIST_TABLE_NAME + " WHERE "
				+ MilanClimaxDBHelper.COLUMN_PROFILE_ID + " = " + selectId);
		String sqlQuery = "INSERT INTO "
				+ MilanClimaxDBHelper.AUDIO_ARTIST_TABLE_NAME + " ("
				+ MilanClimaxDBHelper.COLUMN_PROFILE_ID + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_ARTISTID + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_PROFILE_IP + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_ARTIST_LABEL + ","
				+ MilanClimaxDBHelper.COLUMN_AUDIO_ARTIST_THUMBNAIL + ")"
				+ " VALUES (?, ?, ?, ?, ?);";
		statement = database.compileStatement(sqlQuery);
		database.beginTransaction();
		int size = artistId.size();
		Log.i("mylog", "Artist size:" + artistId.size());
		for (int i = 0; i < size; i++) {
			statement.clearBindings();
			statement.bindLong(1, selectId);
			statement.bindLong(2, artistId.get(i));
			statement.bindString(3, profile_ip);
			statement.bindString(4, Label.get(i));
			statement.bindString(5, Thumbnail.get(i));
			statement.execute();
		}
		try {
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
		Log.i("mylog", "audio save end");
	}

	public List<Audio> selectAudioAlbumDataWithId(Long id) {
		List<Audio> AudioValues = new ArrayList<Audio>();

		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ MilanClimaxDBHelper.AUDIO_TABLE_NAME + " WHERE profile_id = "
				+ id +" ORDER BY " +MilanClimaxDBHelper.COLUMN_AUDIO_LABEL+ " ASC", null);
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");

		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Audio audio = new Audio();

				audio.setThumbnail(cursor.getString(cursor
						.getColumnIndex(MilanClimaxDBHelper.COLUMN_AUDIO_THUMBNAIL)));
				audio.setLabel(cursor.getString(cursor
						.getColumnIndex(MilanClimaxDBHelper.COLUMN_AUDIO_LABEL)));
				audio.setAlbum_id(cursor.getInt(cursor
						.getColumnIndex(MilanClimaxDBHelper.COLUMN_AUDIO_ALBUM_ID)));
				AudioValues.add(audio);
			}
		}
		return AudioValues;
	}
	
	
	public List<Audio> selectRecentlyAddedAudioAlbumDataWithId(Long id) {
		List<Audio> AudioValues = new ArrayList<Audio>();

		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ MilanClimaxDBHelper.RECENTLY_ADDED_AUDIO_TABLE_NAME + " WHERE profile_id = "
				+ id +" ORDER BY " +MilanClimaxDBHelper.COLUMN_AUDIO_LABEL+ " ASC", null);
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");

		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Audio audio = new Audio();

				audio.setThumbnail(cursor.getString(cursor
						.getColumnIndex(MilanClimaxDBHelper.COLUMN_AUDIO_THUMBNAIL)));
				audio.setLabel(cursor.getString(cursor
						.getColumnIndex(MilanClimaxDBHelper.COLUMN_AUDIO_LABEL)));
				audio.setAlbum_id(cursor.getInt(cursor
						.getColumnIndex(MilanClimaxDBHelper.COLUMN_AUDIO_ALBUM_ID)));
				AudioValues.add(audio);
			}
		}
		return AudioValues;
	}

	public List<Audio> selectAudioArtistDataWithId(Long id) {
		List<Audio> AudioValues = new ArrayList<Audio>();

		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ MilanClimaxDBHelper.AUDIO_ARTIST_TABLE_NAME
				+ " WHERE profile_id = " + id +" ORDER BY " +MilanClimaxDBHelper.COLUMN_AUDIO_ARTIST_LABEL+ " ASC", null);
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");

		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Audio audio = new Audio();

				audio.setThumbnail(cursor.getString(cursor
						.getColumnIndex(MilanClimaxDBHelper.COLUMN_AUDIO_ARTIST_THUMBNAIL)));
				audio.setLabel(cursor.getString(cursor
						.getColumnIndex(MilanClimaxDBHelper.COLUMN_AUDIO_ARTIST_LABEL)));
				audio.setArtist_id(cursor.getInt(cursor
						.getColumnIndex(MilanClimaxDBHelper.COLUMN_AUDIO_ARTISTID)));
				AudioValues.add(audio);
			}
		}
		return AudioValues;
	}

	public List<Audio> selectAudioSongDataWithId(Long id) {
		List<Audio> AudioValues = new ArrayList<Audio>();

		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ MilanClimaxDBHelper.AUDIO_SONG_TABLE_NAME
				+ " WHERE profile_id = " + id +" ORDER BY " +MilanClimaxDBHelper.COLUMN_AUDIO_SONG_LABEL+ " ASC", null);
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");

		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Audio audio = new Audio();

				audio.setThumbnail(cursor.getString(cursor
						.getColumnIndex(MilanClimaxDBHelper.COLUMN_AUDIO_SONG_THUMBNAIL)));
				audio.setLabel(cursor.getString(cursor
						.getColumnIndex(MilanClimaxDBHelper.COLUMN_AUDIO_SONG_LABEL)));
				audio.setSong_id(cursor.getInt(cursor
						.getColumnIndex(MilanClimaxDBHelper.COLUMN_SONG_ID)));
				AudioValues.add(audio);
			}
		}
		return AudioValues;
	}

	public Cursor selectAudioSongDataWithAlbumId(Long id, int albumid) {

		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ MilanClimaxDBHelper.AUDIO_SONG_TABLE_NAME
				+ " WHERE profile_id = " + id + " AND album_id=" + albumid +" ORDER BY " +MilanClimaxDBHelper.COLUMN_AUDIO_SONG_LABEL+ " ASC",
				null);
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");

		return cursor;
	}

	public Cursor selectAudioSongDataWithArtistId(Long id, int artistid) {

		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ MilanClimaxDBHelper.AUDIO_SONG_TABLE_NAME
				+ " WHERE profile_id = " + id + " AND artist_id = '" + artistid
				+ "'" +" ORDER BY " +MilanClimaxDBHelper.COLUMN_AUDIO_SONG_LABEL+ " ASC", null);
		Log.i(LOGTAG, "Returned " + artistid + " rows");
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");

		/*
		 * if (cursor.getCount() > 0) { while (cursor.moveToNext()) { Audio
		 * audio = new Audio();
		 * 
		 * audio.setThumbnail(cursor.getString(cursor.getColumnIndex(
		 * MilanClimaxDBHelper.COLUMN_AUDIO_SONG_THUMBNAIL)));
		 * audio.setLabel(cursor
		 * .getString(cursor.getColumnIndex(MilanClimaxDBHelper
		 * .COLUMN_AUDIO_SONG_LABEL)));
		 * audio.setSong_id(cursor.getInt(cursor.getColumnIndex
		 * (MilanClimaxDBHelper.COLUMN_SONG_ID))); AudioValues.add(audio); } }
		 */
		return cursor;
	}

	public Cursor selectAudioCursorDataWithId(Long id) {
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ MilanClimaxDBHelper.AUDIO_TABLE_NAME + " WHERE profile_id = "
				+ id, null);
		return cursor;
	}

	public Cursor selectAudioSongCursorDataWithId(Long id) {
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ MilanClimaxDBHelper.AUDIO_SONG_TABLE_NAME
				+ " WHERE profile_id = " + id, null);
		return cursor;
	}

	public Cursor selectAudioArtistCursorDataWithId(Long id) {
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ MilanClimaxDBHelper.AUDIO_ARTIST_TABLE_NAME
				+ " WHERE profile_id = " + id, null);
		return cursor;
	}

	public List<Video> selectVideoCursorDataWithId(Long id) {
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ MilanClimaxDBHelper.VIDEO_TABLE_NAME + " WHERE profile_id = "
				+ id +" ORDER BY " +MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_LABEL+ " ASC", null);
		List<Video> videoValues = new ArrayList<Video>();
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");

		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Video video = new Video();
				video.setLabel(cursor.getString(6));
				video.setThumbnail(cursor.getString(12));
				video.setDirector_name(cursor.getString(3));
				video.setGenreName(cursor.getString(5));
				video.setYear(cursor.getString(15));
				video.setDuration(Integer.parseInt(cursor.getString(9)));
				video.setRating(cursor.getString(10));
				video.setTagline(cursor.getString(11));
				video.setDescription(cursor.getString(7));
				video.setFanArt(cursor.getString(4));
				video.setMovie_id(cursor.getInt(2));
				videoValues.add(video);
			}
		}
		return videoValues;
	}
	
	
	public List<Video> selectRecentlyAddedVideoCursorDataWithId(Long id) {
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ MilanClimaxDBHelper.RECENTLY_ADDED_VIDEO_TABLE_NAME + " WHERE profile_id = "
				+ id +" ORDER BY " +MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_LABEL+ " ASC", null);
		List<Video> videoValues = new ArrayList<Video>();
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");

		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Video video = new Video();
				video.setLabel(cursor.getString(6));
				video.setThumbnail(cursor.getString(12));
				video.setDirector_name(cursor.getString(3));
				video.setGenreName(cursor.getString(5));
				video.setYear(cursor.getString(15));
				video.setDuration(Integer.parseInt(cursor.getString(9)));
				video.setRating(cursor.getString(10));
				video.setTagline(cursor.getString(11));
				video.setDescription(cursor.getString(7));
				video.setFanArt(cursor.getString(4));
				video.setMovie_id(cursor.getInt(2));
				videoValues.add(video);
			}
		}
		return videoValues;
	}

	public List<Video> selectGenreMovieDataWithId(Long id, String genre) {
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ MilanClimaxDBHelper.VIDEO_TABLE_NAME + " WHERE profile_id = "
				+ id + " AND genre LIKE '%" + genre + "%'" +" ORDER BY " +MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_LABEL+ " ASC" , null);
		List<Video> videoValues = new ArrayList<Video>();
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");

		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Video video = new Video();
				video.setLabel(cursor.getString(6));
				video.setThumbnail(cursor.getString(12));
				video.setDirector_name(cursor.getString(3));
				video.setGenreName(cursor.getString(5));
				video.setYear(cursor.getString(15));
				video.setDuration(Integer.parseInt(cursor.getString(9)));
				video.setRating(cursor.getString(10));
				video.setTagline(cursor.getString(11));
				video.setDescription(cursor.getString(7));
				video.setFanArt(cursor.getString(4));
				video.setMovie_id(cursor.getInt(2));
				videoValues.add(video);
			}
		}
		return videoValues;
	}

	public Cursor selectDirectorCursorDataWithId(Long id) {
		Cursor cursor = database.rawQuery("SELECT DISTINCT("
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_DIRECTOR + ") FROM "
				+ MilanClimaxDBHelper.VIDEO_TABLE_NAME + " WHERE profile_id = "
				+ id, null);
		return cursor;
	}

	public List<Video> selectDirectorMovieDataWithId(Long id, String director) {
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ MilanClimaxDBHelper.VIDEO_TABLE_NAME + " WHERE profile_id = "
				+ id + " AND director LIKE \"%" + director + "%\"" +" ORDER BY " +MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_LABEL+ " ASC", null);
		List<Video> videoValues = new ArrayList<Video>();
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Video video = new Video();
				video.setLabel(cursor.getString(6));
				video.setThumbnail(cursor.getString(12));
				video.setDirector_name(cursor.getString(3));
				video.setGenreName(cursor.getString(5));
				video.setYear(cursor.getString(15));
				video.setDuration(Integer.parseInt(cursor.getString(9)));
				video.setRating(cursor.getString(10));
				video.setTagline(cursor.getString(11));
				video.setDescription(cursor.getString(7));
				video.setFanArt(cursor.getString(4));
				video.setMovie_id(cursor.getInt(2));
				videoValues.add(video);
			}
		}
		return videoValues;
	}

	public Cursor selectStudioCursorDataWithId(Long id) {
		Cursor cursor = database.rawQuery("SELECT DISTINCT("
				+ MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_STUDIO + ") FROM "
				+ MilanClimaxDBHelper.VIDEO_TABLE_NAME + " WHERE profile_id = "
				+ id, null);
		return cursor;
	}

	public List<Video> selectStudioMovieDataWithId(Long id, String studio) {
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ MilanClimaxDBHelper.VIDEO_TABLE_NAME + " WHERE profile_id = "
				+ id + " AND studio LIKE \"%" + studio + "%\"" +" ORDER BY " +MilanClimaxDBHelper.COLUMN_VIDEO_MOVIE_LABEL+ " ASC", null);
		List<Video> videoValues = new ArrayList<Video>();
		Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Video video = new Video();
				video.setLabel(cursor.getString(6));
				video.setThumbnail(cursor.getString(12));
				video.setDirector_name(cursor.getString(3));
				video.setGenreName(cursor.getString(5));
				video.setYear(cursor.getString(15));
				video.setDuration(Integer.parseInt(cursor.getString(9)));
				video.setRating(cursor.getString(10));
				video.setTagline(cursor.getString(11));
				video.setDescription(cursor.getString(7));
				video.setFanArt(cursor.getString(4));
				video.setMovie_id(cursor.getInt(2));
				videoValues.add(video);
			}
		}
		return videoValues;
	}
	
	public void DeleteProfileDataIfExists(Long selectId){
		database.execSQL("DELETE FROM "
				+ MilanClimaxDBHelper.AUDIO_SONG_TABLE_NAME + " WHERE "
				+ MilanClimaxDBHelper.COLUMN_PROFILE_ID + " = " + selectId);
		database.execSQL("DELETE FROM " + MilanClimaxDBHelper.RECENTLY_ADDED_AUDIO_TABLE_NAME);
		database.execSQL("DELETE FROM " + MilanClimaxDBHelper.VIDEO_TABLE_NAME
				+ " WHERE " + MilanClimaxDBHelper.COLUMN_PROFILE_ID + " = "
				+ selectId);
		database.execSQL("DELETE FROM "
				+ MilanClimaxDBHelper.AUDIO_ARTIST_TABLE_NAME + " WHERE "
				+ MilanClimaxDBHelper.COLUMN_PROFILE_ID + " = " + selectId);
		database.execSQL("DELETE FROM "
				+ MilanClimaxDBHelper.RECENTLY_ADDED_VIDEO_TABLE_NAME);
		database.execSQL("DELETE FROM "
				+ MilanClimaxDBHelper.AUDIO_TABLE_NAME + " WHERE "
				+ MilanClimaxDBHelper.COLUMN_PROFILE_ID + " = " + selectId);
	}

	public void close() {
		opSqLiteOpenHelper.close();
	}

}
