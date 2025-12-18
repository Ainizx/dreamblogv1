package cn.open52.dreamblogv1.controller;

import cn.open52.dreamblogv1.entity.Tag;
import cn.open52.dreamblogv1.service.TagService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 创建标签（管理员）
     * @param tag 标签实体
     * @return 创建结果
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> createTag(@RequestBody Tag tag) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Tag createdTag = tagService.createTag(tag);
            result.put("code", 200);
            result.put("message", "标签创建成功");
            result.put("data", createdTag);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "标签创建失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 更新标签（管理员）
     * @param id 标签ID
     * @param tag 标签实体
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> updateTag(@PathVariable Long id, @RequestBody Tag tag) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            tag.setId(id);
            Tag updatedTag = tagService.updateTag(tag);
            result.put("code", 200);
            result.put("message", "标签更新成功");
            result.put("data", updatedTag);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "标签更新失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 删除标签（管理员）
     * @param id 标签ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> deleteTag(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = tagService.deleteTag(id);
            if (success) {
                result.put("code", 200);
                result.put("message", "标签删除成功");
            } else {
                result.put("code", 404);
                result.put("message", "标签不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "标签删除失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 获取标签详情
     * @param id 标签ID
     * @return 标签详情
     */
    @GetMapping("/{id}")
    public Map<String, Object> getTagById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Tag tag = tagService.getTagById(id);
            if (tag != null) {
                result.put("code", 200);
                result.put("message", "获取成功");
                result.put("data", tag);
            } else {
                result.put("code", 404);
                result.put("message", "标签不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 获取所有标签
     * @return 标签列表
     */
    @GetMapping
    public Map<String, Object> getAllTags() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", tagService.getAllTags());
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 分页获取标签列表（管理员）
     * @param page 页码
     * @param size 每页条数
     * @return 标签列表
     */
    @GetMapping("/page")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> getTagPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            Page<Tag> pageParam = new Page<>(page, size);
            IPage<Tag> tagPage = tagService.getTagPage(pageParam);
            
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", tagPage.getRecords());
            result.put("total", tagPage.getTotal());
            result.put("current", tagPage.getCurrent());
            result.put("size", tagPage.getSize());
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        
        return result;
    }
}
