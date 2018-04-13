package bug;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * author :lzy
 * date   :2018年4月12日下午5:39:17
 */

public class A {
	private static Set<String> filterType=new HashSet<String>(){{
    	add("txt");
    	add("doc");
    	add("docx");
    	add("wps");
    	add("xls");
    	add("bmp");
    	add("jpeg");
    	add("png");
    }};
	public static void main(String[] args) {
		
	    String a="png";
	    String b="pnewrg";
	    
	    System.out.println(filterType.contains(a));
	    System.out.println(filterType.contains(b));
	    
	}
	
}
