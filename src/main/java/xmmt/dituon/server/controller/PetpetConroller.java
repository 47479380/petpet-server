package xmmt.dituon.server.controller;

import com.hellokaton.blade.annotation.Path;
import com.hellokaton.blade.annotation.request.*;
import com.hellokaton.blade.annotation.route.GET;
import com.hellokaton.blade.annotation.route.POST;
import com.hellokaton.blade.ioc.annotation.Inject;
import com.hellokaton.blade.mvc.http.ByteBody;
import com.hellokaton.blade.mvc.http.Request;
import com.hellokaton.blade.mvc.http.Response;
import com.hellokaton.blade.mvc.multipart.FileItem;
import kotlin.Pair;
import kotlin.jvm.functions.Function0;
import xmmt.dituon.server.Service.PetpetService;
import xmmt.dituon.server.model.ResponseCode;
import xmmt.dituon.server.model.ResponseResult;
import xmmt.dituon.share.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Path
public class PetpetConroller {
    private static final String QQ_URL = "https://q1.qlogo.cn/g?b=qq&s=100&nk=";
    private Map<String,String> parameterMap=new HashMap<>();
    {
        parameterMap.put("FORM","fromAvatar");
        parameterMap.put("TO","toAvatar");
        parameterMap.put("GROUP","groupAvatar");
        parameterMap.put("BOT","botAvatar");
    }
    @Inject
    private PetpetService petpetService;

    @GET("/petpet/keys")
    public void keys(Response response) {
        response.json(petpetService.getDataMap().keySet());
    }

    @GET("/petpet/alias")
    public void alias(Response response) {
        Map<String, List<String>> map = new HashMap<>();
        petpetService.getDataMap().forEach((key, value) -> {
            map.put(key, value.getAlias());
        });
        response.json(map);
    }

