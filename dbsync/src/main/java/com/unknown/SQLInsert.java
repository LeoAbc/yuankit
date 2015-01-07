package com.unknown;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SQLInsert {

	public static void main(String[] args) throws IOException {
	
		BufferedReader reader = new BufferedReader(new FileReader("d:\\11.txt"));
		
		String line;
		
		while((line = reader.readLine()) != null){
			String[] fields = line.split("\t");
			System.out.println(String.format("INSERT INTO fx_octopus_task_detail_keyword_weekly(`keyword`,`businesstype`,`keywordtype`) VALUES('%s','%s','%s');", fields[0], fields[1], fields[2]));
			//System.out.println(String.format("INSERT INTO fx_octopus_task_detail_keyword_monthly(`keyword`,`businesstype`,`keywordtype`) VALUES('%s','¹¥ÂÔÉçÇø','%s');", fields[0], fields[2]));
			//System.out.println(String.format("update fx_octopus_task_detail_keyword_weekly set keywordtype = '%s' where keyword = '%s' and businesstype='¶È¼Ù';", fields[2], fields[0]));
		}	

		reader.close();
	}

}
