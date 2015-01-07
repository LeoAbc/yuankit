package com.unknown.io;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Base64Encoder {

	public static final String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	
	public static byte[] decode(String base64){
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		char[] base64Encode = base64.toCharArray();
		int i = 0;
		int lastPart = 0;
		int turn = 0;
		while(i < base64Encode.length && base64Encode[i] != '='){
			int current = decodeChar(base64Encode[i]);
			turn = i % 4;			
			if(turn != 0){
				byteBuffer.write((byte)(lastPart | current >> (6 - turn * 2)));
				lastPart = current << (turn + 1) * 2;
			}else{
				lastPart = current << 2;
			}
			i++;
		}		
		if(i < base64Encode.length){
			byteBuffer.write((byte)lastPart);
		}	
		return byteBuffer.toByteArray();
	}
	
	private static int decodeChar(char c){
		int index = alpha.indexOf(c);		
		return index < 0 ? 0 : index;
	}
	
	public static void main(String[] args) throws IOException{
//		final String[] content = {""};
//		FileUtils.readFile("d:\\base64.txt", null, null, new LineIterator<String[]>() {
//
//			@Override
//			public void readLine(String[] context, String line)throws IOException {
//				content[0] = line;
//			}			
//		});
//		FileOutputStream fileOut = new FileOutputStream("11.jpg");
//		fileOut.write(decode(content[0]));
//		fileOut.flush();
//		fileOut.close();
		byte[] dd = decode("1+LK");
		System.out.println(dd.length);
	}
}
