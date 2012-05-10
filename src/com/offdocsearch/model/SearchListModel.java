package com.offdocsearch.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class SearchListModel {

	@SerializedName("found")
	private List<SearchModel> list;	
	
	public String[] getUrlsList(){
		String[] answer;
		if (list != null && !list.isEmpty()) {
			answer = new String[list.size()];
			for(int i = 0; i < list.size(); i++) {
				answer[i] = list.get(i).getUrl();
			}
		} else {
			answer = new String[0];
		}
		return answer;
	}
}
