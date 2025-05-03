package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.RequestSupplyInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface RequestSupplyInfoMapper extends BaseMapper<RequestSupplyInfo> {

    /**
     * 分页获取供应采购
     *
     * @param page     分页对象
     * @param requestSupplyInfo 供应采购
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> querySupplyPage(Page<RequestSupplyInfo> page, @Param("requestSupplyInfo") RequestSupplyInfo requestSupplyInfo);
}
