<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button @click="drawer = true" type="primary" style="margin-left: 16px;">
            配置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="block">
      <p>商城首页配置</p>
      <el-tree :data="data" node-key="id" default-expand-all :expand-on-click-node="false">
        <span class="custom-tree-node" slot-scope="{ node, data }">
          <span>{{ node.label }}</span>
        </span>
      </el-tree>
    </div>
    <el-drawer title="我是标题" :visible.sync="drawer" :with-header="false">
      <div class="block">
        <p>首页配置</p>
        <el-tree :data="dataList" show-checkbox default-expand-all node-key="id" ref="tree" highlight-current :props="defaultProps"
          :default-checked-keys="checkList">
        </el-tree>
      </div>
      <div class="buttons">
        <el-button @click="getCheckedNodes">确定</el-button>
        <el-button @click="resetChecked">清空</el-button>
      </div>
    </el-drawer>
  </div>
</template>
<script>
  let id = 1000;

  export default {
    data() {
      return {
        data: [], //表格的数据
        listLoading: false, //数据加载等待动画
        drawer: false,
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        dataList: [],
        checkList: []
      }
    },
    created() {
      this.getList();
      this.getClassList();
    },
    methods: {
      getList() {
        //查询列表
        this.listLoading = true;
        this.api({
          url: "/index/listIndexClassAll",
          method: "get",
        }).then(data => {
          this.listLoading = false;
          this.data = data;
        })
      },
      getClassList() {
        //查询列表
        this.listLoading = true;
        this.api({
          url: "/index/listClassAll",
          method: "get",
        }).then(data => {
          this.listLoading = false;
          this.dataList = data.list;
          this.checkList = data.checkList;
        })
      },
      getCheckedNodes() {
        let nodeList = this.$refs.tree.getCheckedNodes();
        let param = []
        nodeList.forEach((item) => {
          if (item.children == undefined) {
            param.push(item.id)
          }
        })
        if(param.length==0){
          this.$message.error("请至少选择一个");
          return false
        }
        this.listLoading = true;
        this.api({
          url: "/index/addIndexClass",
          method: "get",
          params: {
            json: param.join(",")
          }
        }).then(data => {
          this.$message({
            message: '配置成功',
            type: 'success',
            duration: 1 * 1000,
            onClose: () => {
              this.drawer=false
              this.getList();
            }
          })
        })
      },
      resetChecked() {
        this.$refs.tree.setCheckedKeys([]);
      }
    }
  }
</script>

<style>
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
</style>
