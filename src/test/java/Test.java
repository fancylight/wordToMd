import wToMd.sax.MdFrameWork;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * <h3>wordToMd</h3>
 *
 * @author : ck
 * @date : 2020-05-11 18:13
 **/
public class Test {
    @org.junit.Test
    public void test() {
        MdFrameWork saxFrameWork = new MdFrameWork();
        saxFrameWork.loadPicDir = "C:\\Users\\86150\\Desktop\\temp";
        saxFrameWork.picPrefix = "__";
        saxFrameWork.parseDocxToMd("DreamWeb2.0开发文档(2)", "D:\\ck\\code\\wordToMd\\target\\test-classes\\word", this.getClass().getResourceAsStream("/word/_rels/document.xml.rels"),
                this.getClass().getResourceAsStream("/word/document.xml"));
    }
}
