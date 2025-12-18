package cn.open52.dreamblogv1.service;

import cn.open52.dreamblogv1.entity.Comment;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CommentService extends IService<Comment> {

    Comment createComment(Comment comment);

    boolean deleteComment(Long commentId, Long userId);

    List<Comment> getCommentsByArticleId(Long articleId);

    List<Comment> getCommentsByUserId(Long userId);

    Comment getCommentById(Long commentId);

    IPage<Comment> listComments(IPage<Comment> page);

    IPage<Comment> searchComments(IPage<Comment> page, String keyword);
}
