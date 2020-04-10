package com.xusy.springbt.util;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

    /**
     * 创建文件夹及文件
     *
     * @param suffixText 后缀类型（pdf、doc、docx、png、jpg）
     */
    public static File mkDirectoryAndFile(String suffixText) {
        String uuid = UUIDGenerator.sequentialUUIDString();
        String pdfPath = FileUtil.class.getResource("/").getPath()
                .concat("files/template");
        File file = new File(pdfPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = uuid.concat(".").concat(suffixText);
        File writeFile = new File(pdfPath, fileName);
        if (!writeFile.exists()) {
            try {
                writeFile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        // 指定文件格式为utf-8
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(writeFile), StandardCharsets.UTF_8);
            writer.write("");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return writeFile;
    }
}
