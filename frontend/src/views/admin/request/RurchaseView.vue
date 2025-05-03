<template>
  <a-modal v-model="show" title="供应采购信息" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px" v-if="rurchaseData !== null">
      <div style="padding-left: 24px;padding-right: 24px;margin-bottom: 50px;margin-top: 50px">
        <a-steps :current="current" progress-dot size="small">
          <a-step title="已提交" />
          <a-step title="运输中" />
          <a-step title="已验收" />
        </a-steps>
      </div>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>采购单：</b>
          {{ rurchaseData.num }}
        </a-col>
        <a-col :span="8"><b>预计价格：</b>
          {{ rurchaseData.price }} 元
        </a-col>
        <a-col :span="8"><b>供应商：</b>
          {{ rurchaseData.name }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>当前状态：</b>
          <span v-if="rurchaseData.status == 1">运输中</span>
          <span v-if="rurchaseData.status == 2">已验收</span>
        </a-col>
        <a-col :span="8"><b>备注信息：</b>
          {{ rurchaseData.content }}
        </a-col>
        <a-col :span="8"><b>采购时间：</b>
          {{ rurchaseData.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>供应地址：</b>
          {{ rurchaseData.putAddress }}
        </a-col>
        <a-col :span="8"><b>采购地址：</b>
          {{ rurchaseData.outAddress }}
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" :gutter="15">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">物品详情</span></a-col>
        <a-col :span="24">
          <a-table :columns="columns" :data-source="goodsList">
          </a-table>
        </a-col>
      </a-row>
    </div>
  </a-modal>
</template>

<script>
import moment from 'moment'
moment.locale('zh-cn')
export default {
  name: 'RurchaseView',
  props: {
    rurchaseShow: {
      type: Boolean,
      default: false
    },
    rurchaseData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.rurchaseShow
      },
      set: function () {
      }
    },
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
  data () {
    return {
      loading: false,
      goodsList: [],
      current: 0
    }
  },
  watch: {
    rurchaseShow: function (value) {
      if (value) {
        if (this.rurchaseData.status == 1) {
          this.current = 1
        }
        if (this.rurchaseData.status == 2) {
          this.current = 2
        }
        this.getGoodsByNum(this.rurchaseData.num)
      }
    }
  },
  methods: {
    getGoodsByNum (num) {
      this.$get('/cos/goods-belong/getGoodsByNum', { num }).then((r) => {
        this.goodsList = r.data.data
        console.log(this.goodsList)
      })
    },
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
