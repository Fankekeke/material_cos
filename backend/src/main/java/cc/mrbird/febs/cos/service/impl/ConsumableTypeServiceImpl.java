package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.ConsumableType;
import cc.mrbird.febs.cos.dao.ConsumableTypeMapper;
import cc.mrbird.febs.cos.service.IConsumableTypeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class ConsumableTypeServiceImpl extends ServiceImpl<ConsumableTypeMapper, ConsumableType> implements IConsumableTypeService {

    /**
     * 分页查询耗材类型信息
     *
     * @param page           分页对象
     * @param consumableType 耗材类别
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectConsumablePage(Page<ConsumableType> page, ConsumableType consumableType) {
        return baseMapper.selectConsumablePage(page, consumableType);
    }
}
