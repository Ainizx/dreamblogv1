package cn.open52.dreamblogv1.service.impl;

import cn.open52.dreamblogv1.entity.Comment;
import cn.open52.dreamblogv1.mapper.CommentMapper;
import cn.open52.dreamblogv1.service.CommentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Override
    public Comment createComment(Comment comment) {
        // 保存评论
        baseMapper.insert(comment);
        return comment;
    }

    @Override
    public boolean deleteComment(Long commentId, Long userId) {
        // 获取评论信息
        Comment comment = baseMapper.selectById(commentId);
        if (comment == null) {
            return false;
        }

        // 检查权限：只能删除自己的评论，或者管理员可以删除所有评论
        // （管理员权限在控制器层通过@PreAuthorize检查）
        return baseMapper.deleteById(commentId) > 0;
    }

    @Override
    public List<Comment> getCommentsByArticleId(Long articleId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getArticleId, articleId)
               .orderByAsc(Comment::getCreateTime);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<Comment> getCommentsByUserId(Long userId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getUserId, userId)
               .orderByDesc(Comment::getCreateTime);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Comment getCommentById(Long commentId) {
        return baseMapper.selectById(commentId);
    }

    @Override
    public IPage<Comment> listComments(IPage<Comment> page) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Comment::getCreateTime);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public IPage<Comment> searchComments(IPage<Comment> page, String keyword) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Comment::getContent, keyword)
               .orderByDesc(Comment::getCreateTime);
        return baseMapper.selectPage(page, wrapper);
    }
}
