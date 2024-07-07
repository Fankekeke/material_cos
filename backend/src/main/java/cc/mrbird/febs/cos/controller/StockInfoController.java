package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.FileDownloadUtils;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.StockInfo;
import cc.mrbird.febs.cos.service.IStockInfoService;
import cc.mrbird.febs.cos.service.IStockPutService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/stock-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StockInfoController {

    private final IStockInfoService stockInfoService;

    private final IStockPutService stockPutService;

    /**
     * 获取主页信息
     *
     * @param type
     * @return
     */
    @GetMapping("/home")
    public R home(Integer type, Integer userId) {
        return R.ok(stockInfoService.home(type, userId));
    }

    /**
     * 下载模板
     */
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) {
        try {
            FileDownloadUtils.downloadTemplate(response, "导入模板.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入信息列表
     */
    @PostMapping("/import")
    public R importExcel(@RequestParam("file") MultipartFile file) {
        try {
            String errorMsg = stockInfoService.importExcel(file);
            if (StrUtil.isNotEmpty(errorMsg)) {
                return R.error(errorMsg);
            }
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.error("导入异常");
    }

    /**
     * 根号用户ID获取领取耗材
     *
     * @param stockInfo
     * @return
     */
    @GetMapping("/getGoodsPutByUserId")
    public R getGoodsPutByUserId(Page page, StockInfo stockInfo) {
        return R.ok(stockInfoService.getGoodsPutByUserId(page, stockInfo));
    }

    /**
     * 入库
     *
     * @param goods
     * @param custodian
     * @param putUser
     * @param content
     * @return
     */
    @PostMapping("/put")
    public R put(String goods, String custodian, String putUser, String content, BigDecimal price) {
        return R.ok(stockInfoService.stockPut(goods, custodian, putUser, content, price));
    }

    /**
     * 分页获取物品出入库详情
     *
     * @param page
     * @param stockInfo
     * @return
     */
    @GetMapping("/detail/page")
    public R stockInfoDetailPage(Page page, StockInfo stockInfo) {
        return R.ok(stockInfoService.stockInfoDetailPage(page, stockInfo));
    }

    /**
     * 分页获取库房信息
     *
     * @param page
     * @param stockInfo
     * @return
     */
    @GetMapping("/page")
    public R page(Page page, StockInfo stockInfo) {
        return R.ok(stockInfoService.stockInfoByPage(page, stockInfo));
    }

    /**
     * 分页获取库房信息
     *
     * @param stockInfo
     * @return
     */
    @GetMapping("/list")
    public R page(StockInfo stockInfo) {
        return R.ok(stockInfoService.stockInfoByList(stockInfo));
    }

    /**
     * 统计看板查询
     *
     * @return 结果
     */
    @GetMapping("/selectBoard")
    public R selectBoard() {
        return R.ok(stockInfoService.selectBoard());
    }

    /**
     * 添加库房信息
     *
     * @param stockInfo
     * @return
     */
    @PostMapping
    public R save(StockInfo stockInfo) {
        return R.ok();
    }


}