    @GET("/petpet/key/:key")
    public void key(Response response, @PathParam String key) {
        response.json(petpetService.getDataMap().get(key));
    }

//    根据qq号获取头像生成petpet
    @POST("/petpet/generate/qq")
    public void qq(Response response, Request request) {

        String key = request.form("key", "");
        KeyData keyData = petpetService.getDataMap().get(key);
        //        找不到key直接返回错误
        if (null==keyData){
            response.status(ResponseCode.ERROR.getCode());
            response.json(ResponseResult.error("找不到key："+key));
            return;
        }
        Map<String, Optional<String>> avatarMap = bindAvatarParameter(request);
        for (AvatarData avatarData : keyData.getAvatar()) {
            Optional<String> avatar = avatarMap.get(avatarData.getType().name());
            if (avatar.isEmpty()){
                response.status(ResponseCode.ERROR.getCode());
                response.json(ResponseResult.error("缺少"+parameterMap.get(avatarData.getType().name())+"参数"));
                return;
            }
        }

        String fromName = request.form("fromName", "");
        String toName = request.form("toName", "");
        String groupName = request.form("groupName", "");
        List<String> textList = Optional.ofNullable(request.formParams().get("textList")).orElse(new ArrayList<String>());
        try {
            AvatarExtraDataProvider avatarExtraDataFromUrls = BaseConfigFactory.getAvatarExtraDataFromUrls(
                    QQ_URL + avatarMap.get("FROM").orElse("")
                    , QQ_URL +  avatarMap.get("TO").orElse(""),
                    QQ_URL +  avatarMap.get("GROUP").orElse("")
                    , QQ_URL +  avatarMap.get("BOT").orElse("")
            );

            TextExtraData textExtraData = new TextExtraData(
                    fromName, toName, groupName, textList
            );

            Pair<InputStream, String> pair = petpetService.generateImage(key, avatarExtraDataFromUrls, textExtraData, null);

            if (null == pair) {
                throw new RuntimeException();
            }
            InputStream inputStream = pair.getFirst();
            String second = pair.getSecond();
            String fileName = key + "." + second;
            response.header("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            byte[] bytes = inputStream.readAllBytes();
            ByteBody body = ByteBody.of(bytes);
            response.body(body);
        } catch (RuntimeException | IOException ignored) {
            response.status(ResponseCode.SERVER_ERROR.getCode());
            response.json(new ResponseResult<>(ResponseCode.SERVER_ERROR.getCode(),"生成petpet失败"));

        }

    }

//    根据url获取头像生成petpet
    @POST("/petpet/generate/url")
    public void url(Response response, Request request) {
        String key = request.form("key", "");
        KeyData keyData = petpetService.getDataMap().get(key);
        //        找不到key直接返回错误
        if (null==keyData){
            response.status(ResponseCode.ERROR.getCode());
            response.json(ResponseResult.error("找不到key："+key));
            return;
        }
        Map<String, Optional<String>> avatarMap = bindAvatarParameter(request);
        for (AvatarData avatarData : keyData.getAvatar()) {
            Optional<String> avatar = avatarMap.get(avatarData.getType().name());
            if (avatar.isEmpty()){
                response.status(ResponseCode.ERROR.getCode());
                response.json(ResponseResult.error("缺少"+parameterMap.get(avatarData.getType().name())+"参数"));
                return;
            }
        }
        String fromName = request.form("fromName", "");
        String toName = request.form("toName", "");
        String groupName = request.form("groupName", "");
        List<String> textList = Optional.ofNullable(request.formParams().get("textList")).orElse(new ArrayList<String>());
        try {
            AvatarExtraDataProvider avatarExtraDataFromUrls = BaseConfigFactory.getAvatarExtraDataFromUrls(
                     avatarMap.get("FROM").orElse("")
                    ,  avatarMap.get("TO").orElse(""),
                      avatarMap.get("GROUP").orElse("")
                    ,   avatarMap.get("BOT").orElse("")
            );

            TextExtraData textExtraData = new TextExtraData(
                    fromName, toName, groupName, textList
            );


            Pair<InputStream, String> pair = petpetService.generateImage(key, avatarExtraDataFromUrls, textExtraData, null);

            if (null == pair) {
                throw new RuntimeException();
            }
            InputStream inputStream = pair.getFirst();
            String second = pair.getSecond();
            String fileName = key + "." + second;
            response.header("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            byte[] bytes = inputStream.readAllBytes();
            ByteBody body = ByteBody.of(bytes);
            response.body(body);
        } catch (RuntimeException | IOException ignored) {
            response.status(ResponseCode.SERVER_ERROR.getCode());
            response.json(new ResponseResult<>(ResponseCode.SERVER_ERROR.getCode(),"生成petpet失败"));
        }
    }

    //  上传头像生成petpet
    @POST("/petpet/generate/post")
    public void file(Response response, Request request) {

        String key = request.form("key", "");
        KeyData keyData = petpetService.getDataMap().get(key);
        //        找不到key直接返回错误
        if (null==keyData){
            response.status(ResponseCode.ERROR.getCode());
            response.json(ResponseResult.error("找不到key："+key));
            return;
        }

//        Optional<FileItem> fromAvatar = request.fileItem("fromAvatar");
//        Optional<FileItem> toAvatar = request.fileItem("toAvatar");
//        Optional<FileItem>  groupAvatar = request.fileItem("groupAvatar");
//        Optional<FileItem> botAvatar = request.fileItem("botAvatar");
//        Map<String, Optional<FileItem>> avatarMap=new HashMap<>();
//        avatarMap.put("FORM",fromAvatar);
//        avatarMap.put("TO",toAvatar);
//        avatarMap.put("GROUP",groupAvatar);
//        avatarMap.put("BOT",botAvatar);
       Map<String,Optional<FileItem>> avatarMap=bindAvatarFile(request);
        for (AvatarData avatarData : keyData.getAvatar()) {
            Optional<FileItem> fileItem = avatarMap.get(avatarData.getType().name());
            if (fileItem.isEmpty()){
                response.status(ResponseCode.ERROR.getCode());
                response.json(ResponseResult.error("缺少"+parameterMap.get(avatarData.getType().name())+"参数"));
                return;
            }
        }
        String fromName = request.form("fromName", "");
        String toName = request.form("toName", "");
        String groupName = request.form("groupName", "");
        Map<String, Optional<FileItem>> textMap=new HashMap<>();
//        TODO
        List<String> textList = Optional.ofNullable(request.formParams().get("textList")).orElse(new ArrayList<String>());

        try {
            TextExtraData textExtraData = new TextExtraData(
                    fromName, toName, groupName, textList
            );
            AvatarExtraDataProvider avatarExtraDataProvider = new AvatarExtraDataProvider(
                    avatarMap.get("FROM").map(fileItem -> newCall(fileItem.getFile())).orElse(null),
                    avatarMap.get("TO").map(item -> newCall(item.getFile())).orElse(null),
                    avatarMap.get("GROUP").map(value -> newCall(value.getFile())).orElse(null),
                    avatarMap.get("BOT").map(fileItem1 -> newCall(fileItem1.getFile())).orElse(null)
            );
            Pair<InputStream, String> pair = petpetService.generateImage(key, avatarExtraDataProvider, textExtraData, null);

            if (null == pair) {
                throw new RuntimeException();
            }
            InputStream inputStream = pair.getFirst();
            String second = pair.getSecond();
            String fileName = key + "." + second;
            response.header("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            byte[] bytes = inputStream.readAllBytes();
            ByteBody body = ByteBody.of(bytes);
            response.body(body);
        }catch (Exception e){
            response.status(ResponseCode.SERVER_ERROR.getCode());
            response.json(new ResponseResult<>(ResponseCode.SERVER_ERROR.getCode(),"生成petpet失败"));
        }
    }
    private Map<String,Optional<String>> bindAvatarParameter(Request request){
        Optional<String> fromAvatar = request.form("fromAvatar");
        Optional<String> toAvatar = request.form("toAvatar");
        Optional<String> groupAvatar = request.form("groupAvatar");
        Optional<String> botAvatar = request.form("botAvatar");
        Map<String, Optional<String>> avatarMap=new HashMap<>();
        avatarMap.put("FROM",fromAvatar);
        avatarMap.put("TO",toAvatar);
        avatarMap.put("GROUP",groupAvatar);
        avatarMap.put("BOT",botAvatar);
        return avatarMap;
    }
    private Map<String,Optional<FileItem>> bindAvatarFile(Request request){
        Optional<FileItem> fromAvatar = request.fileItem("fromAvatar");
        Optional<FileItem> toAvatar = request.fileItem("toAvatar");
        Optional<FileItem> groupAvatar = request.fileItem("groupAvatar");
        Optional<FileItem> botAvatar = request.fileItem("botAvatar");
        Map<String, Optional<FileItem>> avatarMap=new HashMap<>();
        avatarMap.put("FORM",fromAvatar);
        avatarMap.put("TO",toAvatar);
        avatarMap.put("GROUP",groupAvatar);
        avatarMap.put("BOT",botAvatar);
        return avatarMap;
    }
    private static Function0<? extends BufferedImage> newCall(File file){
        return () -> {
            try {
               return ImageIO.read(file);
            } catch (IOException e) {
                return null;
            }
        };
    }
}
