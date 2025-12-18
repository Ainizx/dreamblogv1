package cn.open52.dreamblogv1.controller;

import cn.open52.dreamblogv1.entity.Like;
import cn.open52.dreamblogv1.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/article/{articleId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Like> likeArticle(@PathVariable Long articleId, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        Like like = likeService.like(articleId, LikeService.TargetType.ARTICLE, userId);
        return new ResponseEntity<>(like, HttpStatus.OK);
    }

    @PostMapping("/comment/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Like> likeComment(@PathVariable Long commentId, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        Like like = likeService.like(commentId, LikeService.TargetType.COMMENT, userId);
        return new ResponseEntity<>(like, HttpStatus.OK);
    }

    @DeleteMapping("/article/{articleId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> unlikeArticle(@PathVariable Long articleId, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        boolean unliked = likeService.unlike(articleId, LikeService.TargetType.ARTICLE, userId);
        return unliked ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/comment/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> unlikeComment(@PathVariable Long commentId, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        boolean unliked = likeService.unlike(commentId, LikeService.TargetType.COMMENT, userId);
        return unliked ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/count/article/{articleId}")
    public ResponseEntity<Long> getArticleLikeCount(@PathVariable Long articleId) {
        Long count = likeService.getLikeCount(articleId, LikeService.TargetType.ARTICLE);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/count/comment/{commentId}")
    public ResponseEntity<Long> getCommentLikeCount(@PathVariable Long commentId) {
        Long count = likeService.getLikeCount(commentId, LikeService.TargetType.COMMENT);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/check/article/{articleId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> checkArticleLiked(@PathVariable Long articleId, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        boolean isLiked = likeService.isLiked(articleId, LikeService.TargetType.ARTICLE, userId);
        return new ResponseEntity<>(isLiked, HttpStatus.OK);
    }

    @GetMapping("/check/comment/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> checkCommentLiked(@PathVariable Long commentId, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        boolean isLiked = likeService.isLiked(commentId, LikeService.TargetType.COMMENT, userId);
        return new ResponseEntity<>(isLiked, HttpStatus.OK);
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Like>> getMyLikes(Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        List<Like> likes = likeService.getLikesByUserId(userId);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }
}
