package xmmt.dituon.server.Utils;

import java.io.File;
import java.net.URL;

public  class  PathUtils {
    public static String getSystemResource(String name){
        final URL url = ClassLoader.getSystemResource(name);
        return new File(url.getPath()).getAbsolutePath();
    }
    public static String getDefaultAvatarPath(String name){
        return  getSystemResource("defaultAvatar")+File.separator+name+".jpg";
    }
}
