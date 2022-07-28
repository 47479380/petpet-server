package xmmt.dituon.server;

import com.hellokaton.blade.Blade;
import xmmt.dituon.share.BasePetService;

import java.io.File;


public class WebServer {
    public static final BasePetService petService = new BasePetService();
    public static final short port = 2333;
     public static void main(String[] args) {


         Blade blade = Blade.create();
         blade.staticOptions().showList();
         blade.staticOptions().addStatic("/docs");
         blade.staticOptions().addStatic("/editor");

         blade.listen(port)
                    .start(WebServer.class, args);
        }

}
