package cn.open52.dreamblogv1.service;

import cn.open52.dreamblogv1.entity.Tag;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface TagService extends IService<Tag> {

    /**
     * 创建标签
     * @param tag 标签实体
     * @return 创建的标签
     */
    Tag createTag(Tag tag);

    /**
     * 更新标签
     * @param tag 标签实体
     * @return 更新后的标签
     */
    Tag updateTag(Tag tag);

    /**
     * 删除标签（逻辑删除）
     * @param id 标签ID
     * @return 是否删除成功
     */
    boolean deleteTag(Long id);

    /**
     * 根据ID获取标签
     * @param id 标签ID
     * @return 标签详情
     */
    Tag getTagById(Long id);

    /**
     * 获取所有标签
     * @return 标签列表
     */
    List<Tag> getAllTags();

    /**
     * 分页获取标签列表
     * @param page 分页参数
     * @return 分页标签列表
     */
    IPage<Tag> getTagPage(Page<Tag> page);

    /**
     * 根据名称获取标签
     * @param name 标签名称
     * @return 标签详情
     */
    Tag getTagByName(String name);
}
