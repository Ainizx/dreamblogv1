package cn.open52.dreamblogv1.service;

import cn.open52.dreamblogv1.entity.Category;
import cn.open52.dreamblogv1.mapper.CategoryMapper;
import cn.open52.dreamblogv1.service.impl.CategoryServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category testCategory;

    @BeforeEach
    public void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("测试分类");
        testCategory.setDescription("测试分类描述");
        testCategory.setDeleted(0);
    }

    @Test
    public void testCreateCategory() {
        // 模拟mapper行为
        Mockito.when(categoryMapper.selectOne(Mockito.any())).thenReturn(null);
        Mockito.when(categoryMapper.insert(Mockito.any(Category.class))).thenReturn(1);

        // 执行测试
        Category result = categoryService.createCategory(testCategory);

        // 验证结果
        Assertions.assertNotNull(result);
        Assertions.assertEquals("测试分类", result.getName());

        // 验证mapper方法被调用
        Mockito.verify(categoryMapper, Mockito.times(1)).selectOne(Mockito.any());
        Mockito.verify(categoryMapper, Mockito.times(1)).insert(Mockito.any(Category.class));
    }

    @Test
    public void testCreateCategoryWithDuplicateName() {
        // 模拟mapper行为 - 分类名称已存在
        Mockito.when(categoryMapper.selectOne(Mockito.any())).thenReturn(testCategory);

        // 执行测试 - 应该抛出异常
        Assertions.assertThrows(RuntimeException.class, () -> {
            categoryService.createCategory(testCategory);
        });
    }

    @Test
    public void testUpdateCategory() {
        // 模拟mapper行为
        Mockito.when(categoryMapper.selectById(Mockito.anyLong())).thenReturn(testCategory);
        Mockito.when(categoryMapper.updateById(Mockito.any(Category.class))).thenReturn(1);
        Mockito.when(categoryMapper.selectOne(Mockito.any())).thenReturn(null);
        Mockito.when(categoryMapper.selectById(Mockito.anyLong())).thenReturn(testCategory);

        // 修改测试分类
        testCategory.setName("更新后的分类名称");

        // 执行测试
        Category result = categoryService.updateCategory(testCategory);

        // 验证结果
        Assertions.assertNotNull(result);
        Assertions.assertEquals("更新后的分类名称", result.getName());

        // 验证mapper方法被调用
        Mockito.verify(categoryMapper, Mockito.times(2)).selectById(Mockito.anyLong());
        Mockito.verify(categoryMapper, Mockito.times(1)).selectOne(Mockito.any());
        Mockito.verify(categoryMapper, Mockito.times(1)).updateById(Mockito.any(Category.class));
    }

    @Test
    public void testUpdateCategoryWithNonExistent() {
        // 模拟mapper行为 - 分类不存在
        Mockito.when(categoryMapper.selectById(Mockito.anyLong())).thenReturn(null);

        // 执行测试 - 应该抛出异常
        Assertions.assertThrows(RuntimeException.class, () -> {
            categoryService.updateCategory(testCategory);
        });
    }

    @Test
    public void testUpdateCategoryWithDuplicateName() {
        // 创建一个同名但不同ID的分类
        Category duplicateCategory = new Category();
        duplicateCategory.setId(2L);
        duplicateCategory.setName("测试分类");

        // 模拟mapper行为
        Mockito.when(categoryMapper.selectById(Mockito.anyLong())).thenReturn(testCategory);
        Mockito.when(categoryMapper.selectOne(Mockito.any(QueryWrapper.class))).thenReturn(duplicateCategory);

        // 执行测试 - 应该抛出异常
        Assertions.assertThrows(RuntimeException.class, () -> {
            categoryService.updateCategory(testCategory);
        });
    }

    @Test
    public void testDeleteCategory() {
        // 模拟mapper行为
        Mockito.when(categoryMapper.selectById(Mockito.anyLong())).thenReturn(testCategory);
        Mockito.when(categoryMapper.deleteById(Mockito.anyLong())).thenReturn(1);

        // 执行测试
        boolean result = categoryService.deleteCategory(1L);

        // 验证结果
        Assertions.assertTrue(result);

        // 验证mapper方法被调用
        Mockito.verify(categoryMapper, Mockito.times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    public void testDeleteNonExistentCategory() {
        // 模拟mapper行为 - 分类不存在
        Mockito.when(categoryMapper.selectById(Mockito.anyLong())).thenReturn(null);

        // 执行测试
        boolean result = categoryService.deleteCategory(1L);

        // 验证结果
        Assertions.assertFalse(result);
    }

    @Test
    public void testGetCategoryById() {
        // 模拟mapper行为
        Mockito.when(categoryMapper.selectOne(Mockito.any())).thenReturn(testCategory);

        // 执行测试
        Category result = categoryService.getCategoryById(1L);

        // 验证结果
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId().longValue());
        Assertions.assertEquals("测试分类", result.getName());

        // 验证mapper方法被调用
        Mockito.verify(categoryMapper, Mockito.times(1)).selectOne(Mockito.any());
    }

    @Test
    public void testGetAllCategories() {
        // 准备测试数据
        List<Category> categoryList = Arrays.asList(testCategory);

        // 模拟mapper行为
        Mockito.when(categoryMapper.selectList(Mockito.any())).thenReturn(categoryList);

        // 执行测试
        List<Category> result = categoryService.getAllCategories();

        // 验证结果
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("测试分类", result.get(0).getName());

        // 验证mapper方法被调用
        Mockito.verify(categoryMapper, Mockito.times(1)).selectList(Mockito.any());
    }

    @Test
    public void testGetCategoryPage() {
        // 准备测试数据
        List<Category> categoryList = Arrays.asList(testCategory);
        Page<Category> page = new Page<>(1, 10);
        page.setRecords(categoryList);
        page.setTotal(1);

        // 模拟mapper行为
        Mockito.when(categoryMapper.selectPage(Mockito.any(Page.class), Mockito.any())).thenReturn(page);

        // 执行测试
        IPage<Category> result = categoryService.getCategoryPage(page);

        // 验证结果
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getTotal());
        Assertions.assertEquals(1, result.getRecords().size());
        Assertions.assertEquals("测试分类", result.getRecords().get(0).getName());

        // 验证mapper方法被调用
        Mockito.verify(categoryMapper, Mockito.times(1)).selectPage(Mockito.any(Page.class), Mockito.any());
    }

    @Test
    public void testGetCategoryByName() {
        // 模拟mapper行为
        Mockito.when(categoryMapper.selectOne(Mockito.any())).thenReturn(testCategory);

        // 执行测试
        Category result = categoryService.getCategoryByName("测试分类");

        // 验证结果
        Assertions.assertNotNull(result);
        Assertions.assertEquals("测试分类", result.getName());

        // 验证mapper方法被调用
        Mockito.verify(categoryMapper, Mockito.times(1)).selectOne(Mockito.any());
    }
}
