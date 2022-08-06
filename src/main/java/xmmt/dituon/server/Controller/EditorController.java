package xmmt.dituon.server.Controller;

import com.hellokaton.blade.annotation.Path;
import com.hellokaton.blade.annotation.route.POST;
import com.hellokaton.blade.ioc.annotation.Inject;
import com.hellokaton.blade.mvc.http.Request;
import com.hellokaton.blade.mvc.http.Response;
import com.hellokaton.blade.mvc.multipart.FileItem;
import com.hellokaton.blade.mvc.ui.RestResponse;
import kotlin.Pair;
import kotlin.jvm.functions.Function0;
import xmmt.dituon.server.Exception.PetpetException;
import xmmt.dituon.server.Service.EditorService;
import xmmt.dituon.server.Service.PetpetService;
import xmmt.dituon.server.Utils.ImageUtils;
import xmmt.dituon.share.AvatarData;
import xmmt.dituon.share.AvatarExtraDataProvider;
import xmmt.dituon.share.KeyData;
import xmmt.dituon.share.TextExtraData;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.*;


@Path
public class EditorController extends BaseController {
    private static final String TAG = "EditorController:";
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PetpetService.class);
    @Inject
    private EditorService editorService;
    //    编辑器文件上传
    @POST("/editor/generate")
    public void generate(Request request, Response response) {
        Optional<String> data = request.form("data");
        if (data.isEmpty()) {
            response.json(RestResponse.fail("data不能为空"));
            return;
        }
        try {
            KeyData keyData = KeyData.Companion.getData(data.get());
            TextExtraData textExtraData = this.getTextExtraData(request);
            AvatarExtraDataProvider avatarExtraDataProvider =this.createAvatarExtraData(keyData.getAvatar());
            HashMap<Short, BufferedImage> stickerMap=this.getImageFiles(request);
            Pair<InputStream, String> pair = editorService.generateImage(keyData,stickerMap, avatarExtraDataProvider, textExtraData, null);

            if (null == pair) {
                throw new  PetpetException("生成petpet失败");
            }
            this.downloadImage(response, pair, UUID.randomUUID().toString());
        } catch ( Exception e) {
            log.error(TAG, e.getMessage());
            response.status(400);
            response.json(RestResponse.fail(e.getMessage()));
        }
    }


    private HashMap<Short, BufferedImage>  getImageFiles(Request request) {
        HashMap<Short, BufferedImage> stickerMap = new HashMap<>();
        Map<String, FileItem> fileItemMap = request.fileItems();
        short index=0;
        for (String name : fileItemMap.keySet()) {
            //        过滤不需要的文件
            if (parameterMap.containsValue(name)){
                continue;
            }
            FileItem fileItem = fileItemMap.get(name);
            if (fileItem.isInMemory()){
                stickerMap.put(index, ImageUtils.toBufferedImage(fileItem.getData()));
            }else {
                stickerMap.put(index,ImageUtils.toBufferedImage(fileItem.getFile()));
            }

            index++;
        }
        return stickerMap;
    }
    protected AvatarExtraDataProvider createAvatarExtraData(List<AvatarData> avatarData) {
        Map<String, BufferedImage> avatarMap = new HashMap<>();
        for (AvatarData avatarDatum : avatarData) {
            BufferedImage defaultAvatar = ImageUtils.getDefaultAvatar(parameterMap.get(avatarDatum.getType().name()));
            avatarMap.put(avatarDatum.getType().name(),defaultAvatar);
        }
        return  new AvatarExtraDataProvider(
               newCall(avatarMap.get("FROM")),
                newCall(avatarMap.get("TO")),
                newCall(avatarMap.get("GROUP")),
                newCall(avatarMap.get("BOT"))
        );
    }
    protected static Function0<? extends BufferedImage> newCall(BufferedImage bufferedImage){
        return () -> {
         return bufferedImage;
        };
    }

}
