import wToMd.sax.MdHandler;
import wToMd.sax.SaxFrameWork;

/**
 * <h3>wordToMd</h3>
 *
 * @author : ck
 * @date : 2020-05-11 18:13
 **/
public class Test {
    @org.junit.Test
    public void test() {
        Object obj = SaxFrameWork.parseDoc(this.getClass().getResourceAsStream("document.xml"), new MdHandler());
        int a = 3;
    }
}
