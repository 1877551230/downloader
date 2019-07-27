package sample;

import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

/**
 * 下载时客户端的线程类
 */

public class ClientDownloadAction implements Runnable {

    String filePath;
    public ClientDownloadAction(String filePath)  {
        this.filePath=filePath;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("10.8.38.136",9992);
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

           // GridPane gridPane = new GridPane();

            dos.writeChar('D');
            dos.writeUTF(filePath);
            dos.flush();
            if (dis.readBoolean()) {
                //text1.setFill(Color.GREEN);
                //text1.setText("开始下载");
              //  Platform.runLater(new Runnable() {
                  //  @Override
                  //  public void run() {
                       // Platform.runLater(new Runnable() {
                            //@Override
                           // public void run() {
                                //gridPane.add(pb,4,8);
                                //gridPane.add(progressIndicator,5,8);
                          //  }
                       // });

                 //   }
               // });

                while (true){

                        //接收文件的名字
                        String fileName = dis.readUTF();

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
                        while ((len=dis.read(buffer))!=-1) {

                            dos_local.write(buffer, 0, len);

                           // progress += buffer.length;
                           // double dd = progress / d;
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



                }
            } else {
               // text1.setFill(Color.RED);
               // text1.setText("文件不存在,重新输入");
                System.out.println("没这个文件");
                Thread.currentThread().stop();
            }
        } catch (ConnectException e1){
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
