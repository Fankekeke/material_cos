package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.StorehouseInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface StorehouseInfoMapper extends BaseMapper<StorehouseInfo> {

    /**
     * 分页获取库房管理
     *
     * @param page           分页对象
     * @param storehouseInfo 库房管理
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectStorePage(Page<StorehouseInfo> page, @Param("storehouseInfo") StorehouseInfo storehouseInfo);
}
