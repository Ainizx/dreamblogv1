package cn.open52.dreamblogv1.controller;

import cn.open52.dreamblogv1.entity.Category;
import cn.open52.dreamblogv1.service.CategoryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 创建分类（管理员）
     * @param category 分类实体
     * @return 创建结果
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> createCategory(@RequestBody Category category) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Category createdCategory = categoryService.createCategory(category);
            result.put("code", 200);
            result.put("message", "分类创建成功");
            result.put("data", createdCategory);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "分类创建失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 更新分类（管理员）
     * @param id 分类ID
     * @param category 分类实体
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            category.setId(id);
            Category updatedCategory = categoryService.updateCategory(category);
            result.put("code", 200);
            result.put("message", "分类更新成功");
            result.put("data", updatedCategory);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "分类更新失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 删除分类（管理员）
     * @param id 分类ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> deleteCategory(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = categoryService.deleteCategory(id);
            if (success) {
                result.put("code", 200);
                result.put("message", "分类删除成功");
            } else {
                result.put("code", 404);
                result.put("message", "分类不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "分类删除失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 获取分类详情
     * @param id 分类ID
     * @return 分类详情
     */
    @GetMapping("/{id}")
    public Map<String, Object> getCategoryById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Category category = categoryService.getCategoryById(id);
            if (category != null) {
                result.put("code", 200);
                result.put("message", "获取成功");
                result.put("data", category);
            } else {
                result.put("code", 404);
                result.put("message", "分类不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 获取所有分类
     * @return 分类列表
     */
    @GetMapping
    public Map<String, Object> getAllCategories() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", categoryService.getAllCategories());
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 分页获取分类列表（管理员）
     * @param page 页码
     * @param size 每页条数
     * @return 分类列表
     */
    @GetMapping("/page")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> getCategoryPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            Page<Category> pageParam = new Page<>(page, size);
            IPage<Category> categoryPage = categoryService.getCategoryPage(pageParam);
            
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", categoryPage.getRecords());
            result.put("total", categoryPage.getTotal());
            result.put("current", categoryPage.getCurrent());
            result.put("size", categoryPage.getSize());
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        
        return result;
    }
}
