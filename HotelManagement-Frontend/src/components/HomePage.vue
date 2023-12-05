<script setup lang="ts">
import {computed, h, onMounted, reactive, Ref, ref} from "vue";
import {ControlOutlined, LoginOutlined, LogoutOutlined, UserOutlined} from "@ant-design/icons-vue";
import type {FormInstance, MenuProps} from "ant-design-vue";
import {useRoute, useRouter} from "vue-router";
import axios from "axios";
import Cookies from 'js-cookie';
import {message} from 'ant-design-vue';
import {useStore} from "@/stores/stateStore";

const router = useRouter()

const route = useRoute()

const current_route = ref(route.name ? route.name.toString() : 'main')

const loginFormRef: Ref<FormInstance> = ref<FormInstance>();
const registerFormRef: Ref<FormInstance> = ref<FormInstance>();
const token: Ref<string> = ref(Cookies.get('token'))
const refresh_token: Ref<string> = ref(Cookies.get('refresh_token'))
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
}

const loginFormState = reactive<LoginFormState>({
  username: '',
  password: '',
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
            const token = response.data['token']
            Cookies.set('token', token, {expires: new Date(new Date().getTime() + 15*60 * 1000)});
            Cookies.set('refresh_token', response.data['refresh_token'], {expires: new Date(new Date().getTime() + 20 * 60 * 1000)});
            loginLoading.value = false;
            loginModalOpen.value = false;
            loginFormState.username = ''
            loginFormState.password = ''
            store.changeToLogin()
          } else {
            message.error('Login Failed!')
          }
          loginLoading.value = false
        })
        .catch((err) => {
          if (err.response.status == 401) {
            message.error('Login Failed!')
          } else {
            message.error('Internal Server Error!')
          }
          loginLoading.value = false
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
            registerFormState.password = ''
            registerFormState.username = ''
            registerFormState.email = ''
            registerFormState.lastName = ''
            registerFormState.firstName = ''
            registerFormState.confirmPassword = ''
            loginModalOpen.value = true;
          } else {
            message.error('Register Failed!')
          }
          registerLoading.value = false
        })
        .catch((err) => {
          if (err.response.status == 400) {
            message.error('Register Failed!')
          } else {
            message.error('Internal Server Error!')
          }
          registerLoading.value = false
        });

  })
}
const store = useStore()
const login = computed(() => store.login)
const role = computed(()=> store.role)

const logout = () => {
  Cookies.remove('token')
  Cookies.remove('refresh_token')
  store.changeToLogout()
  if (current_route.value != 'main' && current_route.value != 'rooms' && current_route.value != 'contact') {
    router.push({name: 'main'})
  }
}

onMounted(() => {
  if (token.value == null) {
    if (refresh_token.value != null) {
      axios.post('http://localhost:8080/api/v1/refresh', refresh_token.value).then((response) => {
        if (response.status == 200) {
          token.value = response.data['token']
          refresh_token.value = response.data['refresh_token']
          Cookies.set('token', token.value, {expires: new Date(new Date().getTime() + 15 *60* 1000)});
          Cookies.set('refresh_token', refresh_token.value, {expires: new Date(new Date().getTime() + 20 * 60 * 1000)});
          store.changeToLogin()
        }
      })
    }
  }
})
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
          :rules="[{ required: true, message: 'Please input your password!' },{min:8,message: 'Password must be at least 8 characters!'},{max:16, message: 'Password can only be at most 16 characters!'}]"
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
          <router-link :to="{name:'main'}" class="logo">Marwan<span class="text-primary">Continental</span></router-link>
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
        <div v-else>
          <a-button v-if="role==='CUSTOMER'" type="link" :icon="h(UserOutlined)" class="link-button"
                    @click="()=>{router.push({name:'info'})}">
            My Account
          </a-button>
          <a-button v-else-if="role=='EMPLOYEE' || role == 'OWNER'" type="link" :icon="h(ControlOutlined)"
                    class="link-button"
                    @click="()=>{router.push({name:'reservations-manage'})}">
            Manage Hotel
          </a-button>
          <a-button type="link" :icon="h(LogoutOutlined)" class="link-button"
                    @click="logout">
            Logout
          </a-button>
        </div>
      </a-space>
    </a-layout-header>
    <a-layout-content>
      <RouterView :key="$route.fullPath"/>
    </a-layout-content>
    <a-layout-footer class="footer">
      ©2023 MarwanContinental
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