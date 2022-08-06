package xmmt.dituon.server.Service;

import com.hellokaton.blade.ioc.annotation.Bean;
import com.hellokaton.blade.ioc.annotation.Inject;
import kotlin.Pair;
import xmmt.dituon.share.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Bean
public class EditorService {

    @Inject
    private PetpetService petpetService;


    public <E> HashMap<String, KeyData> getDataMap() {
        return petpetService.getDataMap();
    }

    public Pair<InputStream, String> generateImage(KeyData keyData,HashMap<Short, BufferedImage> stickerMap,
                                                   GifAvatarExtraDataProvider avatarExtraDataProvider,
                                                   TextExtraData textExtraData,
                                                   List<TextData> additionTextDatas) {
        Pair<InputStream,String> pair=null;

        if (null==keyData){
            return null;
        }
        if (null==stickerMap||stickerMap.isEmpty()){
            return null;
        }

        if (keyData.getType()==Type.GIF){
            pair=petpetService.generateGIF(keyData,stickerMap,avatarExtraDataProvider,textExtraData,additionTextDatas);
        }else {
            pair= petpetService.generateImage(keyData,stickerMap,avatarExtraDataProvider,textExtraData,additionTextDatas);
        }
        return pair;
    }
}
