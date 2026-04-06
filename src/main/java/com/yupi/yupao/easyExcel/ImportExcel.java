package com.yupi.yupao.easyExcel;

import com.alibaba.excel.EasyExcel;

import java.util.List;

public class ImportExcel {
    /**
     * 读取excel
     *
     * @param args
     */
    public static void main(String[] args) {
        // 写法1：JDK8+ ,不用额外写一个DemoDataListener
        // since: 3.0.0-beta1
        // 文件名先写死
        String fileName = "D:\\projects\\testExcel.xlsx";
        readByListener(fileName);
    }

    /**
     * 监听器读取
     *
     * @param fileName
     */
    public static void readByListener(String fileName) {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        // 这里每次会读取100条数据 然后返回过来 直接调用使用数据就行
        EasyExcel.read(fileName, XingQiuTableUserInfo.class, new TableListener()).sheet().doRead();
    }

    /**
     * 同步读
     * 同步地返回，会一次性读取所有数据，
     *
     * @param fileName
     */
    public static void synchronousRead(String fileName) {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<XingQiuTableUserInfo> list = EasyExcel.read(fileName).head(XingQiuTableUserInfo.class).sheet().doReadSync();
        for (XingQiuTableUserInfo xingQiuTableUserInfo : list) {
            System.out.println(xingQiuTableUserInfo);
        }
    }

}
