package bug;

import java.io.File;

import javafx.scene.control.TextArea;
import javafx.scene.shape.Circle;

/**
 * author :lzy
 * date   :2018年4月11日上午10:00:30
 */

public class Static {

	public static Board[][] board = new Board[14][14]; // 绘制棋盘 大小14X14  
    public static boolean AIPlay = true; // 为false为人人对战,为true时为和电脑下棋  
    public static boolean AIToken = false; // 为false电脑执白,为true电脑执黑  
    public static char whoseTurn = ' '; // 判断是哪方下棋,在后面的代码中会先置whoseTurn为'B'即默认为黑棋先下  
    public static TextArea textArea = new TextArea(); // 文本域 输出消息用    
    public static Circle[][] arrayCircle = new Circle[14][14];// 将棋子存入arrayCircle中，便于后面的重新开始及悔棋操作  
    public static int step = 0; // 用于标记当前落子的步数  
  
    public static AI robot = new AI(); // AI类  
    
    public static String basePath=System.getProperty("user.home")+File.separator+"solo_chess";
	
}
