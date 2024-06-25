package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.UnitInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface UnitInfoMapper extends BaseMapper<UnitInfo> {

    /**
     * 分页获取计量单位
     *
     * @param page     分页对象
     * @param unitInfo 计量单位
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectUnitPage(Page<UnitInfo> page, @Param("unitInfo") UnitInfo unitInfo);
}
