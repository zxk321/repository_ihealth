package com.zxk.ihealth.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

public class AliYunUtil {
    // 域名Endpoint
    private static final String ENDPOINT = "http://oss-cn-beijing.aliyuncs.com";
    // 设置需要操作的账号的AK
    private static final String ACCESS_KEY_ID = "LTAI4FrBbdyjVHZ8TjtBLFhk";
    // 设置需要操作的账号的SK
    private static final String ACCESS_KEY_SECRET = "61udgDJMNq4GRNVbtE3aYjcFrFJ83j";
    // 要上传的空间
    private static final String BUCKET = "zxk-ihealth";
    // 外链域名
    private static final String DOMAIN = "https://zxk-ihealth.oss-cn-beijing.aliyuncs.com/";

    /**
     * 根据字节数组上传文件
     * @param content
     * @param fileName
     */
    public static void uploadByBytes(byte[] content,String fileName){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID,ACCESS_KEY_SECRET);
        // 上传Byte数组。
        ossClient.putObject(BUCKET, fileName, new ByteArrayInputStream(content));
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 通过路径名上传本地文件
     * @param localFilePath
     * @param fileName
     */
    public static void uploadByFilePath(String localFilePath,String fileName){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID,ACCESS_KEY_SECRET);
        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET, fileName, new File(localFilePath));

        // 如果需要上传时设置存储类型与访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);

        // 上传文件。
        ossClient.putObject(putObjectRequest);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 根据文件名删除单个文件
     * @param fileName
     */
    public static void deleteByFileName(String fileName){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID,ACCESS_KEY_SECRET);

        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。
        // 如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(BUCKET, fileName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 批量删除文件
     * @param fileNameList
     */
    public static void deleteByFileNameList(List<String> fileNameList){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID,ACCESS_KEY_SECRET);

        // 删除文件。key等同于ObjectName，表示删除OSS文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        List<String> keys = fileNameList;

        DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(BUCKET).withKeys(keys));
        List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();

        // 关闭OSSClient。
        ossClient.shutdown();
    }

}
