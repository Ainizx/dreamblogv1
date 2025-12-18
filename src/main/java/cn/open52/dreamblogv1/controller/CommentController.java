package cn.open52.dreamblogv1.controller;

import cn.open52.dreamblogv1.entity.Comment;
import cn.open52.dreamblogv1.service.CommentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        comment.setUserId(userId);
        Comment createdComment = commentService.createComment(comment);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        Comment comment = commentService.getCommentById(commentId);
        if (comment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // 检查权限：只能删除自己的评论，或者管理员可以删除所有评论
        boolean isAdmin = principal.toString().contains("ROLE_ADMIN");
        if (!isAdmin && !comment.getUserId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        boolean deleted = commentService.deleteComment(commentId, userId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<Comment>> getCommentsByArticleId(@PathVariable Long articleId) {
        List<Comment> comments = commentService.getCommentsByArticleId(articleId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Comment>> getCommentsByUserId(@PathVariable Long userId, Principal principal) {
        Long currentUserId = Long.parseLong(principal.getName());
        boolean isAdmin = principal.toString().contains("ROLE_ADMIN");

        // 只能查看自己的评论，或者管理员可以查看所有评论
        if (!isAdmin && !currentUserId.equals(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Comment> comments = commentService.getCommentsByUserId(userId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Comment>> getMyComments(Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        List<Comment> comments = commentService.getCommentsByUserId(userId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IPage<Comment>> listComments(@RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        Page<Comment> page = new Page<>(current, size);
        IPage<Comment> commentPage = commentService.listComments(page);
        return new ResponseEntity<>(commentPage, HttpStatus.OK);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IPage<Comment>> searchComments(@RequestParam String keyword, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        Page<Comment> page = new Page<>(current, size);
        IPage<Comment> commentPage = commentService.searchComments(page, keyword);
        return new ResponseEntity<>(commentPage, HttpStatus.OK);
    }
}
