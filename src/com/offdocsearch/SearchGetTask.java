package com.offdocsearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.offdocsearch.model.SearchListModel;
import android.os.AsyncTask;

// Working with AsyncTasks for better android client performance in future

public class SearchGetTask extends AsyncTask<String, Void, String[]>{

	@Override
	protected String[] doInBackground(String... params) {
		HttpClient client = new DefaultHttpClient();
		HttpGet httpClient = new HttpGet("http://192.168.1.235:8080/?qprof=" + params[0] + "&keyword=" + params[1]);
		Gson gson = new Gson();
		
		try {
			HttpResponse response = client.execute(httpClient);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			String[] answer;
			
			if(statusCode == 200) {
				HttpEntity entity = response.getEntity();
				Reader reader = new InputStreamReader(entity.getContent());
				SearchListModel listModel = gson.fromJson(reader, SearchListModel.class);
				answer = listModel.getUrlsList();
				return answer;
			}
			
		} catch(final ClientProtocolException ex) {
			ex.printStackTrace();
		} catch(final IOException ex){
			ex.printStackTrace();
		}
		return new String[]{};
	}

}
