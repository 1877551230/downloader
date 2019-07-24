package sample;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * 这个run任务逻辑,要给很多个socket提供服务
 */
public class ThreadHandler implements Runnable {

    private Socket socket;
    public ThreadHandler(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        //获取客户端的ip地址
        InetAddress ip = socket.getInetAddress();
        try {
            //构架网络输入输出流
            //网络输出流
            Scanner input = new Scanner(System.in);
            OutputStream os = socket.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bos);
            //网络输入流
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            File file ;
while (true) {
    String filePath = dis.readUTF();
     file = new File(filePath);

    if (!file.exists()) {

        dos.writeBoolean(false);//如果不存在则写出false
        dos.flush(); //清除缓冲,标记数据写出完毕,此时才将数据发送到网络上

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
            dos.writeUTF(length+"");
            dos.flush();
            //准备开始发送文件
            System.out.println(ip.getHostAddress()+"  fileName="+fileName+"  length="+length);
            System.out.println(ip.getHostAddress()+"  开始发送文件...");
            //从本地读取文件到内存中(本地流)
            DataInputStream dis_local = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            byte[] buffer = new byte[4*1024];
            while(true){
                int len = -1;
                if(dis_local!=null){
                    //本地流输入
                    len=dis_local.read(buffer);

                }
                if (len==-1){
                    break;
                }
                //len = dis_local.read(buffer);
                dos.write(buffer,0,len);
            }
            dos.flush();
            dos.close();
            bos.close();
            os.close();
            dis.close();
            socket.close();//关闭套接字
            System.out.println(ip.getHostAddress()+"  文件:"+fileName+"发送完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
