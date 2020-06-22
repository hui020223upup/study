package com.study.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author: whh
 * @date: 2020.06.22
 **/
public class NioServerDemo {

    private static final Logger log = LoggerFactory.getLogger(NioServerDemo.class);

    // 选择器
    private Selector nioServerSelector;

    // 数据通道
    private ServerSocketChannel nioServerSocketChannel;

    public static void main(String[] args) {
        // 先运行本类代码，再运行客户端代码
        NioServerDemo nioServer = new NioServerDemo();

        // 初始化服务器配置
        nioServer.initServerSocketChannel("localhost", 8081);

        // 启动监听
        nioServer.startNioServerSelectorListener();
    }

    /**
     * 注册 Channel
     *
     * @param hostname 主机地址
     * @param port     端口号
     */
    private void initServerSocketChannel(String hostname, int port) {
        try {
            // 初始化选择器
            nioServerSelector = Selector.open();

            // 打开通道
            nioServerSocketChannel = ServerSocketChannel.open();

            // 调整模式为非阻塞
            nioServerSocketChannel.configureBlocking(false);

            // 设置端口
            nioServerSocketChannel.socket().bind(new InetSocketAddress(hostname, port));

            // 注册此通道
            nioServerSocketChannel.register(nioServerSelector, SelectionKey.OP_ACCEPT);

            System.out.println("服务端准备就绪");
        } catch (IOException e) {
            log.error("服务端初始化失败");
        }
    }

    /**
     * 启动 Selector
     */
    private void startNioServerSelectorListener() {
        while (true) {
            try {
                // 选中通道
                nioServerSelector.select();

                // 获取所有 key
                Iterator<SelectionKey> nioServerSelectorIterator = nioServerSelector.selectedKeys().iterator();

                while (nioServerSelectorIterator.hasNext()) {

                    // 获取 key
                    SelectionKey selectionKey = nioServerSelectorIterator.next();

                    // 判断当前 channel 是否可接收 socket 连接
                    if (selectionKey.isAcceptable()) {

                        SocketChannel socketChannel = nioServerSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(nioServerSelector, SelectionKey.OP_READ);

                        // 判断当前通道是否可读取
                    } else if (selectionKey.isReadable()) {
                        callClient(selectionKey);
                    }

                    // 删除已处理完成的 key
                    nioServerSelectorIterator.remove();
                }

            } catch (IOException e) {
                log.error("startNioServerSelectorListener error", e);
            }
        }
    }

    /**
     * 自动回复 Client
     */
    private void callClient(SelectionKey selectionKey) {
        try {
            // 获取对应通道
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

            // 新建缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            // 读取通道数据并存入缓冲区
            int index = socketChannel.read(byteBuffer);

            // 若通道内有数据
            if (index != -1) {

                System.out.println("服务端接收:" + new String(byteBuffer.array()));
                // 自动回复（此处可添加对应业务逻辑）
                socketChannel.write(ByteBuffer.wrap("hello client,im waiting for you!".getBytes()));
                log.info("服务端回复:hello client,im waiting for you!");
                System.out.println("服务端回复:hello client,im waiting for you!");

            } else {
                // 通道内无数据
                socketChannel.close();
            }
        } catch (IOException e) {
            log.error("callClient error", e);
        }
    }
}
