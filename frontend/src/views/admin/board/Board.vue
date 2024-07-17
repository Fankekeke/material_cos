<template>
  <a-drawer
    placement="right"
    width="100%"
    :closable="false"
    :visible="orderShow"
    destroyOnClose
    wrapClassName="aa"
    :getContainer="false"
  >
    <a-icon type="arrow-left" style="position: absolute;z-index: 999;color: red;font-size: 20px;margin: 15px" @click="home"/>
    <dv-border-box-11 title="欢迎各位领导莅临指导" style="height: 100vh;width: 100%;background: #333333">
      <a-row :gutter="20" style="padding: 50px;padding-top: 100px;color: white" v-if="!loading">
        <a-col :span="12">
          <div class="left-chart-1">
            <div class="lc1-details" style="color: white">入库类型统计</div>
<!--            <dv-capsule-chart class="lc1-chart" :config="config" style="height: 300px"/>-->
            <dv-conical-column-chart class="lc1-chart" :config="config" style="height:300px;" />
          </div>
        </a-col>
        <a-col :span="12">
          <div class="right-chart-1">
            <div class="rc1-chart-container">
              <div class="left" style="color: white">
                <div>出库类型统计</div>
              </div>
              <dv-scroll-ranking-board class="right" :config="config2" style="height:300px" />
<!--              <dv-capsule-chart class="right" :config="config2" style="height: 300px"/>-->
            </div>
          </div>
        </a-col>
        <a-col :span="12" style="margin-top: 60px">
          <div class="left-chart-3">
            <div class="lc3-details">库房物品价格统计</div>
            <dv-capsule-chart class="lc3-chart" :config="config3" style="height: 300px"/>
          </div>
        </a-col>
        <a-col :span="12" style="margin-top: 60px">
          <div class="rc1-chart-container">
            <div class="left">
              <div>库房类型统计</div>
            </div>
            <dv-charts class="right" :option="option" style="height: 300px"/>
          </div>
        </a-col>
      </a-row>
    </dv-border-box-11>
  </a-drawer>
</template>

<script>
import LabelTag from '../component/LabelTag'
export default {
  name: 'Charts',
  components: {
    LabelTag
  },
  props: {
    orderShow: {
      type: Boolean,
      default: false
    }
  },
  watch: {
    'orderShow': function (value) {
      if (value) {
        this.selectBoard()
      }
    }
  },
  data () {
    return {
      loading: false,
      option: {
        series: [
          {
            type: 'pie',
            data: [
              { name: '来料检验', value: 93 },
              { name: '过程检验', value: 66 },
              { name: '出货检验', value: 52 }
            ],
            radius: ['45%', '65%'],
            insideLabel: {
              show: false
            },
            outsideLabel: {
              labelLineEndLength: 10,
              formatter: '{percent}%\n{name}',
              style: {
                fontSize: 14,
                fill: '#fff'
              }
            }
          }
        ],
        color: ['#00baff', '#3de7c9', '#fff', '#ffc530', '#469f4b']
      },
      config: {
        data: [
          {
            name: '收费系统',
            value: 167
          },
          {
            name: '通信系统',
            value: 67
          },
          {
            name: '监控系统',
            value: 123
          },
          {
            name: '供配电系统',
            value: 55
          },
          {
            name: '其他',
            value: 98
          }
        ],
        colors: ['#00baff', '#3de7c9', '#fff', '#ffc530', '#469f4b'],
        unit: '件'
      },
      config2: {
        data: [
          {
            name: '收费系统',
            value: 25
          },
          {
            name: '通信系统',
            value: 66
          },
          {
            name: '监控系统',
            value: 123
          },
          {
            name: '供配电系统',
            value: 72
          },
          {
            name: '其他',
            value: 99
          }
        ],
        unit: '件'
      },
      config3: {
        data: [
          {
            name: '收费系统',
            value: 167
          },
          {
            name: '通信系统',
            value: 67
          },
          {
            name: '监控系统',
            value: 123
          },
          {
            name: '供配电系统',
            value: 55
          },
          {
            name: '其他',
            value: 98
          }
        ],
        colors: ['#00baff', '#3de7c9', '#fff', '#ffc530', '#469f4b'],
        unit: '件'
      }
    }
  },
  methods: {
    selectBoard () {
      this.loading = true
      this.$get(`/cos/stock-info/selectBoard`).then((r) => {
        this.option.series[0].data = r.data.type
        // let incomeDate = []
        // let incomeItem1 = {name: '接收数量', value: r.data.incomeReceive}
        // let incomeItem2 = {name: '检测数量', value: r.data.incomeTotal}
        // let incomeItem3 = {name: '合格数量', value: r.data.incomeQualified}
        // let incomeItem4 = {name: '不合格数量', value: r.data.incomeNoQualified}
        // incomeDate.push(incomeItem1)
        // incomeDate.push(incomeItem2)
        // incomeDate.push(incomeItem3)
        // incomeDate.push(incomeItem4)
        this.config.data = r.data.put

        // let processDate = []
        // let processItem1 = {name: '接收数量', value: r.data.processReceive}
        // let processItem2 = {name: '检测数量', value: r.data.processTotal}
        // let processItem3 = {name: '合格数量', value: r.data.processQualified}
        // let processItem4 = {name: '不合格数量', value: r.data.processNoQualified}
        // processDate.push(processItem1)
        // processDate.push(processItem2)
        // processDate.push(processItem3)
        // processDate.push(processItem4)
        this.config2.data = r.data.out

        // let shipDate = []
        // let shipItem1 = {name: '接收数量', value: r.data.shipReceive}
        // let shipItem2 = {name: '检测数量', value: r.data.shipTotal}
        // let shipItem3 = {name: '合格数量', value: r.data.shipQualified}
        // let shipItem4 = {name: '不合格数量', value: r.data.shipNoQualified}
        // shipDate.push(shipItem1)
        // shipDate.push(shipItem2)
        // shipDate.push(shipItem3)
        // shipDate.push(shipItem4)
        this.config3.data = r.data.price

        // let rateDate = []
        // let rateItem1 = {name: '来料检验', value: r.data.incomeQualified}
        // let rateItem2 = {name: '过程检验', value: r.data.processQualified}
        // let rateItem3 = {name: '出货检验', value: r.data.shipQualified}
        // rateDate.push(rateItem1)
        // rateDate.push(rateItem2)
        // rateDate.push(rateItem3)
        setTimeout(() => {
          this.loading = false
        }, 200)
      })
    },
    home () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>
>>> .ant-drawer-body {
  padding: 0 !important;
}
</style>
