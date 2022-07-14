package xmmt.dituon.server.Service;
import com.hellokaton.blade.annotation.route.GET;
import com.hellokaton.blade.ioc.annotation.Bean;
import kotlin.Pair;
import xmmt.dituon.share.*;

import java.io.File;
import java.io.InputStream;
import java.util.List;

@Bean
public class PetpetService extends BasePetService{
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PetpetService.class);

    public PetpetService() {

        File dir = new File("./data/xmmt.dituon.petpet");
        this.readData(dir);
      log.debug("读取"+dir.getAbsolutePath());
    }

}
