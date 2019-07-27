package sample;


import com.sun.org.apache.xpath.internal.patterns.NodeTestFilter;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.*;
import java.lang.annotation.Annotation;
import java.net.Socket;

public class Main extends Application {



    // Label label = new Label("壁纸路径:C:/Users/PC/Desktop/Wallpaper/1.jpg");



     ProgressBar pb = new ProgressBar(0);
     ProgressIndicator progressIndicator = new ProgressIndicator(0);

    GridPane gridPane = new GridPane();


    public Main() throws IOException {
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25,25,25,25));
        Scene scene = new Scene(gridPane,400,350);
        primaryStage.setTitle("百度云 ");
        primaryStage.setScene(scene);
        primaryStage.show();
        Text text = new Text("请输入下载文件路径");
        Text text1 = new Text("请输入上传文件路径");
        Button button = new Button("下载");
        Button button1 = new Button("上传");
        TextField textField = new TextField();
        TextField textField1 = new TextField();
        text.setFont(Font.font("Tahoma", FontWeight.NORMAL,20));
        gridPane.add(text,4,0,2,1);
        gridPane.add(textField,4,1);
        gridPane.add(button,4,2);
        gridPane.add(text1,4,4,2,1);
        gridPane.add(textField1,4,5);
        gridPane.add(button1,4,6);
        /**
         * 点击下载按钮触发
         */
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                        try {
                            String filePath = textField.getText();


                            new Thread(new ClientDownloadAction(filePath)).start();

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


                    new Thread(new ClientUploadAction(filePath1)).start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) throws Exception{

        launch(args);

    }
}
