package xmmt.dituon.server;

import com.hellokaton.blade.Blade;
import com.sun.net.httpserver.HttpServer;
import xmmt.dituon.share.BasePetService;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;


public class WebServer {
    //TODO 作为网络服务器生成图片，可被其它语言使用
    public static final BasePetService petService = new BasePetService();
    public static final short port = 2333;
     public static void main(String[] args) {

            Blade.create().listen(port)
                    .start(WebServer.class, args);
        }

}
