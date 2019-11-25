package com.xusy.springbt.util;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author xsy
 * @create 2019-11-22 13:26
 * @desc
 **/
public class FileUtil {
    //下载文件
    public static void downLoadFile(String fileName, File file, HttpServletResponse response) throws UnsupportedEncodingException {
        // 设置输出的格式
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "utf-8") + "\"");
        // 循环取出流中的数据
        OutputStream out = null;
        InputStream inp = null;
        try {
            out = response.getOutputStream();
            inp = new FileInputStream(file);
            IOUtils.copy(inp, out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inp != null) {
                    inp.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
