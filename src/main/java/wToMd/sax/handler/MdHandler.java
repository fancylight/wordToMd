package wToMd.sax.handler;


import wToMd.common.AbstractParse;
import wToMd.common.PicResources;
import wToMd.doc.pic.MdPicParseImpl;
import wToMd.doc.table.MdTableParseImpl;
import wToMd.doc.title.MdParagraphParseImpl;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <h3>wordToMd</h3>
 *
 * @author : ck
 * @date : 2020-05-11 16:21
 **/
public class MdHandler extends CustomHandler<String, String> {
    private StringBuffer stringBuffer = new StringBuffer();
    private String mdName;
    private String picLoadDir;
    private PicResources picResources;

    public MdHandler(String mdName, String picLoadDir, PicResources picResources) {
        this.mdName = mdName;
        this.picLoadDir = picLoadDir;
        this.picResources = picResources;
        ((MdPicParseImpl) this.list.get(1)).setPicResources(picResources);
    }

    @Override
    protected void initParseList() {
        //表单
        this.list = Stream.of(new MdTableParseImpl(), new MdPicParseImpl(picResources),new MdParagraphParseImpl()).collect(Collectors.toList());
    }

    @Override
    public String getResult() {
        return stringBuffer.toString();
    }

    @Override
    public void acceptData(String data, AbstractParse abstractParse) {
        stringBuffer.append(data);
    }
}
