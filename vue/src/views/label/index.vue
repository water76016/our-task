<template>
  <div>
    <el-container height="100%">
      <el-main>
        <el-table :data="labelData.records" stripe style="width: 100%" border>
          <el-table-column prop="name" label="标签名称" align="center" />
          <el-table-column prop="taskCount" label="所含清单总数" align="center" />
          <el-table-column width="300" label="操作" align="center">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="changeLabel(scope.row)">编辑修改</el-button>
              <el-divider direction="vertical" />
              <el-button type="text" size="small" @click="deleteLabelById(scope.row.id, scope.row.taskCount)">删除标签</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
    </el-container>
    <el-footer>
      <el-row>
        <el-col :span="16"><div>
          <el-pagination
            background
            :current-page="labelData.current"
            :page-sizes="[5,10,15,20]"
            :page-size="labelData.size"
            layout="total, sizes, prev, pager, next, jumper"
            :total="labelData.total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div></el-col>
        <el-col :span="8"><div>
          <el-button type="text" @click="addLabel()">添加标签</el-button>
        </div></el-col>
      </el-row>
    </el-footer>
  </div>
</template>

<style>

</style>

<script>
export default {
  data() {
    return {
      labelData: {
        records: [],
        total: null,
        size: null,
        current: null,
        orders: [],
        searchCount: null,
        pages: null
      }
    }
  },
  created() {
    this.getlabelData(1, 5)
  },
  methods: {
    getlabelData(currentPage, pageSize) {
      this.$axios.get(`label/getPageList/${this.global.user.id}/${currentPage}/${pageSize}`).then((res) => {
        this.labelData = res.data.data
      })
    },
    handleSizeChange(val) {
      this.getlabelData(this.labelData.current, val)
    },
    handleCurrentChange(val) {
      this.getlabelData(val, this.labelData.size)
    },
    deleteLabelById(labelId, taskCount) {
      if (taskCount > 0) {
        this.warning('该标签下还有未完成清单，不能删除')
        return
      }
      this.$confirm('您正在删除该标签, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$axios.get(`label/delete/${labelId}`).then((res) => {
          this.getlabelData(1, 5)
          this.success('删除标签成功')
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    changeLabel(label) {
      this.$prompt('请输入新的标签名称', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[\u4e00-\u9fffa-zA-Z0-9]{1,6}$/,
        inputErrorMessage: '长度不能超过6个字符或不能存在空格'
      }).then(({ value }) => {
        label.name = value
        this.$axios.post('label/update', label).then((res) => {
          this.success('标签名修改成功: ' + value)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入'
        })
      })
    },
    addLabel() {
      this.$prompt('请输入新的标签名称', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[\u4e00-\u9fffa-zA-Z0-9]{1,6}$/,
        inputErrorMessage: '长度不能超过6个字符或不能存在空格'
      }).then(({ value }) => {
        var label = { id: null, userId: this.global.user.id, name: value }
        this.$axios.post('label/add/', label).then((res) => {
          label.id = res.data.data.id
          label.taskCount = 0
          // 如果超过当前页的大小，则弹出一个，再添加进去
          if (this.labelData.records.length >= this.labelData.size) {
            this.labelData.records.pop()
          }
          this.labelData.records.push(label)
          this.labelData.total++
          this.success('添加标签成功: ' + value)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入'
        })
      })
    },
    success(message) {
      this.$message({
        message: message,
        type: 'success'
      })
    },
    warning(message) {
      this.$message({
        message: message,
        type: 'warning'
      })
    }

  }
}
</script>

<style>
  .el-col {
    border-radius: 4px;
    text-align:center
  }
</style>
