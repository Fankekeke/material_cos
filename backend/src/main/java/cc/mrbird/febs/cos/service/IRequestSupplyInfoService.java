package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.RequestSupplyInfo;
import cc.mrbird.febs.cos.entity.RurchaseRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IRequestSupplyInfoService extends IService<RequestSupplyInfo> {

    /**
     * 分页获取供应采购
     *
     * @param page     分页对象
     * @param requestSupplyInfo 供应采购
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> querySupplyPage(Page<RequestSupplyInfo> page, RequestSupplyInfo requestSupplyInfo);

    /**
     * 添加供应采购
     *
     * @param requestSupplyInfo 采购信息
     * @return 结果
     */
    Boolean rurchasePut(RequestSupplyInfo requestSupplyInfo);

    /**
     * 采购单详情-商品物流
     *
     * @param id 采购ID
     * @return 结果
     */
    LinkedHashMap<String, Object> detailPurchase(String id);
}
