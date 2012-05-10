package com.offdocsearch;
import java.util.concurrent.ExecutionException;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

//service for sending requests and getting responses

public class OffDocSearchService extends Service {

	private SearchGetTask searchTask;
	
	//standard binder to bind with service from activity
	
	@Override
	public IBinder onBind(final Intent intent) {
		return new SearchBinder();
	}
	
	
	
	public class SearchBinder extends Binder {
		public OffDocSearchService getService() {
			return OffDocSearchService.this;
		}
	}

	//sending requests
	
	public void sendGetRequest(final String profile, final String query) {
		searchTask = new SearchGetTask();
		searchTask.execute(profile, query);
	}
	
	public String[] updateResponse() {
		try {
			return searchTask.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return new String[]{};
	}
	
}
