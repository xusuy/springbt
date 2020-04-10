package com.xusy.springbt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author xsy
 * @create 2020-04-09 9:49
 * @desc 压缩工具类
 **/
public class PackageUtils {

    //在指定的压缩文件中解析出对应的类型文件另外压缩
    public static void zipAgain(File file, String fileFileName) throws IOException {
        String classpath;
        ZipOutputStream zos = null;
        FileInputStream is = null;
        ZipInputStream zipis = null;
        try {
            //从classpath
            classpath = PackageUtils.class.getResource("/").getPath();
            is = new FileInputStream(file);
            // 把文件输入流转换为压缩输入流
            zipis = new ZipInputStream(is);
            zos = new ZipOutputStream(new FileOutputStream(classpath + "zip/" + fileFileName));
            ZipEntry entry = null;
            //验证是否包含一个xml文件和png图片,并将xml和png图片进行压缩
            while ((entry = zipis.getNextEntry()) != null) {
                if (entry.getName().indexOf(".xml") > 0 || entry.getName().indexOf(".png") > 0) {//后缀不可能在第一位出现所以是>0
                    zos.putNextEntry(new ZipEntry(entry.getName()));
                    byte data[] = new byte[1024];
                    int len = 0;
                    while ((len = zipis.read(data)) != -1) {
                        zos.write(data, 0, len);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (is != null) {
                is.close();
            }
            if (zipis != null) {
                zipis.close();
            }
            if (zos != null) {
                zos.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        File file = new File("D:\\work\\通宝行\\document\\理赔材料_20200408181656.zip");
        PackageUtils.zipAgain(file, "again.zip");
    }
}
