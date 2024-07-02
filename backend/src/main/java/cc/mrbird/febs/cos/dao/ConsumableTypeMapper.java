package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.ConsumableType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface ConsumableTypeMapper extends BaseMapper<ConsumableType> {


    /**
     * 分页查询耗材类型信息
     *
     * @param page           分页对象
     * @param consumableType 耗材类别
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectConsumablePage(Page<ConsumableType> page, @Param("consumableType") ConsumableType consumableType);
}
