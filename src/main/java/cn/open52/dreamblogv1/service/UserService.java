package cn.open52.dreamblogv1.service;

import cn.open52.dreamblogv1.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {

    /**
     * 用户注册
     */
    User register(User user);

    /**
     * 根据用户名查询用户
     */
    User findByUsername(String username);

    /**
     * 根据邮箱查询用户
     */
    User findByEmail(String email);

}
