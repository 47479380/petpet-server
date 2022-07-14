package xmmt.dituon.server.controller;

import com.hellokaton.blade.annotation.Path;
import com.hellokaton.blade.annotation.request.Form;
import com.hellokaton.blade.annotation.request.PathParam;
import com.hellokaton.blade.annotation.request.Query;
import com.hellokaton.blade.annotation.route.GET;
import com.hellokaton.blade.annotation.route.POST;
import com.hellokaton.blade.ioc.annotation.Inject;
import com.hellokaton.blade.mvc.http.ByteBody;
import com.hellokaton.blade.mvc.http.HttpResponse;
import com.hellokaton.blade.mvc.http.Request;
import com.hellokaton.blade.mvc.http.Response;
import kotlin.Pair;
import xmmt.dituon.server.Service.PetpetService;
import xmmt.dituon.server.model.AvatarUrlPo;
import xmmt.dituon.server.model.Msg;
import xmmt.dituon.server.model.TextDataPo;
import xmmt.dituon.share.AvatarExtraDataProvider;
import xmmt.dituon.share.BaseConfigFactory;
import xmmt.dituon.share.BasePetService;
import xmmt.dituon.share.TextExtraData;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path
public class PetpetConroller {
    private static final String QQ_URL = "https://q1.qlogo.cn/g?b=qq&s=100&nk=";
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
        System.out.println(key);
        response.json(petpetService.getDataMap().get(key));
    }

    @GET("/petpet/generate/qq")
    public void qq(Response response, Request request, @Query String key, @Form AvatarUrlPo avatarUrlPo, @Form TextDataPo textDataPo) {


        AvatarExtraDataProvider avatarExtraDataFromUrls = BaseConfigFactory.getAvatarExtraDataFromUrls(
                QQ_URL + avatarUrlPo.getFromAvatar()
                , QQ_URL + avatarUrlPo.getToAvatar(),
                QQ_URL + avatarUrlPo.getGroupAvatar()
                , QQ_URL + avatarUrlPo.getBotAvatar()
        );

        TextExtraData textExtraData = new TextExtraData(
                textDataPo.getFromName(), textDataPo.getToName(), textDataPo.getGroupName(), textDataPo.getTextList()
        );

        Pair<InputStream, String> pair = petpetService.generateImage(key, avatarExtraDataFromUrls, textExtraData, null);
        try {
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
            response.status(500);
            response.json(new Msg("", "生成petpet失败", 500));
        }

    }

    @GET("/petpet/generate/url")
    public void url(Response response, Request request, @Query String key, @Form AvatarUrlPo avatarUrlPo, @Form TextDataPo textDataPo) {
        AvatarExtraDataProvider avatarExtraDataFromUrls = BaseConfigFactory.getAvatarExtraDataFromUrls(
                 avatarUrlPo.getFromAvatar()
                ,  avatarUrlPo.getToAvatar(),
                avatarUrlPo.getGroupAvatar()
                , avatarUrlPo.getBotAvatar()
        );
        TextExtraData textExtraData = new TextExtraData(
                textDataPo.getFromName(),
                textDataPo.getToName(),
                textDataPo.getGroupName(),
                textDataPo.getTextList()
        );

        Pair<InputStream, String> pair = petpetService.generateImage(key, avatarExtraDataFromUrls, textExtraData, null);
        try {
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
            response.status(500);
            response.json(new Msg("", "生成petpet失败", 500));
        }
    }

//    TODO 上传头像生成petpet
    @POST("/petpet/generate/post")
   public void file(Response response){
        response.text("未实现");
    }
}
