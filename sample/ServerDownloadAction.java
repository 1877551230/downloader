package sample;

import java.io.*;

import java.net.InetAddress;
import java.net.Socket;


/**
 * 这个run任务逻辑,要给很多个socket提供服务
 */
public class ServerDownloadAction implements Runnable {

    public Socket socket;
    DataInputStream dis; //网络输出流
    DataOutputStream dos;//网络输入流

    public ServerDownloadAction(Socket socket, DataInputStream dis, DataOutputStream dos) throws IOException {
        this.socket = socket;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {
        try {

            InetAddress ip = socket.getInetAddress();//获取客户端的ip地址
            System.out.println(Thread.currentThread().getName() + "开始请求下载");
            String filePath = dis.readUTF();
            File file = new File(filePath);

            if (!file.exists()) {
                dos.writeBoolean(false);//如果不存在则写出false
                System.out.println("没这个文件");
                dos.flush(); //清除缓冲,标记数据写出完毕,此时才将数据发送到网络上
                Thread.currentThread().stop();

            } else {
                dos.writeBoolean(true);
                dos.flush();
            }


            //能到此处说明文件存在
            //获取文件的名称
            String fileName = file.getName();
            dos.writeUTF(fileName);
            dos.flush();
            //获取文件的长度
            long length = file.length();
            dos.writeUTF(length + "");
            dos.flush();
            //准备开始发送文件
            System.out.println(ip.getHostAddress() + "  fileName=" + fileName + "  length=" + length);
            System.out.println(ip.getHostAddress() + Thread.currentThread() + "  开始发送文件...");
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
            System.out.println(ip.getHostAddress() + "  文件:" + fileName + "发送完毕");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
