package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.LoginLog;
import cc.mrbird.febs.system.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

public interface LoginLogService extends IService<LoginLog> {

    void saveLoginLog (LoginLog loginLog);

    FebsResponse faceLogin(User user, HttpServletRequest request) throws Exception;
}
