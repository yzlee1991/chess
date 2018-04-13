package bug;

import javafx.scene.control.Alert;  
import javafx.scene.control.Alert.AlertType;  
import javafx.scene.layout.Pane;  
import javafx.scene.paint.Color;  
  
public class Board extends Pane {  
  
    public char token = ' '; // token B 表示black黑棋 W表示white  
    public int thisStep;// 用于标记当前落子步数  
    private Alert winner = new Alert(AlertType.INFORMATION);  
//    private Media moveSong = new Media(getClass().getClassLoader().getResource("put.mp3").toString());  
//    private Media winSong = new Media(getClass().getClassLoader().getResource("win.wav").toString());  
//    private MyCircle circle = null;  
    // 构造方法 每个格子大小为15x15 为每个格子注册一个鼠标事件  
    public Board(){  
          
    }  
      
    public Board(int x, int y) {  
        setStyle("-fx-border-color:black");  
        this.setPrefSize(50, 50);  
        this.setOnMouseClicked(e -> {  
            handleMouseClick(x, y);  
        });  
    }  
  
    // 鼠标点击事件 判断是否获胜 若没有人获胜 则改变棋子颜色继续下子  
    public void handleMouseClick(int x, int y) {  
  
        // whoseTure一开始为空 当点击开始游戏按钮时置为'B' 即黑棋开始下棋 进入下面的if分支  
        // 若不点击开始游戏按钮，即whoseTurn为空 则无法进入下面的if分支 即无法开始游戏  
        if (token == ' ' && Static.whoseTurn != ' ' && Static.AIPlay == false) {  
            // 此处为当人下了一子后 若AIPlay为true 电脑开始下棋 即人机对战 否则为人人对战  
            setToken(Static.whoseTurn, x, y,true);  
            Static.textArea.insertText(0, "估值:" + Static.robot.Evaluate(x, y, Static.whoseTurn) + " ");  
              
            if (judge(Static.whoseTurn, x, y) == true)  
                printWinner();  
            else  
                Static.whoseTurn = (Static.whoseTurn == 'B') ? 'W' : 'B'; // 改变落子者  
//            MediaPlayer mediaPlayer = new MediaPlayer(moveSong);  
//            mediaPlayer.setVolume(50);  
//            mediaPlayer.play();  
        }  
  
        //若AIPlay为True 则为人机对战  
        if (token == ' ' && Static.whoseTurn != ' ' && Static.AIPlay == true) {  
            if (Static.whoseTurn != ' ') {  
                setToken(Static.whoseTurn, x, y,true);  
                Static.textArea.insertText(0, "估值:" + Static.robot.Evaluate(x, y, Static.whoseTurn) + " ");  
                if (judge(Static.whoseTurn, x, y) == true)  
                    printWinner();  
                else  
                    Static.whoseTurn = (Static.whoseTurn == 'B') ? 'W' : 'B'; // 改变落子者  
//                MediaPlayer mediaPlayer = new MediaPlayer(moveSong);  
//                mediaPlayer.setVolume(50);  
//                mediaPlayer.play();  
            }  
            //若whoseTurn不为空 即未分出胜负，则执行搜索函数，根据人下的子，搜索AI的下一步落子  
            if (Static.whoseTurn != ' ')  
                Static.robot.search(x,y);           
        }  
    }  
  
    // setToken函数 若token为B则在棋盘上鼠标点击位置绘制黑子 为W则绘制白子  
    public void setToken(char c, int x, int y,boolean flag) {  
  
        token = c;  
        MyCircle circle = null;  
        int row = x + 1, column = y + 1;// 行数 列数  
        Static.step++;  
        Static.board[x][y].thisStep = Static.step;// 落一子步数+1 thisStep用于判断悔棋的操作  
        if (token == 'B') {  
            circle = new MyCircle(20, Color.BLACK);  
            // 在textArea上输出相应的落子位置 具体效果为——步数:1 黑棋: 行 3, 列 10  
            if(flag==true)  
            Static.textArea.insertText(0, "步数:" + Static.step + " 黑棋:行" + column + ",列" + row + '\n' + '\n');  
        } else if (token == 'W') {  
            circle = new MyCircle(20, Color.WHITE);  
            if(flag==true)  
            Static.textArea.insertText(0, "步数:" + Static.step + " 白棋:行" + column + ",列" + row + '\n' + '\n');  
        }  
        // 将圆心绑定到落子的方格中间  
        circle.centerXProperty().bind(Static.board[x][y].widthProperty().divide(2));  
        circle.centerYProperty().bind(Static.board[x][y].heightProperty().divide(2));  
        Static.board[x][y].getChildren().add(circle);  
        Static.board[x][y].token = c;  
        Static.arrayCircle[x][y] = circle;  
    }  
  
    /* 
     * -------胜 负 判 断函数---------- 
     */  
    public boolean judge(char whoseTurn, int x, int y) {  
        boolean flag = false;  
        if (checkCount(whoseTurn, x, y, 1, 0) >= 5)  
            flag = true;  
        else if (checkCount(whoseTurn, x, y, 0, 1) >= 5)  
            flag = true;  
        else if (checkCount(whoseTurn, x, y, 1, -1) >= 5)  
            flag = true;  
        else if (checkCount(whoseTurn, x, y, 1, 1) >= 5)  
            flag = true;  
        return flag;  
    }  
  
    // 判断连子函数  
    public int checkCount(char whoseTurn, int x, int y, int xChange, int yChange) {  
        int count = 1;  
        int tempX = xChange;  
        int tempY = yChange;  
        while (x + xChange >= 0 && x + xChange < 14 && y + yChange >= 0 && y + yChange < 14  
                && whoseTurn == Static.board[x + xChange][y + yChange].token) {  
            count++;  
            if (xChange != 0)  
                xChange++;  
            if (yChange != 0) {  
                if (yChange > 0)  
                    yChange++;  
                else  
                    yChange--;  
            }  
        }  
  
        xChange = tempX;  
        yChange = tempY;  
  
        while (x - xChange >= 0 && x - xChange < 14 && y - yChange >= 0 && y - yChange < 14  
                && whoseTurn == Static.board[x - xChange][y - yChange].token) {  
            count++;  
            if (xChange != 0)  
                xChange++;  
            if (yChange != 0) {  
                if (yChange > 0)  
                    yChange++;  
                else  
                    yChange--;  
            }  
        }  
        return count;  
    }  
  
    // 在文本域输出是谁获胜 若无人获胜则改变下棋方继续游戏  
    public void printWinner() {  
          
        //输出胜利音效  
//        MediaPlayer mediaPlayer = new MediaPlayer(winSong);  
//        mediaPlayer.setVolume(50);  
//        mediaPlayer.play();  
          
        if (Static.whoseTurn == 'B') {  
            Static.whoseTurn = ' ';  
            Static.textArea.insertText(0, "黑棋获胜！点击开始游戏进行新对战！" + '\n' + '\n');  
            winner.setContentText("黑棋获胜！！！");  
            winner.setHeaderText(null);  
            winner.showAndWait();  
        } else if (Static.whoseTurn == 'W') {  
            Static.whoseTurn = ' ';  
            Static.textArea.insertText(0, "白棋获胜！点击开始游戏进行新对战！" + '\n' + '\n');  
            winner.setContentText("白棋获胜！！！");  
            winner.setHeaderText(null);  
            winner.showAndWait();  
        }         
    }  
}  
