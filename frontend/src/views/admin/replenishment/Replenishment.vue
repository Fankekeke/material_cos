<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="物品名称"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-input v-model="queryParams.name"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="盘库时间"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-range-picker @change="onPickerChange" />
              </a-form-item>
            </a-col>
          </div>
          <span style="float: right; margin-top: 3px;">
              <a-button type="primary" @click="search">查询</a-button>
              <a-button style="margin-left: 8px" @click="reset">重置</a-button>
            </span>
        </a-row>
      </a-form>
    </div>
    <div>
      <div class="operator">
        <a-button @click="replenishment">盘库</a-button>
      </div>
      <!-- 表格区域 -->
      <a-table ref="TableInfo"
               :columns="columns"
               :rowKey="record => record.id"
               :dataSource="dataSource"
               :pagination="pagination"
               :loading="loading"
               :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
               :scroll="{ x: 900 }"
               @change="handleTableChange">
        <template slot="titleShow" slot-scope="text, record">
          <template>
            <a-badge status="processing"/>
            <a-tooltip>
              <template slot="title">
                {{ record.title }}
              </template>
              {{ record.title.slice(0, 8) }} ...
            </a-tooltip>
          </template>
        </template>
        <template slot="contentShow" slot-scope="text, record">
          <template>
            <a-tooltip>
              <template slot="title">
                {{ record.content }}
              </template>
              {{ record.content.slice(0, 80) }} ...
            </a-tooltip>
          </template>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-icon type="folder-open" @click="view(record)" title="查 看" style="margin-right: 15px"></a-icon>
          <a-icon type="download" @click="downLoad(record)" title="下 载" style="margin-right: 15px"></a-icon>
        </template>
      </a-table>
      <record-view
        @close="handlerecordViewClose"
        :recordShow="recordView.visiable"
        :recordData="recordView.data">
      </record-view>
    </div>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import RecordView from './ReplenishmentView'
import {mapState} from 'vuex'
import { newSpread, floatForm, floatReset, saveExcel } from '@/utils/spreadJS'
import moment from 'moment'
moment.locale('zh-cn')

export default {
  name: 'request',
  components: {RecordView, RangeDate},
  data () {
    return {
      advanced: false,
      requestAdd: {
        visiable: false
      },
      requestEdit: {
        visiable: false
      },
      recordView: {
        visiable: false,
        data: null
      },
      queryParams: {},
      filteredInfo: null,
      sortedInfo: null,
      paginationInfo: null,
      dataSource: [],
      selectedRowKeys: [],
      loading: false,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      userList: []
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    columns () {
      return [{
        title: '盘库统计',
        dataIndex: 'content',
        scopedSlots: {customRender: 'contentShow'}
      }, {
        title: '盘库时间',
        dataIndex: 'taskDate',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: {customRender: 'operation'}
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    onPickerChange (date, dateString) {
      if (date.length !== 0) {
        this.queryParams.startDate = moment(date[0]).format('YYYY-MM-DD')
        this.queryParams.endDate = moment(date[1]).format('YYYY-MM-DD')
      } else {
        delete this.queryParams.startDate
        delete this.queryParams.endDate
      }
    },
    replenishment () {
      this.$get('/cos/storehouse-info/replenishment').then((r) => {
        this.$message.success('等在盘库~请稍等')
      })
    },
    downLoad (row) {
      this.$message.loading('正在生成', 0)
      let goodsList = JSON.parse(row.replenishment)
      let newData = []
      goodsList.forEach((item, index) => {
        newData.push([item.materialName, item.model !== null ? item.model : '- -', item.quantity, item.measurementUnit, item.unitPrice])
      })
      let spread = newSpread('replenishment')
      spread = floatForm(spread, 'replenishment', newData)
      saveExcel(spread, '盘库单.xlsx')
      floatReset(spread, 'replenishment', newData.length)
      this.$message.destroy()
    },
    view (row) {
      this.recordView.data = row
      this.recordView.visiable = true
    },
    handlerecordViewClose () {
      this.recordView.visiable = false
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    add () {
      this.requestAdd.visiable = true
    },
    handleRequestAddClose () {
      this.requestAdd.visiable = false
    },
    handleRequestAddSuccess () {
      this.requestAdd.visiable = false
      this.$message.success('入库成功')
      this.search()
    },
    handleDeptChange (value) {
      this.queryParams.deptId = value || ''
    },
    batchDelete () {
      if (!this.selectedRowKeys.length) {
        this.$message.warning('请选择需要删除的记录')
        return
      }
      let that = this
      this.$confirm({
        title: '确定删除所选中的记录?',
        content: '当您点击确定按钮后，这些记录将会被彻底删除',
        centered: true,
        onOk () {
          let ids = that.selectedRowKeys.join(',')
          that.$delete('/cos/replenishment-info/' + ids).then(() => {
            that.$message.success('删除成功')
            that.selectedRowKeys = []
            that.search()
          })
        },
        onCancel () {
          that.selectedRowKeys = []
        }
      })
    },
    search () {
      let {sortedInfo, filteredInfo} = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }
      this.fetch({
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams,
        ...filteredInfo
      })
    },
    reset () {
      // 取消选中
      this.selectedRowKeys = []
      // 重置分页
      this.$refs.TableInfo.pagination.current = this.pagination.defaultCurrent
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
        this.paginationInfo.pageSize = this.pagination.defaultPageSize
      }
      // 重置列过滤器规则
      this.filteredInfo = null
      // 重置列排序规则
      this.sortedInfo = null
      // 重置查询参数
      this.queryParams = {}
      this.fetch()
    },
    handleTableChange (pagination, filters, sorter) {
      // 将这三个参数赋值给Vue data，用于后续使用
      this.paginationInfo = pagination
      this.filteredInfo = filters
      this.sortedInfo = sorter

      this.fetch({
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...this.queryParams,
        ...filters
      })
    },
    fetch (params = {}) {
      // 显示loading
      this.loading = true
      if (this.paginationInfo) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.TableInfo.pagination.current = this.paginationInfo.current
        this.$refs.TableInfo.pagination.pageSize = this.paginationInfo.pageSize
        params.size = this.paginationInfo.pageSize
        params.current = this.paginationInfo.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.size = this.pagination.defaultPageSize
        params.current = this.pagination.defaultCurrent
      }
      this.$get('/cos/replenishment-info/page', {
        ...params
      }).then((r) => {
        let data = r.data.data
        const pagination = {...this.pagination}
        pagination.total = data.total
        this.dataSource = data.records
        this.pagination = pagination
        // 数据加载完毕，关闭loading
        this.loading = false
      })
    }
  },
  watch: {}
}
</script>
<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
