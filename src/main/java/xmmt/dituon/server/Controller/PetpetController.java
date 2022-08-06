package xmmt.dituon.server.Controller;

import com.hellokaton.blade.annotation.Path;
import com.hellokaton.blade.annotation.request.*;
import com.hellokaton.blade.annotation.route.GET;
import com.hellokaton.blade.annotation.route.POST;
import com.hellokaton.blade.ioc.annotation.Inject;
import com.hellokaton.blade.mvc.http.Request;
import com.hellokaton.blade.mvc.http.Response;
import com.hellokaton.blade.mvc.ui.RestResponse;
import kotlin.Pair;
import xmmt.dituon.server.Exception.PetpetException;
import xmmt.dituon.server.Service.PetpetService;
import xmmt.dituon.share.*;

import java.io.InputStream;
import java.util.*;

@Path
public class PetpetController extends BaseController {
    private static final String TAG = "PetpetController";
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PetpetService.class);

    private static final String QQ_URL = "https://q1.qlogo.cn/g?b=qq&s=100&nk=";

    @Inject
    private PetpetService petpetService;

    @GET("/")
    public void index1(Response response) {

        response.redirect("/docs/index.html");
    }

    @GET("/petpet")
    public void index(Response response) {

        response.redirect("/docs/index.html");
    }

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
        if (null == keyData) {
            response.json(RestResponse.fail(key+"不存在"));
            return;
        }
        try {
            AvatarExtraDataProvider avatarExtraDataFromUrls = this.getAvatarExtraDataFromQQ(request,keyData);
            TextExtraData textExtraData =this.getTextExtraData(request);

            Pair<InputStream, String> pair = petpetService.generateImage(key, avatarExtraDataFromUrls, textExtraData, null);

            if (null == pair) {
                throw new  PetpetException("生成petpet失败");
            }
            this.downloadImage(response, pair, key);
        } catch (Exception e) {
            log.error(TAG, e.getMessage());
            response.json(RestResponse.fail(e.getMessage()));
        }

    }

    //    根据url获取头像生成petpet
    @POST("/petpet/generate/url")
    public void url(Response response, Request request) {
        String key = request.form("key", "");
        KeyData keyData = petpetService.getDataMap().get(key);
        //        找不到key直接返回错误
        if (null == keyData) {
            response.json(RestResponse.fail(key+"不存在"));
            return;
        }

           try {
            AvatarExtraDataProvider avatarExtraDataFromUrls = this.getAvatarExtraDataFromUrls(request,keyData);

            TextExtraData textExtraData = this.getTextExtraData(request);

            Pair<InputStream, String> pair = petpetService.generateImage(key, avatarExtraDataFromUrls, textExtraData, null);

               if (null == pair) {
                   throw new  PetpetException("生成petpet失败");
               }
               this.downloadImage(response, pair, key);
           }catch (Exception e) {
               log.error(TAG, e.getMessage());
               response.json(RestResponse.fail(e.getMessage()));
           }
    }

    //  上传头像生成petpet
    @POST("/petpet/generate/post")
    public void file(Response response, Request request) {

        String key = request.form("key", "");
        KeyData keyData = petpetService.getDataMap().get(key);
        //        找不到key直接返回错误
        if (null == keyData) {
            response.json(RestResponse.fail(key+"不存在"));
            return;
        }
        try {
            TextExtraData textExtraData = this.getTextExtraData(request);
            AvatarExtraDataProvider avatarExtraDataProvider =this.getAvatarExtraDataFromFile(request,keyData);
            Pair<InputStream, String> pair = petpetService.generateImage(key, avatarExtraDataProvider, textExtraData, null);

            if (null == pair) {
                throw new  PetpetException("生成petpet失败");
            }
            this.downloadImage(response, pair, key);
        } catch (Exception e) {
            log.error(TAG, e.getMessage());
            response.json(RestResponse.fail(e.getMessage()));
        }
    }

    protected AvatarExtraDataProvider getAvatarExtraDataFromUrls(Request request, KeyData keyData) {

        Map<String, Optional<String>> avatarMap = getAvatarParameter(request);
        this.checkAvatarParameter(keyData, avatarMap);
       return BaseConfigFactory.getAvatarExtraDataFromUrls(
                avatarMap.get("FROM").orElse("")
                , avatarMap.get("TO").orElse(""),
                avatarMap.get("GROUP").orElse("")
                , avatarMap.get("BOT").orElse("")
        );
    }

    protected AvatarExtraDataProvider getAvatarExtraDataFromQQ(Request request, KeyData keyData) {

        Map<String, Optional<String>> avatarMap = getAvatarParameter(request);
        this.checkAvatarParameter(keyData, avatarMap);
        return BaseConfigFactory.getAvatarExtraDataFromUrls(
                QQ_URL + avatarMap.get("FROM").orElse("")
                , QQ_URL + avatarMap.get("TO").orElse(""),
                QQ_URL + avatarMap.get("GROUP").orElse("")
                , QQ_URL + avatarMap.get("BOT").orElse("")
        );
    }
    /**
     * 检查是否缺少必要的参数 缺少 throw  PetpetException
     *
     * @param keyData
     * @param avatarMap
     */
    protected void checkAvatarParameter(KeyData keyData, Map<String, Optional<String>> avatarMap) throws PetpetException {
        for (AvatarData avatarData : keyData.getAvatar()) {
            Optional<String> avatar = avatarMap.get(avatarData.getType().name());
            if (avatar.isEmpty()) {
                throw new PetpetException("缺少" + parameterMap.get(avatarData.getType().name()) + "参数");
            }
        }
    }

    /**
     * 获取Avatar的url参数
     *
     * @param request
     * @return url参数的map对象
     */
    protected Map<String, Optional<String>> getAvatarParameter(Request request) {
        Optional<String> fromAvatar = request.form("fromAvatar");
        Optional<String> toAvatar = request.form("toAvatar");
        Optional<String> groupAvatar = request.form("groupAvatar");
        Optional<String> botAvatar = request.form("botAvatar");
        Map<String, Optional<String>> avatarMap = new HashMap<>();
        avatarMap.put("FROM", fromAvatar);
        avatarMap.put("TO", toAvatar);
        avatarMap.put("GROUP", groupAvatar);
        avatarMap.put("BOT", botAvatar);
        return avatarMap;
    }

}
