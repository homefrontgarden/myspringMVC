package com.jsalpha.utils.utils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author dengjingsi * 实现过滤以某字符串结尾的过滤器，空构造器，默认实现过滤以.class结尾的文件
 */
public class SuffixFilenameFilter implements FilenameFilter {
    private String endWith = ".class";

    /**
     * 构建默认扫描.class后缀的文件过滤器
     */
    public SuffixFilenameFilter() {
    }

    /**
     * 构建扫描endWith后缀的文件过滤器 * @param endWith
     */
    public SuffixFilenameFilter(String endWith) {
        this.endWith = endWith;
    }

    @Override
    public boolean accept(File dir, String name) {
        if (name.endsWith(endWith)) {
            return true;
        }
        return false;
    }
}
