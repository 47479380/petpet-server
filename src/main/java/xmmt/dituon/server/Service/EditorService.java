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
                                                   AvatarExtraDataProvider avatarExtraDataProvider,
                                                   TextExtraData textExtraData,
                                                   List<TextData> additionTextDatas) {
     return petpetService.generateImage(keyData,stickerMap,avatarExtraDataProvider,textExtraData,additionTextDatas);

    }
}
