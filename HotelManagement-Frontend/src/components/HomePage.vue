<script setup lang="ts">
import {computed, h, reactive, ref} from "vue";
import {LoginOutlined} from "@ant-design/icons-vue";
import type {MenuProps} from "ant-design-vue";
import {useRoute, useRouter} from "vue-router";

const router = useRouter()

const route = useRoute()

const current_route = ref(route.name ? route.name.toString() : 'main')

const current = ref<string[]>([current_route.value]);
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

const loginModalOpen = ref(false)

const handleLoginCancel = () => {
  loginModalOpen.value = false
}

const loginLoading = ref(false)

interface LoginFormState {
  username: string;
  password: string;
  remember: boolean;
}

const loginFormState = reactive<LoginFormState>({
  username: '',
  password: '',
  remember: false,
});
const onLoginFinish = (values: any) => {
  console.log('Success:', values);
};

const onLoginFinishFailed = (errorInfo: any) => {
  console.log('Failed:', errorInfo);
};

const loginDisabled = computed(() => {
  return !(loginFormState.username && loginFormState.password);
});

const registerModalOpen = ref(false)

const handleRegisterCancel = () => {
  registerModalOpen.value = false
}

const registerLoading = ref(false)

interface RegisterFormState {
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  password: string;
}

const registerFormState = reactive<RegisterFormState>({
  firstName: '',
  lastName: '',
  username: '',
  email: '',
  password: '',
});
const onRegisterFinish = (values: any) => {
  console.log('Success:', values);
};

const onRegisterFinishFailed = (errorInfo: any) => {
  console.log('Failed:', errorInfo);
};

const registerDisabled = computed(() => {
  return !(registerFormState.username && registerFormState.password);
});
</script>

<template>
  <a-modal v-model:open="loginModalOpen" title="Login" :mask-closable=false :closable=false>
    <template #footer>
      <a-button key="back" @click="handleLoginCancel">Cancel</a-button>
      <a-button key="submit" type="primary" :loading="loginLoading" :disabled="loginDisabled">Login</a-button>
    </template>
    <div style="height: 20px"></div>
    <a-form :model="loginFormState"
            :label-col="{ span: 8 }"
            :wrapper-col="{ span: 12 }"
            autocomplete="off"
            @finish="onLoginFinish"
            @finishFailed="onLoginFinishFailed"
    >
      <a-form-item
          label="Username"
          name="username"
          :rules="[{ required: true, message: 'Please input your username!' }]"
      >
        <a-input v-model:value="loginFormState.username"/>
      </a-form-item>

      <a-form-item
          label="Password"
          name="password"
          :rules="[{ required: true, message: 'Please input your password!' }]"
      >
        <a-input-password v-model:value="loginFormState.password"/>
      </a-form-item>

      <a-form-item name="remember" :wrapper-col="{ offset: 8, span: 16 }">
        <a-checkbox v-model:checked="loginFormState.remember">Remember me</a-checkbox>
      </a-form-item>
    </a-form>
  </a-modal>

  <a-modal v-model:open="registerModalOpen" title="Register" :mask-closable=false :closable=false style="width: 570px;">
    <template #footer>
      <a-button key="back" @click="handleRegisterCancel">Cancel</a-button>
      <a-button key="submit" type="primary" :loading="registerLoading" :disabled="registerDisabled">Register</a-button>
    </template>
    <div style="height: 20px"></div>
    <a-form :model="registerFormState"
            autocomplete="off"
            @finish="onRegisterFinish"
            @finishFailed="onRegisterFinishFailed"
            style="display: flex;flex-direction: column;align-items: end;margin-right: 15px;"
    >
      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item
              label="First Name"
              name="firstName"
              :rules="[{ required: true, message: 'Please input your first name!' }]"
          >
            <a-input v-model:value="registerFormState.firstName" style="width: 120px"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
              label="Last Name"
              name="lastName"
              :rules="[{ required: true, message: 'Please input your last name!' }]"
          >
            <a-input v-model:value="registerFormState.lastName" style="width: 120px"/>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row style="text-align: right">
        <a-form-item
            label="Username"
            name="username"
            :rules="[{ required: true, message: 'Please input your username!' }]"
        >
          <a-input v-model:value="registerFormState.username" style="width:354px"/>
        </a-form-item>

      </a-row>
      <a-row style="text-align: right">
        <a-form-item
            label="Email"
            name="email"
            :rules="[{ required: true, message: 'Please input your email!' }]"
        >
          <a-input v-model:value="registerFormState.email" style="width: 354px"/>
        </a-form-item>
      </a-row>
      <a-row>
        <a-form-item
            label="Password"
            name="password"
            :rules="[{ required: true, message: 'Please input your password!' }]"
        >
          <a-input-password v-model:value="registerFormState.password" style="width: 353px"/>
        </a-form-item>
      </a-row>
      <a-row>
        <a-form-item
            label="Confirm Password"
            name="confirmPassword"
            :rules="[{ required: true, message: 'Please input your password again!' }]"

        >
          <a-input-password v-model:value="registerFormState.password" style="width: 353px"/>
        </a-form-item>
      </a-row>
    </a-form>
  </a-modal>

  <a-layout>
    <a-layout-header class="header">
      <a-space :size=50>
        <a-space :size=30>
          <router-link :to="{name:'main'}" class="logo" @click='()=>{current=["main"]}'>The<span class="text-primary">Hotel</span>
          </router-link>
          <a-menu mode="horizontal" :style="{lineHeight:'80px'}" v-model:selectedKeys="current" :items="items"
                  @click="goto"/>
        </a-space>
        <div>
          <a-button type="link" :icon="h(LoginOutlined)" class="link-button" @click="()=>{loginModalOpen = true}">
            Login
          </a-button>
          <a-divider type="vertical"/>
          <a-button type="link" class="link-button" @click="()=>{registerModalOpen=true}">Register</a-button>
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

.footer {
  background: rgba(0, 0, 0, 0.8);
  text-align: center;
  vertical-align: center;
  color: white;
}

</style>