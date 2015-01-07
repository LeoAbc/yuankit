package com.unknown.io;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSonFile {

	public static void main(String[] args) throws IOException, JSONException {
		StringBuilder strBuilder = new StringBuilder();
		
		FileUtils.readFile("d:\\1.json", strBuilder, null, new LineIterator<StringBuilder>() {
			@Override
			public void readLine(StringBuilder context, String line) throws IOException {
				context.append(line);
			}			
		});
		
		JSONObject navagate = new JSONObject(strBuilder.toString());		
		navagate = navagate.getJSONObject("content");
		navagate = navagate.getJSONObject("page");
		navagate = navagate.getJSONObject("voltron_unified_search_json");
		navagate = navagate.getJSONObject("search");
		JSONArray results = navagate.getJSONArray("results");
		for(int i = 0; i < results.length(); i++){
			JSONObject pp = results.getJSONObject(i).getJSONObject("person");
			String ss = pp.getString("firstName") + " " + pp.getString("lastName");
			ss = pp.getString("fmt_location");
			ss = pp.getString("fmt_industry");
			ss = pp.getString("fmt_headline");
			pp = pp.getJSONObject("actions");
			pp = pp.getJSONObject("secondaryActions");
			JSONArray actions = pp.getJSONArray("secondaryActionsList");
			ss = actions.getJSONObject(3).getString("link_inviteMemberFromProfile_1");
		}
		navagate = navagate.getJSONObject("baseData");
		navagate = navagate.getJSONObject("resultPagination");
		JSONObject nextURL = navagate.getJSONObject("nextPage");		
	}

}
