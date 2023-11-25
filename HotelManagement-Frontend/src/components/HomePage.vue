<script setup lang="ts">
import {h, ref} from "vue";
import {LoginOutlined} from "@ant-design/icons-vue";
import type {MenuProps} from "ant-design-vue";
import { useRoute, useRouter} from "vue-router";

const router = useRouter()

const route = useRoute()

const current_route = route.name ? route.name.toString(): 'main'

const current = ref<string[]>([current_route]);
const items = ref<MenuProps['items']>([
  {
    key: 'main',
    label: 'Home',
    title: 'Home',
  },
  {
    key: 'rooms',
    label: 'Rooms',
    title: 'Rooms',
  },
  {
    key: 'contact',
    label: 'Contact',
    title: 'Contact',
  },
]);

const goto = (input: { item: object, key: string, keyPath: object }) => {
  router.push({name: input['key']})
}
</script>

<template>
  <a-layout>
    <a-layout-header class="header">
      <a-space :size=50>
        <a-space :size=30>
          <router-link :to="{name:'main'}" class="logo" @click='()=>{current_route="main"}'>The<span class="text-primary">Hotel</span></router-link>
          <a-menu mode="horizontal" :style="{lineHeight:'80px'}" v-model:selectedKeys="current" :items="items"
                  @click="goto"/>
        </a-space>
        <div>
          <a-button type="link" :icon="h(LoginOutlined)" class="link-button">Login</a-button>
          <a-divider type="vertical"/>
          <a-button type="link" class="link-button">Register</a-button>
        </div>
      </a-space>
    </a-layout-header>
    <a-layout-content>
      <RouterView/>
    </a-layout-content>
    <a-layout-footer class="footer">
      ©2023 TheHotel
    </a-layout-footer>
  </a-layout>
</template>

<style scoped>
.header {
  background: white;
  text-align: center;
  min-height: 100px;
}

.logo {
  float: left;
  font-size: 22px;
  color: black;
}

.link-button {
  padding-top: 0;
  padding-bottom: 60px;
}

.text-primary {
  color: orangered;
}

.footer {
  background: rgba(0,0,0,0.8);
  text-align: center;
  vertical-align: center;
  color: white;
}

</style>