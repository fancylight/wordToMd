package wToMd.pic;

import org.xml.sax.Attributes;
import wToMd.table.Parse;

/**
 * <h3>wordToMd</h3>
 *
 * @author : ck
 * @date : 2020-05-12 16:31
 **/
public class MdPicParseImpl extends Parse<MdPicContextBuild> {

    @Override
    public Object buildTableResult() {
        return null;
    }

    @Override
    public void dealStartEle(String uri, String localName, String qName, Attributes attributes) {
        if (!support())
            return;
    }

    @Override
    public void endEle(String uri, String localName, String qName) {

    }

    @Override
    public void dealText(String tag, String context) {

    }
}
