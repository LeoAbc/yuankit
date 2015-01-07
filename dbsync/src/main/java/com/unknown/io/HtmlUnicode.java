package com.unknown.io;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class HtmlUnicode {

	public static void main(String[] args) throws IOException {
		StringBuilder strBuilder = new StringBuilder(4096);
		System.out.println("开始处理文件[" + args[0] + "]");
		long begin = System.currentTimeMillis();
		FileUtils.readFile(args[0], strBuilder, null, new LineIterator<StringBuilder>() {
			@Override
			public void readLine(StringBuilder context, String line) throws IOException {				
				int i0 = line.indexOf("&#");
				int i1 = 0;
				while(i0 >= 0){
					context.append(line.substring(i1, i0));
					i1 = line.indexOf(';', i0);
					if(i1 >= 0){
						int radis = 10;
						String number = line.substring(i0 + 2, i1).toLowerCase();
						if(number.contains("x")){
							radis = 16;
						}
						number = number.replace("0x", "").replace("x", "");
						context.append((char)(Integer.parseInt(number, radis)));
					}else{
						i1 = i0;
						break;
					}
					i0 = line.indexOf("&#", ++i1);
				}
				if(i1 < line.length()){
					context.append(line.substring(i1));
				}
				context.append("\r\n");
			}			
		});
		outPut(args, strBuilder);
		System.out.println("文件[" + args[0] + "]处理完毕，耗时 " + (System.currentTimeMillis() - begin));
	}


	private static void outPut(String[] args, StringBuilder strBuilder)
			throws IOException {
		String target = null;
		if(args.length > 1){
			target = args[1];
		}
		Writer writer = null;
		try{
			writer = getWriter(target);
			writer.write(strBuilder.toString());
		}finally{
			if(writer != null){
				writer.flush();
				writer.close();
			}
		}
	}

	
	public static Writer getWriter(String target) throws IOException{
		if(target != null && !target.isEmpty()){
			return new FileWriter(target);
		}		
		return new OutputStreamWriter(System.out);
	}
}
