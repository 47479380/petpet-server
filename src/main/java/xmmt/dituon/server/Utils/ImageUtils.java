package xmmt.dituon.server.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class ImageUtils {

    public static BufferedImage toBufferedImage(File file){
        try {

            return ImageIO.read(file);
        } catch (IOException e) {
            return null;
        }
    }
    public static BufferedImage toBufferedImage(byte[] data){
        try {

                return  ImageIO.read(new ByteArrayInputStream(data));

        } catch (IOException e) {
            return null;
        }
    }
    public static BufferedImage getDefaultAvatar(String name){

        String defaultAvatarPath = PathUtils.getDefaultAvatarPath(name);
        try {
           return ImageIO.read(new File(defaultAvatarPath));
        } catch (IOException e) {
            return null;
        }
    }
}
