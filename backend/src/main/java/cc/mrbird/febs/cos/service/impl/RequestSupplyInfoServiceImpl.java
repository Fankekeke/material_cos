package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.dao.StockPutMapper;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.dao.RequestSupplyInfoMapper;
import cc.mrbird.febs.cos.service.IGoodsBelongService;
import cc.mrbird.febs.cos.service.IRequestSupplyInfoService;
import cc.mrbird.febs.cos.service.IStockInfoService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RequestSupplyInfoServiceImpl extends ServiceImpl<RequestSupplyInfoMapper, RequestSupplyInfo> implements IRequestSupplyInfoService {

    private final StockPutMapper stockPutMapper;

    private final IStockInfoService stockInfoService;

    private final IGoodsBelongService goodsBelongService;

    /**
     * 分页获取供应采购
     *
     * @param page     分页对象
     * @param requestSupplyInfo 供应采购
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> querySupplyPage(Page<RequestSupplyInfo> page, RequestSupplyInfo requestSupplyInfo) {
        return baseMapper.querySupplyPage(page, requestSupplyInfo);
    }

    @Override
    public Boolean rurchasePut(RequestSupplyInfo requestSupplyInfo) {
        List<GoodsBelong> goodsBelongList = goodsBelongService.list(Wrappers.<GoodsBelong>lambdaQuery().eq(GoodsBelong::getNum, requestSupplyInfo.getNum()));
        // 添加入库单
        StockPut stockPut = new StockPut();
        stockPut.setContent(requestSupplyInfo.getContent());
        stockPut.setCreateDate(DateUtil.formatDateTime(new Date()));
        stockPut.setCustodian("管理员");
        stockPut.setPutUser("管理员");
        stockPut.setPrice(requestSupplyInfo.getPrice());
        stockPut.setNum("PUT-" + System.currentTimeMillis());
        stockPutMapper.insert(stockPut);

        goodsBelongList.forEach(item -> {
            item.setCreateDate(DateUtil.formatDateTime(new Date()));
            item.setNum(stockPut.getNum());
            // 判断库房物品是否存在
            StockInfo stockInfo = stockInfoService.getOne(Wrappers.<StockInfo>lambdaQuery().eq(StockInfo::getName, item.getName()).eq(StockInfo::getTypeId, item.getTypeId()).eq(StockInfo::getIsIn, 0));
            if (stockInfo != null) {
                // 更改库房数据
                stockInfoService.update(Wrappers.<StockInfo>lambdaUpdate().set(StockInfo::getAmount, stockInfo.getAmount() + item.getAmount())
                        .set(StockInfo::getPrice, stockInfo.getPrice())
                        .eq(StockInfo::getName, stockInfo.getName()));
            } else {
                // 重新添加库房数据
                StockInfo stock = new StockInfo();
                stock.setName(item.getName());
                stock.setAmount(item.getAmount());
                stock.setCreateDate(DateUtil.formatDateTime(new Date()));
                stock.setType(item.getType());
                stock.setTypeId(item.getTypeId());
                stock.setUnit(item.getUnit());
                stock.setPrice(item.getPrice());
                stock.setIsIn(0);
                stockInfo = stock;
                stockInfoService.save(stock);
            }
            // 添加入库记录
            StockInfo stockInfoPut = new StockInfo();
            stockInfoPut.setParentId(stockInfo.getId());
            stockInfoPut.setName(item.getName());
            stockInfoPut.setAmount(item.getAmount());
            stockInfoPut.setCreateDate(DateUtil.formatDateTime(new Date()));
            stockInfoPut.setType(item.getType());
            stockInfoPut.setTypeId(item.getTypeId());
            stockInfoPut.setUnit(item.getUnit());
            stockInfoPut.setPrice(item.getPrice());
            stockInfoPut.setIsIn(1);
            stockInfoService.save(stockInfoPut);

            // 添加所属信息
            GoodsBelong goodsBelong = new GoodsBelong();
            goodsBelong.setNum(stockPut.getNum());
            goodsBelong.setCreateDate(DateUtil.formatDateTime(new Date()));
            goodsBelong.setAmount(item.getAmount());
            goodsBelong.setName(item.getName());
            goodsBelong.setPrice(item.getPrice());
            goodsBelong.setType(item.getType());
            goodsBelong.setTypeId(item.getTypeId());
            goodsBelong.setUnit(item.getUnit());
            goodsBelongService.save(goodsBelong);
        });
        // 修改状态
        this.update(Wrappers.<RequestSupplyInfo>lambdaUpdate().set(RequestSupplyInfo::getStatus, 2).eq(RequestSupplyInfo::getId, requestSupplyInfo.getId()));
        return true;
    }

    @Override
    public LinkedHashMap<String, Object> detailPurchase(String id) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("logistics", Collections.emptyList());
            }
        };

        // 采购单信息
        RequestSupplyInfo purchase = this.getById(id);

        if (StrUtil.isNotEmpty(purchase.getLogistics())) {
            List<LogisticsInfo> logisticsList = JSONUtil.toList(purchase.getLogistics(), LogisticsInfo.class);
            result.put("logistics", logisticsList);
        }
        return result;
    }
}
