<script setup lang="ts">
import Cookies from 'js-cookie'
import {onMounted, Ref, ref, watch} from "vue";
import axios from "axios";
import {useRoute, useRouter} from "vue-router";
import {MenuProps} from "ant-design-vue";


const router = useRouter()
const route = useRoute()

const current_route = ref(route.name ? route.name.toString() : 'info')

const token: Ref<string> = ref(Cookies.get('token'))
const refresh_token: Ref<string> = ref(Cookies.get('refresh_token'))
const loading: Ref<boolean> = ref(true)
const name: Ref<string> = ref('')


onMounted(() => {
  if (token.value == null) {
    if (refresh_token.value != null) {
      axios.post('http://localhost:8080/api/v1/refresh', refresh_token.value).then((response) => {
        if (response.status == 200) {
          token.value = response.data
        } else {
          router.push({name: 'main'})
        }
      }).catch(() => {
        router.push({name: 'main'})
      })
    }
  }
  axios.get('http://localhost:8080/api/v1/customers/name', {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  }).then((response) => {
    if (response.status == 200) {
      name.value = response.data
      loading.value = false
    }
  }).catch(() => {
  })
})

const currentRoute = ref<string[]>([current_route.value]);
// Management page navigation items
const items = ref<MenuProps['items']>([
  {
    key: 'reservations',
    label: 'My Reservations',
    title: 'My Reservations',
  },
  {
    key: 'users',
    label: 'User Management',
    title: 'User Management',
  },
  {
    key: 'tasks',
    label: 'Task Management',
    title: 'Task Management',
  },
]);


</script>

<template>
  <a-spin :spinning="loading">
    <div class="header-image">
      <div class="overlayer">
        <div class="img-info">Hello, {{ name }}</div>
      </div>
    </div>
    <a-layout>
      <a-layout-sider>
        <a-menu style="height: 500px" v-model:selectedKeys="currentRoute" :items="items" @click="(input)=>{router.push({name:input['key']})}"/>
      </a-layout-sider>
      <a-layout-content style="padding: 24px">
        <RouterView :key="$route.fullPath"/>
      </a-layout-content>
    </a-layout>
  </a-spin>
</template>


<style scoped>
.overlayer {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.a-layout-sider {
  background: #fff;
  border-right: 1px solid #e8e8e8;
}

.a-menu {
  height: 100%;
  border-right: none;
}

.a-menu-item {
  font-size: 1rem;
  color: #595959;
}

.a-menu-item:hover {
  background: #f5f5f5;
}

.a-menu-item-selected {
  background-color: #e6f7ff;
  color: #1890ff;
}

.a-layout-content {
  padding: 24px;
  background: #fff;
  min-height: 280px;
}

.a-spin {
  max-width: 100%;
}
</style>

