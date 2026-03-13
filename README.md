### 基于SpringBoot + Vue的仓储管理系统.

仓库管理系统、智能仓储平台、库存管理信息系统

#### 管理员功能模块介绍：
###### 库房信息：维护仓库名称、位置、容量及负责人等基础资料。入库管理：审核并记录物资到货后的入库操作与数据。库房物品：查看和管理所有库存物资的实时信息与状态。出库管理：审批并执行物资领用或调拨的出库流程。出入库明细：查询所有物资的详细进出库时间、数量及经办人。采购组合：配置常用物资采购套餐，提升采购效率。计星单位：（应为“计量单位”）管理物资的单位如件、箱、千克等。物资盘点：组织定期盘点，核对账面与实际库存差异。物品类型：分类管理物资，如办公用品、设备、耗材等。公告信息：发布仓储政策、盘点通知或系统重要提醒。申请审批：审核员工提交的物资领用或采购申请。供应商管理：维护合作供应商信息、资质及供货记录。角色管理：配置不同岗位的操作权限与系统角色。用户管理：管理所有员工账号、部门及登录权限。物资采购：发起、跟踪并完成物资采购全流程。出库单&入库单下载：生成并导出标准化出入库单据用于存档或财务。数据大屏：可视化展示库存总量、周转率、预警物资等核心指标。人脸识别登录：支持通过人脸验证安全登录系统。

#### 员工功能模块介绍：
###### 库房信息：查看可用仓库位置及基本信息，便于申请物资。采购单入库：根据采购订单确认到货并协助完成入库登记。库房入库：执行或上报物资入库操作，更新库存数据。

#### 库房管理员功能模块介绍：
###### 用户信息变更：协助修改本库房相关用户的基础信息。物品申请：代为提交或处理本部门物资领用申请。申请记录：查看历史物资申请及审批状态。出库单下载：下载已审批的出库单据用于配送或财务核对。

#### 安装环境

JAVA 环境 

Node.js环境 [https://nodejs.org/en/] 选择14.17

Yarn 打开cmd， 输入npm install -g yarn !!!必须安装完毕nodejs

Mysql 数据库 [https://blog.csdn.net/qq_40303031/article/details/88935262] 一定要把账户和密码记住

redis

Idea 编译器 [https://blog.csdn.net/weixin_44505194/article/details/104452880]

WebStorm OR VScode 编译器 [https://www.jianshu.com/p/d63b5bae9dff]

#### 采用技术及功能

后端：SpringBoot、MybatisPlus、MySQL、Redis、
前端：Vue、Apex、Antd、Axios
报表：Spread.js

平台前端：vue(框架) + vuex(全局缓存) + rue-router(路由) + axios(请求插件) + apex(图表)  + antd-ui(ui组件)

平台后台：springboot(框架) + redis(缓存中间件) + shiro(权限中间件) + mybatisplus(orm) + restful风格接口 + mysql(数据库)

开发环境：windows10 or windows7 ， vscode or webstorm ， idea + lambok

库房管理，库房申请，采购管理，采购申请，物品审批，入库管理，库房物品，出库管理，出入库明细，用户管理，采购组合，公告信息，个人信息，报表统计，物资类别，出入库物品明细，数据大屏，库房盘库，审批管理，公告管理，供应商管理，管理员设置，计量单位，人脸识别登录

使用spring boot + vue开发的仓库管理系统，系统用户分为三类：管理员、员工、库房管理员
管理员：库房信息，入库管理，库房物品，出库管理，出入库明细，采购组合，计星单位，物资盘点，物品类型，公告信息，申请审批，供应商管理，角色管理，用户管理，物资采购，出库单&入库单下载，数据大屏，人脸识别登录
员工：库房信息，采购单入库，库房入库
库房管理员：用户信息变更，物品申请，申请记录，出库单下载

#### 前台启动方式
安装所需文件 yarn install 
运行 yarn run dev

#### 默认后台账户密码
[管理员]
admin
1234qwer

[采购员]
caigou
1234qwer

[用户]
lisi
1234qwer
#### 项目截图

|  |  |
|---------------------|---------------------|
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1720362134765.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1720361808758.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1720362090923.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1720361786585.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1720362079784.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1720361742660.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1720361964051.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1720361728040.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1720361927539.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1720361716264.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1720361904967.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1720361703707.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1720361890943.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1720361688801.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1720361854151.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1720361674818.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1720361841423.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1720361663466.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1720361826094.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1720361617192.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/work/936e9baf53eb9a217af4f89c616dc19.png) |

#### 演示视频

暂无

#### 获取方式

Email: fan1ke2ke@gmail.com

WeChat: `Storm_Berserker`

`附带部署与讲解服务，因为要恰饭资源非免费，伸手党勿扰，谢谢理解😭`

> 1.项目纯原创，不做二手贩子 2.一次购买终身有效 3.项目讲解持续到答辩结束 4.非常负责的答辩指导 5.**黑奴价格**

> 项目部署调试不好包退！功能逻辑没讲明白包退！

#### 其它资源

[2025年-答辩顺利通过-客户评价🍜](https://berserker287.github.io/2025/06/18/2025%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2024年-答辩顺利通过-客户评价👻](https://berserker287.github.io/2024/06/06/2024%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2023年-答辩顺利通过-客户评价🐢](https://berserker287.github.io/2023/06/14/2023%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2022年-答辩通过率100%-客户评价🐣](https://berserker287.github.io/2022/05/25/%E9%A1%B9%E7%9B%AE%E4%BA%A4%E6%98%93%E8%AE%B0%E5%BD%95/)

[毕业答辩导师提问的高频问题](https://berserker287.github.io/2023/06/13/%E6%AF%95%E4%B8%9A%E7%AD%94%E8%BE%A9%E5%AF%BC%E5%B8%88%E6%8F%90%E9%97%AE%E7%9A%84%E9%AB%98%E9%A2%91%E9%97%AE%E9%A2%98/)

[50个高频答辩问题-技术篇](https://berserker287.github.io/2023/06/13/50%E4%B8%AA%E9%AB%98%E9%A2%91%E7%AD%94%E8%BE%A9%E9%97%AE%E9%A2%98-%E6%8A%80%E6%9C%AF%E7%AF%87/)

[计算机毕设答辩时都会问到哪些问题？](https://www.zhihu.com/question/31020988)

[计算机专业毕业答辩小tips](https://zhuanlan.zhihu.com/p/145911029)

#### 接JAVAWEB毕设，纯原创，价格公道，诚信第一

`网站建设、小程序、H5、APP、各种系统 选题+开题报告+任务书+程序定制+安装调试+项目讲解+论文+答辩PPT`

More info: [悲伤的橘子树](https://berserker287.github.io/)

<p><img align="center" src="https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/%E5%90%88%E4%BD%9C%E7%89%A9%E6%96%99%E6%A0%B7%E5%BC%8F%20(3).png" alt="fankekeke" /></p>
