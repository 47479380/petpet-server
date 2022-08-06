package xmmt.dituon.server.Controller;

import com.hellokaton.blade.mvc.http.ByteBody;
import com.hellokaton.blade.mvc.http.Request;
import com.hellokaton.blade.mvc.http.Response;
import com.hellokaton.blade.mvc.multipart.FileItem;
import kotlin.Pair;
import kotlin.jvm.functions.Function0;
import xmmt.dituon.server.Exception.PetpetException;
import xmmt.dituon.server.Utils.ImageUtils;
import xmmt.dituon.share.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BaseController {

    protected final static Map<String,String> parameterMap=new HashMap<>();

    static  {
        parameterMap.put("FORM","fromAvatar");
        parameterMap.put("TO","toAvatar");
        parameterMap.put("GROUP","groupAvatar");
        parameterMap.put("BOT","botAvatar");
    }

    /**
     * 获取Avatar文件参数
     * @param request
     * @return Avatar文件的map
     */
    protected Map<String,Optional<FileItem>> getAvatarFile(Request request){
        Optional<FileItem> fromAvatar = request.fileItem("fromAvatar");
        Optional<FileItem> toAvatar = request.fileItem("toAvatar");
        Optional<FileItem> groupAvatar = request.fileItem("groupAvatar");
        Optional<FileItem> botAvatar = request.fileItem("botAvatar");
        Map<String, Optional<FileItem>> avatarMap=new HashMap<>();
        avatarMap.put("FROM",fromAvatar);
        avatarMap.put("TO",toAvatar);
        avatarMap.put("GROUP",groupAvatar);
        avatarMap.put("BOT",botAvatar);
        return avatarMap;
    }
    protected static Function0<? extends BufferedImage> newCall(FileItem fileItem){
        return () -> {
            if (fileItem.isInMemory()){
                return ImageUtils.toBufferedImage(fileItem.getData());
            }
          return ImageUtils.toBufferedImage(fileItem.getFile());
        };
    }

    protected void downloadImage(Response response,Pair<InputStream, String> pair, String key) throws IOException {
        InputStream inputStream = pair.getFirst();
        String second = pair.getSecond();
        String fileName = key + "." + second;
        response.header("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
        byte[] bytes = inputStream.readAllBytes();
        ByteBody body = ByteBody.of(bytes);
        response.body(body);
    }

    /**
     * 根据参数创建 TextExtraData
     * @param request
     * @return TextExtraData
     */
    protected TextExtraData getTextExtraData(Request request){
        String fromName = request.form("fromName", "");
        String toName = request.form("toName", "");
        String groupName = request.form("groupName", "");
        List<String> textList = Optional.ofNullable(request.formParams().get("textList")).orElse(new ArrayList<String>());
        return new TextExtraData(
                fromName, toName, groupName, textList
        );
    }

    protected AvatarExtraDataProvider getAvatarExtraDataFromFile(Request request, KeyData keyData) {
        Map<String, Optional<FileItem>> avatarMap = getAvatarFile(request);
        this.checkAvatarFileParameter(keyData, avatarMap);
        return  new AvatarExtraDataProvider(
                avatarMap.get("FROM").map(BaseController::newCall).orElse(null),
                avatarMap.get("TO").map(BaseController::newCall).orElse(null),
                avatarMap.get("GROUP").map(BaseController::newCall).orElse(null),
                avatarMap.get("BOT").map(BaseController::newCall).orElse(null)
        );
    }

    /**
     * 检查是否缺少必要的参数 缺少 throw  PetpetException
     * @param keyData
     * @param avatarMap
     */
    protected void checkAvatarFileParameter(KeyData keyData,Map<String, Optional<FileItem>> avatarMap)throws PetpetException{
        for (AvatarData avatarData : keyData.getAvatar()) {
            Optional<FileItem> fileItem = avatarMap.get(avatarData.getType().name());
            if (fileItem.isEmpty()){
                throw new PetpetException("缺少"+parameterMap.get(avatarData.getType().name())+"参数");
            }
        }
    }
}
