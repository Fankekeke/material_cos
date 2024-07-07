<template>
  <a-modal v-model="show" title="导入物品信息" @cancel="onClose" :width="500" :footer="null">
    <a-upload-dragger
      name="file"
      :multiple="true"
      accept=".xls, .xlsx"
      action="http://127.0.0.1:9527/cos/stock-info/import"
      @change="handleChange"
    >
      <p class="ant-upload-drag-icon">
        <a-icon type="inbox" />
      </p>
      <p class="ant-upload-text">
        Click or drag file to this area to upload
      </p>
      <p class="ant-upload-hint">
        Support for a single or bulk upload
      </p>
    </a-upload-dragger>
    <a-button-group style="margin-top: 20px">
      <a-button type="primary" icon="cloud-download" @click="download"/>
    </a-button-group>
  </a-modal>
</template>

<script>
import {mapState} from 'vuex'
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'examAdd',
  props: {
    examAddVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.examAddVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  methods: {
    handleChange ({file}) {
      if (file.response !== undefined) {
        console.log(file.response.code)
        console.log(file.status)
        if (file.response.code === 500 && file.status === 'done') {
          this.$message.error(file.response.msg)
        } else if (file.response.code === 0 && file.status === 'done') {
          this.$emit('success')
        }
      }
    },
    download () {
      window.location.href = 'http://127.0.0.1:9527/cos/stock-info/template'
    },
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        images.push(image.response)
      })
      this.form.validateFields((err, values) => {
        values.images = images.length > 0 ? images.join(',') : null
        if (!err) {
          this.loading = true
          this.$post('/cos/stock-info/import', {
            ...values
          }).then((r) => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
