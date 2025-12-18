package cn.open52.dreamblogv1.service.impl;

import cn.open52.dreamblogv1.entity.Tag;
import cn.open52.dreamblogv1.mapper.TagMapper;
import cn.open52.dreamblogv1.service.TagService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Tag createTag(Tag tag) {
        // 检查标签名称是否已存在
        if (getTagByName(tag.getName()) != null) {
            throw new RuntimeException("标签名称已存在");
        }
        
        // 保存标签
        tagMapper.insert(tag);
        
        return tag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Tag updateTag(Tag tag) {
        // 验证标签是否存在
        Tag existingTag = tagMapper.selectById(tag.getId());
        if (existingTag == null || existingTag.getDeleted() == 1) {
            throw new RuntimeException("标签不存在");
        }
        
        // 检查新名称是否与其他标签重复
        Tag sameNameTag = getTagByName(tag.getName());
        if (sameNameTag != null && !sameNameTag.getId().equals(tag.getId())) {
            throw new RuntimeException("标签名称已存在");
        }
        
        // 更新标签
        tagMapper.updateById(tag);
        
        return tagMapper.selectById(tag.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTag(Long id) {
        // 验证标签是否存在
        Tag tag = tagMapper.selectById(id);
        if (tag == null || tag.getDeleted() == 1) {
            return false;
        }
        
        // 逻辑删除标签
        return tagMapper.deleteById(id) > 0;
    }

    @Override
    public Tag getTagById(Long id) {
        return tagMapper.selectOne(new QueryWrapper<Tag>()
                .lambda()
                .eq(Tag::getId, id)
                .eq(Tag::getDeleted, 0));
    }

    @Override
    public List<Tag> getAllTags() {
        return tagMapper.selectList(new QueryWrapper<Tag>()
                .lambda()
                .eq(Tag::getDeleted, 0)
                .orderByAsc(Tag::getName));
    }

    @Override
    public IPage<Tag> getTagPage(Page<Tag> page) {
        return tagMapper.selectPage(page, new QueryWrapper<Tag>()
                .lambda()
                .eq(Tag::getDeleted, 0)
                .orderByAsc(Tag::getName));
    }

    @Override
    public Tag getTagByName(String name) {
        return tagMapper.selectOne(new QueryWrapper<Tag>()
                .lambda()
                .eq(Tag::getName, name)
                .eq(Tag::getDeleted, 0));
    }
}
