package xmmt.dituon.Server;

import com.hellokaton.blade.Blade;
import com.hellokaton.blade.options.CorsOptions;
import xmmt.dituon.share.BasePetService;


public class WebServer {
    public static final BasePetService petService = new BasePetService();
    public static final short port = 2333;

    public static void main(String[] args) {


        Blade blade = Blade.create();
         blade.cors(CorsOptions.create());
        blade.listen(port)
                .start(WebServer.class, args);
    }


}