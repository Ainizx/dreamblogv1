package cn.open52.dreamblogv1.service;

import cn.open52.dreamblogv1.entity.User;
import cn.open52.dreamblogv1.entity.UserRole;
import cn.open52.dreamblogv1.mapper.UserMapper;
import cn.open52.dreamblogv1.mapper.UserRoleMapper;
import cn.open52.dreamblogv1.service.impl.UserServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRoleMapper userRoleMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setEmail("test@example.com");
        testUser.setStatus(1);
    }

    @Test
    public void testRegister() {
        // 模拟mapper行为
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encryptedPassword");
        
        // 模拟insert操作，并确保user对象有id值
        Mockito.when(userMapper.insert(Mockito.any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L); // 设置id值
            return 1;
        });
        
        // 模拟userRoleMapper的insert操作
        Mockito.when(userRoleMapper.insert(Mockito.any(UserRole.class))).thenReturn(1);

        // 执行测试
        User result = userService.register(testUser);

        // 验证结果
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("testuser", result.getUsername());
        Assertions.assertEquals("test@example.com", result.getEmail());
        Assertions.assertEquals("encryptedPassword", result.getPassword());
        Assertions.assertEquals(1, result.getStatus().intValue());

        // 验证mapper方法被调用
        Mockito.verify(userMapper, Mockito.times(1)).insert(Mockito.any(User.class));
        Mockito.verify(userRoleMapper, Mockito.times(1)).insert(Mockito.any(UserRole.class));
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(Mockito.anyString());
    }

    @Test
    public void testFindByUsername() {
        // 模拟mapper行为
        Mockito.when(userMapper.selectOne(Mockito.any(QueryWrapper.class))).thenReturn(testUser);

        // 执行测试
        User result = userService.findByUsername("testuser");

        // 验证结果
        Assertions.assertNotNull(result);
        Assertions.assertEquals("testuser", result.getUsername());

        // 验证mapper方法被调用
        Mockito.verify(userMapper, Mockito.times(1)).selectOne(Mockito.any(QueryWrapper.class));
    }

    @Test
    public void testFindByEmail() {
        // 模拟mapper行为
        Mockito.when(userMapper.selectOne(Mockito.any(QueryWrapper.class))).thenReturn(testUser);

        // 执行测试
        User result = userService.findByEmail("test@example.com");

        // 验证结果
        Assertions.assertNotNull(result);
        Assertions.assertEquals("test@example.com", result.getEmail());

        // 验证mapper方法被调用
        Mockito.verify(userMapper, Mockito.times(1)).selectOne(Mockito.any(QueryWrapper.class));
    }
}
