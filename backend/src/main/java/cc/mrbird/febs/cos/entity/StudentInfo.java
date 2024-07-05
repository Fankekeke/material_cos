package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StudentInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 编号
     */
    private String code;

    /**
     * 姓名
     */
    private String name;

    /**
     * 实验小组
     */
    private String team;

    /**
     * 所属账户
     */
    private Long userId;

    /**
     * 备注
     */
    private String content;

    /**
     * 创建时间
     */
    private String createDate;
    private String images;

    @TableField(exist = false)
    private Integer accountStatus;

}
