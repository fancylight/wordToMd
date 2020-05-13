package wToMd.sax.handler;


import wToMd.common.PicResources;
import wToMd.doc.pic.MdPicAbstractParseImpl;
import wToMd.doc.table.MdTableAbstractParseImpl;

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
    }

    @Override
    protected void initParseList() {
        //表单
        this.list = Stream.of(new MdTableAbstractParseImpl(), new MdPicAbstractParseImpl(picResources)).collect(Collectors.toList());
    }

    @Override
    public String getResult() {
        return stringBuffer.toString();
    }

    @Override
    public void acceptData(String data) {
        stringBuffer.append(data);
    }
}
