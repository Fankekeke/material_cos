package cc.mrbird.febs.cos.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockInfoExcel {

    /**
     * 入库物品名称
     */
    private String name;

    /**
     * 型号
     */
    private String mode;

    /**
     * 类型
     */
    private String type;

    /**
     * 类型ID
     */
    private String typeId;

    /**
     * 物品数量
     */
    private Integer amount;

    /**
     * 物品单位
     */
    private String unit;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 库房
     */
    private String stock;

    /**
     * 库房
     */
    private Integer stockId;
}
