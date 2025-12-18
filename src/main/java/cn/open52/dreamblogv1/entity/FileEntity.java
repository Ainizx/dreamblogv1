package cn.open52.dreamblogv1.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("file")
public class FileEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("original_name")
    private String originalName;

    @TableField("path")
    private String path;

    @TableField("size")
    private Long size;

    @TableField("type")
    private String type;

    @TableField("user_id")
    private Long userId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;

}