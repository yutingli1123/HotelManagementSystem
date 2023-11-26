<script setup lang="ts">
import {computed, h, reactive, ref} from "vue";
import {LoginOutlined} from "@ant-design/icons-vue";
import type {FormInstance, MenuProps} from "ant-design-vue";
import {useRoute, useRouter} from "vue-router";
import axios from "axios";
import Cookies from 'js-cookie';
import {message} from 'ant-design-vue';

const router = useRouter()

const route = useRoute()

const current_route = ref(route.name ? route.name.toString() : 'main')

const loginFormRef: FormInstance = ref<FormInstance>();
const registerFormRef: FormInstance = ref<FormInstance>();

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

const goto = (input: {
  item: object,
  key: string,
  keyPath: object
}) => {
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
  confirmPassword: string,
}

const registerFormState = reactive<RegisterFormState>({
  firstName: '',
  lastName: '',
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
});

const registerDisabled = computed(() => {
  return !(registerFormState.username && registerFormState.password && registerFormState.firstName && registerFormState.lastName && registerFormState.email && registerFormState.confirmPassword);
});

const onLogin = () => {
  loginLoading.value = true
  loginFormRef.value.validateFields().then(() => {
    axios
        .post("http://localhost:8080/api/v1/login", {
          username: loginFormState.username,
          password: loginFormState.password,
        })
        .then((response) => {
          if (response.status == 200) {
            console.log(response.data)
            Cookies.set('token', response.data['token'], {expires: new Date(new Date().getTime() + 15 * 60 * 1000)});
            Cookies.set('refresh_token', response.data['refresh_token'], {expires: new Date(new Date().getTime() + 20 * 60 * 1000)});
            loginLoading.value = false;
            loginModalOpen.value = false;
            loginFormState.username = ''
            loginFormState.password = ''
            login.value = true
          } else {
            loginLoading.value = false;
            message.error('Login Failed!')
          }
        })
        .catch((err) => {
          loginLoading.value = false
          if (err.response.status == 401) {
            message.error('Login Failed!')
          } else {
            message.error('Internal Server Error!')
          }
        });
  })
}

const onRegister = () => {
  registerLoading.value = true
  registerFormRef.value.validateFields().then(() => {
    axios
        .post("http://localhost:8080/api/v1/register", {
          name: registerFormState.firstName + ' ' + registerFormState.lastName,
          password: registerFormState.password,
          username: registerFormState.username,
          email: registerFormState.email,
        })
        .then((response) => {
          if (response.status == 200) {
            loginFormState.username = registerFormState.username;
            loginFormState.password = registerFormState.password;
            registerModalOpen.value = false;
            loginModalOpen.value = true;
          } else {
            registerLoading.value = false
          }
        })
        .catch((err) => {
          registerLoading.value = false
          if (err.response.status == 400) {
            message.error('Register Failed!')
          } else {
            message.error('Internal Server Error!')
          }
        });
  })
}
const login = ref(Cookies.get('token') != null)
</script>

<template>
  <a-modal v-model:open="loginModalOpen" title="Login" :mask-closable=false :closable=false>
    <template #footer>
      <a-button key="loginBack" @click="handleLoginCancel">Cancel</a-button>
      <a-button key="loginSubmit" type="primary" :loading="loginLoading" :disabled="loginDisabled" @click="onLogin">
        Login
      </a-button>
    </template>
    <div style="height: 20px"></div>
    <a-form :model="loginFormState"
            ref="loginFormRef"
            :label-col="{ span: 8 }"
            :wrapper-col="{ span: 12 }"
            autocomplete="off"
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

  <a-modal v-model:open="registerModalOpen" title="Register" :mask-closable=false :closable=false>
    <template #footer>
      <a-button key="registerBack" @click="handleRegisterCancel">Cancel</a-button>
      <a-button key="registerSubmit" type="primary" :loading="registerLoading" :disabled="registerDisabled"
                @click="onRegister">Register
      </a-button>
    </template>
    <div style="height: 20px"></div>
    <a-form :model="registerFormState"
            ref="registerFormRef"
            autocomplete="off"
            :label-col="{ span: 8 }"
            :wrapper-col="{ span: 12 }"
    >
      <a-form-item
          label="First Name"
          name="firstName"
          :rules="[{ required: true, message: 'Please input your first name!' }]"
      >
        <a-input v-model:value="registerFormState.firstName"/>
      </a-form-item>
      <a-form-item
          label="Last Name"
          name="lastName"
          :rules="[{ required: true, message: 'Please input your last name!' }]"
      >
        <a-input v-model:value="registerFormState.lastName"/>
      </a-form-item>

      <a-form-item
          label="Username"
          name="username"
          :rules="[{ required: true, message: 'Please input your username!' }]"
      >
        <a-input v-model:value="registerFormState.username"/>
      </a-form-item>

      <a-form-item
          label="Email"
          name="email"
          :rules="[{ required: true, message: 'Please input your email!' }, {type: 'email', message: 'Not a valid email!'}]"
      >
        <a-input v-model:value="registerFormState.email"/>
      </a-form-item>

      <a-form-item
          label="Password"
          name="password"
          :rules="[{ required: true, message: 'Please input your password!' }]"
      >
        <a-input-password v-model:value="registerFormState.password"/>
      </a-form-item>

      <a-form-item
          label="Confirm Password"
          name="confirmPassword"
          :rules="[{ required: true, message: 'Please input your password again!' },  {validator: (rule, value, callback) => {
            if (value != registerFormState.password) {
              callback('Two passwords don\'t match!')
            } else {
              callback()
            }
          }}]"
      >
        <a-input-password v-model:value="registerFormState.confirmPassword"/>
      </a-form-item>
    </a-form>
  </a-modal>

  <a-layout>
    <a-layout-header class="header">
      <a-space :size=50>
        <a-space :size=30>
          <router-link :to="{name:'main'}" class="logo">The<span class="text-primary">Hotel</span></router-link>
          <a-menu mode="horizontal" :style="{lineHeight:'80px'}" v-model:selectedKeys="current" :items="items"
                  @click="goto"/>
        </a-space>
        <div v-if="!login">
          <a-button type="link" :icon="h(LoginOutlined)" class="link-button" @click="()=>{loginModalOpen = true}">
            Login
          </a-button>
          <a-divider type="vertical"/>
          <a-button type="link" class="link-button" @click="()=>{registerModalOpen=true}">Register</a-button>
        </div>
        <div v-if="login">
          <a-button type="link" :icon="h(LoginOutlined)" class="link-button"
                    @click="()=>{router.push({name:'account'})}">
            My Account
          </a-button>
        </div>
      </a-space>
    </a-layout-header>
    <a-layout-content>
      <RouterView :key="$route.fullPath"/>
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