package sample;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.omg.PortableServer.THREAD_POLICY_ID;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class ClientUploadAction implements Runnable {
    GridPane gridPane ;
    Label label = new Label();
    Label label2 = new Label("0%");
    Label label3 = new Label();
    ProgressBar progressBar = new ProgressBar(0);
    HBox hBox = new HBox();
    VBox vBox;
    String filePath;
    double dd = 0;
    public ClientUploadAction(String filePath,GridPane gridPane,VBox vBox) throws IOException {
        this.vBox = vBox;
        this.gridPane = gridPane;
        this.filePath=filePath;
    }
    @Override
    public void run() {
        try {

            Socket socket = new Socket("10.8.38.136",9992);
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            File file = new File(filePath);

                if (!file.exists()) {
                    System.out.println("没这个文件");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.titleProperty().set("提示");
                            alert.headerTextProperty().set("没有找到文件");
                            alert.showAndWait();
                            /*
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
                            scene.getStylesheets().add(getClass().getResource("MainStyle.css").toExternalForm());
                            window.setScene(scene);
                            window.showAndWait();*/
                        }
                    });
                    Thread.currentThread().interrupt();
                } else {


            //从本地读取文件到内存中(本地流)
            DataInputStream dis_local = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
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
                    double d = Double.parseDouble(length+"");

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    hBox.setSpacing(20);
                                    hBox.setMinSize(20,20);
                                    label3.setText((d/1024/1024+"").substring(0,4)+"mb");
                                    label.setText(fileName);
                                    hBox.getChildren().add(label);
                                    hBox.getChildren().add(label3);
                                    hBox.getChildren().add(progressBar);
                                    hBox.getChildren().add(label2);
                                    vBox.getChildren().add(hBox);
                                }
                            });

            byte[] buffer = new byte[4 * 1024];


                        long progress = 0;
                        int len = -1;
                        while ((len = dis_local.read(buffer)) != -1) {
                            //本地流输入
                            dos.write(buffer, 0, len);
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

                            dos.flush();
                            dos.close();
                            dis.close();
                            socket.close();
                            System.out.println("文件上传完毕");
                            Thread.currentThread().interrupt();

                }

        } catch (ConnectException e1) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.titleProperty().set("提示");
                    alert.headerTextProperty().set("请检查网络连接");
                    alert.showAndWait();
                }
            });


        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
