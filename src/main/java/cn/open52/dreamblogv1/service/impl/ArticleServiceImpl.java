package cn.open52.dreamblogv1.service.impl;

import cn.open52.dreamblogv1.entity.Article;
import cn.open52.dreamblogv1.entity.ArticleTag;
import cn.open52.dreamblogv1.entity.UserRole;
import cn.open52.dreamblogv1.mapper.ArticleMapper;
import cn.open52.dreamblogv1.mapper.ArticleTagMapper;
import cn.open52.dreamblogv1.mapper.UserRoleMapper;
import cn.open52.dreamblogv1.service.ArticleService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;
    
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Article createArticle(Article article) {
        // 设置默认值
        article.setViewCount(0);
        article.setLikeCount(0);
        article.setCommentCount(0);
        article.setStatus(1); // 默认发布状态
        
        // 保存文章
        articleMapper.insert(article);
        
        // 处理文章标签关联
        if (!CollectionUtils.isEmpty(article.getTags())) {
            article.getTags().forEach(tagId -> {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tagId);
                articleTagMapper.insert(articleTag);
            });
        }
        
        return article;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Article updateArticle(Article article, Long userId) {
        System.out.println("[DEBUG] updateArticle called with articleId: " + article.getId() + ", userId: " + userId);
        
        // 验证权限
        Article existingArticle = articleMapper.selectById(article.getId());
        System.out.println("[DEBUG] Existing article found: " + existingArticle);
        
        if (existingArticle == null) {
            System.out.println("[DEBUG] Article not found");
            throw new RuntimeException("文章不存在");
        }
        
        if (existingArticle.getDeleted() == 1) {
            System.out.println("[DEBUG] Article is deleted");
            throw new RuntimeException("文章已删除");
        }
        
        // 直接在服务层再次验证权限，确保安全
        // 检查当前用户是否是文章作者或者管理员
        // 这里通过查询用户角色来判断是否是管理员
        List<UserRole> userRoles = userRoleMapper.selectList(
                new QueryWrapper<UserRole>()
                        .lambda()
                        .eq(UserRole::getUserId, userId));
        boolean isAdmin = userRoles.stream().anyMatch(role -> role.getRoleId() == 1);
        
        if (!existingArticle.getUserId().equals(userId) && !isAdmin) {
            System.out.println("[DEBUG] Permission denied: userId " + userId + " is not the author of article " + article.getId() + " and is not admin");
            throw new RuntimeException("没有权限修改该文章");
        }
        
        // 更新文章字段（只更新传入的非null字段）
        if (article.getTitle() != null) {
            existingArticle.setTitle(article.getTitle());
            System.out.println("[DEBUG] Updated title: " + article.getTitle());
        }
        if (article.getContent() != null) {
            existingArticle.setContent(article.getContent());
            System.out.println("[DEBUG] Updated content");
        }
        if (article.getSummary() != null) {
            existingArticle.setSummary(article.getSummary());
            System.out.println("[DEBUG] Updated summary");
        }
        if (article.getCategoryId() != null) {
            existingArticle.setCategoryId(article.getCategoryId());
            System.out.println("[DEBUG] Updated categoryId: " + article.getCategoryId());
        }
        if (article.getStatus() != null) {
            existingArticle.setStatus(article.getStatus());
            System.out.println("[DEBUG] Updated status: " + article.getStatus());
        }
        
        int updateResult = articleMapper.updateById(existingArticle);
        System.out.println("[DEBUG] Update result: " + updateResult);
        
        // 处理文章标签关联
        if (article.getTags() != null) {
            System.out.println("[DEBUG] Updating tags: " + article.getTags());
            
            // 删除原有标签关联
            articleTagMapper.delete(
                    new QueryWrapper<ArticleTag>()
                            .lambda()
                            .eq(ArticleTag::getArticleId, article.getId()));
            
            // 添加新的标签关联
            if (!CollectionUtils.isEmpty(article.getTags())) {
                article.getTags().forEach(tagId -> {
                    ArticleTag articleTag = new ArticleTag();
                    articleTag.setArticleId(article.getId());
                    articleTag.setTagId(tagId);
                    articleTagMapper.insert(articleTag);
                });
            }
        }
        
        Article updatedArticle = articleMapper.selectById(article.getId());
        System.out.println("[DEBUG] Updated article: " + updatedArticle);
        
        return updatedArticle;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteArticle(Long id, Long userId) {
        // 验证权限
        Article article = articleMapper.selectById(id);
        if (article == null || article.getDeleted() == 1) {
            return false;
        }
        
        // 只有管理员或文章作者可以删除
        // 这里先不处理权限，后续在控制器中通过@PreAuthorize处理
        
        // 逻辑删除文章
        return articleMapper.deleteById(id) > 0;
    }

    @Override
    public Article getArticleById(Long id) {
        return articleMapper.selectOne(new QueryWrapper<Article>()
                .lambda()
                .eq(Article::getId, id)
                .eq(Article::getDeleted, 0));
    }

    @Override
    public IPage<Article> getArticlePage(Page<Article> page, Wrapper<Article> wrapper) {
        return articleMapper.selectPage(page, wrapper);
    }

    @Override
    public IPage<Article> searchArticles(Page<Article> page, String keyword, Long categoryId, Long tagId) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Article::getDeleted, 0)
                .eq(Article::getStatus, 1)
                .orderByDesc(Article::getCreateTime);
        
        // 关键词搜索
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.lambda()
                    .like(Article::getTitle, keyword)
                    .or()
                    .like(Article::getContent, keyword)
                    .or()
                    .like(Article::getSummary, keyword);
        }
        
        // 分类筛选
        if (categoryId != null) {
            queryWrapper.lambda().eq(Article::getCategoryId, categoryId);
        }
        
        // 标签筛选
        if (tagId != null) {
            // 先查询有该标签的文章ID
            List<ArticleTag> articleTags = articleTagMapper.selectList(new QueryWrapper<ArticleTag>()
                    .lambda().eq(ArticleTag::getTagId, tagId));
            
            if (!CollectionUtils.isEmpty(articleTags)) {
                List<Long> articleIds = articleTags.stream()
                        .map(ArticleTag::getArticleId)
                        .collect(Collectors.toList());
                queryWrapper.lambda().in(Article::getId, articleIds);
            } else {
                // 如果没有该标签的文章，返回空结果
                return new Page<>();
            }
        }
        
        return articleMapper.selectPage(page, queryWrapper);
    }

    @Override
    public boolean incrementViewCount(Long id) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "view_count");
        queryWrapper.eq("id", id);
        Article article = articleMapper.selectOne(queryWrapper);
        if (article != null) {
            article.setViewCount(article.getViewCount() + 1);
            return articleMapper.updateById(article) > 0;
        }
        return false;
    }

    @Override
    public boolean incrementLikeCount(Long id) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "like_count");
        queryWrapper.eq("id", id);
        Article article = articleMapper.selectOne(queryWrapper);
        if (article != null) {
            article.setLikeCount(article.getLikeCount() + 1);
            return articleMapper.updateById(article) > 0;
        }
        return false;
    }

    @Override
    public boolean decrementLikeCount(Long id) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "like_count");
        queryWrapper.eq("id", id);
        queryWrapper.ge("like_count", 1);
        Article article = articleMapper.selectOne(queryWrapper);
        if (article != null) {
            article.setLikeCount(article.getLikeCount() - 1);
            return articleMapper.updateById(article) > 0;
        }
        return false;
    }

    @Override
    public boolean updateCommentCount(Long id, int count) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "comment_count");
        queryWrapper.eq("id", id);
        
        if (count < 0) {
            queryWrapper.ge("comment_count", Math.abs(count));
        }
        
        Article article = articleMapper.selectOne(queryWrapper);
        if (article != null) {
            article.setCommentCount(article.getCommentCount() + count);
            return articleMapper.updateById(article) > 0;
        }
        
        return false;
    }
}
