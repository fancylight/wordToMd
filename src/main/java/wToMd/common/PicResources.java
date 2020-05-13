package wToMd.common;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wToMd.doc.pic.MdPicAbstractParseImpl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <h3>wordToMd</h3>
 *
 * @author : ck
 * @date : 2020-05-13 10:47
 **/
public class PicResources {
    private Map<String, PicResource> map = new HashMap();
    private Log log = LogFactory.getLog(PicResources.class);

    public void addResource(String wordId, PicResource picResource) {
        map.put(wordId, picResource);
    }
    public PicResource getPicResource(String wordId){
        return map.get(wordId);
    }
    /**
     * 将图片拷贝到指定的文件路径
     * <p>
     * 根据指定参数将图片拷贝到特定的文件夹,同时可以赋予图片新的名称,并且将{@link PicResource#getPicUrl()}改变为拷贝的新路径,
     * 该新值会被后文中的{@link MdPicAbstractParseImpl}使用
     *
     * @param picDir 拷贝目的地
     * @param prefix 新图片名称前缀
     * @param suffix 新图片名称后缀
     */
    public void uploadResource(String picDir, String prefix, String suffix) {
        log.info("开始上传图片到指定文件夹" + picDir);
        map.values().forEach(picResource -> {
            try {
                String docPic = picResource.getPicUrl();
                String picName = docPic.substring(docPic.lastIndexOf("/") + 1);
                String newDocPic = picDir + File.separator + prefix + picName;
                log.info(String.format("拷贝%s到%s", docPic, picDir));
                FileUtils.copyFile(new File(docPic), new File(newDocPic));
                log.info("拷贝正确");
            } catch (IOException e) {
                e.printStackTrace();
                log.warn("拷贝失败");
            }
        });
    }
}
