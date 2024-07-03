package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.ReplenishmentInfo;
import cc.mrbird.febs.cos.entity.StockInfo;
import cc.mrbird.febs.cos.entity.StorehouseInfo;
import cc.mrbird.febs.cos.dao.StorehouseInfoMapper;
import cc.mrbird.febs.cos.service.IConsumableTypeService;
import cc.mrbird.febs.cos.service.IReplenishmentInfoService;
import cc.mrbird.febs.cos.service.IStockInfoService;
import cc.mrbird.febs.cos.service.IStorehouseInfoService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StorehouseInfoServiceImpl extends ServiceImpl<StorehouseInfoMapper, StorehouseInfo> implements IStorehouseInfoService {

    private final IStockInfoService stockInfoService;
    private final IReplenishmentInfoService replenishmentInfoService;

    private final IConsumableTypeService consumableTypeService;

    /**
     * 分页获取库房管理
     *
     * @param page           分页对象
     * @param storehouseInfo 库房管理
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectStorePage(Page<StorehouseInfo> page, StorehouseInfo storehouseInfo) {
        return baseMapper.selectStorePage(page, storehouseInfo);
    }

    /**
     * 任务盘库
     */
    @Override
    public void diskLibrary() {
        List<LinkedHashMap<String, Object>> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        List<StockInfo> storehouseInfoList = stockInfoService.list(Wrappers.<StockInfo>lambdaQuery().eq(StockInfo::getIsIn, 0).lt(StockInfo::getAmount, 5));
        storehouseInfoList.forEach(item -> {
            LinkedHashMap<String, Object> resultItem = new LinkedHashMap<String, Object>() {
                {
                    put("materialName", item.getName());
                    put("materialType", item.getTypeId());
                    put("model", item.getType());
                    put("quantity", item.getAmount());
                    put("measurementUnit", item.getUnit());
                    put("unitPrice", item.getPrice());
                }
            };
            if (item.getAmount() == 0) {
                sb.append("【").append(item.getName()).append("】库存余量不足，请及时补货");
            } else {
                sb.append("【").append(item.getName()).append("】库存余量不足，当前库存为 ").append(item.getAmount()).append(item.getUnit());
            }
            result.add(resultItem);
        });
        // 设置盘库记录
        ReplenishmentInfo replenishment = new ReplenishmentInfo();
        replenishment.setStatus(0);
        replenishment.setTaskDate(DateUtil.formatDateTime(new Date()));
        replenishment.setContent(StrUtil.isEmpty(sb.toString()) ? "库存充足" : sb.toString());
        replenishment.setReplenishment(JSONUtil.toJsonStr(result));
        replenishmentInfoService.saveReplenishment(replenishment);
    }
}
