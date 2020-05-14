package wToMd.doc.title;

import wToMd.common.CommonSymbol;
import wToMd.common.ContextBuild;

/**
 * <h3>wordToMd</h3>
 *
 * @author : ck
 * @date : 2020-05-14 14:40
 **/
public class MdParagraphContextBuild implements ContextBuild<String> {
    private StringBuilder stringBuilder = new StringBuilder();

    /**
     * 将<w:tc>文字数据</w:tc>部分的添加
     *
     * @param text
     */
    public void insertNewStrByT(String text) {
        stringBuilder.append(text);
    }

    public void endWithLine() {
        stringBuilder.append(CommonSymbol.commonLine);
    }

    @Override
    public String build() {
        return stringBuilder.toString();
    }
}
