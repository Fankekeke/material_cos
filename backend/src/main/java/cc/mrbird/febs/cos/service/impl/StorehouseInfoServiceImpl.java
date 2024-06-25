package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.StorehouseInfo;
import cc.mrbird.febs.cos.dao.StorehouseInfoMapper;
import cc.mrbird.febs.cos.service.IStorehouseInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class StorehouseInfoServiceImpl extends ServiceImpl<StorehouseInfoMapper, StorehouseInfo> implements IStorehouseInfoService {

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
}
