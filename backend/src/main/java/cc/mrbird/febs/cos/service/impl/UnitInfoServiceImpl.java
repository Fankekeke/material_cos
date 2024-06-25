package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.UnitInfo;
import cc.mrbird.febs.cos.dao.UnitInfoMapper;
import cc.mrbird.febs.cos.service.IUnitInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class UnitInfoServiceImpl extends ServiceImpl<UnitInfoMapper, UnitInfo> implements IUnitInfoService {

    /**
     * 分页获取计量单位
     *
     * @param page     分页对象
     * @param unitInfo 计量单位
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectUnitPage(Page<UnitInfo> page, UnitInfo unitInfo) {
        return baseMapper.selectUnitPage(page, unitInfo);
    }
}
