package com.yupi.yupao.easyExcel;

import com.alibaba.excel.EasyExcel;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 导入用户到数据库
 * todo 暂未实现的
 */
public class ImportXingQiuUser {
    public static void main(String[] args) {
        // 写法1：JDK8+ ,不用额外写一个DemoDataListener
        // since: 3.0.0-beta1
        // 文件名先写死
        String fileName = "D:\\projects\\testExcel.xlsx";
        List<XingQiuTableUserInfo> userInfoList = EasyExcel.read(fileName, XingQiuTableUserInfo.class, new TableListener()).sheet().doReadSync();
        System.out.println("总数 = " + userInfoList.size());
        /**
         * 使用stream将取出的数据按昵称过滤
         *  username 非空的元素按 username 分组
         */
        Map<String, List<XingQiuTableUserInfo>> listMap = userInfoList.stream()
                .filter(userInfo -> StringUtils.isNotEmpty(userInfo.getUsername()))
                .collect(Collectors.groupingBy(XingQiuTableUserInfo::getUsername));
        for (Map.Entry<String, List<XingQiuTableUserInfo>> stringListEntry : listMap.entrySet()) {
            if (stringListEntry.getValue().size() > 1) {
                System.out.println("username = " + stringListEntry.getKey());
                System.out.println("有重复的");
            }
        }
        System.out.println("不重复的昵称数 = " + listMap.keySet().size());
    }
}
