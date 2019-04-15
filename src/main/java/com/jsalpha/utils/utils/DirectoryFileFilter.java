package com.jsalpha.utils.utils;

import java.io.File;
import java.io.FileFilter;

/**
 * @author dengjingsi * 目录文件过滤器，过滤指定路径下的目录
 */
public class DirectoryFileFilter implements FileFilter {
    @Override
    public boolean accept(File pathname) {
        if (pathname.isDirectory()) {
            return true;
        }
        return false;
    }
}

