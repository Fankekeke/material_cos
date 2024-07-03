<template>
  <div>
    <a-row style="margin-top: 15px">
      <a-col :span="24">
        <div style="background: #ECECEC; padding: 30px;">
          <a-row :gutter="16">
            <a-col :span="6">
              <a-card hoverable>
                <a-row>
                  <a-col :span="24" style="font-size: 13px;margin-bottom: 8px;font-family: SimHei">本月检测数量/单</a-col>
                  <a-col :span="4"><a-icon type="arrow-up" style="font-size: 30px;margin-top: 3px"/></a-col>
                  <a-col :span="18" style="font-size: 28px;font-weight: 500;font-family: SimHei">
                    {{ titleData.incomeMonth }}
                    <span style="font-size: 20px;margin-top: 3px">元</span>
                  </a-col>
                </a-row>
              </a-card>
            </a-col>
            <a-col :span="6">
              <a-card hoverable>
                <a-row>
                  <a-col :span="24" style="font-size: 13px;margin-bottom: 8px;font-family: SimHei">本月通过单数</a-col>
                  <a-col :span="4"><a-icon type="arrow-up" style="font-size: 30px;margin-top: 3px"/></a-col>
                  <a-col :span="18" style="font-size: 28px;font-weight: 500;font-family: SimHei">
                    {{ titleData.workOrderMonth }}
                    <span style="font-size: 20px;margin-top: 3px">单</span>
                  </a-col>
                </a-row>
              </a-card>
            </a-col>
            <a-col :span="6">
              <a-card hoverable>
                <a-row>
                  <a-col :span="24" style="font-size: 13px;margin-bottom: 8px;font-family: SimHei">本年检测数量/单</a-col>
                  <a-col :span="4"><a-icon type="arrow-up" style="font-size: 30px;margin-top: 3px"/></a-col>
                  <a-col :span="18" style="font-size: 28px;font-weight: 500;font-family: SimHei">
                    {{ titleData.incomeYear }}
                    <span style="font-size: 20px;margin-top: 3px">单</span>
                  </a-col>
                </a-row>
              </a-card>
            </a-col>
            <a-col :span="6">
              <a-card hoverable>
                <a-row>
                  <a-col :span="24" style="font-size: 13px;margin-bottom: 8px;font-family: SimHei">本年通过单数</a-col>
                  <a-col :span="4"><a-icon type="arrow-up" style="font-size: 30px;margin-top: 3px"/></a-col>
                  <a-col :span="18" style="font-size: 28px;font-weight: 500;font-family: SimHei">
                    {{ titleData.workOrderYear }}
                    <span style="font-size: 20px;margin-top: 3px">单</span>
                  </a-col>
                </a-row>
              </a-card>
            </a-col>
          </a-row>
        </div>
      </a-col>
    </a-row>
    <a @click="orderMapOpen">统计看板</a>
    <a-row style="margin-top: 15px" :gutter="16">
      <a-col :span="12">
        <a-card hoverable :bordered="false" style="width: 100%">
          <a-skeleton active v-if="loading" />
          <apexchart  v-if="!loading" type="area"  height="300" :options="chartOptions" :series="series"></apexchart>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card hoverable :bordered="false" style="width: 100%">
          <a-skeleton active v-if="loading" />
          <apexchart v-if="!loading" type="area"  height="300" :options="chartOptions1" :series="series1"></apexchart>
        </a-card>
      </a-col>
    </a-row>
    <board
      @close="handleorderMapViewClose"
      :orderShow="orderMapView.visiable">
    </board>
    <a-row style="margin-top: 15px">
<!--      <a-col :span="9">-->
<!--        <a-card hoverable :bordered="false" style="width: 100%">-->
<!--          <a-skeleton active v-if="loading" />-->
<!--          <apexchart v-if="!loading" type="donut" height="270" :options="chartOptions2" :series="series2"></apexchart>-->
<!--        </a-card>-->
<!--      </a-col>-->
<!--      <a-col :span="15">-->
<!--        <a-card hoverable :bordered="false" style="width: 100%">-->
<!--          <a-skeleton active v-if="loading" />-->
<!--          <apexchart v-if="!loading" type="scatter" height="300" :options="chartOptions3" :series="series3"></apexchart>-->
<!--        </a-card>-->
<!--      </a-col>-->
    </a-row>
