package wToMd.rel.pic;

import org.xml.sax.Attributes;
import wToMd.common.AbstractParse;
import wToMd.common.EventSource;
import wToMd.common.PicResource;

/**
 * <h3>wordToMd</h3>
 *
 * @author : ck
 * @date : 2020-05-13 11:29
 **/
public class RelPicAbstractParseImpl extends AbstractParse<RelPicParseBuild> {
    public RelPicAbstractParseImpl() {
    }

    public RelPicAbstractParseImpl(boolean isInner) {
        super(isInner);
    }

    public RelPicAbstractParseImpl(Class<RelPicParseBuild> contextBuildClass, boolean isInner) {
        super(contextBuildClass, isInner);
    }

    @Override
    public PicResource buildTableResult() {
        return contextBuild.build();
    }

    @Override
    public void dealStartEle(String uri, String localName, String qName, Attributes attributes) {
        if (!support())
            return;
        if (qName.equals(RelPicXmlDefine.RELATIONSHIP_TAG)) {
            contextBuild = newBuild();
            String id = attributes.getValue(RelPicXmlDefine.ID_ATTR);
            String target = attributes.getValue(RelPicXmlDefine.TARGET_ATTR);
            //判断是否为图片文件
            if (target.startsWith("media") && !target.endsWith("emf")) {
                contextBuild.createPicResource(id, target);
                sendData();
            }
        }

    }

    @Override
    public void endEle(String uri, String localName, String qName) {

    }

    @Override
    public void dealText(String tag, String context) {

    }

    @Override
    protected RelPicParseBuild newBuild() {
        contextBuild = super.newBuild();
        if (contextBuild == null) {
            contextBuild = new RelPicParseBuild();
        }
        return contextBuild;
    }
}
