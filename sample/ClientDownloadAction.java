package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

/**
 * 下载时客户端的线程类
 */

public class ClientDownloadAction extends Application implements Runnable {
    Label label;
    GridPane gridPane;

    VBox vBox;
    public static int i = -1;
     double dd = 0;
    ProgressBar progressBar = new ProgressBar(0);

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    String filePath;

    public ClientDownloadAction(String filePath,GridPane gridPane,VBox vBox) {
        this.filePath = filePath;
        this.gridPane = gridPane;

        this.vBox = vBox;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("10.8.38.136", 9992);
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            // GridPane gridPane = new GridPane();

            dos.writeChar('D');
            dos.writeUTF(filePath);
            dos.flush();
            if (dis.readBoolean()) {

                ProgressBar progressBar = new ProgressBar(0);



                i=100;
                //接收文件的名字
                String fileName = dis.readUTF();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        HBox hBox = new HBox();
                        label = new Label(fileName);
                        hBox.getChildren().add(label);
                        hBox.getChildren().add(progressBar);
                        vBox.getChildren().add(hBox);
                    }
                });

                //接收文件的长度
                String length = dis.readUTF();
                double d = Double.parseDouble(length);
                //输出
                // text2.setText("文件名为:" + fileName + " 文件长度:" + length);

                //从网络流上接收文件数据,并存储到内存,把内存数据输出到本地硬盘
                //System.out.println("开始接收文件...");
                //构建本地流输出
                DataOutputStream dos_local = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("d:/ac/" + fileName)));
                //构建缓冲

                byte[] buffer = new byte[1024 * 4];//服务端和客户端缓冲大小一样
                //循环从网络流中读入数据进内存
                long progress = 0;
                int len = -1;
                while ((len = dis.read(buffer)) != -1) {
                    dos_local.write(buffer, 0, len);
                   // Main.over += buffer.length;

                    progress += buffer.length;
                    dd = progress / d;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {



                            //gridPane.add(vBox,4,3);
                            progressBar.setProgress(dd);
                        }
                    });


                    //System.out.println(progress/d);
                    // pb.setProgress(dd);
                    // progressIndicator.setProgress(dd);
                }
                dos_local.flush();
                dos_local.close();
                dis.close();
                dos.close();
                socket.close();
                System.out.println("接收文件完毕");
                // text1.setText("接收文件完毕");
                Thread.currentThread().stop();


            } else {
                // text1.setFill(Color.RED);
                // text1.setText("文件不存在,重新输入");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Stage window = new Stage();
                        window.setTitle("提示");
                        window.initModality(Modality.APPLICATION_MODAL);
                        window.setMinWidth(300);
                        window.setMinHeight(150);
                        Label label = new Label("没有这个文件,请重新输入");

                        VBox box = new VBox(10);
                        box.getChildren().addAll(label);
                        box.setAlignment(Pos.CENTER);
                        Scene scene = new Scene(box);
                        window.setScene(scene);
                        window.showAndWait();
                    }
                });

                System.out.println("没这个文件");
                Thread.currentThread().stop();
            }
        } catch (ConnectException e1) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Stage window = new Stage();
                    window.setTitle("提示");
                    window.initModality(Modality.APPLICATION_MODAL);
                    window.setMinWidth(300);
                    window.setMinHeight(150);
                    Label label = new Label("网络无连接,请检查网络");

                    VBox box = new VBox(10);
                    box.getChildren().addAll(label);
                    box.setAlignment(Pos.CENTER);
                    Scene scene = new Scene(box);
                    window.setScene(scene);
                    window.showAndWait();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox hBox = new HBox();
        hBox.getChildren().addAll();

    }
}
