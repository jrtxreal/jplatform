package com.dev.platform.service.enums;

/**
 * @author liujj
 * @description 文件类型枚举类
 * @date 2021-06-11
 */
public enum FileTypeEnum {

    // word
    DOC(".doc", "word"),

    // word
    DOCX(".docx", "word"),

    XLS(".xls", "excel"),

    XLSX(".xlsx", "excel"),

    PPT(".ppt", "ppt"),

    PPTX(".pptx", "ppt"),

    PDF(".pdf", "pdf"),

    JPG(".jpg", "jpg"),

    JPEG(".jpeg", "jpeg"),

    PNG(".png", "png"),

    TIF(".tif", "tif"),

    TIFF(".tiff", "tiff");


    private String type;

    private String desc;

    FileTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 判断文件是否是word文档
     *
     * @param fileType 文件格式
     * @return
     */
    public static boolean isWordFile(String fileType) {
        if (FileTypeEnum.DOC.getType().equalsIgnoreCase(fileType)) {
            return true;
        }

        if (FileTypeEnum.DOCX.getType().equalsIgnoreCase(fileType)) {
            return true;
        }
        return false;
    }

    /**
     * 判断文件是否是excel文档
     *
     * @param fileType 文件格式
     * @return
     */
    public static boolean isExcelFile(String fileType) {

        if (FileTypeEnum.XLS.getType().equalsIgnoreCase(fileType)) {
            return true;
        }

        if (FileTypeEnum.XLSX.getType().equalsIgnoreCase(fileType)) {
            return true;
        }
        return false;
    }

}
