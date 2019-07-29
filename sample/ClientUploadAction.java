package sample;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ClientUploadAction implements Runnable {
    Label label;
    GridPane gridPane;
    ProgressBar progressBar = new ProgressBar(0);
    VBox vBox;
    String filePath;
    public ClientUploadAction(String filePath) throws IOException {

        this.filePath=filePath;
    }
    @Override
    public void run() {
        try {

            Socket socket = new Socket("10.8.38.136",9992);
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            InetAddress ip = socket.getInetAddress();//获取客户端的ip地址
            File file = new File(filePath);

                if (!file.exists()) {
                    System.out.println("没这个文件");
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
                    Thread.currentThread().stop();
                } else {



            //能到此处说明文件存在
            //获取文件的名称
            String fileName = file.getName();
            dos.writeChar('U');
            dos.writeUTF(fileName);
            dos.flush();
            //获取文件的长度
            long length = file.length();
            dos.writeUTF(length + "");
            dos.flush();

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

            //从本地读取文件到内存中(本地流)
            DataInputStream dis_local = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            byte[] buffer = new byte[4 * 1024];
            int len = -1;
            while ((len = dis_local.read(buffer)) != -1) {
                //本地流输入
                dos.write(buffer, 0, len);

            }
            dos.flush();

                    dos.close();
                    dis.close();
                    socket.close();
                    System.out.println("文件上传完毕");
                    Thread.currentThread().stop();
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
