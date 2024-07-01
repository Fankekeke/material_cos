package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.UnitInfo;
import cc.mrbird.febs.cos.service.IUnitInfoService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/unit-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UnitInfoController {

    private final IUnitInfoService unitInfoService;

    /**
     * 分页获取计量单位
     *
     * @param page     分页对象
     * @param unitInfo 计量单位
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<UnitInfo> page, UnitInfo unitInfo) {
        return R.ok(unitInfoService.selectUnitPage(page, unitInfo));
    }

    /**
     * 新增计量单位
     *
     * @param unitInfo 计量单位
     * @return 结果
     */
    @PostMapping
    public R save(UnitInfo unitInfo) {
        unitInfo.setCode("UN-" + System.currentTimeMillis());
        unitInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(unitInfoService.save(unitInfo));
    }

    /**
     * 修改计量单位
     *
     * @param unitInfo 计量单位
     * @return 结果
     */
    @PutMapping
    public R edit(UnitInfo unitInfo) {
        return R.ok(unitInfoService.updateById(unitInfo));
    }

    /**
     * 删除计量单位
     *
     * @param ids 参数
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(unitInfoService.removeByIds(ids));
    }
}
