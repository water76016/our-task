import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'dashboard' }
    }]
  },
  {
    path: '/add',
    component: Layout,
    redirect: '/addTask',
    children: [{
      path: 'addTask',
      name: 'AddTask',
      component: () => import('@/views/task/add'),
      meta: { title: '添加清单', icon: 'dashboard' }
    }]
  },

  {
    path: '/show',
    component: Layout,
    redirect: '/show/all',
    name: 'Show',
    meta: { title: '列表', icon: 'el-icon-s-help' },
    children: [
      {
        path: 'task',
        name: 'Task',
        component: () => import('@/views/task/index'),
        meta: { title: '清单列表', icon: 'tree' }
      },
      {
        path: 'category',
        name: 'Category',
        component: () => import('@/views/category/index'),
        meta: { title: '分类列表', icon: 'tree' }
      },
      {
        path: 'label',
        name: 'Label',
        component: () => import('@/views/future/index'),
        meta: { title: '标签列表', icon: 'table' }
      }
    ]
  },
  {
    path: '/help',
    component: Layout,
    redirect: '/help/all',
    name: 'help',
    meta: { title: '帮助中心', icon: 'el-icon-s-help' },
    children: [
      {
        path: 'systemSettings',
        name: 'systemSettings',
        component: () => import('@/views/future/index'),
        meta: { title: '系统设置', icon: 'tree' }
      },
      {
        path: 'feedback',
        name: 'feedback',
        component: () => import('@/views/future/index'),
        meta: { title: '提交反馈', icon: 'tree' }
      },
      {
        path: 'contact',
        name: 'contact',
        component: () => import('@/views/future/index'),
        meta: { title: '联系我', icon: 'tree' }
      }
    ]
  }
]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
