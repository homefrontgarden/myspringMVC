package com.jsalpha.utils.common;

import java.io.File;
import java.io.FileFilter;

/**
 * @author dengjingsi
 * 目录文件过滤器，过滤指定目录下的文件
 */
public class DirectoryFileFilter implements FileFilter {
    @Override
    public boolean accept(File pathname) {
        if(pathname.isDirectory()){
            return true;
        }
        return false;
    }
}
