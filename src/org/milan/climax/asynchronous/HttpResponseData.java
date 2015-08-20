package org.milan.climax.asynchronous;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.JsonReader;

public class HttpResponseData extends AsyncTask<String, JsonReader, HttpResponse> {

	private DefaultHttpClient httpclient;
	private HttpPost httpPost;
	private StringEntity stringEntity;
	private HttpResponse httpResponse;

	@SuppressLint("NewApi")
	@Override
	protected HttpResponse doInBackground(String...params) {
		// TODO Auto-generated method stub
		httpResponse = null;
		try {
			httpclient = new DefaultHttpClient();
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 2000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			httpclient.setParams(httpParameters);
			httpPost = new HttpPost(params[0]);
			stringEntity = new StringEntity(params[1]);
			httpPost.setEntity(stringEntity);
			httpPost.setHeader("Content-type", "application/json");
			httpResponse = httpclient.execute(httpPost);
			
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return httpResponse;
	}
}
