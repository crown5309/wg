import Vue from 'vue'
import Router from 'vue-router'
// in development env not use Lazy Loading,because Lazy Loading too many pages will cause webpack hot update too slow.so only in production use Lazy Loading
/* layout */
import Layout from '../views/layout/Layout'

const _import = require('./_import_' + process.env.NODE_ENV)
Vue.use(Router)
export const constantRouterMap = [
  {path: '/login', component: _import('login/index'), hidden: true},
  {path: '/404', component: _import('404'), hidden: true},
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: '首页',
    hidden: true,
    children: [{
      path: 'dashboard', component: _import('dashboard/index')
    }]
  }
]
export default new Router({
   mode: 'hash', //后端支持可开 (部署改为hash)
  scrollBehavior: () => ({y: 0}),
  routes: constantRouterMap
})
export const asyncRouterMap = [
  {
    path: '/index',
    component: Layout,
    redirect: '/index/',
    name: '',
    meta: {title: '首页管理', icon: 'indexManager'},
    children: [
      {
        path: '', name: 'banner管理', component: _import('index/banner'), meta: {title: 'banner列表', icon: 'banner'}, menu: 'banner_list'
      },
      {
        path: 'indexClass',
        name: '首页配置',
        component: _import('index/indexClass'),
        meta: {title: '首页配置', icon: 'setting'},
        menu: 'index_class'
      },
    ]
  },
  {
    path: '/user',
    component: Layout,
    redirect: '/user/',
    name: '',
    meta: {title: '用户权限', icon: 'userManager'},
    children: [
      {
        path: '', name: '用户列表', component: _import('user/user'), meta: {title: '用户列表', icon: 'user'}, menu: 'user'
      },
      {
        path: 'role',
        name: '权限管理',
        component: _import('user/role'),
        meta: {title: '权限管理', icon: 'password'},
        menu: 'role'
      },
      {
        path: 'xiaochengxurole',
        name: '小程序权限',
        component: _import('user/xiaochengxurole'),
        meta: {title: '小程序权限', icon: 'xiaochengxurolepassword'},
        menu: 'xiaochengxurole'
      },
      {
        path: 'xiaochengxuroleManager',
        name: '小程序菜单',
        component: _import('user/xiaochengxuroleManager'),
        meta: {title: '小程序菜单', icon: 'xiaochengxu'},
        menu: 'xiaochengxuroleManager'
      },
    ]
  },
  {
    path: '/goods',
    component: Layout,
    redirect: '/goods/',
    name: '',
    meta: {title: '商品管理', icon: 'goodsManager'},
    children: [
      {
        path: '', name: '商品列表', component: _import('goods/listgoods'), meta: {title: '商品列表', icon: 'goods'}, menu: 'goods_goods'
      },
      {
        path: 'goods',
        name: '分类管理',
        component: _import('goods/listgoodsClass'),
        meta: {title: '分类管理', icon: 'goodsClass'},
        menu: 'goods_class'
      },
    ]
  },
  {
    path: '/order',
    component: Layout,
    redirect: '/order/',
    name: '',
    meta: {title: '订单管理', icon: 'orderManager'},
    children: [
      {
        path: '', name: '订单列表', component: _import('order/listOrder'), meta: {title: '订单列表', icon: 'order'}, menu: 'order_order'
      },
    ]
  },
  // {
  //   path: '/shop',
  //   component: Layout,
  //   redirect: '/shop/',
  //   name: '',
  //   meta: {title: '商铺管理', icon: 'shopManager'},
  //   children: [
  //      {
  //        path: 'article',
  //        name: '商铺列表',
  //        component: _import('shop/article'),
  //        meta: {title: '商铺列表', icon: 'shop'},
  //        menu: 'article'
  //      },
  //      {
  //        path: 'shop',
  //        name: '商铺分类',
  //        component: _import('shop/shopClass'),
  //        meta: {title: '商铺分类', icon: 'shopClass'},
  //        menu: 'shopClass'
  //      }
  //   ]
  // },
  {
    path: '/sysParams',
    component: Layout,
    redirect: '/sysParams/',
    name: '',
    meta: {title: '系统配置', icon: 'sys'},
    children: [
       {
         path: 'sysParams',
         name: '系统参数',
         component: _import('sys/paramList'),
         meta: {title: '系统参数', icon: 'sys_param'},
         menu: 'sysParams'
       },
    ]
  },
  {path: '*', redirect: '/404', hidden: true}
]
