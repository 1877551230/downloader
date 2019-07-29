package sample;

import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端:给客户端提供服务
 * 1.接收客户端发送过来的文件路径,并判断文件是否存在
 * 2.获取文件的名称和文件的长度,并写到网络上
 * 3.从本服务器硬盘读取文件内容到内存
 * 4.把内存的文件数据写到网络上
 * <p>
 * 一次只能给一个客户端提供服务
 */
public class Server {


    public static void main(String[] args) throws Exception {
        //创建了一个ServerSocket对象
        ServerSocket ss = new ServerSocket(9992);
        System.out.println("服务器已经启动");
        while (true) {
            Socket socket = ss.accept();
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));//网络输入流
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            char c = dis.readChar();
            if (c == 'D') {


                new Thread(new ServerDownloadAction(socket, dis, dos)).start();

            }
            if (c == 'U') {

                new Thread(new ServerUploadAction(socket, dis, dos)).start();
            }


        }
    }


}