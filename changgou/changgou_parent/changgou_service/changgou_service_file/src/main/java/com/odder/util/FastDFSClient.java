package com.odder.util;

import com.odder.file.FastDFSFile;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.concurrent.TransferQueue;

/**
 * @Description FastDFS操作工具
 * 实现信息获取、文件上传、文件下载、文件删除的相关操作
 * @Author Odder
 * @Date 2019/12/19 20:14
 * @Version 1.0
 */
public class FastDFSClient {
    static {
        try {
            //1、获取配置文件路径-filePath = new ClassPathResource("fdfs_client.conf").getPath()
            String path = new ClassPathResource("fdfs_client.conf").getPath();
            //2、加载配置文件-ClientGlobal.init(配置文件路径)
            ClientGlobal.init(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TrackerServer getTrackerServer() {
        //3、创建一个TrackerClient对象。直接new一个。
        TrackerClient trackerClient = new TrackerClient();
        //4、使用TrackerClient对象创建连接，getConnection获得一个TrackerServer对象。
        TrackerServer connection = null;
        try {
            connection = trackerClient.getConnection();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return connection;
    }

    /**
     * @return
     */
    public static StorageClient getStorageClient() {
        //5、创建一个StorageClient对象，直接new一个，需要两个参数TrackerServer对象、null
        return new StorageClient(getTrackerServer(), null);
    }

    /**
     * update
     *
     * @param fastDFSFile
     * @return java.lang.String[]
     * @date 20:56 2019/12/19
     * @author Odder
     **/
    public static String[] update(FastDFSFile fastDFSFile) {
        String[] updateRsult = null;

        //附加参数
        NameValuePair[] nameValuePairs = new NameValuePair[1];
        nameValuePairs[0] = new NameValuePair("author", fastDFSFile.getAuthor());
        //上传文件
        try {
            updateRsult = getStorageClient().upload_file(fastDFSFile.getContent(), fastDFSFile.getExt(), nameValuePairs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateRsult;
    }

    /**
     * info
     *
     * @param groupName
     * @param remoteFilename
     * @return org.csource.fastdfs.FileInfo
     * @date 21:56 2019/12/19
     * @author Odder
     **/
    public static FileInfo getFileInfo(String groupName, String remoteFilename) {

        FileInfo fileInfo = null;
        try {
            fileInfo = getStorageClient().get_file_info(groupName, remoteFilename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileInfo;
    }

    /**
     * download file
     *
     * @param groupName
     * @param remoteFilename
     * @return java.io.InputStream
     * @date 22:13 2019/12/19
     * @author Odder
     **/
    public static InputStream downloadFile(String groupName, String remoteFilename) {
        byte[] bytes = new byte[0];
        try {
            bytes = getStorageClient().download_file(groupName, remoteFilename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(bytes);
    }


    /**
     * delete
     *
     * @param groupName
     * @param remoteFilename
     * @return void
     * @date 22:17 2019/12/19
     * @author Odder
     **/
    public static void deleteFile(String groupName, String remoteFilename) {
        try {
            int i = getStorageClient().delete_file(groupName, remoteFilename);
        } catch (IOException | MyException e) {
            e.printStackTrace();
        }
    }

    /**
     * get storageserver info
     *
     * @param groupName
     * @return org.csource.fastdfs.StorageServer
     * @date 22:21 2019/12/19
     * @author Odder
     **/
    public static StorageServer getStorageServer(String groupName) {
        try {
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            return trackerClient.getStoreStorage(trackerServer, groupName);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  getServerInfo
     * @param groupName
     * @param remoteFilename
     * @return org.csource.fastdfs.ServerInfo[]
     * @date 22:34 2019/12/19
     * @author Odder
     **/
    public static  ServerInfo[] getServerInfo(String groupName, String remoteFilename){
        try {
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            return trackerClient.getFetchStorages(trackerServer, groupName, remoteFilename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * getTrackerUrl
     * @param
     * @return java.lang.String
     * @date 22:43 2019/12/19
     * @author Odder
     **/
    public static String getTrackerUrl(){
        try {
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            //拼接TrackerUrl
            String url = "http://" + trackerServer.getInetSocketAddress().getHostString() + ":"
                    + ClientGlobal.getG_tracker_http_port() + "/";
            return url;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    //测试方法

    /**
     * 测试方法
     *
     * @param args
     * @return void
     * @date 22:18 2019/12/19
     * @author Odder
     **/
    public static void main(String[] args) {
        //获取文件信息-测试
        //group1/M00/00/00/wKjThF1pAcKAYNhxAA832942OCg928.jpg
    /*FileInfo info = getFileInfo("group1", "M00/00/00/wKjThF1pAcKAYNhxAA832942OCg928.jpg");
    System.out.println(info);*/
        try {
            //文件下载-测试
            InputStream is = downloadFile("group1", "M00/00/00/wKjThF1pAcKAYNhxAA832942OCg928.jpg");
            //定义输出流对象
            OutputStream out = new FileOutputStream("D:/1.jpg");
            //定义缓冲区
            byte[] buff = new byte[1024];
            //读取输入流
            while ((is.read(buff) > -1)) {
                out.write(buff);
            }
            //释放资源
            out.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}