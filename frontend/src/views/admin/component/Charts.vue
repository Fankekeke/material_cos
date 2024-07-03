<template>
  <dv-border-box-11 title="欢迎各位领导莅临指导" style="height: 80vh">
    <a-row style="padding: 50px;padding-top: 80px">
      <a-col :span="12">
        <div class="left-chart-1">
          <div class="lc1-details" style="color: white">设备运行总数<span>430</span></div>
          <dv-capsule-chart class="lc1-chart" :config="config" />
          <dv-decoration-2 style="height:10px;" />
        </div>
      </a-col>
      <a-col :span="12">
        <div class="right-chart-1">
          <div class="rc1-chart-container">
            <div class="left" style="color: white">
              <div>设备运行总数 262</div>
            </div>
            <dv-capsule-chart class="right" :config="config2" />
          </div>
        </div>
      </a-col>
    </a-row>
  </dv-border-box-11>
</template>

<script>
export default {
  name: 'Charts',
  data () {
    return {
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
      }
    }
  }
}
</script>

<style scoped>

</style>
