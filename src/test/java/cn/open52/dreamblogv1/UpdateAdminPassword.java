package cn.open52.dreamblogv1;

import cn.open52.dreamblogv1.entity.User;
import cn.open52.dreamblogv1.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class UpdateAdminPassword {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void updateAdminPassword() {
        // 创建BCrypt密码编码器
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "123456";
        String encodedPassword = encoder.encode(rawPassword);
        
        System.out.println("Raw password: " + rawPassword);
        System.out.println("Encoded password: " + encodedPassword);
        
        // 查询admin用户
        User admin = userMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .eq(User::getUsername, "admin")
                .eq(User::getDeleted, 0));
        
        if (admin != null) {
            // 更新密码
            admin.setPassword(encodedPassword);
            int result = userMapper.updateById(admin);
            System.out.println("Update result: " + (result > 0 ? "success" : "failed"));
        } else {
            System.out.println("Admin user not found");
        }
    }
}