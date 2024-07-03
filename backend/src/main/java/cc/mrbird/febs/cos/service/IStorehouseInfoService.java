package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.StorehouseInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IStorehouseInfoService extends IService<StorehouseInfo> {

    /**
     * 分页获取库房管理
     *
     * @param page           分页对象
     * @param storehouseInfo 库房管理
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectStorePage(Page<StorehouseInfo> page, StorehouseInfo storehouseInfo);

    /**
     * 任务盘库
     */
    void diskLibrary();
}
