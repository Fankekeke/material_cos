package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.dao.StockInfoMapper;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StockInfoServiceImpl extends ServiceImpl<StockInfoMapper, StockInfo> implements IStockInfoService {

    private final IStockPutService stockPutService;

    private final IGoodsBelongService goodsBelongService;

    private final IBulletinInfoService bulletinInfoService;

    private final IStudentInfoService studentInfoService;

    private final IConsumableTypeService consumableTypeService;

    @Override
    public IPage<LinkedHashMap<String, Object>> stockInfoByPage(Page page, StockInfo stockInfo) {
        return baseMapper.stockInfoByPage(page, stockInfo);
    }

    // 获取库房信息
    @Override
    public List<LinkedHashMap<String, Object>> stockInfoByList(StockInfo stockInfo) {
        return baseMapper.stockInfoByList(stockInfo);
    }

    /**
     * 统计看板查询
     *
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectBoard() {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("out", Collections.emptyList());
                put("put", Collections.emptyList());
                put("price", Collections.emptyList());
                put("type", Collections.emptyList());
            }
        };

        // 库房信息
        List<StockInfo> stockInfoList = this.list(Wrappers.<StockInfo>lambdaQuery().eq(StockInfo::getIsIn, 0));
        // 入库信息
        List<StockInfo> putStockInfoList = this.list(Wrappers.<StockInfo>lambdaQuery().eq(StockInfo::getIsIn, 1));
        // 出库信息
        List<StockInfo> outStockInfoList = this.list(Wrappers.<StockInfo>lambdaQuery().eq(StockInfo::getIsIn, 2));

        // 物品类型
        List<ConsumableType> typeList = consumableTypeService.list();
        Map<Integer, String> typeMap = typeList.stream().collect(Collectors.toMap(ConsumableType::getId, ConsumableType::getName));

        // 出库统计
        List<LinkedHashMap<String, Object>> outRateList = new ArrayList<>();
        Map<Integer, List<StockInfo>> outRate = outStockInfoList.stream().collect(Collectors.groupingBy(StockInfo::getTypeId));
        outRate.forEach((key, value) -> {
            LinkedHashMap<String, Object> rateItem = new LinkedHashMap<String, Object>() {
                {
                    put("name", typeMap.get(key));
                    put("value", value.size());
                }
            };
            outRateList.add(rateItem);
        });

        // 入库统计
        List<LinkedHashMap<String, Object>> putRateList = new ArrayList<>();
        Map<Integer, List<StockInfo>> putRate = putStockInfoList.stream().collect(Collectors.groupingBy(StockInfo::getTypeId));
        putRate.forEach((key, value) -> {
            LinkedHashMap<String, Object> rateItem = new LinkedHashMap<String, Object>() {
                {
                    put("name", typeMap.get(key));
                    put("value", value.size());
                }
            };
            putRateList.add(rateItem);
        });

        // 库房统计
        List<LinkedHashMap<String, Object>> stockRateList = new ArrayList<>();
        // 库房价格统计
        List<LinkedHashMap<String, Object>> stockPriceRateList = new ArrayList<>();
        Map<Integer, List<StockInfo>> stockRate = stockInfoList.stream().collect(Collectors.groupingBy(StockInfo::getTypeId));
        stockRate.forEach((key, value) -> {
            LinkedHashMap<String, Object> rateItem = new LinkedHashMap<String, Object>() {
                {
                    put("name", typeMap.get(key));
                    put("value", value.size());
                }
            };
            LinkedHashMap<String, Object> priceRateItem = new LinkedHashMap<String, Object>() {
                {
                    put("name", typeMap.get(key));
                    put("value", value.stream().map(e -> NumberUtil.mul(e.getAmount(), e.getPrice())).reduce(BigDecimal.ZERO,BigDecimal::add));
                }
            };
            stockRateList.add(rateItem);
            stockPriceRateList.add(rateItem);
        });
        result.put("out", outRateList);
        result.put("put", putRateList);
        result.put("price", stockPriceRateList);
        result.put("type", stockRateList);
        return result;
    }

    @Override
    public Boolean stockPut(String goods, String custodian, String putUser, String content, BigDecimal price) {
        // 添加入库单
        StockPut stockPut = new StockPut();
        stockPut.setContent(content);
        stockPut.setCreateDate(DateUtil.formatDateTime(new Date()));
        stockPut.setCustodian(custodian);
        stockPut.setPutUser(putUser);
        stockPut.setPrice(price);
        stockPut.setNum("PUT-"+new Date().getTime());
        stockPutService.save(stockPut);

        // 添加入库
        JSONArray array = JSONUtil.parseArray(goods);
        List<GoodsBelong> goodsBelongList = JSONUtil.toList(array, GoodsBelong.class);
        goodsBelongList.forEach(item -> {
            item.setCreateDate(DateUtil.formatDateTime(new Date()));
            item.setNum(stockPut.getNum());
            // 判断库房物品是否存在
            StockInfo stockInfo = this.getOne(Wrappers.<StockInfo>lambdaQuery().eq(StockInfo::getName, item.getName()).eq(StockInfo::getTypeId, item.getTypeId()).eq(StockInfo::getIsIn, 0));
            if (stockInfo != null) {
                // 更改库房数据
                this.update(Wrappers.<StockInfo>lambdaUpdate().set(StockInfo::getAmount, stockInfo.getAmount()+item.getAmount())
                        .set(StockInfo::getPrice, stockInfo.getPrice())
                        .eq(StockInfo::getId, stockInfo.getId()));
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
                this.save(stock);
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
            this.save(stockInfoPut);

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
        return true;
    }

    @Override
    public IPage<LinkedHashMap<String, Object>> stockInfoDetailPage(Page page, StockInfo stockInfo) {
        return baseMapper.stockInfoDetailPage(page, stockInfo);
    }

    @Override
    public IPage<LinkedHashMap<String, Object>> getGoodsPutByUserId(Page page, StockInfo stockInfo) {
        return baseMapper.getGoodsPutByUserId(page, stockInfo);
    }

    @Override
    public LinkedHashMap<String, Object> home(Integer type, Integer userId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("bulletinList", bulletinInfoService.list());
        if (type == 74) {
            result.put("studentInfo", studentInfoService.getOne(Wrappers.<StudentInfo>lambdaQuery().eq(StudentInfo::getUserId, userId)));
        }
        result.put("stockPutRate", baseMapper.stockPutRate());
        result.put("stockPutTypeRate", baseMapper.stockPutTypeRate());
        result.put("stockOutRate", baseMapper.stockOutRate());
        result.put("stockOutTypeRate", baseMapper.stockOutTypeRate());
        result.put("stockInfo", baseMapper.stockInfoByMonth());
        return result;
    }

    /**
     * 导入信息列表
     *
     * @param file 文件
     * @return 结果
     */
    @Override
    public String importExcel(MultipartFile file) throws Exception {
        ExcelReader excelReader = ExcelUtil.getReader(file.getInputStream(), 0);
        setExcelHeaderAlias(excelReader);
        List<StockInfo> reports = excelReader.read(1, 2, Integer.MAX_VALUE, StockInfo.class);
        StringBuilder error = new StringBuilder("");
        if (CollectionUtil.isEmpty(reports)) {
            error.append("导入数据不得为空。");
            return error.toString();
        }
        for (StockInfo expert : reports) {
            if (StrUtil.isEmpty(expert.getName())) {
                error.append("\n名称不能为空");
                return error.toString();
            }
            expert.setCreateDate(DateUtil.formatDateTime(new Date()));
        }
        if (StrUtil.isEmpty(error.toString())) {
            this.saveBatch(reports);
            return null;
        }
        return error.toString();
    }

    /**
     * 设置HeaderAlias
     *
     * @param excelReader HeaderAlias
     */
    public void setExcelHeaderAlias(ExcelReader excelReader) {
        excelReader.addHeaderAlias("功能供应商名称", "name");
        excelReader.addHeaderAlias("单位简称或代号", "abbreviation");
        excelReader.addHeaderAlias("统一社会信用代码", "creditCode");
        excelReader.addHeaderAlias("单位性质", "nature");
        excelReader.addHeaderAlias("二级企业单位性质", "natureTwo");
        excelReader.addHeaderAlias("经营状态", "status");
        excelReader.addHeaderAlias("法定代表人", "corporateRepresentative");
        excelReader.addHeaderAlias("法定代表人身份证号", "corporateRepresentativeId");
        excelReader.addHeaderAlias("法定代表人电话", "corporateRepresentativePhone");
        excelReader.addHeaderAlias("注册资本", "registeredCapital");
        excelReader.addHeaderAlias("注册资金币种", "registeredCapitalCurrency");
        excelReader.addHeaderAlias("成立日期", "establishmentDate");
        excelReader.addHeaderAlias("营业期限始期", "businessBeginDate");
        excelReader.addHeaderAlias("营业期限止期", "businessEndDate");
        excelReader.addHeaderAlias("注册地址", "registeredAddress");
        excelReader.addHeaderAlias("经营范围", "businessScope");
        excelReader.addHeaderAlias("省", "province");
        excelReader.addHeaderAlias("市", "city");
        excelReader.addHeaderAlias("区", "district");
        excelReader.addHeaderAlias("英文企业名称", "enName");
        excelReader.addHeaderAlias("所属行业", "industry");
        excelReader.addHeaderAlias("单位简介", "unitDescription");
    }
}
