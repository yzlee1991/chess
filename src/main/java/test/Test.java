package test;

import java.util.Optional;



import javafx.application.Application;  
import javafx.geometry.Insets;  
import javafx.stage.Stage;  
import javafx.scene.Scene;  
import javafx.scene.control.Alert;  
import javafx.scene.control.Alert.AlertType;  
import javafx.scene.control.Button;  
import javafx.scene.control.ButtonType;  
import javafx.scene.control.Label;  
import javafx.scene.control.RadioButton;  
import javafx.scene.control.TextArea;  
import javafx.scene.control.ToggleGroup;  
import javafx.scene.layout.GridPane;  
import javafx.scene.layout.Pane;  
import javafx.scene.layout.VBox;  
import javafx.scene.shape.Circle;  
import javafx.scene.shape.Line;  
import javafx.scene.text.Font;  
  
/* ----------------------  
 *   
    南昌大学 计科142级 周天钜  
      
    毕业设计 基于α-β剪枝树的五子棋人机博弈系统  
      
    完成时间 2018.4.1  
      
------------------------*/  
  
public class Test extends Application {  
  
    public static Board[][] board = new Board[14][14]; // 绘制棋盘 大小14X14  
    public static boolean AIPlay = true; // 为false为人人对战,为true时为和电脑下棋  
    public static boolean AIToken = false; // 为false电脑执白,为true电脑执黑  
    public static char whoseTurn = ' '; // 判断是哪方下棋,在后面的代码中会先置whoseTurn为'B'即默认为黑棋先下  
    public static TextArea textArea = new TextArea(); // 文本域 输出消息用    
    public static Circle[][] arrayCircle = new Circle[14][14];// 将棋子存入arrayCircle中，便于后面的重新开始及悔棋操作  
    public static int step = 0; // 用于标记当前落子的步数  
  
    public static AI robot = new AI(); // AI类  
  
//    private Image image = new Image("timg.jpg"); // 背景图片  
//    private Media media = new Media(getClass().getClassLoader().getResource("BGM.mp3").toString());  
    private Button btSet = new Button("游戏设置");  
    private Button btStart = new Button("开始游戏");  
    private Button btRegret = new Button("悔    棋");  
    private Button btSurrender = new Button("认    输");  
    private Button btExit = new Button("结束游戏");  
  
    // 弹出式窗口，分别为点击游戏设置、游戏投降即退出游戏时弹出一个确认窗口  
    private Alert startConfirm = new Alert(AlertType.CONFIRMATION, "该局对战尚未结束，是否开始新游戏?");  
    private Alert setConfirm = new Alert(AlertType.INFORMATION);  
    private Alert surrenderConfirm = new Alert(AlertType.CONFIRMATION, "是否认输？");  
    private Alert exitConfirm = new Alert(AlertType.CONFIRMATION, "是否退出游戏？");  
    private Alert winner = new Alert(AlertType.INFORMATION);  
  
