<template>
  <div>
    <el-container height="100%">
      <el-main>
        <el-table :data="categoryData.records" stripe style="width: 100%" border>
          <el-table-column prop="name" label="分类名称" align="center" />
          <el-table-column prop="taskCount" label="所含清单总数" align="center" />
          <el-table-column width="300" label="操作" align="center">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="changeCategory(scope.row)">编辑修改</el-button>
              <el-divider direction="vertical" />
              <el-button type="text" size="small" @click="deleteCategoryById(scope.row.id, scope.row.taskCount)">删除分类</el-button>
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
            :current-page="categoryData.current"
            :page-sizes="[5, 10, 15, 20]"
            :page-size="categoryData.size"
            layout="total, sizes, prev, pager, next, jumper"
            :total="categoryData.total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div></el-col>
        <el-col :span="8"><div>
          <el-button type="text" @click="addCategory()">添加分类</el-button>
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
      categoryData: {
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
    this.getUsercategoryDataList(1, 5)
  },
  methods: {
    getUsercategoryDataList(currentPage, pageSize) {
      this.$axios.get(`category/getPageList/${this.global.user.id}/${currentPage}/${pageSize}`).then((res) => {
        this.categoryData = res.data.data
      })
    },
    handleSizeChange(val) {
      this.getUsercategoryDataList(this.categoryData.current, val)
    },
    handleCurrentChange(val) {
      this.getUsercategoryDataList(val, this.categoryData.size)
    },
    deleteCategoryById(categoryId, taskCount) {
      if (taskCount > 0) {
        this.warning('该分类下还有未完成清单，不能删除')
        return
      }
      this.$confirm('您正在删除该分类, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$axios.get(`category/delete/${categoryId}/${this.global.user.id}`).then((res) => {
          this.getUsercategoryDataList(1, 5)
        })
        this.success('删除分类成功')
        this.getUsercategoryDataList(1, 5)
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    changeCategory(category) {
      this.$prompt('请输入新的分类名称', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[\u4e00-\u9fffa-zA-Z0-9]{1,6}$/,
        inputErrorMessage: '长度不能超过6个字符或不能存在空格'
      }).then(({ value }) => {
        category.name = value
        console.log(category)
        this.$axios.post('category/update/', category).then((res) => {
          this.success('分类名修改成功:' + value)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入'
        })
      })
    },
    addCategory() {
      this.$prompt('请输入新的分类名称', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[\u4e00-\u9fffa-zA-Z0-9]{1,6}$/,
        inputErrorMessage: '长度不能超过6个字符或不能存在空格'
      }).then(({ value }) => {
        var category = { id: null, userId: this.global.user.id, name: value }
        this.$axios.post('category/add/', category).then((res) => {
          category.id = res.data.data.id
          category.taskCount = 0
          // 如果超过当前页的大小，则弹出一个，再添加进去
          if (this.categoryData.records.length >= this.categoryData.size) {
            this.categoryData.records.pop()
          }
          this.categoryData.records.push(category)
          this.categoryData.total++
          this.success('添加分类成功: ' + value)
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
