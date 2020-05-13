package wToMd.common;

/**
 * <h3>wordToMd</h3>
 *
 * @author : ck
 * @date : 2020-05-13 10:45
 **/
public class PicResource {
    private String wordResId;//表示word中的id
    private String picUrl;//表示rel.xml中引用的相对位置
    public PicResource(String wordResId, String picUrl) {
        this.wordResId = wordResId;
        this.picUrl = picUrl;
    }

    public String getWordResId() {
        return wordResId;
    }

    public void setWordResId(String wordResId) {
        this.wordResId = wordResId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
