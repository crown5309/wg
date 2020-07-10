<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" @click="showCreate" v-if="hasPerm('shop_class:add')">添加
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit highlight-current-row>
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="className" label="分类名称" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="showOrder" label="显示顺序" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="管理" width="200">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)" v-if="hasPerm('shop_class:update')">修改</el-button>
          <el-button type="danger" icon="delete" @click="showDelete(scope.$index)" v-if="hasPerm('shop_class:delete')">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNum"
      :page-size="listQuery.pageRow" :total="totalCount" :page-sizes="[10, 20, 50, 100]" layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form class="small-space" :model="tempArticle" label-position="left" label-width="100px" style='width: 300px; margin-left:50px;'>
        <el-form-item label="分类名称">
          <el-input type="text" v-model="tempArticle.className" placeholder="请输入分类名称">
          </el-input>
        </el-form-item>
        <el-form-item label="显示顺序">
          <el-input type="number" v-model="tempArticle.showOrder"placeholder="请输入显示顺序">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createArticle">创 建</el-button>
        <el-button type="primary" v-else @click="updateArticle">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        totalCount: 0, //分页组件--数据总条数
        list: [], //表格的数据
        listLoading: false, //数据加载等待动画
        listQuery: {
          pageNum: 1, //页码
          pageRow: 50, //每页条数
          name: ''
        },
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '创建'
        },
        tempArticle: {
          id: "",
          className: "",
          showOrder: ""
        }
      }
    },
    created() {
      this.getList();
    },
    methods: {
      getList() {
        //查询列表
        if (!this.hasPerm('shop_class:list')) {
          return
        }
        this.listLoading = true;
        this.api({
          url: "/shopClass/listShopClass",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
          this.totalCount = data.totalCount;
        })
      },
      handleSizeChange(val) {
        //改变每页数量
        this.listQuery.pageRow = val
        this.handleFilter();
      },
      handleCurrentChange(val) {
        //改变页码
        this.listQuery.pageNum = val
        this.getList();
      },
      getIndex($index) {
        //表格序号
        return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
      },
      showCreate() {
        //显示新增对话框
        this.tempArticle.className = "";
        this.tempArticle.showOrder = "";
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },
      showUpdate($index) {
        //显示修改对话框
        this.tempArticle.id = this.list[$index].id;
        this.tempArticle.className = this.list[$index].className;
        this.tempArticle.showOrder = this.list[$index].showOrder;
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      createArticle() {
        debugger
        if(this.tempArticle.className==''){
          this.$message.error("请输入商品名称");
          return
        }
        if(this.tempArticle.showOrder==''){
          this.$message.error("请输入商品名称");
          return
        }
        this.dialogFormVisible = true
        //保存新文章
        this.api({
          url: "/shopClass/addShopClass",
          method: "post",
          data: this.tempArticle
        }).then(() => {
          this.$message({
            message: "新增成功",
            type: 'success',
            duration: 1 * 1000,
            onClose: () => {
              this.getList();
              this.dialogFormVisible = false
              this.listLoading = false;
            }
          })
        })
      },
      updateArticle() {
        if(this.tempArticle.className=''){
          this.$message.error("请输入商品名称");
          return
        }
        if(this.tempArticle.showOrder=''){
          this.$message.error("请输入商品名称");
          return
        }
        this.listLoading = true;
        //修改文章
        this.api({
          url: "/shopClass/updateShopClass",
          method: "post",
          data: this.tempArticle
        }).then(() => {
          this.$message({
            message: "修改成功",
            type: 'success',
            duration: 1 * 1000,
            onClose: () => {
              this.getList();
              this.dialogFormVisible = false
              this.listLoading = false;
            }
          })
        })
      },
      showDelete($index){
        let _vue = this;
        this.listLoading = true;
        this.$confirm('确定删除?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          _vue.api({
            url: "/shopClass/deleteShopClass",
            method: "post",
            params: {id:this.list[$index].id}
          }).then(() => {
            this.$message({
              message: "删除成功",
              type: 'success',
              duration: 1 * 1000,
              onClose: () => {
                this.getList();
                this.dialogFormVisible = false
                this.listLoading = false;
              }
            })
          }).catch(() => {
            _vue.$message.error("删除失败")
          })
        })
      }
    }
  }
</script>
