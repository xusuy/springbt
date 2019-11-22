package com.xusy.springbt.util;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * @author xsy
 * @create 2019-11-22 13:26
 * @desc
 **/
public class FileUtil {
    //下载文件
    public static void downLoadFile(String fileName, InputStream inp, HttpServletResponse response) throws IOException {
        // 设置输出的格式
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "utf-8") + "\"");
        // 循环取出流中的数据
        OutputStream out = response.getOutputStream();
        IOUtils.copy(inp, out);
    }
}
