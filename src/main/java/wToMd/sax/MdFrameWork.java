package wToMd.sax;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import wToMd.common.PicResources;
import wToMd.sax.handler.CustomHandler;
import wToMd.sax.handler.MdHandler;
import wToMd.sax.handler.PicHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * <h3>wordToMd</h3>
 *
 * @author : ck
 * @date : 2020-05-11 16:19
 **/
public class MdFrameWork {
    public String loadPicDir;
    public String picPrefix;
    public String picSuffix;
    private static Log log = LogFactory.getLog(MdFrameWork.class);

    /**
     * 该函数用来处理/word/document.xml以及其引用资源word/_rels/document.xml.rels文件
     *
     * @param docDir         解压后文档目录
     * @param relInputStream document.xml.rels文件
     * @param docInputStream document.xml文件
     */
    public void parseDocxToMd(String docName, String docDir, InputStream relInputStream, InputStream docInputStream) {
        log.info(String.format("开始解析%s的xml格式", docName));
        log.info(String.format("xml目录为%s", docDir));
        log.info(String.format("开始处理内部所有引用的图片资源"));
        PicHandler picHandler = new PicHandler(docDir);
        PicResources picResources = parseDoc(relInputStream, picHandler);
        log.info("开始处理内部所有引用的图片资源结束");
        //拷贝图片到指定文件夹,并附带相应的前后缀
        log.info(String.format("开始拷贝引用图片到指定上传路径%s", loadPicDir));
        picResources.uploadResource(loadPicDir, docName + picPrefix, picSuffix);
        log.info("上传结束");
        MdHandler mdHandler = new MdHandler(docName, loadPicDir, picResources);
        String context = parseDoc(docInputStream, mdHandler);
        int a = 3;
    }

    private <T, U> U parseDoc(InputStream inputStream, CustomHandler<T, U> defaultHandler) {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = spf.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(defaultHandler);
            xmlReader.parse(new InputSource(inputStream));
            return defaultHandler.getResult();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
