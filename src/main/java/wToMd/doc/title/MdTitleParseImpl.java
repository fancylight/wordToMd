package wToMd.doc.title;

import org.xml.sax.Attributes;
import wToMd.common.AbstractParse;

/**
 * <h3>wordToMd</h3>
 *
 * @author : ck
 * @date : 2020-05-14 14:40
 **/
public class MdTitleParseImpl extends AbstractParse<MdTitleContextBuild> {
    @Override
    public Object buildTableResult() {
        return null;
    }

    @Override
    public void dealStartEle(String uri, String localName, String qName, Attributes attributes) {

    }

    @Override
    public void endEle(String uri, String localName, String qName) {

    }

    @Override
    public void dealText(String tag, String context) {

    }
}
