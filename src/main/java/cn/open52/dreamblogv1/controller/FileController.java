package cn.open52.dreamblogv1.controller;

import cn.open52.dreamblogv1.entity.FileEntity;
import cn.open52.dreamblogv1.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FileEntity> uploadFile(@RequestParam("file") MultipartFile file, Principal principal) throws IOException {
        Long userId = Long.parseLong(principal.getName());
        FileEntity uploadedFile = fileService.uploadFile(file, userId);
        return new ResponseEntity<>(uploadedFile, HttpStatus.OK);
    }

    @GetMapping("/download/{fileId}")
    @PreAuthorize("isAuthenticated()")
    public void downloadFile(@PathVariable Long fileId, Principal principal, HttpServletResponse response) throws IOException {
        // 检查权限
        FileEntity file = fileService.getFileById(fileId);
        if (file == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Long userId = Long.parseLong(principal.getName());
        boolean isAdmin = principal.toString().contains("ROLE_ADMIN");

        if (!isAdmin && !file.getUserId().equals(userId)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        fileService.downloadFile(fileId, response);
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<FileEntity>> listMyFiles(Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        List<FileEntity> files = fileService.listUserFiles(userId);
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @GetMapping("/{fileId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FileEntity> getFile(@PathVariable Long fileId, Principal principal) {
        FileEntity file = fileService.getFileById(fileId);
        if (file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Long userId = Long.parseLong(principal.getName());
        boolean isAdmin = principal.toString().contains("ROLE_ADMIN");

        if (!isAdmin && !file.getUserId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    @DeleteMapping("/{fileId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        boolean deleted = fileService.deleteFile(fileId, userId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 管理员接口：获取所有文件
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FileEntity>> listAllFiles() {
        List<FileEntity> files = fileService.list();
        return new ResponseEntity<>(files, HttpStatus.OK);
    }
}
