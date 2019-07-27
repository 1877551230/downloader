package sample;

import java.io.*;

import java.net.InetAddress;
import java.net.Socket;



/**
 * 这个run任务逻辑,要给很多个socket提供服务
 */
    public class ServerDownloadAction implements Runnable {

    public  Socket socket;

    public ServerDownloadAction(Socket socket) throws IOException {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream())); //网络输出流
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));//网络输入流
            InetAddress ip = socket.getInetAddress();//获取客户端的ip地址
            System.out.println(Thread.currentThread().getName()+"开始下载");
            String filePath = dis.readUTF();
            File file = new File(filePath);
            while (true) {
                if (!file.exists()) {
                    dos.writeBoolean(false);//如果不存在则写出false
                    System.out.println("没这个文件");
                    dos.flush(); //清除缓冲,标记数据写出完毕,此时才将数据发送到网络上
                    Thread.currentThread().stop();

                } else {
                    dos.writeBoolean(true);
                    dos.flush();
                    break;
                }
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
