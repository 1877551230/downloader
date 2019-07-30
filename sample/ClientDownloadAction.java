package sample;


import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

/**
 * 下载时客户端的线程类
 */

public class ClientDownloadAction implements Runnable {
    HBox hBox = new HBox();

    Label label = new Label();
    Label label2 = new Label("0%");
    Label label3 = new Label();
    GridPane gridPane ;
    VBox vBox;
    double dd = 0;
    ProgressBar progressBar = new ProgressBar(0);
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

            dos.writeChar('D');
            dos.writeUTF(filePath);
            dos.flush();
            if (dis.readBoolean()) {
                //接收文件的名字
                String fileName = dis.readUTF();
                //接收文件的长度
                String length = dis.readUTF();
                double d = Double.parseDouble(length);


                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Thread.currentThread().setPriority(1);
                        hBox.setSpacing(20);
                        label3.setText((d/1024/1024+"").substring(0,4)+"mb");
                        label.setText(fileName);
                        hBox.getChildren().add(label);
                        hBox.getChildren().add(label3);
                        hBox.getChildren().add(progressBar);
                        hBox.getChildren().add(label2);
                        vBox.getChildren().add(hBox);
                    }
                });

                byte[] buffer = new byte[1024 * 4];//服务端和客户端缓冲大小一样
                //从网络流上接收文件数据,并存储到内存,把内存数据输出到本地硬盘
                //构建本地流输出
                DataOutputStream dos_local = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("C:\\Users\\PC\\Desktop\\" + fileName)));


//循环从网络流中读入数据进内存
                        long progress = 0;
                        int len = -1;
                        while ((len = dis.read(buffer)) != -1) {
                            dos_local.write(buffer, 0, len);
                            progress += len;
                            dd = progress / d;


                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(dd);
                                    if (dd==1.0){
                                        label2.setText("100%");
                                    }else{
                                        label2.setText((dd*100+"").substring(0,4)+"%");
                                    }
                                }
                            });


                        }
                        dos_local.flush();
                        dos_local.close();
                        dis.close();
                        dos.close();
                        socket.close();
                        System.out.println("下载文件完毕");
                        // text1.setText("接收文件完毕");
                        Thread.currentThread().interrupt();



            } else {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.titleProperty().set("提示");
                        alert.headerTextProperty().set("没有找到文件");
                        alert.showAndWait();
                       /* Stage window = new Stage();
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
                        window.showAndWait();*/
                    }
                });

                System.out.println("没这个文件");
                Thread.currentThread().interrupt();
            }
        } catch (ConnectException e1) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.titleProperty().set("提示");
                    alert.headerTextProperty().set("网络无连接,请检查网络");
                    alert.showAndWait();
                   /* Stage window = new Stage();
                    window.setTitle("提示");
                    window.initModality(Modality.APPLICATION_MODAL);
                    window.setMinWidth(300);
                    window.setMinHeight(150);
                    Label label = new Label("网络无连接,请检查网络");

                    VBox box = new VBox(10);
                    box.getChildren().addAll(label);
                    box.setAlignment(Pos.CENTER);
                    Scene scene = new Scene(box);
                    scene.getStylesheets().add(getClass().getResource("MainStyle.css").toExternalForm());
                    window.setScene(scene);
                    window.showAndWait();*/
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
