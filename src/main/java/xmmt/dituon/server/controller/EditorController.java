package xmmt.dituon.server.controller;

import com.hellokaton.blade.Blade;
import com.hellokaton.blade.annotation.Path;
import com.hellokaton.blade.annotation.route.POST;
import com.hellokaton.blade.ioc.annotation.Inject;
import com.hellokaton.blade.kit.JsonKit;
import com.hellokaton.blade.mvc.http.ByteBody;
import com.hellokaton.blade.mvc.http.Request;
import com.hellokaton.blade.mvc.http.Response;
import com.hellokaton.blade.mvc.multipart.FileItem;
import com.hellokaton.blade.mvc.ui.RestResponse;
import kotlin.Pair;
import xmmt.dituon.server.Exception.PetpetException;
import xmmt.dituon.server.Service.EditorService;
import xmmt.dituon.server.Service.PetpetService;
import xmmt.dituon.server.model.ResponseCode;
import xmmt.dituon.server.model.ResponseResult;
import xmmt.dituon.share.AvatarData;
import xmmt.dituon.share.AvatarExtraDataProvider;
import xmmt.dituon.share.KeyData;
import xmmt.dituon.share.TextExtraData;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@Path
public class EditorController extends BaseController {
    private static final String TAG = "EditorController:";
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PetpetService.class);
    @Inject
    private EditorService editorService;
    private final static String EDITOR_FILE_PATH;

    static {
        File editor_file = new File("EDITOR_FILE");
        if (editor_file.isDirectory()) {
            EDITOR_FILE_PATH = editor_file.getAbsolutePath();
            log.info("");
        } else if (editor_file.exists()) {
            EDITOR_FILE_PATH = null;
            log.error(TAG, "无法创建EDITOR_FILE_PATH目录，因为当前路径有同名文件");
        } else {
            boolean mkdirs = editor_file.mkdirs();
            EDITOR_FILE_PATH = editor_file.getAbsolutePath();
            log.info(TAG, "成功创建EDITOR_FILE_PATH目录", editor_file.getAbsolutePath());
        }
    }

    //    编辑器文件上传
    @POST("/editor/generate")
    public void generate(Request request, Response response) {
        Optional<String> data = request.form("data");
        if (data.isEmpty()) {
            response.json(RestResponse.fail("data不能为空"));
            return;
        }
        KeyData keyData = KeyData.Companion.getData(data.get());
        try {
            TextExtraData textExtraData = this.getTextExtraData(request);
            AvatarExtraDataProvider avatarExtraDataProvider =this.getAvatarExtraDataFromFile(request,keyData);
            HashMap<Short, BufferedImage> stickerMap=this.getImageFiles(request);
            Pair<InputStream, String> pair = editorService.generateImage(keyData,stickerMap, avatarExtraDataProvider, textExtraData, null);

            if (null == pair) {
                throw new  PetpetException("生成petpet失败");
            }
            this.downloadImage(response, pair, UUID.randomUUID().toString());
        } catch ( Exception e) {
            log.error(TAG, e.getMessage());
            response.json(new ResponseResult<>(ResponseCode.SERVER_ERROR.getCode(), e.getMessage()));
        }
    }

    private HashMap<Short, BufferedImage>  getImageFiles(Request request) {
        HashMap<Short, BufferedImage> stickerMap = new HashMap<>();
        Map<String, FileItem> fileItemMap = request.fileItems();
        short index=0;
        for (String key : fileItemMap.keySet()) {
            //        过滤不需要的文件
            if (parameterMap.containsValue(key)){
                fileItemMap.remove(key);
                continue;
            }
            stickerMap.put(index,toBufferedImage(fileItemMap.get(key)));
            index++;
        }
        return stickerMap;
    }


}
