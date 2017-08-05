package message.client.test.socket;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Base64;
 
public class TCPEchoClientNonblocking {
    public static void main(String args[]) throws Exception{
        
        String server = "127.0.0.1"; 
        //第二个参数为要发送到服务端的字符串
        
        FileInputStream fis = 
				new FileInputStream("./test_file/20106635319267.jpg");
		byte[] bs = new byte[fis.available()];
		fis.read(bs);
		String s = Base64.getEncoder().encodeToString(bs);
		System.out.println(s);
        byte[] argument = s.getBytes();
        //如果有第三个参数，则作为端口号，如果没有，则端口号设为7
        int servPort = 9001;
        //创建一个信道，并设为非阻塞模式
        SocketChannel clntChan = SocketChannel.open();
        clntChan.configureBlocking(false);
        //向服务端发起连接
        if (!clntChan.connect(new InetSocketAddress(server, servPort))){
            //不断地轮询连接状态，直到完成连接
            while (!clntChan.finishConnect()){
                //在等待连接的时间里，可以执行其他任务，以充分发挥非阻塞IO的异步特性
                //这里为了演示该方法的使用，只是一直打印"."
                System.out.print(".");  
            }
        }
        //为了与后面打印的"."区别开来，这里输出换行符
        System.out.print("\n");
        //分别实例化用来读写的缓冲区
        ByteBuffer writeBuf = ByteBuffer.wrap(argument);
        clntChan.write(writeBuf);
        
       
        clntChan.close();
    }
}
