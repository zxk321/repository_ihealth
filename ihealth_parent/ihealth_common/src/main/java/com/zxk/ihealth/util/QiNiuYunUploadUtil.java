package com.zxk.ihealth.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class QiNiuYunUploadUtil {
    // 设置需要操作的账号的AK
    private static final String ACCESS_KEY = "XXXXXXXXXX";
    // 设置需要操作的账号的SK
    private static final String SECRET_KEY = "XXXXXXXXXXXXX";
    // 要上传的空间
    private static final String BUCKET = "XXXXXXXXXX";
    // 域名domainOfBucket
    private static final String DOMAIN_OF_BUCKET = "XXXXXXXXXX";
    // 华东是Zone.zone0(),华北是Zone.zone1(),华南是Zone.zone2(),北美是Zone.zoneNa0()
    private static final Zone ZONE = Zone.zone0();   // todo

    /**
     * 通过字节数组上传文件
     * @param uploadBytes
     * @param fileName
     */
    public static void uploadByBytes(byte[] uploadBytes,String fileName){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(ZONE);
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;
        try {
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            String upToken = auth.uploadToken(BUCKET);
            try {
                Response response = uploadManager.put(uploadBytes, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
    }

    /**
     * 通过文件路径上传文件
     * @param localFilePath
     * @param fileName
     */
    public static void uploadByFilePath(String localFilePath,String fileName){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(ZONE);
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String upToken = auth.uploadToken(BUCKET);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    /**
     * 删除文件
     * @param fileName
     */
    public static void deleteByFileName(String fileName){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(ZONE);
        //...其他参数参考类注释

        String key = fileName;
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(BUCKET, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }
}
