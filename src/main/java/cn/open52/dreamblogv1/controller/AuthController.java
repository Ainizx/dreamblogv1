package cn.open52.dreamblogv1.controller;

import cn.open52.dreamblogv1.dto.LoginDTO;
import cn.open52.dreamblogv1.dto.RegisterDTO;
import cn.open52.dreamblogv1.entity.User;
import cn.open52.dreamblogv1.service.UserService;
import cn.open52.dreamblogv1.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Map<String, Object> register(@Valid @RequestBody RegisterDTO registerDTO) {
        Map<String, Object> result = new HashMap<>();

        // 检查用户名是否已存在
        if (userService.findByUsername(registerDTO.getUsername()) != null) {
            result.put("code", 400);
            result.put("message", "用户名已存在");
            return result;
        }

        // 检查邮箱是否已存在
        if (userService.findByEmail(registerDTO.getEmail()) != null) {
            result.put("code", 400);
            result.put("message", "邮箱已存在");
            return result;
        }

        // 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword());
        user.setNickname(registerDTO.getNickname());
        user.setEmail(registerDTO.getEmail());

        // 注册用户
        userService.register(user);

        result.put("code", 200);
        result.put("message", "注册成功");
        result.put("data", user);
        return result;
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Map<String, Object> login(@Valid @RequestBody LoginDTO loginDTO) {
        Map<String, Object> result = new HashMap<>();
        
        System.out.println("[DEBUG] 登录请求：username=" + loginDTO.getUsername() + ", password=" + loginDTO.getPassword());

        try {
            // 认证
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
            );
            
            System.out.println("[DEBUG] 认证成功：authentication=" + authentication);

            // 设置认证信息
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 生成JWT令牌
            User user = userService.findByUsername(loginDTO.getUsername());
            System.out.println("[DEBUG] 查询到用户：user=" + user);
            
            String token = jwtUtils.generateToken(user.getId());
            System.out.println("[DEBUG] 生成令牌：token=" + token);

            result.put("code", 200);
            result.put("message", "登录成功");
            result.put("token", token);
            result.put("user", user);
        } catch (Exception e) {
            System.out.println("[DEBUG] 登录失败：" + e.getMessage());
            e.printStackTrace();
            result.put("code", 401);
            result.put("message", "登录失败：" + e.getMessage());
        }
        
        return result;
    }

}