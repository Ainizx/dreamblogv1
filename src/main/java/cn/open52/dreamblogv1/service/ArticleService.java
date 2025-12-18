package cn.open52.dreamblogv1.service;

import cn.open52.dreamblogv1.entity.Article;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ArticleService extends IService<Article> {

    /**
     * 创建文章
     * @param article 文章实体
     * @return 创建的文章
     */
    Article createArticle(Article article);

    /**
     * 更新文章
     * @param article 文章实体
     * @param userId 当前用户ID
     * @return 更新后的文章
     */
    Article updateArticle(Article article, Long userId);

    /**
     * 删除文章（逻辑删除）
     * @param id 文章ID
     * @param userId 当前用户ID
     * @return 是否删除成功
     */
    boolean deleteArticle(Long id, Long userId);

    /**
     * 根据ID获取文章详情
     * @param id 文章ID
     * @return 文章详情
     */
    Article getArticleById(Long id);

    /**
     * 分页获取文章列表
     * @param page 分页参数
     * @param wrapper 查询条件
     * @return 分页文章列表
     */
    IPage<Article> getArticlePage(Page<Article> page, Wrapper<Article> wrapper);

    /**
     * 搜索文章
     * @param page 分页参数
     * @param keyword 关键词
     * @param categoryId 分类ID
     * @param tagId 标签ID
     * @return 分页搜索结果
     */
    IPage<Article> searchArticles(Page<Article> page, String keyword, Long categoryId, Long tagId);

    /**
     * 增加文章浏览量
     * @param id 文章ID
     * @return 是否增加成功
     */
    boolean incrementViewCount(Long id);

    /**
     * 增加文章点赞数
     * @param id 文章ID
     * @return 是否增加成功
     */
    boolean incrementLikeCount(Long id);

    /**
     * 减少文章点赞数
     * @param id 文章ID
     * @return 是否减少成功
     */
    boolean decrementLikeCount(Long id);

    /**
     * 更新文章评论数
     * @param id 文章ID
     * @param count 评论数变化量（+1或-1）
     * @return 是否更新成功
     */
    boolean updateCommentCount(Long id, int count);
}
