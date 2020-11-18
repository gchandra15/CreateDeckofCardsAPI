package com.shs.api.utlities;

import java.util.ArrayList;



public class tagValueFromResponse {

	public String tagValueString(String s,String tagName) {
		
		//System.out.println("Splitting from Response");
		ArrayList<String> AL = new ArrayList<String>();
		AL.add(s);
		String AL2 = AL.get(0).split(tagName)[1];
		
		String AL3 = AL2.split("<")[0];
		return AL3;
	}



}