package cn.open52.dreamblogv1;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    @Test
    public void testPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 测试密码验证
        String rawPassword = "123456";
        String encodedPassword = "$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2"; // admin用户的密码
        
        System.out.println("Raw password: " + rawPassword);
        System.out.println("Encoded password: " + encodedPassword);
        System.out.println("Password matches: " + encoder.matches(rawPassword, encodedPassword));
        
        // 测试新密码加密
        String newEncodedPassword = encoder.encode("123456");
        System.out.println("New encoded password: " + newEncodedPassword);
        System.out.println("New password matches: " + encoder.matches("123456", newEncodedPassword));
    }
}