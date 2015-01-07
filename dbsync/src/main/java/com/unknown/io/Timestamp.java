package com.unknown.io;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Timestamp {

	public static void main(String[] args) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		System.out.println(df.parse("2014-09-1").getTime());
	}

}
