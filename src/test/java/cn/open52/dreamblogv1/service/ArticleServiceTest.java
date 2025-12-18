package cn.open52.dreamblogv1.service;

import cn.open52.dreamblogv1.entity.Article;
import cn.open52.dreamblogv1.entity.UserRole;
import cn.open52.dreamblogv1.mapper.ArticleMapper;
import cn.open52.dreamblogv1.mapper.UserRoleMapper;
import cn.open52.dreamblogv1.service.impl.ArticleServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    @Mock
    private ArticleMapper articleMapper;
    
    @Mock
    private UserRoleMapper userRoleMapper;

    @InjectMocks
    private ArticleServiceImpl articleService;

    private Article testArticle;

    @BeforeEach
    public void setUp() {
        testArticle = new Article();
        testArticle.setId(1L);
        testArticle.setTitle("测试文章");
        testArticle.setContent("测试内容");
        testArticle.setSummary("测试摘要");
        testArticle.setCategoryId(1L);
        testArticle.setUserId(1L);
        testArticle.setViewCount(0);
        testArticle.setLikeCount(0);
        testArticle.setCommentCount(0);
        testArticle.setStatus(1);
        testArticle.setDeleted(0);
    }

    @Test
    public void testCreateArticle() {
        // 执行测试
        Article result = articleService.createArticle(testArticle);

        // 验证结果
        Assertions.assertNotNull(result);
        Assertions.assertEquals("测试文章", result.getTitle());
        Assertions.assertEquals(0, result.getViewCount().intValue());
        Assertions.assertEquals(0, result.getLikeCount().intValue());
        Assertions.assertEquals(0, result.getCommentCount().intValue());
        Assertions.assertEquals(1, result.getStatus().intValue());

        // 验证mapper方法被调用
        Mockito.verify(articleMapper, Mockito.times(1)).insert(Mockito.any(Article.class));
    }

    @Test
    public void testUpdateArticle() {
        // 创建模拟用户角色（管理员角色）
        UserRole adminRole = new UserRole();
        adminRole.setUserId(1L);
        adminRole.setRoleId(1L); // 1表示管理员角色
        List<UserRole> userRoles = Arrays.asList(adminRole);

        // 模拟mapper行为
        Mockito.when(articleMapper.selectById(Mockito.anyLong())).thenReturn(testArticle);
        Mockito.when(userRoleMapper.selectList(Mockito.any())).thenReturn(userRoles);
        Mockito.when(articleMapper.updateById(Mockito.any(Article.class))).thenReturn(1);
        Mockito.when(articleMapper.selectById(Mockito.anyLong())).thenReturn(testArticle);

        // 修改测试文章
        testArticle.setTitle("更新后的文章标题");

        // 执行测试
        Article result = articleService.updateArticle(testArticle, 1L);

        // 验证结果
        Assertions.assertNotNull(result);
        Assertions.assertEquals("更新后的文章标题", result.getTitle());

        // 验证mapper方法被调用
        Mockito.verify(articleMapper, Mockito.times(2)).selectById(Mockito.anyLong());
        Mockito.verify(userRoleMapper, Mockito.times(1)).selectList(Mockito.any());
        Mockito.verify(articleMapper, Mockito.times(1)).updateById(Mockito.any(Article.class));
    }

    @Test
    public void testDeleteArticle() {
        // 模拟mapper行为
        Mockito.when(articleMapper.selectById(Mockito.anyLong())).thenReturn(testArticle);
        Mockito.when(articleMapper.deleteById(Mockito.anyLong())).thenReturn(1);

        // 执行测试
        boolean result = articleService.deleteArticle(1L, 1L);

        // 验证结果
        Assertions.assertTrue(result);

        // 验证mapper方法被调用
        Mockito.verify(articleMapper, Mockito.times(1)).selectById(Mockito.anyLong());
        Mockito.verify(articleMapper, Mockito.times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    public void testGetArticleById() {
        // 模拟mapper行为
        Mockito.when(articleMapper.selectOne(Mockito.any())).thenReturn(testArticle);

        // 执行测试
        Article result = articleService.getArticleById(1L);

        // 验证结果
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId().longValue());
        Assertions.assertEquals("测试文章", result.getTitle());

        // 验证mapper方法被调用
        Mockito.verify(articleMapper, Mockito.times(1)).selectOne(Mockito.any());
    }

    @Test
    public void testGetArticlePage() {
        // 准备测试数据
        List<Article> articleList = Arrays.asList(testArticle);
        Page<Article> page = new Page<>(1, 10);
        page.setRecords(articleList);
        page.setTotal(1);

        // 模拟mapper行为
        Mockito.when(articleMapper.selectPage(Mockito.any(Page.class), Mockito.any())).thenReturn(page);

        // 执行测试
        IPage<Article> result = articleService.getArticlePage(page, new QueryWrapper<>());

        // 验证结果
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getTotal());
        Assertions.assertEquals(1, result.getRecords().size());
        Assertions.assertEquals("测试文章", result.getRecords().get(0).getTitle());

        // 验证mapper方法被调用
        Mockito.verify(articleMapper, Mockito.times(1)).selectPage(Mockito.any(Page.class), Mockito.any());
    }

    @Test
    public void testIncrementViewCount() {
        // 模拟查询结果
        Article article = new Article();
        article.setId(1L);
        article.setViewCount(10);
        Mockito.when(articleMapper.selectOne(Mockito.any())).thenReturn(article);
        
        // 模拟更新操作
        Mockito.when(articleMapper.updateById(Mockito.any(Article.class))).thenReturn(1);

        // 执行测试
        boolean result = articleService.incrementViewCount(1L);

        // 验证结果
        Assertions.assertTrue(result);

        // 验证mapper方法被调用
        Mockito.verify(articleMapper, Mockito.times(1)).selectOne(Mockito.any());
        Mockito.verify(articleMapper, Mockito.times(1)).updateById(Mockito.any(Article.class));
    }
}