    @Override // 主面板  
    public void start(Stage primaryStage) {  
  
    	
        try {  
            // 背景图片  
            Pane pane = new Pane();  
//            pane.getChildren().add(new ImageView(image));  
  
            //背景音乐  
//            MediaPlayer mediaPlayer = new MediaPlayer(media);  
//            mediaPlayer.setVolume(30);  
//            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);  
//            mediaPlayer.setAutoPlay(true);  
  
            //初始化棋盘  
            GridPane gridPane = new GridPane();  
            for (int i = 0; i < 14; i++)  
                for (int j = 0; j < 14; j++)  
                    gridPane.add(board[i][j] = new Board(i, j), i, j);  
  
            // 把棋盘 放在pane中间  
            gridPane.setLayoutX(150);  
            gridPane.setLayoutY(30);  
  
            // 绘制按钮 添加到VBOX中 VBOX在后续会添加到Pane的左侧  
            VBox vBox = new VBox(50);  
            vBox.setPadding(new Insets(20, 20, 20, 20));  
            vBox.setLayoutY(100);  
            vBox.getChildren().addAll(btSet, btStart, btRegret, btSurrender, btExit);  
  
            // 游戏设置按钮  
            btSet.setOnAction(e -> {  
                // 点击 游戏设置按钮 弹出一个新的面板 游戏的所有设置在此面板中调节  
                // 只允许在游戏未开始时进行设置  
                if (whoseTurn == ' ') {  
                    Stage settingStage = new Stage();  
                    Pane settingPane = new Pane();  
                    ToggleGroup group1 = new ToggleGroup();// group1对应radioButton1、2  
                                                            // 对应label1  
                    ToggleGroup group2 = new ToggleGroup();  
                    ToggleGroup group3 = new ToggleGroup();  
                    Label label1 = new Label("对战模式");  
                    Label label2 = new Label("人机对战设置");  
                    Label label3 = new Label("游戏难度");  
                    label1.setLayoutX(30);  
                    label1.setLayoutY(15);  
                    label2.setLayoutX(30);  
                    label2.setLayoutY(100);  
                    label3.setLayoutX(30);  
                    label3.setLayoutY(190);  
  
                    // 添加单选按钮 这一块为对战模式设置 默认为人人对战  
                    RadioButton rb1 = new RadioButton("人机对战");  
                    RadioButton rb2 = new RadioButton("人人对战");  
                    rb1.setLayoutX(50);  
                    rb1.setLayoutY(50);  
                    rb2.setLayoutX(200);  
                    rb2.setLayoutY(50);  
                    // 将rb1 rb2加入group中  
                    rb1.setToggleGroup(group1);  
                    rb2.setToggleGroup(group1);  
                    rb1.setSelected(true);  
                    Line line1 = new Line(0, 80, 350, 80);  
  
                    // 这一块为人机对战设置 默认为电脑执白  
                    RadioButton rb3 = new RadioButton("电脑执黑");  
                    RadioButton rb4 = new RadioButton("电脑执白");  
                    rb3.setLayoutX(50);  
                    rb3.setLayoutY(130);  
                    rb4.setLayoutX(200);  
                    rb4.setLayoutY(130);  
                    rb3.setToggleGroup(group2);  
                    rb4.setToggleGroup(group2);  
                    rb4.setSelected(true);  
                    Line line2 = new Line(0, 165, 350, 165);  
  
                    // 这一块为游戏难度设置  
                    RadioButton rb5 = new RadioButton("简单");  
                    RadioButton rb6 = new RadioButton("正常");  
                    RadioButton rb7 = new RadioButton("困难");  
  
                    rb5.setLayoutX(50);  
                    rb5.setLayoutY(220);  
                    rb6.setLayoutX(200);  
                    rb6.setLayoutY(220);  
                    rb7.setLayoutX(50);  
                    rb7.setLayoutY(250);  
                    rb5.setToggleGroup(group3);  
                    rb5.setSelected(true);  
                    rb6.setToggleGroup(group3);  
                    rb7.setToggleGroup(group3);  
                    Line line3 = new Line(0, 280, 350, 280);  
  
                    // 确认按钮与取消按钮  
                    Button confirm = new Button("确认");  
                    Button cancel = new Button("取消");  
                    confirm.setLayoutX(90);  
                    confirm.setLayoutY(315);  
                    cancel.setLayoutX(190);  
                    cancel.setLayoutY(315);  
                    // 添加按钮事件 点击确认将修改游戏参数 点击取消直接关闭设置窗口  
                    confirm.setOnAction(event -> {  
                        // 若选中第一个radioButton则为人机对战，否则为人人对战  
                        if (rb1.isSelected() == true) {  
                            AIPlay = true;  
                            rb1.setSelected(true);  
                        } else if (rb2.isSelected() == true) {  
                            AIPlay = false;  
                            rb2.setSelected(true);  
                        }  
                        // 若选中第三个按钮则为电脑执黑 ，否则为电脑执白  
                        if (rb3.isSelected() == true) {  
                            AIToken = true;  
                            rb3.setSelected(true);  
                        } else if (rb4.isSelected() == true) {  
                            AIToken = false;  
                            rb4.setSelected(true);  
                        }  
                        settingStage.close();  
                    });  
                    cancel.setOnAction(event -> {  
                        settingStage.close();  
                    });  
  
                    settingPane.getChildren().addAll(label1, rb1, rb2, line1, label2, rb3, rb4, line2, label3, rb5, rb6,  
                            rb7, line3, confirm, cancel);  
                    Scene settingScene = new Scene(settingPane);  
                    settingScene.getStylesheets().add(getClass().getResource("textStyle.css").toExternalForm());  
                    settingStage.setHeight(400);  
                    settingStage.setWidth(350);  
                    settingStage.setScene(settingScene);  
                    settingStage.setTitle("游戏设置");  
                    settingStage.setResizable(false); // 设置面板大小不可变  
                    settingStage.show();  
                } else {  
                    setConfirm.setHeaderText(null);  
                    setConfirm.setContentText("请先结束这局游戏后再进行游戏设置！");  
                    setConfirm.showAndWait();  
                }  
            });  
  
            // 游戏开始按钮 点击此按钮初始化棋盘开始游戏  
            btStart.setOnAction(e -> {  
                // 点击此按钮前whoseTurn=' '无法在棋盘上落子  
                if (whoseTurn != ' ') {  
                    startConfirm.setTitle("开始游戏");  
                    startConfirm.setHeaderText(null);  
                    Optional<ButtonType> result = startConfirm.showAndWait();  
                    if (result.isPresent() && result.get() == ButtonType.OK)  
                        reSet();  
                }  
                reSet();  
  
            });  
  
            // 悔棋按钮  
            btRegret.setOnAction(e -> {  
                // 若为人机对战 则点一次悔棋退两步 即电脑退一步 人退一步  
                if (AIPlay == true && step > 2 && whoseTurn != ' ') {  
                    regret();  
                    regret();  
                }  
                // 若为人人对战则点一次悔棋退一步  
                if (AIPlay == false && step > 1 && whoseTurn != ' ') {  
                    regret();  
                }  
                if (step % 2 == 0)// 若当前步数为奇数 则黑棋下 否则白棋下  
                    whoseTurn = 'B';  
                else if (step % 2 == 1)  
                    whoseTurn = 'W';  
            });  
  
            // 投降按钮  
            btSurrender.setOnAction(e -> {  
                surrenderConfirm.setTitle("认输");  
                surrenderConfirm.setHeaderText(null);  
                if (whoseTurn == 'B') { // 如果当前是黑子下 认输时输出白棋获胜  
                    Optional<ButtonType> result = surrenderConfirm.showAndWait();  
                    // 点击确认按钮则认输  
                    if (result.isPresent() && result.get() == ButtonType.OK) {  
                        textArea.insertText(0, "白棋获胜！" + '\n' + '\n');  
                        winner.setContentText("白棋获胜！！！");  
                        winner.setHeaderText(null);  
                        winner.showAndWait();  
                    }  
  
                } else if (whoseTurn == 'W') {  
                    Optional<ButtonType> result = surrenderConfirm.showAndWait();  
                    if (result.isPresent() && result.get() == ButtonType.OK) {  
                        textArea.insertText(0, "黑棋获胜！" + '\n' + '\n');  
                        winner.setContentText("黑棋获胜！！！");  
                        winner.setHeaderText(null);  
                        winner.showAndWait();  
                    }  
                }  
                whoseTurn = ' ';  
            });  
  
            // 游戏结束按钮  
            btExit.setOnAction(e -> {  
                exitConfirm.setTitle("退出游戏");  
                exitConfirm.setHeaderText(null);  
                Optional<ButtonType> result = exitConfirm.showAndWait();  
                // 点击确认按钮则退出游戏  
                if (result.isPresent() && result.get() == ButtonType.OK)  
                    System.exit(-1);  
            });  
  
            // 绘制文本框 文字大小为16号字体 规定最大宽度和最小高度 放在pane右侧  
            textArea.setFont(new Font(16));  
            textArea.setMaxWidth(315);  
            textArea.setMinHeight(500);  
            textArea.setLayoutX(860);  
            textArea.setLayoutY(100);  
            textArea.setEditable(false);  
            textArea.insertText(0, "点击开始游戏进行游戏！" + '\n' + '\n');  
  
            pane.getChildren().addAll(vBox, gridPane, textArea);  
            Scene scene = new Scene(pane);  
            // 使用buttonStyle中的按钮样式  
            scene.getStylesheets().add(getClass().getResource("buttonStyle.css").toExternalForm());  
            primaryStage.setScene(scene);  
            primaryStage.setTitle("五子棋Demo");  
            primaryStage.setResizable(false); // 设置棋盘大小不可变  
            primaryStage.show();  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
    }  
  
    // 重置棋盘函数  
    public void reSet() {  
        textArea.insertText(0, "游戏开始！" + '\n' + '\n');  
        for (int i = 0; i < 14; i++)  
            for (int j = 0; j < 14; j++) {  
                board[i][j].getChildren().remove(arrayCircle[i][j]);  
                board[i][j].token = ' ';  
            }  
        whoseTurn = 'B';  
        step = 0;  
        // 若为人机对战且电脑执黑子 电脑第一子落在行7竖7  
        if (AIToken == true) {  
            board[6][6].setToken(whoseTurn, 6, 6, true);  
            whoseTurn = 'W';  
        }  
    }  
  
    // --------------悔棋函数-------------------  
    public void regret() {  
        for (int i = 0; i < 14; i++) {  
            for (int j = 0; j < 14; j++) {  
                if (board[i][j].thisStep == step) { // 若棋子当前步数为所下步数则清除该棋子  
                    board[i][j].getChildren().remove(arrayCircle[i][j]);  
                    board[i][j].token = ' ';  
                    if (whoseTurn == 'W' && AIPlay == false)// 若下一步为白棋下即当前是黑棋下完，则在文本框输出黑方悔棋  
                        textArea.insertText(0, "黑方悔棋！" + '\n' + '\n');  
                    else if (whoseTurn == 'B' && AIPlay == false)  
                        textArea.insertText(0, "白方悔棋！" + '\n' + '\n');  
                }  
            }  
        }  
        step--;  
    }  
  
    
    public static void main(String[] args){  
        launch(args); 
    }  
}  
