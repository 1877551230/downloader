package sample;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ServerUploadAction implements Runnable {
    DataInputStream dis; //网络输出流
    DataOutputStream dos;//网络输入流
    public Socket socket;

    public ServerUploadAction(Socket socket, DataInputStream dis, DataOutputStream dos) {
        this.socket = socket;
        this.dis = dis;
        this.dos = dos;
    }


    @Override
    public void run() {

        try {

            InetAddress ip = socket.getInetAddress();//获取客户端的ip地址

            //接收文件的名字
            String fileName = dis.readUTF();

            //接收文件的长度
            String length = dis.readUTF();
            System.out.println(ip.getHostAddress() + "  fileName=" + fileName + "  length=" + length);
            System.out.println(ip.getHostAddress() + Thread.currentThread() + "  开始请求上传...");
            //从网络流上接收文件数据,并存储到内存,把内存数据输出到本地硬盘
            //构建本地流输出
            DataOutputStream dos_local = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("C:\\Users\\PC\\Desktop\\uploader\\" + fileName)));
            //构建缓冲
            byte[] buffer = new byte[1024 * 4];//服务端和客户端缓冲大小一样
            //循环从网络流中读入数据进内存
            long progress = 0;
            int len = -1;
            while ((len = dis.read(buffer)) != -1) {

                dos_local.write(buffer, 0, len);

            }
            dos_local.flush();
            dos_local.close();
            dis.close();
            dos.close();
            socket.close();
            System.out.println("接收上传完毕");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
