<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {reactive, ref} from "vue";
import type {Ref} from 'vue'
import {message} from "ant-design-vue";
import {useStore} from "@/stores/stateStore";
import axios from "axios";
import Cookies from "js-cookie";
import dayjs from "dayjs";
import {onMounted} from "vue";

const route = useRoute()

const checkIn = route.query.checkIn
const checkOut = route.query.checkOut
const guests = route.query.guests
const roomType = route.query.type

const loginLoading = ref(false)
const registerLoading = ref(false)

const token: Ref<string> = ref(Cookies.get('token'))
const refresh_token: Ref<string> = ref(Cookies.get('refresh_token'))
const router = useRouter()

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
})

interface LoginFormState {
  username: string;
  password: string;
}

const loginFormState = reactive<LoginFormState>({
  username: '',
  password: '',
});

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

const store = useStore()
const login = ref(store.login)
const onLogin = (value) => {
  loginLoading.value = true
  axios
      .post("http://localhost:8080/api/v1/login", {
        username: value.username,
        password: value.password,
      })
      .then((response) => {
        if (response.status == 200) {
          const tokenObtain = response.data['token']
          const refreshTokenObtain = response.data['refresh_token']
          Cookies.set('token', tokenObtain, {expires: new Date(new Date().getTime() + 15 * 60 * 1000)});
          Cookies.set('refresh_token', refreshTokenObtain, {expires: new Date(new Date().getTime() + 20 * 60 * 1000)});
          loginFormState.username = ''
          loginFormState.password = ''
          token.value = tokenObtain
          refresh_token.value = refreshTokenObtain
          login.value = true
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
}

const onRegister = (value) => {
  registerLoading.value = true
  axios
      .post("http://localhost:8080/api/v1/register", {
        name: value.firstName + ' ' + value.lastName,
        password: value.password,
        username: value.username,
        email: value.email,
      })
      .then((response) => {
        if (response.status == 200) {
          axios
              .post("http://localhost:8080/api/v1/login", {
                username: value.username,
                password: value.password,
              })
              .then((response) => {
                if (response.status == 200) {
                  const tokenObtain = response.data['token']
                  const refreshTokenObtain = response.data['refresh_token']
                  Cookies.set('token', tokenObtain, {expires: new Date(new Date().getTime() + 15 * 60 * 1000)});
                  Cookies.set('refresh_token', refreshTokenObtain, {expires: new Date(new Date().getTime() + 20 * 60 * 1000)});
                  registerFormState.password = ''
                  registerFormState.username = ''
                  registerFormState.email = ''
                  registerFormState.lastName = ''
                  registerFormState.firstName = ''
                  registerFormState.confirmPassword = ''
                  store.changeToLogin()
                  token.value = tokenObtain
                  refresh_token.value = refreshTokenObtain
                  login.value = true
                } else {
                  message.error('Login Failed!')
                }
                registerLoading.value = false
              })
              .catch((err) => {
                if (err.response.status == 401) {
                  message.error('Login Failed!')
                } else {
                  message.error('Internal Server Error!')
                }
                registerLoading.value = false
              });
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
}

const confirm = () => {
  axios.post('http://localhost:8080/api/v1/reservations/book', {
    'checkInDate': dayjs(checkIn).toISOString(),
    'checkOutDate': dayjs(checkOut).toISOString(),
    'roomType': roomType
  }, {
    headers: {
      Authorization: 'Bearer ' + token.value
    }
  }).then(response => {
    if (response.status == 200) {
      message.info('Book Successfully!')
      router.push({name: 'success'})
    } else {
      message.error('Book Failed!')
    }
  }).catch(() => {
    message.error('Book Failed!')
  })
}
</script>

<template>
  <div style="position: relative">
    <img class="background_image" src="/regular.jpg" alt="Background Image"/>
    <div class="overlayer"/>
  </div>
  <div style="height: 30px"/>

  <div v-if="!login">
    <a-row style="display: flex;justify-content: center">
      <a-col>
        <a-card>
          <a-row style="display: flex;justify-content: center;margin-bottom: 20px">
            <p style="font-size: 24px; font-weight: bold">Login</p>
          </a-row>
          <a-row>
            <a-form :model="loginFormState"
                    autocomplete="off"
                    :label-col="{ span: 8 }"
                    :wrapper-col="{ span: 12 }"
                    @finish="onLogin"
            >
              <a-form-item label="Username"
                           name="username"
                           :rules="[{ required: true, message: 'Please input your username!' }]">
                <a-input v-model:value="loginFormState.username"/>
              </a-form-item>
              <a-form-item label="Password"
                           name="password"
                           :rules="[{ required: true, message: 'Please input your username!' }]">
                <a-input-password v-model:value="loginFormState.password"/>
              </a-form-item>
              <a-form-item :wrapper-col="{ offset: 14, span: 16 }">
                <a-button type="primary" html-type="submit" :loading="loginLoading">Login</a-button>
              </a-form-item>
            </a-form>
          </a-row>
        </a-card>
      </a-col>
      <a-col>
        <a-row>
          <p style="font-size: 24px; margin-left: 50px;margin-right: 50px;margin-top: 55px">or</p>
        </a-row>
        <a-row>
          <a-divider type="vertical"/>
        </a-row>
      </a-col>
      <a-col>
        <a-card>
          <a-row style="display: flex;justify-content: center;margin-bottom: 20px">
            <p style="font-size: 24px; font-weight: bold">Register</p>
          </a-row>
          <a-row>
            <a-form :model="registerFormState"
                    autocomplete="off"
                    :label-col="{ span: 10 }"
                    :wrapper-col="{ span: 12 }"
                    @finish="onRegister"
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
                  :rules="[{ required: true, message: 'Please input your password again!' },
                  {validator: (rule, value, callback) => {
            if (value != registerFormState.password) {
              callback('Two passwords don\'t match!')
            } else {
              callback()
            }
          }}]">
                <a-input-password v-model:value="registerFormState.confirmPassword"/>
              </a-form-item>
              <a-form-item :wrapper-col="{ offset: 16, span: 16 }">
                <a-button type="primary" html-type="submit" :loading="registerLoading">Register</a-button>
              </a-form-item>
            </a-form>
          </a-row>
        </a-card>
      </a-col>
    </a-row>
  </div>

  <div v-else style="display: flex;justify-content: center">
    <a-card>
      <a-row>
        <a-col>
          <a-card hoverable style="width: 400px;">
            <template #cover>
              <img alt="Room Image"
                   :src="roomType == 'REGULAR'? 'regular.jpg': roomType == 'DELUXE'? 'deluxe.jpg':roomType == 'DOUBLE'? 'double.jpg':''"/>
            </template>
            <a-card-meta
                :title="roomType == 'REGULAR' ? 'Regular Room' : roomType == 'DELUXE' ? 'Deluxe Room' : roomType == 'DOUBLE' ? 'Double Room' : ''">
              <template #description>
                {{
                  roomType == 'REGULAR' ? 'Room with one queen bed. Amenities include a private washroom with shower.' : roomType == 'DELUXE' ? 'Larger room with one queen bed. Amenities include a private washroom with shower, a sofa, a working desk with chair.' : roomType == 'DOUBLE' ? 'Room with two queen beds. Amenities include a private washroom with shower and a sofa.' : ''
                }}
              </template>
            </a-card-meta>
          </a-card>
        </a-col>
        <a-divider type="vertical" style="height: 400px;margin-left: 30px;margin-right: 30px"/>
        <a-col>
          <a-card>
            <a-col>
              <a-row>
                <a-col>
                  <a-row>
                    <p style="font-size: 16px;font-weight: bold">Check-In Date</p>
                  </a-row>
                  <a-row>
                    {{ dayjs(checkIn).format('MMM/DD/YYYY') }}
                  </a-row>
                </a-col>
              </a-row>
              <a-divider/>

              <a-row>
                <a-col>
                  <a-row>
                    <p style="font-size: 16px;font-weight: bold">Check-Out Date</p>
                  </a-row>
                  <a-row>
                    {{ dayjs(checkOut).format('MMM/DD/YYYY') }}
                  </a-row>
                </a-col>
              </a-row>
              <a-divider/>
              <a-row>
                <a-col>
                  <a-row>
                    <p style="font-size: 16px;font-weight: bold">Guests</p>
                  </a-row>
                  <a-row>
                    {{ guests }}
                  </a-row>
                </a-col>
              </a-row>
            </a-col>
          </a-card>
        </a-col>
        <div style="margin: 20px"/>
        <a-col style="display: flex;align-items: center">
          <a-button type="primary" @click="confirm">Confirm Booking</a-button>
        </a-col>
      </a-row>
    </a-card>
  </div>
  <div style="margin-bottom: 30px"></div>
</template>

<style scoped>
.background_image {
  width: 100%;
  height: 230px;
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
</style>