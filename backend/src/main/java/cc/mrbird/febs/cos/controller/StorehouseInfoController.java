package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.StorehouseInfo;
import cc.mrbird.febs.cos.service.IStorehouseInfoService;
import cn.hutool.core.date.DateUtil;
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
@RequestMapping("/cos/storehouse-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StorehouseInfoController {

    private final IStorehouseInfoService storehouseInfoService;

    /**
     * 分页获取库房管理
     *
     * @param page           分页对象
     * @param storehouseInfo 库房管理
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<StorehouseInfo> page, StorehouseInfo storehouseInfo) {
        return R.ok(storehouseInfoService.selectStorePage(page, storehouseInfo));
    }

    /**
     * 盘库
     */
    @GetMapping("/replenishment")
    public R replenishment() {
        storehouseInfoService.diskLibrary();
        return R.ok(true);
    }

    /**
     * 新增库房管理
     *
     * @param storehouseInfo 库房管理
     * @return 结果
     */
    @PostMapping
    public R save(StorehouseInfo storehouseInfo) {
        storehouseInfo.setCode("SH-" + System.currentTimeMillis());
        storehouseInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(storehouseInfoService.save(storehouseInfo));
    }

    /**
     * 修改库房管理
     *
     * @param storehouseInfo 库房管理
     * @return 结果
     */
    @PutMapping
    public R edit(StorehouseInfo storehouseInfo) {
        return R.ok(storehouseInfoService.updateById(storehouseInfo));
    }

    /**
     * 删除库房管理
     *
     * @param ids 参数
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(storehouseInfoService.removeByIds(ids));
    }
}
