<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="el-icon-add" @click="showCreate" v-if="hasPerm('sysParams:add')">添加
          </el-button>
          <el-button type="primary" icon="plus" @click="showSang" v-if="hasPerm('sysParams:list')" v-show="listQuery.parentId!=0">返回上一级
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
      <el-table-column align="center" prop="name" label="名称" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="value" label="值" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="code" label="code码" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="管理" width="300">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-iconedit" @click="showUpdate(scope.$index)" v-if="hasPerm('sysParams:update')">修改</el-button>
          <el-button type="primary" icon="el-iconsearch" @click="showXia(scope.$index)" v-if="hasPerm('sysParams:list')" v-show="listQuery.parentId==0">下级</el-button>
          <el-button type="primary" icon="el-icondelete" @click="showDelete(scope.$index)" v-if="hasPerm('sysParams:delete')">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNum"
      :page-size="listQuery.pageRow" :total="totalCount" :page-sizes="[10, 20, 50, 100]" layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form class="small-space" :model="sysParams" label-position="left" label-width="80px" style='width: 300px; margin-left:80px;'>
        <el-form-item label="名称">
          <el-input type="text" v-model="sysParams.name"  placeholder="请输入名称">
          </el-input>
        </el-form-item>
        <el-form-item label="值">
          <el-input type="text" v-model="sysParams.value"  placeholder="请输入值">
          </el-input>
        </el-form-item>
       <el-form-item label="code" v-show="textMap[dialogStatus]=='创建'">
         <el-input type="text" v-model="sysParams.code"  placeholder="请输入code">
         </el-input>
       </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createSysParams">创 建</el-button>
        <el-button type="primary" v-else @click="updateSysParams">修 改</el-button>
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
          name: '',
          parentId: "0",
          shangId: "",
        },
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '创建'
        },
        sysParams: {
          id: "",
          name: "",
          value: "",
          code: "",
          parentId: 0
        },
      }
    },
    created() {
      this.getList();
    },
    methods: {
      getList() {
        //查询列表
        if (!this.hasPerm('sysParams:list')) {
          return
        }
        this.listLoading = true;
        this.api({
          url: "/param/listSysParams",
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
        this.sysParams.name = "";
        this.sysParams.code = "";
        this.sysParams.value = "";
         this.sysParams.parentId =this.listQuery.parentId;
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },
      showUpdate($index) {
        //显示修改对话框
        this.sysParams.id = this.list[$index].id;
        this.sysParams.name = this.list[$index].name;
        this.sysParams.code = this.list[$index].code;
        this.sysParams.value = this.list[$index].value;
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      createSysParams() {
         this.listLoading = true;
        //保存
        this.api({
          url: "/param/addSysParams",
          method: "post",
          params: this.sysParams
        }).then(() => {
          this.$message({
            message: "添加成功",
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
      updateSysParams() {
        this.listLoading = true;
        //修改
        this.api({
          url: "/param/updateSysParams",
          method: "post",
          params: this.sysParams
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
      this.sysParams.id=this.list[$index].id
      let _vue = this;
      this.$confirm('确定删除?', '提示', {
        confirmButtonText: '确定',
        showCancelButton: false,
        type: 'warning'
      }).then(() => {
        _vue.api({
          url: "/param/deleteSysParams",
          method: "post",
          params: this.sysParams
        }).then(() => {
          this.$message({
            message: "删除成功",
            type: 'success',
            duration: 1 * 1000,
            onClose: () => {
              this.getList();
            }
          })
        }).catch(() => {
          _vue.$message.error("删除失败")
        })
      })
    },
      showXia($index) {
        this.listQuery.parentId = this.list[$index].id
        this.getList()
      },
      showSang() {
        this.listQuery.parentId = 0
        this.getList()
      },
    }
  }
</script>
