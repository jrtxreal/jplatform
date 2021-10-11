package com.dev.platform.common.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @version 1.0
 * @description: File转MultipartFile方法
 * @author: liujj
 * @date 2021/6/22 17:48
 */
public class MultipartFileUtil {

    private static final int SIZE = 1024 * 8;

    public static MultipartFile fileToMultipartFile(File file, String fileType) {
        FileItem fileItem = createFileItem(file, fileType);
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        return multipartFile;
    }

    private static FileItem createFileItem(File file, String fileType) {
        FileItem item = null;
        FileInputStream fis = null;
        OutputStream os = null;

        int bytesRead = 0;
        byte[] buffer = new byte[SIZE];
        try {
            FileItemFactory factory = new DiskFileItemFactory(16, null);
            item = factory.createItem(file.getName(), fileType, true, file.getName());
            fis = new FileInputStream(file);
            os = item.getOutputStream();

            while ((bytesRead = fis.read(buffer, 0, SIZE)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                }
                if (null != os) {
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return item;
    }
}
