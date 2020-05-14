package wToMd.doc.pic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import wToMd.common.AbstractParse;
import wToMd.common.CommonSymbol;
import wToMd.common.PicResources;
import wToMd.event.EventType;

/**
 * <h3>wordToMd</h3>
 *
 * @author : ck
 * @date : 2020-05-13 15:41
 **/
public class MdPicParseImpl extends AbstractParse<MdPicContextBuild> {
    private PicResources picResources;
    private static Log log = LogFactory.getLog(MdPicParseImpl.class);

    public MdPicParseImpl(PicResources picResources) {
        this.picResources = picResources;
    }

    public MdPicParseImpl(PicResources picResources, boolean isInner) {
        super(isInner);
        this.picResources = picResources;
    }

    public PicResources getPicResources() {
        return picResources;
    }

    public void setPicResources(PicResources picResources) {
        this.picResources = picResources;
    }

    @Override
    protected MdPicContextBuild newBuild() {
        contextBuild = super.newBuild();
        if (contextBuild == null)
            contextBuild = new MdPicContextBuild();
        return contextBuild;
    }

    @Override
    protected void initEvent() {
        this.acceptEvent = new EventType[]{EventType.PICBEGIN, EventType.PICEND};
    }

    @Override
    public String buildTableResult() {
        return contextBuild.build()+ CommonSymbol.commonLine;
    }

    @Override
    public void dealStartEle(String uri, String localName, String qName, Attributes attributes) {
        if (!support(true))
            return;
        if (qName.endsWith(PicXmlDefine.A_BLIP)) { //遇到了图片引用标志
            String picRel = attributes.getValue(PicXmlDefine.R_EMBED);
            String url = picResources.getPicResource(picRel).getPicUrl();
            newBuild();
            if (url == null) {
                log.warn(String.format("尝试获取%s引用图片失败,请检查", picRel), new RuntimeException());
            }
            contextBuild.insertMdPic(url, "");
            sendData();
        }
    }

    @Override
    public void endEle(String uri, String localName, String qName) {

    }

    @Override
    public void dealText(String tag, String context) {

    }
}
