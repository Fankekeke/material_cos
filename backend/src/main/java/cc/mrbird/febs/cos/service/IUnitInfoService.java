package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.UnitInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IUnitInfoService extends IService<UnitInfo> {

    /**
     * 分页获取计量单位
     *
     * @param page     分页对象
     * @param unitInfo 计量单位
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectUnitPage(Page<UnitInfo> page, UnitInfo unitInfo);
}
