package cn.open52.dreamblogv1.service;

import cn.open52.dreamblogv1.entity.Like;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface LikeService extends IService<Like> {

    interface TargetType {
        int ARTICLE = 1;
        int COMMENT = 2;
    }

    Like like(Long targetId, int targetType, Long userId);

    boolean unlike(Long targetId, int targetType, Long userId);

    Long getLikeCount(Long targetId, int targetType);

    boolean isLiked(Long targetId, int targetType, Long userId);

    List<Like> getLikesByUserId(Long userId);

    List<Like> getLikesByTarget(Long targetId, int targetType);
}
