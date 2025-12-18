package cn.open52.dreamblogv1.service;

import cn.open52.dreamblogv1.entity.Tag;
import cn.open52.dreamblogv1.mapper.TagMapper;
import cn.open52.dreamblogv1.service.impl.TagServiceImpl;
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
public class TagServiceTest {

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    private Tag testTag;

    @BeforeEach
    public void setUp() {
        testTag = new Tag();
        testTag.setId(1L);
        testTag.setName("测试标签");
        testTag.setDeleted(0);
    }

    @Test
    public void testCreateTag() {
        // 模拟mapper行为
        Mockito.when(tagMapper.selectOne(Mockito.any())).thenReturn(null);
        Mockito.when(tagMapper.insert(Mockito.any(Tag.class))).thenReturn(1);

        // 执行测试
        Tag result = tagService.createTag(testTag);

        // 验证结果
        Assertions.assertNotNull(result);
        Assertions.assertEquals("测试标签", result.getName());

        // 验证mapper方法被调用
        Mockito.verify(tagMapper, Mockito.times(1)).insert(Mockito.any(Tag.class));
    }

    @Test
    public void testCreateTagWithDuplicateName() {
        // 模拟mapper行为 - 标签名称已存在
        Mockito.when(tagMapper.selectOne(Mockito.any())).thenReturn(testTag);

        // 执行测试 - 应该抛出异常
        Assertions.assertThrows(RuntimeException.class, () -> {
            tagService.createTag(testTag);
        });
    }

    @Test
    public void testUpdateTag() {
        // 模拟mapper行为
        Mockito.when(tagMapper.selectById(Mockito.anyLong())).thenReturn(testTag);
        Mockito.when(tagMapper.updateById(Mockito.any(Tag.class))).thenReturn(1);
        Mockito.when(tagMapper.selectOne(Mockito.any())).thenReturn(null);

        // 修改测试标签
        testTag.setName("更新后的标签名称");

        // 执行测试
        Tag result = tagService.updateTag(testTag);

        // 验证结果
        Assertions.assertNotNull(result);
        Assertions.assertEquals("更新后的标签名称", result.getName());

        // 验证mapper方法被调用
        Mockito.verify(tagMapper, Mockito.times(1)).updateById(Mockito.any(Tag.class));
    }

    @Test
    public void testUpdateTagWithNonExistent() {
        // 模拟mapper行为 - 标签不存在
        Mockito.when(tagMapper.selectById(Mockito.anyLong())).thenReturn(null);

        // 执行测试 - 应该抛出异常
        Assertions.assertThrows(RuntimeException.class, () -> {
            tagService.updateTag(testTag);
        });
    }

    @Test
    public void testUpdateTagWithDuplicateName() {
        // 创建一个同名但不同ID的标签
        Tag duplicateTag = new Tag();
        duplicateTag.setId(2L);
        duplicateTag.setName("测试标签");

        // 模拟mapper行为
        Mockito.when(tagMapper.selectById(Mockito.anyLong())).thenReturn(testTag);
        Mockito.when(tagMapper.selectOne(Mockito.any())).thenReturn(duplicateTag);

        // 执行测试 - 应该抛出异常
        Assertions.assertThrows(RuntimeException.class, () -> {
            tagService.updateTag(testTag);
        });
    }

    @Test
    public void testDeleteTag() {
        // 模拟mapper行为
        Mockito.when(tagMapper.selectById(Mockito.anyLong())).thenReturn(testTag);
        Mockito.when(tagMapper.deleteById(Mockito.anyLong())).thenReturn(1);

        // 执行测试
        boolean result = tagService.deleteTag(1L);

        // 验证结果
        Assertions.assertTrue(result);

        // 验证mapper方法被调用
        Mockito.verify(tagMapper, Mockito.times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    public void testDeleteNonExistentTag() {
        // 模拟mapper行为 - 标签不存在
        Mockito.when(tagMapper.selectById(Mockito.anyLong())).thenReturn(null);

        // 执行测试
        boolean result = tagService.deleteTag(1L);

        // 验证结果
        Assertions.assertFalse(result);
    }

    @Test
    public void testGetTagById() {
        // 模拟mapper行为
        Mockito.when(tagMapper.selectOne(Mockito.any())).thenReturn(testTag);

        // 执行测试
        Tag result = tagService.getTagById(1L);

        // 验证结果
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId().longValue());
        Assertions.assertEquals("测试标签", result.getName());

        // 验证mapper方法被调用
        Mockito.verify(tagMapper, Mockito.times(1)).selectOne(Mockito.any());
    }

    @Test
    public void testGetAllTags() {
        // 准备测试数据
        List<Tag> tagList = Arrays.asList(testTag);

        // 模拟mapper行为
        Mockito.when(tagMapper.selectList(Mockito.any())).thenReturn(tagList);

        // 执行测试
        List<Tag> result = tagService.getAllTags();

        // 验证结果
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("测试标签", result.get(0).getName());

        // 验证mapper方法被调用
        Mockito.verify(tagMapper, Mockito.times(1)).selectList(Mockito.any());
    }

    @Test
    public void testGetTagPage() {
        // 准备测试数据
        List<Tag> tagList = Arrays.asList(testTag);
        Page<Tag> page = new Page<>(1, 10);
        page.setRecords(tagList);
        page.setTotal(1);

        // 模拟mapper行为
        Mockito.when(tagMapper.selectPage(Mockito.any(Page.class), Mockito.any())).thenReturn(page);

        // 执行测试
        IPage<Tag> result = tagService.getTagPage(page);

        // 验证结果
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getTotal());
        Assertions.assertEquals(1, result.getRecords().size());
        Assertions.assertEquals("测试标签", result.getRecords().get(0).getName());

        // 验证mapper方法被调用
        Mockito.verify(tagMapper, Mockito.times(1)).selectPage(Mockito.any(Page.class), Mockito.any());
    }

    @Test
    public void testGetTagByName() {
        // 模拟mapper行为
        Mockito.when(tagMapper.selectOne(Mockito.any())).thenReturn(testTag);

        // 执行测试
        Tag result = tagService.getTagByName("测试标签");

        // 验证结果
        Assertions.assertNotNull(result);
        Assertions.assertEquals("测试标签", result.getName());

        // 验证mapper方法被调用
        Mockito.verify(tagMapper, Mockito.times(1)).selectOne(Mockito.any());
    }
}
