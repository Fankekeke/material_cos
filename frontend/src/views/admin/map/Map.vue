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
    <div style="width: 100%">
      <a-icon type="arrow-left" style="position: absolute;z-index: 999;color: red;font-size: 20px;margin: 15px" @click="home"/>
      <a-row style="height:100vh;font-family: SimHei">
        <a-col :span="15" style="height: 100%;">
          <div id="areas" style="width: 100%;height: 100%;box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);background:#ec9e3c;color:#fff"></div>
        </a-col>
        <a-col :span="9" style="height: 100%;box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);color:#fff">
          <div>
            <div class="scenicInfo" style="height: 100vh; overflow-y: auto;padding-left: 5px;overflow-x: hidden">
              <div style="font-size: 13px">
                <div style="padding-left: 24px;padding-right: 24px;margin-bottom: 50px;margin-top: 50px">
                  <a-steps :current="current" progress-dot size="small">
                    <a-step title="已提交" />
                    <a-step title="运输中" />
                    <a-step title="已验收" />
                  </a-steps>
                </div>
                <br/>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;" :gutter="15">
                  <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">物品详情</span></a-col>
                  <a-col :span="24">
                    <a-table :columns="columns" :data-source="goodsList">
                    </a-table>
                  </a-col>
                </a-row>
                <br/>
                <div v-if="orderData !== null" style="font-size: 13px;color: black">
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
                    <a-col :span="12" ><b>采购单：</b>
                      <span>{{ orderData.num }}</span>
                    </a-col>
                    <a-col :span="12"><b>预计价格：</b>
                      {{ orderData.price }} 元
                    </a-col>
                    <br/>
                    <br/>
                    <a-col :span="12"><b>供应商：</b>
                      {{ orderData.name }}
                    </a-col>
                  </a-row>
                  <br/>
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col :span="12"><b>当前状态：</b>
                      <span v-if="orderData.status == 1">运输中</span>
                      <span v-if="orderData.status == 2">已验收</span>
                    </a-col>
                    <a-col :span="12"><b>备注信息：</b>
                      {{ orderData.content }}
                    </a-col>
                    <br/>
                    <br/>
                    <a-col :span="12"><b>采购时间：</b>
                      {{ orderData.createDate }}
                    </a-col>
                  </a-row>
                  <br/>
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col :span="12"><b>供应地址：</b>
                      {{ orderData.putAddress }}
                    </a-col>
                    <a-col :span="12"><b>采购地址：</b>
                      {{ orderData.outAddress }}
                    </a-col>
                  </a-row>
                </div>
              </div>
            </div>
          </div>
        </a-col>
      </a-row>
    </div>
  </a-drawer>
</template>

