package wToMd.common;

/**
 * 实际上用来构造数据
 * @param <T> 表示转换的数据类型
 */
public interface ContextBuild<T> {
    /**
     * 此处的数据实际就是每一个标签处理过后返回的数据
     * @return
     */
    T build();
}
