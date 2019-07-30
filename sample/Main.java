package sample;


import com.sun.org.apache.xpath.internal.patterns.NodeTestFilter;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;


public class Main extends Application {


    ProgressIndicator progressIndicator = new ProgressIndicator();
    VBox vBox = new VBox();
    VBox vBox2 = new VBox();
    GridPane gridPane = new GridPane();
    public Main() throws IOException {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
        Scene scene = new Scene(gridPane, 500, 500);
        scene.getStylesheets().add(getClass().getResource("MainStyle.css").toExternalForm());
        vBox.setSpacing(5);
        vBox2.setSpacing(5);
        primaryStage.initStyle(StageStyle.UNIFIED);
        primaryStage.setTitle("百度云 ");
        primaryStage.setScene(scene);
        primaryStage.show();
        Text text = new Text("请输入下载文件路径");
        Text text1 = new Text("请输入上传文件路径");
        Button button = new Button("下载");
        Button button1 = new Button("上传");
        Button button2 = new Button("使用必读");
        TextField textField = new TextField();
        TextField textField1 = new TextField();
       // text.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        gridPane.add(text, 4, 2, 2, 1);
        gridPane.add(textField, 4, 3);
        gridPane.add(button, 4, 4);
        gridPane.add(vBox,4,5);

        gridPane.add(text1, 4, 6, 2, 1);
        gridPane.add(textField1, 4, 7);
        gridPane.add(button1, 4, 8);
        gridPane.add(vBox2,4,9);
        gridPane.add(button2,3,1);
        /**
         * 点击下载按钮触发
         */
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String filePath = textField.getText();
                    ClientDownloadAction clientDownloadAction = new ClientDownloadAction(filePath,gridPane,vBox);
                    new Thread(clientDownloadAction).start();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        /**
         * 点击上传按钮触发
         */
        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {

                    String filePath1 = textField1.getText();
                    ClientUploadAction cda = new ClientUploadAction(filePath1,gridPane,vBox2);
                    new Thread(cda).start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
        
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage window = new Stage();
                window.setTitle("提示");
                window.initModality(Modality.APPLICATION_MODAL);
                window.setMinWidth(300);
                window.setMinHeight(150);
                Label labe2 = new Label("所有文件将保存在桌面名字为downloader的文件夹");
                Label label = new Label("壁纸文件夹--C:\\Users\\PC\\Desktop\\wallpaper");
                Label label1 = new Label("壁纸全部以数字命名如1.jpg");
                Label label2 = new Label("音乐文件夹--C:\\Users\\PC\\Desktop\\wallpaper");
                Label label3 = new Label("音乐压缩包名字:音乐.7z");
                Label label4 = new Label("作为回报,请共享您的壁纸,以您的用户名-壁纸名命名");
                VBox box = new VBox(10);
                box.getChildren().addAll(labe2,label,label1,label3,label4);
                box.setAlignment(Pos.CENTER);
                Scene scene = new Scene(box);
                scene.getStylesheets().add(getClass().getResource("MainStyle.css").toExternalForm());
                window.setScene(scene);
                window.showAndWait();
            }
        });

    }


    public static void main(String[] args) throws Exception {
        launch(args);

    }
}
