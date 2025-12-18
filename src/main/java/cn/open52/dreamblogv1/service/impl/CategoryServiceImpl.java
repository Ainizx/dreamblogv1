package cn.open52.dreamblogv1.service.impl;

import cn.open52.dreamblogv1.entity.Category;
import cn.open52.dreamblogv1.mapper.CategoryMapper;
import cn.open52.dreamblogv1.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Category createCategory(Category category) {
        // 检查分类名称是否已存在
        if (getCategoryByName(category.getName()) != null) {
            throw new RuntimeException("分类名称已存在");
        }
        
        // 保存分类
        categoryMapper.insert(category);
        
        return category;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Category updateCategory(Category category) {
        // 验证分类是否存在
        Category existingCategory = categoryMapper.selectById(category.getId());
        if (existingCategory == null || existingCategory.getDeleted() == 1) {
            throw new RuntimeException("分类不存在");
        }
        
        // 检查新名称是否与其他分类重复
        Category sameNameCategory = getCategoryByName(category.getName());
        if (sameNameCategory != null && !sameNameCategory.getId().equals(category.getId())) {
            throw new RuntimeException("分类名称已存在");
        }
        
        // 更新分类
        categoryMapper.updateById(category);
        
        return categoryMapper.selectById(category.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCategory(Long id) {
        // 验证分类是否存在
        Category category = categoryMapper.selectById(id);
        if (category == null || category.getDeleted() == 1) {
            return false;
        }
        
        // 逻辑删除分类
        return categoryMapper.deleteById(id) > 0;
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryMapper.selectOne(new QueryWrapper<Category>()
                .lambda()
                .eq(Category::getId, id)
                .eq(Category::getDeleted, 0));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryMapper.selectList(new QueryWrapper<Category>()
                .lambda()
                .eq(Category::getDeleted, 0)
                .orderByAsc(Category::getName));
    }

    @Override
    public IPage<Category> getCategoryPage(Page<Category> page) {
        return categoryMapper.selectPage(page, new QueryWrapper<Category>()
                .lambda()
                .eq(Category::getDeleted, 0)
                .orderByAsc(Category::getName));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryMapper.selectOne(new QueryWrapper<Category>()
                .lambda()
                .eq(Category::getName, name)
                .eq(Category::getDeleted, 0));
    }
}
