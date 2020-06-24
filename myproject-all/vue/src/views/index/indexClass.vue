<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" @click="" v-if="hasPerm('goodsClass:add')">添加
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="block">
      <p>使用 scoped slot</p>
      <el-tree :data="data"  node-key="id" default-expand-all :expand-on-click-node="false">
        <span class="custom-tree-node" slot-scope="{ node, data }">
          <span>{{ node.label }}</span>
          <span>
            <el-image
                  style="width: 20; height: 20px"
                  src="https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg"
                  :fit="fit"></el-image>
          </span>
        </span>
      </el-tree>
    </div>

  </div>
</template>
<script>
  let id = 1000;

  export default {
    data() {
      const data = [{
        id: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
        label: '一级 1',
        imgUrl: '1',
        children: [{
          id: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
          label: '二级 1-1',
          imgUrl: '1',
          children: [{
            id: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
            label: '三级 1-1-1',
            imgUrl: '1'
          }, {
            id: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
            label: '三级 1-1-2',
            imgUrl: '1'
          }]
        }]
      }, {
        id: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
        label: '一级 2',
        imgUrl: '1',
        children: [{
          id: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
          label: '二级 2-1',
          imgUrl: '1'
        }, {
          id: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
          label: '二级 2-2',
          imgUrl: '1'
        }]
      }, {
        id: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
        label: '一级 3',
        imgUrl: '1',
        children: [{
          id: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
          label: '二级 3-1',
          imgUrl: '1'
        }, {
          id: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
          label: '二级 3-2',
          imgUrl: '1'
        }]
      }];
      return {
        data: JSON.parse(JSON.stringify(data)),
        data: JSON.parse(JSON.stringify(data))
      }
    },

    methods: {
      append(data) {
        const newChild = {
          id: id++,
          label: 'testtest',
          children: []
        };
        if (!data.children) {
          this.$set(data, 'children', []);
        }
        data.children.push(newChild);
      },

      remove(node, data) {
        const parent = node.parent;
        const children = parent.data.children || parent.data;
        const index = children.findIndex(d => d.id === data.id);
        children.splice(index, 1);
      },
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
