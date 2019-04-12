package com.jsalpha.utils.common;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author dengjingsi
 * 实现过滤以某字符串结尾的过滤器，空构造器，默认实现过滤以.class结尾的文件
 */
public class SuffixFilenameFilter implements FilenameFilter {
    private String endWith = ".class";

    public SuffixFilenameFilter(String endWith) {
        this.endWith = endWith;
    }

    @Override
    public boolean accept(File dir, String name) {
        if(name.endsWith(endWith)){
            return true;
        }
        return false;
    }
}
