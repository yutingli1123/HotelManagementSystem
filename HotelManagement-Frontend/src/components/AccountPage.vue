<script setup lang="ts">
import Cookies from 'js-cookie'
import {h, onMounted, Ref, ref} from "vue";
import axios from "axios";
import {useRoute, useRouter} from "vue-router";
import {MenuProps} from "ant-design-vue";
import {BookOutlined, InfoCircleOutlined} from "@ant-design/icons-vue";
import {useStore} from "@/stores/stateStore";

const router = useRouter()
const route = useRoute()
const store = useStore()

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
          token.value = response.data['token']
          refresh_token.value = response.data['refresh_token']
          Cookies.set('token', token.value, {expires: new Date(new Date().getTime() + 15 *60 * 1000)});
          Cookies.set('refresh_token', refresh_token.value, {expires: new Date(new Date().getTime() + 20 * 60 * 1000)});
          store.changeToLogin()
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

        } else {
          router.push({name: 'main'})
        }
      }).catch(() => {
        router.push({name: 'main'})
      })
    }
  } else {
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
  }
})

const current = ref<string[]>([current_route.value]);
const items = ref<MenuProps['items']>([
  {
    key: 'info',
    label: 'My Info',
    title: 'My Info',
    icon: h(InfoCircleOutlined)
  },
  {
    type: 'divider'
  },
  {
    key: 'reservations',
    label: 'My Reservations',
    title: 'My Reservations',
    icon: h(BookOutlined)
  },
]);

</script>

<template>
  <a-spin :spinning="loading">
    <div style="position: relative">
      <img class="background_image" src="/account_background.jpg" alt="Background Image"/>
      <div class="overlayer">
        <div class="img-info">Hello, {{ name }}</div>
      </div>
    </div>
    <a-layout>
      <a-layout-sider>
        <a-menu style="height: 500px" v-model:selectedKeys="current" :items="items"
                @click="(input)=>{router.push({name:input['key']})}"/>
      </a-layout-sider>
      <a-layout-content>
        <RouterView :key="$route.fullpath"/>
      </a-layout-content>
    </a-layout>
  </a-spin>
</template>

<style scoped>
.background_image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.overlayer {
  background: rgba(0, 0, 0, 0.4);
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.img-info {
  color: white;
  font-size: 60px;
  text-align: center;
  font-family: "Times New Roman", serif;
}
</style>