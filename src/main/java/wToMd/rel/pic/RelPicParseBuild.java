package wToMd.rel.pic;

import wToMd.common.ContextBuild;
import wToMd.common.PicResource;

import java.io.File;

/**
 * <h3>wordToMd</h3>
 *
 * @author : ck
 * @date : 2020-05-13 11:29
 **/
public class RelPicParseBuild implements ContextBuild<PicResource> {
    private PicResource picResource;

    @Override
    public PicResource build() {
        return picResource;
    }

    /**
     * @param id     wordId
     * @param target target值
     */
    public void createPicResource(String id, String target) {
        picResource = new PicResource(id, target);
    }
}
