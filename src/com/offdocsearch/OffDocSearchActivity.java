package com.offdocsearch;

import android.app.ListActivity;


import com.offdocsearch.OffDocSearchService.SearchBinder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

//main activity of application.
//inflating main.xml as header for main listactivity

public class OffDocSearchActivity extends ListActivity implements OnClickListener{
	
	private EditText searchEditText;
	private RadioButton rbItPro, rbDev;
	private OffDocSearchService searchService;
	private boolean bound;
	private SearchServiceConnection serviceConnection;
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    ListView listView = getListView();
	    View view = getLayoutInflater().inflate(R.layout.main, null);
	    final Button button = (Button) view.findViewById(R.id.searchButton);
	    button.setOnClickListener(this);
	    searchEditText = (EditText) view.findViewById(R.id.searchText);
	    rbDev = (RadioButton) view.findViewById(R.id.radioButtonDev);
	    rbItPro = (RadioButton) view.findViewById(R.id.radioButtonItPro);
	    
	    serviceConnection = new SearchServiceConnection();
	    listView.addHeaderView(view);
	    
	    String[] lookupAnswers = new String[] {};
	    setArrayAdapter(lookupAnswers);
 
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String httpUrl = (String) parent.getItemAtPosition(position);
				if (!httpUrl.startsWith("https://") && !httpUrl.startsWith("http://")){
					httpUrl = "http://" + httpUrl;
				}
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(httpUrl));
				startActivity(browserIntent);
			}});
	   	}

	//binding to service on start
	
	@Override
	protected void onStart() {
		super.onStart();
		Intent binderIntent = new Intent(this, OffDocSearchService.class);
		bindService(binderIntent, serviceConnection, Context.BIND_AUTO_CREATE);
	}
	
	//unbounding from service
	
	@Override
	protected void onStop() {
		super.onStop();
		if(bound) {
			unbindService(serviceConnection);
			bound = false;
		}
	}
	
	//on click "search" button sending checked profile and query from "edittext" element to web server with search engine 
	
	public void onClick(final View v) {
		String query =  searchEditText.getText().toString();
		String profile="";
		if (rbDev.isChecked() || rbItPro.isChecked()){
			if (rbDev.isChecked()){
				profile=getString(R.string.devProfile);
			}
			if (rbItPro.isChecked()){
				profile=getString(R.string.itproProfile);
			}
			searchService.sendGetRequest(profile, query);
			setArrayAdapter(searchService.updateResponse());
		} else {
			Toast.makeText(getApplicationContext(), "Please choose between profiles!", Toast.LENGTH_SHORT).show(); 
		}
	}
	
	//connection to service

	private class SearchServiceConnection implements ServiceConnection {

		public void onServiceConnected(final ComponentName name, final IBinder service) {
			SearchBinder searchBinder = (SearchBinder) service;
			searchService = searchBinder.getService();
			bound = true;
		}

		public void onServiceDisconnected(final ComponentName name) { bound = false; }
	}

	public void setArrayAdapter(final String[] array) {
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,array));
	}
	
}