<!--    <a-row style="margin-top: 15px">-->
<!--      <a-col :span="12">-->
<!--        <a-card hoverable :loading="loading" :bordered="false" title="公告信息" style="margin-top: 15px">-->
<!--          <div style="padding: 0 22px">-->
<!--            <a-list item-layout="vertical" :pagination="pagination" :data-source="bulletinList">-->
<!--              <a-list-item slot="renderItem" key="item.title" slot-scope="item, index">-->
<!--                <template slot="actions">-->
<!--              <span key="message">-->
<!--                <a-icon type="message" style="margin-right: 8px" />-->
<!--                {{ item.date }}-->
<!--              </span>-->
<!--                </template>-->
<!--                <a-list-item-meta :description="item.content" style="font-size: 14px">-->
<!--                  <a slot="title">{{ item.title }}</a>-->
<!--                </a-list-item-meta>-->
<!--              </a-list-item>-->
<!--            </a-list>-->
<!--          </div>-->
<!--        </a-card>-->
<!--      </a-col>-->
<!--    </a-row>-->
  </div>
</template>

<script>
import board from '../../board/Board.vue'
import {mapState} from 'vuex'
export default {
  name: 'Home',
  computed: {
    ...mapState({
      multipage: state => state.setting.multipage,
      user: state => state.account.user
    })
  },
  components: {board},
  data () {
    return {
      orderMapView: {
        visiable: false,
        data: null
      },
      pagination: {
        onChange: page => {
          console.log(page)
        },
        pageSize: 2
      },
      bulletinList: [],
      titleData: {
        incomeMonth: 0,
        workOrderMonth: 0,
        incomeYear: 0,
        workOrderYear: 0
      },
      loading: false,
      series: [{
        name: '收益',
        data: []
      }],
      chartOptions: {
        chart: {
          type: 'area',
          height: 300
        },
        xaxis: {
          categories: []
        },
        stroke: {
          curve: 'stepline'
        },
        dataLabels: {
          enabled: false
        },
        title: {
          text: '近十天检测统计',
          align: 'left'
        },
        markers: {
          hover: {
            sizeOffset: 4
          }
        }
      },
      series1: [],
      chartOptions1: {
        chart: {
          type: 'area',
          height: 300
        },
        title: {
          text: '近十天通过统计',
          align: 'left'
        },
        plotOptions: {
          bar: {
            horizontal: false,
            columnWidth: '55%'
          }
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          show: true,
          width: 2,
          colors: ['transparent']
        },
        xaxis: {
          categories: []
        },
        yaxis: {
          title: {
            text: ''
          }
        },
        fill: {
          opacity: 1
        },
        tooltip: {
          y: {
            formatter: function (val) {
              return val + ' 单'
            }
          }
        }
      },
      series2: [],
      chartOptions2: {
        chart: {
          type: 'donut',
          height: 300
        },
        labels: ['整租', '合租'],
        title: {
          text: '工单服务类型统计',
          align: 'left'
        },
        responsive: [{
          breakpoint: 380,
          options: {
            chart: {
              width: 200
            },
            legend: {
              position: 'bottom'
            }
          }
        }]
      },
      series3: [{
        name: 'SAMPLE A',
        data: [
          [16.4, 5.4], [21.7, 2], [25.4, 3], [19, 2], [10.9, 1], [13.6, 3.2], [10.9, 7.4], [10.9, 0], [10.9, 8.2], [16.4, 0], [16.4, 1.8], [13.6, 0.3], [13.6, 0], [29.9, 0], [27.1, 2.3], [16.4, 0], [13.6, 3.7], [10.9, 5.2], [16.4, 6.5], [10.9, 0], [24.5, 7.1], [10.9, 0], [8.1, 4.7], [19, 0], [21.7, 1.8], [27.1, 0], [24.5, 0], [27.1, 0], [29.9, 1.5], [27.1, 0.8], [22.1, 2]]
      }, {
        name: 'SAMPLE B',
        data: [
          [36.4, 13.4], [1.7, 11], [5.4, 8], [9, 17], [1.9, 4], [3.6, 12.2], [1.9, 14.4], [1.9, 9], [1.9, 13.2], [1.4, 7], [6.4, 8.8], [3.6, 4.3], [1.6, 10], [9.9, 2], [7.1, 15], [1.4, 0], [3.6, 13.7], [1.9, 15.2], [6.4, 16.5], [0.9, 10], [4.5, 17.1], [10.9, 10], [0.1, 14.7], [9, 10], [12.7, 11.8], [2.1, 10], [2.5, 10], [27.1, 10], [2.9, 11.5], [7.1, 10.8], [2.1, 12]]
      }, {
        name: 'SAMPLE C',
        data: [
          [21.7, 3], [23.6, 3.5], [24.6, 3], [29.9, 3], [21.7, 20], [23, 2], [10.9, 3], [28, 4], [27.1, 0.3], [16.4, 4], [13.6, 0], [19, 5], [22.4, 3], [24.5, 3], [32.6, 3], [27.1, 4], [29.6, 6], [31.6, 8], [21.6, 5], [20.9, 4], [22.4, 0], [32.6, 10.3], [29.7, 20.8], [24.5, 0.8], [21.4, 0], [21.7, 6.9], [28.6, 7.7], [15.4, 0], [18.1, 0], [33.4, 0], [16.4, 0]]
      }],
      chartOptions3: {
        chart: {
          height: 350,
          type: 'scatter',
          zoom: {
            enabled: true,
            type: 'xy'
          }
        },
        title: {
          text: '近十天收入统计',
          align: 'left'
        },
        xaxis: {
          tickAmount: 10,
          labels: {
            formatter: function (val) {
              return parseFloat(val).toFixed(1)
            }
          }
        },
        yaxis: {
          tickAmount: 7
        }
      }
    }
  },
  mounted () {
    this.loading = true
    this.selectHomeData()
    setTimeout(() => {
      this.loading = false
    }, 200)
  },
  methods: {
    orderMapOpen () {
      this.orderMapView.visiable = true
    },
    handleorderMapViewClose () {
      this.orderMapView.visiable = false
    },
    selectHomeData () {
      this.$get('/cos/materiel-info/home/data').then((r) => {
        let titleData = { staffNum: r.data.staffNum, clienteleNum: r.data.clienteleNum, stationNum: r.data.stationNum, materielNum: r.data.materielNum }
        this.$emit('setTitle', titleData)
        this.titleData.incomeMonth = r.data.incomeMonth
        this.titleData.workOrderMonth = r.data.workOrderMonth
        this.titleData.incomeYear = r.data.incomeYear
        this.titleData.workOrderYear = r.data.workOrderYear
        let series = []
        let values1 = []
        let values2 = []
        let values3 = []
        r.data.paymentRecord1.forEach(obj => {
          values1.push({x: obj.days, y: obj.amount})
        })
        let itemData1 = { name: '来料检测', data: values1 }
        r.data.paymentRecord2.forEach(obj => {
          values2.push({x: obj.days, y: obj.amount})
        })
        let itemData2 = { name: '过程检测', data: values2 }
        r.data.paymentRecord3.forEach(obj => {
          values3.push({x: obj.days, y: obj.amount})
        })
        let itemData3 = { name: '出货检测', data: values3 }
        series.push(itemData1)
        series.push(itemData2)
        series.push(itemData3)
        this.series = series

        let series1 = []
        let values4 = []
        let values5 = []
        let values6 = []
        r.data.orderRecord1.forEach(obj => {
          values4.push({x: obj.days, y: obj.amount})
        })
        let itemData4 = { name: '来料检测', data: values4 }
        r.data.orderRecord2.forEach(obj => {
          values5.push({x: obj.days, y: obj.amount})
        })
        let itemData5 = { name: '过程检测', data: values5 }
        r.data.orderRecord3.forEach(obj => {
          values6.push({x: obj.days, y: obj.amount})
        })
        let itemData6 = { name: '出货检测', data: values6 }
        series1.push(itemData4)
        series1.push(itemData5)
        series1.push(itemData6)
        this.series1 = series1
      })
    }
  }
}
</script>

<style scoped>

</style>
