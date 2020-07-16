<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <div class="demo-input-size">
            <span>用户名:</span>
            <el-input placeholder="请输入用户名" v-model="listQuery.myname" class="inputSerach" clearable>
              <i slot="prefix" class="el-input__icon el-icon-search"></i>
            </el-input>
            <span>添加时间:</span>
            <el-date-picker v-model="listQuery.createTime" type="datetimerange" range-separator="至" start-placeholder="开始日期"
              end-placeholder="结束日期" value-format="yyyy-MM-dd HH:mm:ss">
            </el-date-picker>
            <el-button type="primary" icon="el-icon-search" @click="getList" v-if="hasPerm('user:list')">搜索</el-button>
          </div>
        </el-form-item>
      </el-form>
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" @click="showCreate" v-if="hasPerm('shopUser:add')">添加
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
      <el-table-column align="center" prop="username" label="用户名" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="telephone" label="手机号" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="roleName" label="角色" style="width: 60px;">
        <template slot-scope="scope">
          <el-tag type="primary" v-text="scope.row.roleName"></el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="" label="小程序角色" style="width: 60px;">
        <template slot-scope="scope">
          <el-tag type="primary" v-text="scope.row.weixinRoleName"></el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="管理" width="200">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)" v-if="hasPerm('shopUser:update')">修改</el-button>
          <el-button type="danger" icon="delete" @click="showDelete(scope.$index)" v-if="hasPerm('shopUser:delete')">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNum"
      :page-size="listQuery.pageRow" :total="totalCount" :page-sizes="[10, 20, 50, 100]" layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form class="small-space" :model="tempArticle" label-position="left" label-width="120px" style='width: px; margin-left:50px;'  >
        <el-form-item label="用户名或手机号" class="phoneclass">
          <el-input type="text" v-model="tempArticle.world" placeholder="请输入用户名或手机号">
          </el-input>
          <el-button type="primary" icon="el-icon-search" @click="getUserInfo">搜索</el-button>
        </el-form-item>
        <div v-for="item in gridData">
          <el-col :span="12">
          <el-card class="box-card">
            <el-checkbox v-model="item.checked" @change="click(item)">{{item.username}}</el-checkbox>
            <div>用户名：{{item.username}}</div>
            <div>手机号：{{item.telephone}}</div>
          </el-card>
          </el-col>
        </div>
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
          world: "",
        },
        gridData: [],
        userIds:[]
      }
    },
    created() {
      this.getList();
    },
    methods: {
      getList() {
        //查询列表
        if (!this.hasPerm('shopUser:list')) {
          return
        }
        this.listLoading = true;
        this.api({
          url: "/shop/listShopUser",
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
        if (this.userIds.length==0) {
          this.$message.error("请至少选择一个用户");
          return
        }
    //    this.dialogFormVisible = true
        //保存新文章
        this.api({
          url: "/shop/addShopUser",
          method: "post",
          params: {
            userIds:this.userIds.join(",")
          }
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
        if (this.tempArticle.className = '') {
          this.$message.error("请输入商品名称");
          return
        }
        if (this.tempArticle.showOrder = '') {
          this.$message.error("请输入商品名称");
          return
        }
        //this.listLoading = true;
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
      getUserInfo() {
        this.api({
          url: "/shop/serachShopUser",
          method: "post",
          params:{
            world:this.tempArticle.world
          }
        }).then((data) => {
          this.gridData = data
        })
      },
      click(item){
        if(item.checked==true){
          this.userIds.push(item.id)
        }else{
         for(var i=0;i<this.userIds.length;i++){
           if(this.userIds[i]==item.id){
             this.userIds.splice(i, 1)
           }
         }

        }
        console.log(this.userIds)
      },
      showDelete($index) {
        let _vue = this;

        this.$confirm('确定删除?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
               this.listLoading = true;
          _vue.api({
            url: "/shop/deleteShopUser",
            method: "post",
            params: {
              id: this.list[$index].userId
            }
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
<style>
  .phoneclass .el-input {
    width: 25%
  }
.box-card{
  margin-left: 8px;
  margin-top:8px;
}
.dialog-footer{
  clear: both;
}
 .inputSerach {
   width: 10%;
 }
 .dialog-footer{
   margin-top:8px;
 }
</style>
