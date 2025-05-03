package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 供应采购
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RequestSupplyInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 采购申请单号
     */
    private String num;

    /**
     * 采购说明
     */
    private String content;

    /**
     * 预计金额
     */
    private BigDecimal price;

    /**
     * 供应商ID
     */
    private Integer enterpriseId;

    /**
     * 地址
     */
    private String putAddress;

    /**
     * 经度
     */
    private BigDecimal putLongitude;

    /**
     * 纬度
     */
    private BigDecimal putLatitude;

    /**
     * 地址
     */
    private String outAddress;

    /**
     * 经度
     */
    private BigDecimal outLongitude;

    /**
     * 纬度
     */
    private BigDecimal outLatitude;

    /**
     * 采购物流
     */
    private String logistics;

    /**
     * 采购状态（1.运输中 2.已验收）
     */
    private Integer status;

    /**
     * 采购时间
     */
    private String createDate;

    @TableField(exist = false)
    private String name;

    @TableField(exist = false)
    private String goods;


}
