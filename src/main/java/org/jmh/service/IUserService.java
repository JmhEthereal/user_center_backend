package org.jmh.service;

import org.jmh.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author jmh
 * @since 2023-10-13
 */
public interface IUserService extends IService<User> {
    /**
     *
     * @param userAccount 用户账号
     * @param userPassword 密码
     * @param checkPassword 新密码
     * @param planetCode 星球编号
     * @return 用户id
     */
    long userRegister(String userAccount,String userPassword,String checkPassword,String planetCode);

    /**
     *
     * @param userAccount 登录账号
     * @param userPassword 登录密码
     * @return 用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    User getCleanUser(User user);

    /**
     * 用户注销
     * @param request 请求
     */
    int userLogout(HttpServletRequest request);
}
