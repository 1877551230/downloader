package sample;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;


import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Main extends Application {
     TextField textField = new TextField();
     Button button = new Button("下载");
     Text text1 = new Text();
     Text text2 = new Text();
     ProgressBar pb = new ProgressBar(0);
    GridPane gridPane = new GridPane();
    static String filePath;
    Socket socket = new Socket("127.0.0.1",9992);
    DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

    public Main() throws IOException {
    }

    @Override
    public void start(Stage primaryStage) throws Exception{


        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25,25,25,25));
        Scene scene = new Scene(gridPane,400,350);
        primaryStage.setTitle("下载器 ");
        primaryStage.setScene(scene);
        primaryStage.show();
        Text text = new Text("请输入文件路径");
        text.setFont(Font.font("Tahoma", FontWeight.NORMAL,20));
        gridPane.add(text,0,0,2,1);
        gridPane.add(text2,1,7);
        gridPane.add(textField,0,1);
gridPane.add(pb,0,8);

        gridPane.add(text1,1,6);
        gridPane.add(button,0,2);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            send();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                   }
                }).start();


            }
        });
    }

public void send() throws IOException {
        filePath = textField.getText();
        dos.writeUTF(filePath);
        dos.flush();
        if (dis.readBoolean()) {

            text1.setText("开始下载");
            download();
        } else {
            text1.setFill(Color.RED);
            text1.setText("文件不存在,重新输入");
        }

}
public void download() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //接收文件的名字
                    String fileName = dis.readUTF();

                    //接收文件的长度
                    String length = dis.readUTF();
                    double d = Double.parseDouble(length);

                    //输出
                    text2.setText("文件名为:"+fileName+" 文件长度:"+length);

                    //从网络流上接收文件数据,并存储到内存,把内存数据输出到本地硬盘
                    //System.out.println("开始接收文件...");
                    //构建本地流输出
                    DataOutputStream dos_local = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("D:/ab/"+fileName)));
                    //构建缓冲

                    byte[] buffer = new byte[1024*4];//服务端和客户端缓冲大小一样
                    //循环从网络流中读入数据进内存
                    int progress=0;
                    while(true){

                        int len = -1;
                        if(dis!=null){
                            //从网络上读数据
                            len = dis.read(buffer);

                        }
                        if(len==-1){
                            break;
                        }

                        dos_local.write(buffer,0,len);
                        progress += buffer.length;
                        System.out.println(progress/d);
                        pb.setProgress(progress/d);
                    }
                    dos_local.close();
                    dis.close();
                    dos.close();
                    socket.close();
                    text1.setText("接收文件完毕");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

}
    public static void main(String[] args) throws Exception{

        launch(args);







    }
}
