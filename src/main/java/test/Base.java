package test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * author :lzy
 * date   :2018年4月11日上午11:41:44
 */

public class Base {

	public static void main(String[] args) throws Exception {
		
		File file=new File("E:\\javafx\\icon");
		if(file.isDirectory()){
			for(File f:file.listFiles()){
				System.out.println(f.getName());
			}
		}
		
//		final Base64.Decoder decoder = Base64.getDecoder();
//		final Base64.Encoder encoder = Base64.getEncoder();
//		final String text = "字串文字";
//		final byte[] textByte = text.getBytes("UTF-8");
//		//编码
//		final String encodedText = encoder.encodeToString(textByte);
//		System.out.println(encodedText);
//		//解码
//		System.out.println(new String(decoder.decode(encodedText), "UTF-8"));
//
//		final Base64.Decoder decoder = Base64.getDecoder();
//		final Base64.Encoder encoder = Base64.getEncoder();
//		final String text = "字串文字";
//		final byte[] textByte = text.getBytes("UTF-8");
//		//编码
//		final String encodedText = encoder.encodeToString(textByte);
//		System.out.println(encodedText);
//		//解码
//		System.out.println(new String(decoder.decode(encodedText), "UTF-8"));
		
	}
	
	
	
	public static void  encode() throws Exception{
		File file=new File("E:\\javafx\\icon\\account.png");
		Long length=file.length();
		byte[] bytes=new byte[length.intValue()];
		FileInputStream fis=new FileInputStream(file);
		BufferedInputStream bis=new BufferedInputStream(fis);
		bis.read(bytes);
		bis.close();
		
		final Base64.Encoder encoder = Base64.getEncoder();
		final String encodedText = encoder.encodeToString(bytes);
		
		save(encodedText);
		String str=read();
		
		final Base64.Decoder decoder = Base64.getDecoder();
		byte[] b=decoder.decode(str);
		save(b);
	}
	
	
	public static void save(String str) throws Exception{
		FileWriter fw=new FileWriter("E:\\javafx\\icon\\1.txt");
		fw.write(str);
		fw.flush();
		fw.close();
	}
	
	public static String read() throws Exception{
		FileReader fr=new FileReader("E:\\javafx\\icon\\1.txt");
		BufferedReader br=new BufferedReader(fr);
		return br.readLine();
	}
	
	public static void save(byte[] b) throws Exception{
		FileOutputStream fos=new FileOutputStream("E:\\javafx\\icon\\tt\\1.png");
		fos.write(b);
		fos.flush();
		fos.close();
		
				
	}
	
}
