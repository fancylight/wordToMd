package wToMd.sax;


import wToMd.table.MdTableParseImpl;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <h3>wordToMd</h3>
 *
 * @author : ck
 * @date : 2020-05-11 16:21
 **/
public class MdHandler extends CustomHandler<String> {
    private StringBuffer stringBuffer = new StringBuffer();

    public MdHandler() {
        //表单
        this.list = Stream.of(new MdTableParseImpl()).collect(Collectors.toList());
        initParse();
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
