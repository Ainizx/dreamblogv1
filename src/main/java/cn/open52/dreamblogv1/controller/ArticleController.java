package cn.open52.dreamblogv1.controller;

import cn.open52.dreamblogv1.entity.Article;
import cn.open52.dreamblogv1.service.ArticleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 创建文章
     * @param article 文章实体
     * @param principal 当前用户
     * @return 创建结果
     */
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Map<String, Object> createArticle(@RequestBody Article article, Principal principal) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 设置文章作者
            article.setUserId(Long.parseLong(principal.getName()));
            Article createdArticle = articleService.createArticle(article);
            result.put("code", 200);
            result.put("message", "文章创建成功");
            result.put("data", createdArticle);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "文章创建失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 更新文章
     * @param id 文章ID
     * @param article 文章实体
     * @param principal 当前用户
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (@articleService.getArticleById(#id) != null and @articleService.getArticleById(#id).getUserId() == T(java.lang.Long).valueOf(authentication.name))")
    public Map<String, Object> updateArticle(@PathVariable Long id, @RequestBody Article article, Principal principal) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            article.setId(id);
            Article updatedArticle = articleService.updateArticle(article, Long.parseLong(principal.getName()));
            result.put("code", 200);
            result.put("message", "文章更新成功");
            result.put("data", updatedArticle);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "文章更新失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 删除文章
     * @param id 文章ID
     * @param principal 当前用户
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (@articleService.getArticleById(#id) != null and @articleService.getArticleById(#id).getUserId() == T(java.lang.Long).valueOf(authentication.name))")
    public Map<String, Object> deleteArticle(@PathVariable Long id, Principal principal) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = articleService.deleteArticle(id, Long.parseLong(principal.getName()));
            if (success) {
                result.put("code", 200);
                result.put("message", "文章删除成功");
            } else {
                result.put("code", 404);
                result.put("message", "文章不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "文章删除失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 获取文章详情
     * @param id 文章ID
     * @return 文章详情
     */
    @GetMapping("/{id}")
    public Map<String, Object> getArticleById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Article article = articleService.getArticleById(id);
            if (article != null) {
                // 增加浏览量
                articleService.incrementViewCount(id);
                article.setViewCount(article.getViewCount() + 1);
                
                result.put("code", 200);
                result.put("message", "获取成功");
                result.put("data", article);
            } else {
                result.put("code", 404);
                result.put("message", "文章不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 获取文章列表（分页）
     * @param page 页码
     * @param size 每页条数
     * @param categoryId 分类ID
     * @return 文章列表
     */
    @GetMapping
    public Map<String, Object> getArticleList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long categoryId) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            Page<Article> pageParam = new Page<>(page, size);
            QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(Article::getDeleted, 0)
                    .eq(Article::getStatus, 1)
                    .orderByDesc(Article::getCreateTime);
            
            if (categoryId != null) {
                queryWrapper.lambda().eq(Article::getCategoryId, categoryId);
            }
            
            IPage<Article> articlePage = articleService.getArticlePage(pageParam, queryWrapper);
            
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", articlePage.getRecords());
            result.put("total", articlePage.getTotal());
            result.put("current", articlePage.getCurrent());
            result.put("size", articlePage.getSize());
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 搜索文章
     * @param page 页码
     * @param size 每页条数
     * @param keyword 关键词
     * @param categoryId 分类ID
     * @param tagId 标签ID
     * @return 搜索结果
     */
    @GetMapping("/search")
    public Map<String, Object> searchArticles(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long tagId) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            Page<Article> pageParam = new Page<>(page, size);
            IPage<Article> articlePage = articleService.searchArticles(pageParam, keyword, categoryId, tagId);
            
            result.put("code", 200);
            result.put("message", "搜索成功");
            result.put("data", articlePage.getRecords());
            result.put("total", articlePage.getTotal());
            result.put("current", articlePage.getCurrent());
            result.put("size", articlePage.getSize());
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "搜索失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 获取当前用户的文章列表
     * @param page 页码
     * @param size 每页条数
     * @param principal 当前用户
     * @return 用户文章列表
     */
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Map<String, Object> getMyArticles(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            Principal principal) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            Page<Article> pageParam = new Page<>(page, size);
            QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(Article::getDeleted, 0)
                    .eq(Article::getUserId, Long.parseLong(principal.getName()))
                    .orderByDesc(Article::getCreateTime);
            
            IPage<Article> articlePage = articleService.getArticlePage(pageParam, queryWrapper);
            
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", articlePage.getRecords());
            result.put("total", articlePage.getTotal());
            result.put("current", articlePage.getCurrent());
            result.put("size", articlePage.getSize());
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        
        return result;
    }
}
