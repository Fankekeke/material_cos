package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.authentication.JWTToken;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.domain.ActiveUser;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.common.utils.*;
import cc.mrbird.febs.system.dao.LoginLogMapper;
import cc.mrbird.febs.system.domain.LoginLog;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.domain.UserConfig;
import cc.mrbird.febs.system.manager.UserManager;
import cc.mrbird.febs.system.service.LoginLogService;
import cc.mrbird.febs.system.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service("loginLogService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private FebsProperties properties;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserManager userManager;

    @Override
    @Transactional
    public void saveLoginLog(LoginLog loginLog) {
        loginLog.setLoginTime(new Date());
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip = IPUtil.getIpAddr(request);
        loginLog.setIp(ip);
        loginLog.setLocation(AddressUtil.getCityInfo(ip));
        this.save(loginLog);
    }

    @Override
    public FebsResponse faceLogin(User user, HttpServletRequest request) throws Exception {

        if (User.STATUS_LOCK.equals(user.getStatus())) {
            throw new FebsException("账号已被锁定,请联系管理员！");
        }

        User userData = this.userManager.getUser(user.getUsername());

        // 更新用户登录时间
        this.userService.updateLoginTime(userData.getUsername());
        // 保存登录记录
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(userData.getUsername());
        this.loginLogService.saveLoginLog(loginLog);

        String token = FebsUtil.encryptToken(JWTUtil.sign(userData.getUsername(), userData.getPassword()));
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(properties.getShiro().getJwtTimeOut());
        String expireTimeStr = DateUtil.formatFullTime(expireTime);
        JWTToken jwtToken = new JWTToken(token, expireTimeStr);

        String userId = this.saveTokenToRedis(userData, jwtToken, request);
        userData.setId(userId);

        Map<String, Object> userInfo = this.generateUserInfo(jwtToken, userData);
        return new FebsResponse().message("认证成功").data(userInfo);
    }

    private String saveTokenToRedis(User user, JWTToken token, HttpServletRequest request) throws Exception {
//        String ip = IPUtil.getIpAddr(request);
        String ip = "127.0.0.1";

        // 构建在线用户
        ActiveUser activeUser = new ActiveUser();
        activeUser.setUsername(user.getUsername());
        activeUser.setIp(ip);
        activeUser.setToken(token.getToken());
        activeUser.setLoginAddress(AddressUtil.getCityInfo(ip));

        // zset 存储登录用户，score 为过期时间戳
        this.redisService.zadd(FebsConstant.ACTIVE_USERS_ZSET_PREFIX, Double.valueOf(token.getExipreAt()), mapper.writeValueAsString(activeUser));
        // redis 中存储这个加密 token，key = 前缀 + 加密 token + .ip
        this.redisService.set(FebsConstant.TOKEN_CACHE_PREFIX + token.getToken() + StringPool.DOT + ip, token.getToken(), properties.getShiro().getJwtTimeOut() * 1000);

        return activeUser.getId();
    }

    /**
     * 生成前端需要的用户信息，包括：
     * 1. token
     * 2. Vue Router
     * 3. 用户角色
     * 4. 用户权限
     * 5. 前端系统个性化配置信息
     *
     * @param token token
     * @param user  用户信息
     * @return UserInfo
     */
    private Map<String, Object> generateUserInfo(JWTToken token, User user) {
        String username = user.getUsername();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("token", token.getToken());
        userInfo.put("exipreTime", token.getExipreAt());

        Set<String> roles = this.userManager.getUserRoles(username);
        userInfo.put("roles", roles);

        Set<String> permissions = this.userManager.getUserPermissions(username);
        userInfo.put("permissions", permissions);

        UserConfig userConfig = this.userManager.getUserConfig(String.valueOf(user.getUserId()));
        userInfo.put("config", userConfig);

        user.setPassword("it's a secret");
        userInfo.put("user", user);
        return userInfo;
    }
}
