package cn.open52.dreamblogv1.service;

import cn.open52.dreamblogv1.entity.FileEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface FileService extends IService<FileEntity> {

    FileEntity uploadFile(MultipartFile file, Long userId) throws IOException;

    void downloadFile(Long fileId, HttpServletResponse response) throws IOException;

    List<FileEntity> listUserFiles(Long userId);

    boolean deleteFile(Long fileId, Long userId);

    FileEntity getFileById(Long fileId);
}
