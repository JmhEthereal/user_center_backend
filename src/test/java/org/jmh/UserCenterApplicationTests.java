package org.jmh;

import org.jmh.service.IUserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

@SpringBootTest
class UserCenterApplicationTests {
    @Autowired
    private IUserService userService;

    @Test
    void contextLoads() {

    }

    @Test
    void userRegisterTest() {
        String userAccount = "jmhn";
        String userPassword = "";
        String checkPassword = "123456";
        String planetCode="1";
        long result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assert.assertEquals(-1, result);
        userAccount = "jmh";
        result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assert.assertEquals(-1, result);
        userAccount = "jmhnb";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assert.assertTrue(result > 0);

    }
}
