package cn.open52.dreamblogv1.service.impl;

import cn.open52.dreamblogv1.entity.User;
import cn.open52.dreamblogv1.entity.UserRole;
import cn.open52.dreamblogv1.mapper.UserMapper;
import cn.open52.dreamblogv1.mapper.UserRoleMapper;
import cn.open52.dreamblogv1.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User register(User user) {
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 设置默认状态
        user.setStatus(1);
        // 保存用户
        userMapper.insert(user);
        
        // 为新用户分配普通用户角色
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(2L); // 普通用户角色ID
        userRoleMapper.insert(userRole);
        
        return user;
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>()
                .eq("username", username)
                .eq("deleted", 0));
    }

    @Override
    public User findByEmail(String email) {
        return userMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>()
                .eq("email", email)
                .eq("deleted", 0));
    }

}