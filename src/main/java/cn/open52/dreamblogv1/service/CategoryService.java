package cn.open52.dreamblogv1.service;

import cn.open52.dreamblogv1.entity.Category;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CategoryService extends IService<Category> {

    /**
     * 创建分类
     * @param category 分类实体
     * @return 创建的分类
     */
    Category createCategory(Category category);

    /**
     * 更新分类
     * @param category 分类实体
     * @return 更新后的分类
     */
    Category updateCategory(Category category);

    /**
     * 删除分类（逻辑删除）
     * @param id 分类ID
     * @return 是否删除成功
     */
    boolean deleteCategory(Long id);

    /**
     * 根据ID获取分类
     * @param id 分类ID
     * @return 分类详情
     */
    Category getCategoryById(Long id);

    /**
     * 获取所有分类
     * @return 分类列表
     */
    List<Category> getAllCategories();

    /**
     * 分页获取分类列表
     * @param page 分页参数
     * @return 分页分类列表
     */
    IPage<Category> getCategoryPage(Page<Category> page);

    /**
     * 根据名称获取分类
     * @param name 分类名称
     * @return 分类详情
     */
    Category getCategoryByName(String name);
}
