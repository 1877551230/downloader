package sample;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端:给客户端提供服务
 * 1.接收客户端发送过来的文件路径,并判断文件是否存在
 * 2.获取文件的名称和文件的长度,并写到网络上
 * 3.从本服务器硬盘读取文件内容到内存
 * 4.把内存的文件数据写到网络上
 *
 * 一次只能给一个客户端提供服务
 */
public class Server {
    public static void main(String[] args) throws Exception {

        //创建了一个ServerSocket对象
        ServerSocket ss = new ServerSocket(9992);
        System.out.println("服务器已经启动");
        while (true) {
            Socket socket = ss.accept();
            //仅代表run逻辑
            ThreadHandler th = new ThreadHandler(socket);
            Thread t = new Thread(th);
            t.start();
        }

    }
}