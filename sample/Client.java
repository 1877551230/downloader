package sample;

import javafx.scene.paint.Color;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;



/**
 * 客户端:
 * 1.连接服务端
 * 2.从键盘输入服务器计算机中的文件路径,并请求下载
 * 3.如果服务端有文件,就返回给客户端文件的名称和文件长度
 * 4.从网络上获取服务器下载的文件数据,进入客户端的内存
 * 5.把内存的数据写到客户端的硬盘上
 */
public class Client {

    public static void main(String[] args)throws IOException {
        Main main1 = new Main();
        //Scanner input = new Scanner(System.in);
        //创建socket对象(ip地址,端口号),连接服务器
        Socket socket = new Socket("127.0.0.1",9992);
        //构架网络输入输出流
        //网络输入流
        DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        //网络输出流
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        //从客户端获取网络上传递过来的字符串


       // System.out.println("请输入服务端中的文件的路径");
        String filePath = main1.textField.getText();
        //把文件路径发送到网络流
        dos.writeUTF(filePath);
        dos.flush();
        if(dis.readBoolean()){
            main1.text1.setFill(Color.FIREBRICK);
            main1.text1.setText("开始下载");
        }else{
            System.out.println("文件不存在");
            return;
        }
        //接收文件的名字
        String fileName = dis.readUTF();

        //接收文件的长度
        String length = dis.readUTF();
        //输出
        System.out.println("文件名为:"+fileName+" 文件长度:"+length);

        //从网络流上接收文件数据,并存储到内存,把内存数据输出到本地硬盘
        System.out.println("开始接收文件...");
        //构建本地流输出
        DataOutputStream dos_local = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("D:/ab/"+fileName)));
        //构建缓冲
        byte[] buffer = new byte[4];//服务端和客户端缓冲大小一样
        //循环从网络流中读入数据进内存
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
        }
        dos_local.close();
        dis.close();
        dos.close();
        socket.close();
        System.out.println("接收文件完毕,文件路径d:/aa/" +fileName+" 文件大小"+length);
    }
}
