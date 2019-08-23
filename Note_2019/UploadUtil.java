package com.pinyougou.util;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

/***
 *
 *  FastDFS文件上传
 ****/
public class UploadUtil {


    /****
     * 文件上传
     *      1.vfile:准备上传的文件全路径
     *      2.trackerserver:指定TrackerServer通信地址信息  Socket通信
     */
    public static String[] upload(String trackerserver, String vfile) throws Exception{
        //将classpath:替换成类路径
        trackerserver = getClasspath(trackerserver);
        vfile = getClasspath(vfile);


        //初始化加载TrackerServer服务地址通信信息
        ClientGlobal.init(trackerserver);

        //创建一个TrackerServer的客户端,目的是链接TrackerServer
        TrackerClient trackerClient = new TrackerClient();

        //通过TrackerServer客户端创建一个TrackerServer服务端对象。获取链接
        TrackerServer trackerServer = trackerClient.getConnection();

        //通过TrackerServer创建一个Storage客户端
        StorageClient storageClient = new StorageClient(trackerServer,null);

        //通过Storage客户端上传文件
        //storageClient.upload_file("文件地址","后缀名","文件组名");
        String[] uploads = storageClient.upload_file(vfile, null, null);

        //获取文件上传信息   [0]:组名   [1]：存储路径
        for (String upload : uploads) {
            System.out.println(upload);
        }

        return uploads;
    }



    /****
     * 文件上传
     *      1.buffer:要上传的文件的字节数组
     *      2.trackerserver:指定TrackerServer通信地址信息  Socket通信
     *      3.上传的文件后缀名
     */
    public static String[] upload(String trackerserver, byte[] buffer,String ext) throws Exception{
        //将classpath:换成类路径
        trackerserver = getClasspath(trackerserver);

        //初始化加载TrackerServer服务地址通信信息
        ClientGlobal.init(trackerserver);

        //创建一个TrackerServer的客户端,目的是链接TrackerServer
        TrackerClient trackerClient = new TrackerClient();

        //通过TrackerServer客户端创建一个TrackerServer服务端对象。获取链接
        TrackerServer trackerServer = trackerClient.getConnection();

        //通过TrackerServer创建一个Storage客户端
        StorageClient storageClient = new StorageClient(trackerServer,null);

        //通过Storage客户端上传文件
        //storageClient.upload_file("文件地址","后缀名","文件组名");
        String[] uploads = storageClient.upload_file(buffer, ext, null);

        //获取文件上传信息   [0]:组名   [1]：存储路径
        for (String upload : uploads) {
            System.out.println(upload);
        }

        return uploads;
    }


    /***
     * 替换classpath:
     * path  =  classpath:1.jpg
     * @param path
     * @return
     */
    public static String getClasspath(String path){
        //获取类路径
        String classPath = UploadUtil.class.getResource("/").getPath();

        //将classpath:换成类路径
        path = path.replace("classpath:",classPath);
        return path;
    }

    public static void main(String[] args) throws Exception{
        //classpath:
        //String path = "D:\\project\\workspace54\\pinyougou\\pinyougou-parent\\pinyougou-common\\src\\main\\resources\\";
        String path="classpath:";
        String vfile = path+"1.jpg";
        String trackerserver= path+"tracker.conf";
        upload(trackerserver,vfile);

        //读取文件的字节数组，上传文件
        byte[] buffer = FileUtils.toByteArray(getClasspath(vfile));

        UploadUtil.upload(trackerserver,buffer,"jpg");


    }


}
