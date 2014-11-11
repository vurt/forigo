package com.vurt.node.agent.file;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Vurt on 14/11/11.
 */
public class FileUtil {

    /**
     * 读取整个目录的最后修改时间(查看所有文件和目录的最后修改时间，找到最晚的那个)
     *
     * @param directory 目标目录
     * @return 目录的最后修改时间
     */
    public static long getLastModified(File directory) {
        File[] files = directory.listFiles();
        if (files.length == 0) return directory.lastModified();
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File o1, File o2) {
                return new Long(o2.lastModified()).compareTo(o1.lastModified()); //latest 1st
            }
        });
        return files[0].lastModified();
    }
}
