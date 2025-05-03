package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.EnterpriseInfo;
import cc.mrbird.febs.cos.entity.GoodsBelong;
import cc.mrbird.febs.cos.entity.LogisticsInfo;
import cc.mrbird.febs.cos.entity.RequestSupplyInfo;
import cc.mrbird.febs.cos.service.IEnterpriseInfoService;
import cc.mrbird.febs.cos.service.IGoodsBelongService;
import cc.mrbird.febs.cos.service.IRequestSupplyInfoService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/request-supply-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RequestSupplyInfoController {

    private final IRequestSupplyInfoService requestSupplyInfoService;

    private final IEnterpriseInfoService enterpriseInfoService;

    private final IGoodsBelongService goodsBelongService;

    /**
     * 分页获取供应采购
     *
     * @param page              分页对象
     * @param requestSupplyInfo 供应采购
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<RequestSupplyInfo> page, RequestSupplyInfo requestSupplyInfo) {
        return R.ok(requestSupplyInfoService.querySupplyPage(page, requestSupplyInfo));
    }

    /**
     * 根据订单查询物流
     *
     * @param orderId 订单ID
     * @return 结果
     */
    @GetMapping("/logistics/{orderId}")
    public R selectLogistics(@PathVariable("orderId") Integer orderId) {
        RequestSupplyInfo orderInfo = requestSupplyInfoService.getById(orderId);
        if (StrUtil.isNotEmpty(orderInfo.getLogistics())) {
            List<LogisticsInfo> logisticsList = JSONUtil.toList(orderInfo.getLogistics(), LogisticsInfo.class);
            return R.ok(logisticsList);
        } else {
            return R.ok(Collections.emptyList());
        }
    }

    /**
     * 新增供应采购
     *
     * @param requestSupplyInfo 供应采购
     * @return 结果
     */
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public R save(RequestSupplyInfo requestSupplyInfo) {
        requestSupplyInfo.setNum("RS-" + System.currentTimeMillis());
        requestSupplyInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        // 获取企业信息
        EnterpriseInfo enterpriseInfo = enterpriseInfoService.getOne(Wrappers.<EnterpriseInfo>lambdaQuery().eq(EnterpriseInfo::getId, requestSupplyInfo.getEnterpriseId()));
        if (enterpriseInfo != null) {
            requestSupplyInfo.setPutAddress(enterpriseInfo.getAddress());
            requestSupplyInfo.setPutLongitude(enterpriseInfo.getLongitude());
            requestSupplyInfo.setPutLatitude(enterpriseInfo.getLatitude());
        }
        List<GoodsBelong> goodsBelongList = JSONUtil.toList(requestSupplyInfo.getGoods(), GoodsBelong.class);

        goodsBelongList.forEach(item -> {
            item.setCreateDate(DateUtil.formatDateTime(new Date()));
            item.setNum(requestSupplyInfo.getNum());
        });
        goodsBelongService.saveBatch(goodsBelongList);
        return R.ok(requestSupplyInfoService.save(requestSupplyInfo));
    }

    /**
     * 采购单详情-商品物流
     *
     * @param purchaseId 采购ID
     * @return 结果
     */
    @GetMapping("/detail/{purchaseId}")
    public R detailPurchase(@PathVariable("purchaseId") String purchaseId) {
        return R.ok(requestSupplyInfoService.detailPurchase(purchaseId));
    }

    /**
     * 采购入库
     *
     * @param id 采购入库
     * @return 结果
     */
    @GetMapping("/rurchasePut/{id}")
    public R rurchasePut(@PathVariable("id") Integer id) {
        RequestSupplyInfo requestSupplyInfo = requestSupplyInfoService.getById(id);
        return R.ok(requestSupplyInfoService.rurchasePut(requestSupplyInfo));
    }

    /**
     * 修改供应采购
     *
     * @param requestSupplyInfo 供应采购
     * @return 结果
     */
    @PutMapping
    public R edit(RequestSupplyInfo requestSupplyInfo) {
        return R.ok(requestSupplyInfoService.updateById(requestSupplyInfo));
    }

    /**
     * 删除供应采购
     *
     * @param ids 参数
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(requestSupplyInfoService.removeByIds(ids));
    }
}
