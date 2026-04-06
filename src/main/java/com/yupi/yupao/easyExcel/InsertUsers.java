package com.yupi.yupao.easyExcel;

import com.yupi.yupao.model.domain.User;
import com.yupi.yupao.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;

@Component
public class InsertUsers {
    @Resource
    private UserService userService;

    public void doInsertUser() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 1000;
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("假小玉");
            user.setUserAccount("gaga");
            user.setUserAvatar("images/avatar3.jpg");
            user.setTags("[]");
            user.setGender(0);
            user.setUserPassword("12345678");
            user.setPhone("110119120114");
            user.setEmail("2026@qq.com");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setPlanetCode("11111111");
        }
        stopWatch.stop();
        System.out.println(stopWatch.getLastTaskTimeMillis());
    }
}
