package network;

import container.ActiveExpireCycleTask;
import container.DataBase;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import network.handler.*;
import serialize.RdbCheckTask;

import java.util.concurrent.TimeUnit;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-17
 * Time: 16:16
 */
public class RedisServer {
    private final int port;

    public RedisServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        DataBase       db    = new DataBase();
        EventLoopGroup group = new NioEventLoopGroup();
        db.rdbLoad();
        group.scheduleAtFixedRate(new ActiveExpireCycleTask(db), 0, 5, TimeUnit.SECONDS);
        group.scheduleAtFixedRate(new RdbCheckTask(db), 0, 5, TimeUnit.SECONDS);
        try {
            ServerBootstrap sb = new ServerBootstrap();

            sb.group(group) // 绑定线程池
              .childAttr(AttributeKey.valueOf("DB"), db)
              .channel(NioServerSocketChannel.class) // 指定使用的channel
              .localAddress(this.port)// 绑定监听端口
              .childHandler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作

                  @Override
                  protected void initChannel(SocketChannel ch) throws Exception {
                      ch.pipeline().addFirst(new RedisFrameEncodeHandler());
                      ch.pipeline().addFirst(new EchoHandler());


                      ch.pipeline().addLast(new RedisFrameDecodeHandler());
                      ch.pipeline().addLast(new CommandExecutorHandler());
                      ch.pipeline().addLast(new ExceptionHandler());
                  }
              });
            ChannelFuture cf = sb.bind().sync(); // 服务器异步创建绑定
            cf.channel().closeFuture().sync(); // 关闭服务器通道
        } finally {
            group.shutdownGracefully().sync(); // 释放线程池资源
        }
    }

    public static void main(String[] args) throws Exception {
        new RedisServer(6379).start(); // 启动
    }
}
