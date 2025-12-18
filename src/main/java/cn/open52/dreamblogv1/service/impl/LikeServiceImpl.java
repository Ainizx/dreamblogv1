package cn.open52.dreamblogv1.service.impl;

import cn.open52.dreamblogv1.entity.Like;
import cn.open52.dreamblogv1.mapper.LikeMapper;
import cn.open52.dreamblogv1.service.LikeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements LikeService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Like like(Long targetId, int targetType, Long userId) {
        // 检查是否已经点赞
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, userId)
               .eq(Like::getTargetId, targetId)
               .eq(Like::getTargetType, targetType);

        Like existingLike = baseMapper.selectOne(wrapper);
        if (existingLike != null) {
            return existingLike;
        }

        // 创建新的点赞记录
        Like like = new Like();
        like.setUserId(userId);
        like.setTargetId(targetId);
        like.setTargetType(targetType);
        baseMapper.insert(like);
        return like;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlike(Long targetId, int targetType, Long userId) {
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, userId)
               .eq(Like::getTargetId, targetId)
               .eq(Like::getTargetType, targetType);

        return baseMapper.delete(wrapper) > 0;
    }

    @Override
    public Long getLikeCount(Long targetId, int targetType) {
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getTargetId, targetId)
               .eq(Like::getTargetType, targetType);
        return baseMapper.selectCount(wrapper);
    }

    @Override
    public boolean isLiked(Long targetId, int targetType, Long userId) {
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, userId)
               .eq(Like::getTargetId, targetId)
               .eq(Like::getTargetType, targetType);
        return baseMapper.selectCount(wrapper) > 0;
    }

    @Override
    public List<Like> getLikesByUserId(Long userId) {
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, userId)
               .orderByDesc(Like::getCreateTime);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<Like> getLikesByTarget(Long targetId, int targetType) {
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getTargetId, targetId)
               .eq(Like::getTargetType, targetType);
        return baseMapper.selectList(wrapper);
    }
}
