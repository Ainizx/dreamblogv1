package cn.open52.dreamblogv1.service.impl;

import cn.open52.dreamblogv1.entity.FileEntity;
import cn.open52.dreamblogv1.mapper.FileMapper;
import cn.open52.dreamblogv1.service.FileService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, FileEntity> implements FileService {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    public FileEntity uploadFile(MultipartFile file, Long userId) throws IOException {
        // 确保上传目录存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 生成唯一文件名
        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf(".") + 1);
        String uniqueName = UUID.randomUUID().toString() + "." + extension;
        String filePath = uploadPath + File.separator + uniqueName;

        // 保存文件到服务器
        File dest = new File(filePath);
        file.transferTo(dest);

        // 保存文件信息到数据库
        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(uniqueName);
        fileEntity.setOriginalName(originalName);
        fileEntity.setPath(filePath);
        fileEntity.setSize(file.getSize());
        fileEntity.setType(file.getContentType());
        fileEntity.setUserId(userId);

        baseMapper.insert(fileEntity);
        return fileEntity;
    }

    @Override
    public void downloadFile(Long fileId, HttpServletResponse response) throws IOException {
        // 从数据库获取文件信息
        FileEntity fileEntity = baseMapper.selectById(fileId);
        if (fileEntity == null) {
            throw new IOException("File not found");
        }

        File file = new File(fileEntity.getPath());
        if (!file.exists()) {
            throw new IOException("File not found on server");
        }

        // 设置响应头
        response.setContentType(fileEntity.getType());
        response.setContentLengthLong(file.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileEntity.getOriginalName() + "\"");

        // 写入响应流
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
    }

    @Override
    public List<FileEntity> listUserFiles(Long userId) {
        LambdaQueryWrapper<FileEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FileEntity::getUserId, userId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean deleteFile(Long fileId, Long userId) {
        // 获取文件信息
        FileEntity fileEntity = baseMapper.selectById(fileId);
        if (fileEntity == null) {
            return false;
        }

        // 检查权限（只能删除自己的文件）
        if (!fileEntity.getUserId().equals(userId)) {
            return false;
        }

        // 删除服务器上的文件
        File file = new File(fileEntity.getPath());
        if (file.exists()) {
            file.delete();
        }

        // 从数据库删除记录
        return baseMapper.deleteById(fileId) > 0;
    }

    @Override
    public FileEntity getFileById(Long fileId) {
        return baseMapper.selectById(fileId);
    }
}
