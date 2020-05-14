package wToMd.sax.handler;


import wToMd.common.AbstractParse;
import wToMd.common.PicResource;
import wToMd.common.PicResources;
import wToMd.rel.pic.RelPicParseImpl;

import java.io.File;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <h3>wordToMd</h3>
 * 用来获取在res文件中定义的图片资源
 *
 * @author : ck
 * @date : 2020-05-13 10:43
 **/
public class PicHandler extends CustomHandler<PicResource, PicResources> {
    private PicResources picResources = new PicResources();
    private String docDir; //如D:\ck\code\wordToMd\src\test\word\

    public PicHandler(String docDir) {

        this.docDir = docDir;
    }

    @Override
    public PicResources getResult() {
        return picResources;
    }

    @Override
    protected void initParseList() {
        this.list = Stream.of(new RelPicParseImpl()).collect(Collectors.toList());
    }

    /**
     * 此处用来将{@link PicResource#getPicUrl()} 部分修改为解压文件夹中的media的绝对路径
     *
     * @param data
     */
    @Override
    public void acceptData(PicResource data, AbstractParse abstractParse) {
        picResources.addResource(data.getWordResId(), data);
        String url = data.getPicUrl();
        data.setPicUrl(docDir + File.separator + File.separator + url);
    }
}
