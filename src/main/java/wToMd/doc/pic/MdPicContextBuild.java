package wToMd.doc.pic;

import wToMd.common.ContextBuild;

/**
 * <h3>wordToMd</h3>
 *
 * @author : ck
 * @date : 2020-05-13 15:41
 **/
public class MdPicContextBuild implements ContextBuild<String> {
    private StringBuilder stringBuilder = new StringBuilder();

    @Override
    public String build() {
        return stringBuilder.toString();
    }

    public void insertMdPic(String picLink,String desc) {
        stringBuilder.append("![](").append(picLink).append(" \""+desc).append("\")");
    }
}