<script>
import baiduMap from '@/utils/map/baiduMap'
export default {
  name: 'Map',
  props: {
    orderShow: {
      type: Boolean,
      default: false
    },
    orderData: {
      type: Object
    }
  },
  data () {
    return {
      current: 0,
      userInfo: null,
      orderInfo: null,
      merchantInfo: null,
      orderItemInfo: [],
      addressInfo: null,
      staffInfo: null,
      evaluateInfo: null,
      staffList: [],
      communityRent: 0,
      rentShow: false,
      communityShow: false,
      communityData: null,
      checkedList: ['Apple', 'Orange'],
      indeterminate: true,
      checkAll: false,
      plainOptions: ['Apple', 'Pear', 'Orange'],
      visible: false,
      rentList: [],
      communityList: [],
      community: null,
      nowPoint: null,
      roadData: [],
      goodsList: [],
      checkLoading: false,
      echartsShow: false,
      getShop: null,
      putShop: null,
      series: [{
        name: '得分',
        data: []
      }],
      chartOptions: {
        chart: {
          height: 380,
          type: 'radar'
        },
        title: {
          text: ''
        },
        xaxis: {
          categories: ['交付得分', '价格得分', '质量得分', '准时得分', '服务得分', '综合得分']
        }
      }
    }
  },
  computed: {
    columns () {
      return [{
        title: '物品名称',
        dataIndex: 'name'
      }, {
        title: '型号',
        dataIndex: 'type'
      }, {
        title: '数量',
        dataIndex: 'amount'
      }, {
        title: '所属类型',
        dataIndex: 'consumableName'
      }, {
        title: '单位',
        dataIndex: 'unit'
      }, {
        title: '单价',
        dataIndex: 'price'
      }]
    }
  },
  watch: {
    'orderShow': function (value) {
      if (value) {
        this.dataInit(this.orderData.num)
      }
    }
  },
  methods: {
    dataInit (num) {
      this.$get('/cos/goods-belong/getGoodsByNum', { num }).then((r) => {
        this.goodsList = r.data.data
        if (this.orderData.status == 1) {
          this.current = 1
        }
        if (this.orderData.status == 2) {
          this.current = 2
        }
        setTimeout(() => {
          baiduMap.initMap('areas')
          this.getLocal()
          this.navigation(this.orderData)
        }, 200)
      })
    },
    navigation (orderData) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions: {map: baiduMap.rMap(), autoViewport: true}})
      // eslint-disable-next-line no-undef
      driving.search(new BMap.Point(orderData.putLongitude, orderData.putLatitude), new BMap.Point(orderData.outLongitude, orderData.outLatitude))
      // this.getRoadData()
    },
    getRoadData () {
      let options = {
        onSearchComplete: results => {
          // eslint-disable-next-line eqeqeq,no-undef
          if (driving.getStatus() == BMAP_STATUS_SUCCESS) {
            // 获取第一条方案
            let plan = results.getPlan(0)
            // 获取方案的驾车线路
            // eslint-disable-next-line no-unused-vars
            let route = plan.getRoute(0)
            // 获取每个关键步骤,并输出到页面
            let s = []
            for (let j = 0; j < plan.getNumRoutes(); j++) {
              let route = plan.getRoute(j)
              for (let i = 0; i < route.getNumSteps(); i++) {
                let step = route.getStep(i)
                s.push((i + 1) + '. ' + step.getDescription())
              }
            }
            this.roadData = s
          }
        }
      }
      // eslint-disable-next-line no-undef
      let driving = new BMap.DrivingRoute(baiduMap.rMap(), options)
      // eslint-disable-next-line no-undef
      let start = new BMap.Point(this.nowPoint.lng, this.nowPoint.lat)
      let end = null
      if (this.rentShow) {
        end = new BMap.Point(this.rentData.longitude, this.rentData.latitude)
      } else {
        end = new BMap.Point(this.communityData.longitude, this.communityData.latitude)
      }
      // eslint-disable-next-line no-undef
      driving.search(start, end)
    },
    checkEvaluate (score) {
      let data = [score.deliverScore, score.priceScore, score.qualityScore, score.scheduleScore, score.serviceScore, score.overScore, 100]
      this.series[0].data = data
    },
    home () {
      this.$emit('close')
    },
    gisOnChange (e) {
      let data = null
      switch (e.target.value) {
        case '1':
          data = this.getShop
          break
        case '2':
          data = this.putShop
          break
      }
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions: {map: baiduMap.rMap(), autoViewport: true}})
      // eslint-disable-next-line no-undef
      driving.search(new BMap.Point(this.nowPoint.lng, this.nowPoint.lat), new BMap.Point(data.longitude, data.latitude))
    },
    getLocal () {
      // eslint-disable-next-line no-undef
      let geolocation = new BMap.Geolocation()
      geolocation.getCurrentPosition(r => {
        this.nowPoint = r.point
      }, {enableHighAccuracy: true})
    }
  }
}
</script>

<style scoped>
  >>> .ant-drawer-body {
    padding: 0 !important;
  }
  >>> .ant-card-meta-title {
    font-size: 13px;
    font-family: SimHei;
  }
  >>> .ant-card-meta-description {
    font-size: 13px;
    font-family: SimHei;
  }
  >>> .ant-divider-with-text-left {
    margin: 0;
  }

  >>> .ant-card-head-title {
    font-size: 13px;
    font-family: SimHei;
  }
  >>> .ant-card-extra {
    font-size: 13px;
    font-family: SimHei;
  }
  >>> .ant-radio-button-wrapper {
    border-radius: 0;
  }
</style>
