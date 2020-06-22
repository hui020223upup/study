package com.study.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author: whh
 * @date: 2020.06.22
 **/
public class NioClientDemo {

    private static final Logger log = LoggerFactory.getLogger(NioClientDemo.class);

    // 定义通道
    private SocketChannel socketChannel;

    public static void main(String[] args) {
        // 先运行服务端代码，再运行本类
        NioClientDemo nioClient = new NioClientDemo();

        // 初始化连接服务器配置
        nioClient.initClientChannel("localhost", 8081);

        // 发送消息
        nioClient.callServer("hello server,what are you doing?");
    }

    /**
     * 初始化客户端 NIO Channel
     *
     * @param hostname
     * @param port
     */
    public void initClientChannel(String hostname, int port) {
        try {
            // 初始化 socket
            InetSocketAddress inetSocketAddress = new InetSocketAddress(hostname, port);

            // 建立通道
            socketChannel = SocketChannel.open(inetSocketAddress);
            System.out.println("客户端准备就绪");
        } catch (IOException e) {
            log.error("初始化客户端失败", e);
        }
    }

    /**
     * 通信 Server
     *
     * @param callStr
     */
    public void callServer(String callStr) {
        // 将字符串转换为 byte 数组，便于稍后传输
        byte[] requestByte = new String(callStr).getBytes();

        // 创建一个1024 容量的 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.wrap(requestByte);
        System.out.println("客户端发送:" + new String(byteBuffer.array()));
        if (null == socketChannel) {
            System.out.println("请初始化客户端");
            return;
        }
        try {
            // 向通道写入数据
            socketChannel.write(byteBuffer);

            // 清空缓冲区（数据并未被删除，但位置、标记、限制被重置）
            byteBuffer.clear();

            // 读取被服务器更新的数据
            socketChannel.read(byteBuffer);
            System.out.println("客户端接收:" + new String(byteBuffer.array()));
            // 关闭通道
            socketChannel.close();

        } catch (IOException e) {
            log.info("通信出错", e);
        }
    }
}
