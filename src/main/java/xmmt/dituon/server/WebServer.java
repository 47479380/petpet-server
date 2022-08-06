package xmmt.dituon.server;

import com.hellokaton.blade.Blade;
import com.hellokaton.blade.mvc.BladeConst;
import com.hellokaton.blade.options.CorsOptions;
import xmmt.dituon.share.BasePetService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class WebServer {
    public static final BasePetService petService = new BasePetService();
    public static final short port = 2333;
     public static void main(String[] args)  {


         Blade blade = Blade.create();
         blade.cors(CorsOptions.create());
         blade.staticOptions().addStatic("/docs");
         blade.staticOptions().addStatic("/editor");
         blade.listen(port)
                    .start(WebServer.class, args);
        }

}
