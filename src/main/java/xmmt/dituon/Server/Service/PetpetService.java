package xmmt.dituon.Server.Service;
import com.hellokaton.blade.ioc.annotation.Bean;
import kotlin.Pair;
import xmmt.dituon.Server.Exception.PetpetException;

import xmmt.dituon.share.*;

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


    /**
       根据自定义keyData生成petpet png图片
     */
    public Pair<InputStream, String> generateImage(KeyData data,HashMap<Short, BufferedImage> stickerMap,
                                                   GifAvatarExtraDataProvider gifAvatarExtraDataProvider,
                                                   TextExtraData textExtraData,
                                                   List<TextData> additionTextDatas) {

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
                    avatarList.add(new AvatarModel(avatarData, gifAvatarExtraDataProvider, data.getType()));
                }
            }
            if (data.getType() == Type.IMG) {
                BufferedImage sticker = stickerMap.get((short) 0);
                assert sticker != null;
                InputStream inputStream = BaseImageMaker.makeImage(avatarList, textList, sticker, antialias);
                return new Pair<>(inputStream, "png");
            }
        } catch (Exception ex) {
         return null;
        }
        return null;
    }
    /**
     根据自定义keyData生成petpet gif图片
     */
  public  Pair<InputStream, String> generateGIF(KeyData data,HashMap<Short, BufferedImage> stickerMap,
                                                GifAvatarExtraDataProvider gifAvatarExtraDataProvider,
                                                TextExtraData textExtraData,
                                                List<TextData> additionTextDatas){

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
                  avatarList.add(new AvatarModel(avatarData, gifAvatarExtraDataProvider, data.getType()));
              }
          }

          if (data.getType() == Type.GIF) {

              //TODO
//                if (data.getBackground() != null) { //从配置文件读背景
//                    BufferedImage sticker = new BackgroundModel(data.getBackground(), avatarList, textList).getImage();
//                    for (short i = 0; i < avatarList.get(0).getPosLength(); i++) {
//                        stickerMap.put(i, sticker);
//                    }
//                }
              InputStream inputStream = BaseGifMaker.makeGIF(avatarList, textList, stickerMap, antialias);
              return new Pair<>(inputStream, "gif");
          }

      } catch (Exception ex) {
          throw new PetpetException("生成图片失败");
      }
      return null;

    }

}
