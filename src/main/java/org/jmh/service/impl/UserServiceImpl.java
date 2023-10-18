package org.jmh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jmh.common.ErrorCode;
import org.jmh.exception.BusinessException;
import org.jmh.model.User;
import org.jmh.mapper.UserMapper;
import org.jmh.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author jmh
 * @since 2023-10-13
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final String SALT = "jmh";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");

        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");

        }
        if (planetCode.length() < 1 || planetCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号过长");

        }

        String regx = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(regx).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        //账号不能为空
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        User ru = baseMapper.selectOne(queryWrapper);
        if (ru != null) {
            return -1;
        }

        //星球编号唯一
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        User ru2 = baseMapper.selectOne(queryWrapper);
        if (ru2 != null) {
            return -1;
        }

        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(newPassword);
        user.setPlanetCode(planetCode);
        int i = baseMapper.insert(user);
        if (i < 1) {
            return -1;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 8) {
            return null;
        }

        String regx = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(regx).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", newPassword);
        User user = baseMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("user login failed,userAccount or userPassword is wrong");
            return null;
        }

        User safeUser = getCleanUser(user);
        request.getSession().setAttribute("userState", safeUser);
        return safeUser;
    }

    @Override
    public User getCleanUser(User user) {
        if (user == null) {
            return null;
        }
        User safeUser = new User();
        safeUser.setId(user.getId());
        safeUser.setUsername(user.getUsername());
        safeUser.setUserAccount(user.getUserAccount());
        safeUser.setAvatarUrl(user.getAvatarUrl());
        safeUser.setGender(user.getGender());
        safeUser.setPhone(user.getPhone());
        safeUser.setEmail(user.getEmail());
        safeUser.setUserStatus(user.getUserStatus());
        safeUser.setCreateTime(user.getCreateTime());
        safeUser.setRole(user.getRole());
        safeUser.setPlanetCode(user.getPlanetCode());
        return safeUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute("userState");
        return 1;
    }
}
