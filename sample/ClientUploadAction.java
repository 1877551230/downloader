package sample;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ClientUploadAction implements Runnable {

    String filePath;
    public ClientUploadAction(String filePath) throws IOException {

        this.filePath=filePath;
    }
    @Override
    public void run() {
        try {

            Socket socket = new Socket("10.8.38.136",9992);
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            InetAddress ip = socket.getInetAddress();//获取客户端的ip地址
            File file = new File(filePath);

                if (!file.exists()) {
                    System.out.println("没这个文件");
                    Thread.currentThread().stop();
                } else {



            //能到此处说明文件存在
            //获取文件的名称
            String fileName = file.getName();
            dos.writeChar('U');
            dos.writeUTF(fileName);
            dos.flush();
            //获取文件的长度
            long length = file.length();
            dos.writeUTF(length + "");
            dos.flush();

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
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
