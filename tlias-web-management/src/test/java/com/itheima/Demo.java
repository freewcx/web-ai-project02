package com.itheima;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.*;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;

public class Demo {

    public static void main(String[] args) throws Exception {
        System.out.println("========== 开始执行OSS上传测试 ==========");
        
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";
        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "heima-java-web-ai-2026";
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = "001.jpg";
        // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        String filePath= "D:\\images\\5fb3f4ff9727495a9e89bb950a4025b6.jpg";
        // 填写Bucket所在地域。以华东1（杭州）为例，Region填写为cn-hangzhou。
        String region = "cn-beijing";
        
        System.out.println("Endpoint: " + endpoint);
        System.out.println("Bucket: " + bucketName);
        System.out.println("Object: " + objectName);
        System.out.println("文件路径: " + filePath);
        System.out.println("Region: " + region);
        
        // 检查文件是否存在
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("❌ 错误：文件不存在 - " + filePath);
            return;
        }
        System.out.println("✅ 文件存在，大小: " + file.length() + " bytes");
        
        // 创建OSSClient实例。
        // 当OSSClient实例不再使用时，调用shutdown方法以释放资源。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);        
        OSS ossClient = OSSClientBuilder.create()
        .endpoint(endpoint)
        .credentialsProvider(credentialsProvider)
        .clientConfiguration(clientBuilderConfiguration)
        .region(region)               
        .build();
        
        System.out.println("✅ OSS客户端创建成功，开始上传...");

        try {
            // 创建PutObjectRequest对象。
            byte[] content = Files.readAllBytes(file.toPath());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new ByteArrayInputStream(content));
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            
            System.out.println("✅ 上传成功！");
            System.out.println("ETag: " + result.getETag());
            System.out.println("RequestId: " + result.getRequestId());
            System.out.println("文件访问URL: https://" + bucketName + "." + endpoint.replace("https://", "") + "/" + objectName);           
        } catch (OSSException oe) {
            System.err.println("❌ OSS异常：请求到达了OSS，但被拒绝");
            System.err.println("Error Message: " + oe.getErrorMessage());
            System.err.println("Error Code: " + oe.getErrorCode());
            System.err.println("Request ID: " + oe.getRequestId());
            System.err.println("Host ID: " + oe.getHostId());
        } catch (ClientException ce) {
            System.err.println("❌ 客户端异常：客户端与OSS通信时遇到严重问题");
            System.err.println("Error Message: " + ce.getMessage());
            if (ce.getMessage().contains("No credentials")) {
                System.err.println("\n💡 提示：需要设置环境变量 OSS_ACCESS_KEY_ID 和 OSS_ACCESS_KEY_SECRET");
                System.err.println("在Windows PowerShell中设置：");
                System.err.println("  $env:OSS_ACCESS_KEY_ID=\"你的AccessKeyId\"");
                System.err.println("  $env:OSS_ACCESS_KEY_SECRET=\"你的AccessKeySecret\"");
            }
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
                System.out.println("✅ OSS客户端已关闭");
            }
        }
        
        System.out.println("========== 程序执行完毕 ==========");
    }
}