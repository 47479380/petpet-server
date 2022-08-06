package xmmt.dituon.server.Service;
import com.hellokaton.blade.annotation.route.GET;
import com.hellokaton.blade.ioc.annotation.Bean;
import com.hellokaton.blade.kit.Assert;
import kotlin.Pair;
import xmmt.dituon.server.Exception.PetpetException;

import xmmt.dituon.share.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.*;

@Bean
public class PetpetService extends BasePetService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PetpetService.class);

    public PetpetService() {

        File dir = new File("./data/xmmt.dituon.petpet");
        this.readData(dir);
      log.debug("读取"+dir.getAbsolutePath());
    }
    //
    public boolean isKey(String value){
        return this.dataMap.containsKey(value);
    }


    public Pair<InputStream, String> generateImage(KeyData data,HashMap<Short, BufferedImage> stickerMap,
                                                   AvatarExtraDataProvider avatarExtraDataProvider,
                                                   TextExtraData textExtraData,
                                                   List<TextData> additionTextDatas) {

        Assert.notNull(data,"keyData不能为空");
       if (null==stickerMap||stickerMap.isEmpty()){
           throw new IllegalArgumentException("背景图片不能为空");
       }
        try {
            ArrayList<TextModel> textList = new ArrayList<>();
            // add from KeyData
            if (!data.getText().isEmpty()) {
                for (TextData textElement : data.getText()) {
                    textList.add(new TextModel(textElement, textExtraData));
                }
            }
            // add from params
            if (additionTextDatas != null) {
                for (TextData textElement : additionTextDatas) {
                    textList.add(new TextModel(textElement, textExtraData));
                }
            }

            ArrayList<AvatarModel> avatarList = new ArrayList<>();

            if (!data.getAvatar().isEmpty()) {
                for (AvatarData avatarData : data.getAvatar()) {
                    avatarList.add(new AvatarModel(avatarData, avatarExtraDataProvider, data.getType()));
                }
            }

            if (data.getType() == Type.GIF) {


                if (data.getBackground() != null) { //从配置文件读背景
                    BufferedImage sticker = new BackgroundModel(data.getBackground(), avatarList).getImage();
                    for (short i = 0; i < avatarList.get(0).getPosLength(); i++) {
                        stickerMap.put(i, sticker);
                    }
                }
                InputStream inputStream = gifMaker.makeAvatarGIF(avatarList, textList, stickerMap, antialias);
                return new Pair<>(inputStream, "gif");
            }

            if (data.getType() == Type.IMG) {
                BufferedImage sticker = stickerMap.get((short)0);
                if (data.getBackground() != null)
                    sticker = new BackgroundModel(data.getBackground(), avatarList).getImage();
                assert sticker != null;
                InputStream inputStream = imageMaker.makeImage(avatarList, textList, sticker, antialias);
                return new Pair<>(inputStream, "png");
            }

        } catch (Exception ex) {
            throw new PetpetException("解析 KeyData 出错");
        }

        return null;
    }
}
