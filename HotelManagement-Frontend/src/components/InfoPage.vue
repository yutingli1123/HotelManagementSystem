<script setup lang="ts">
import {computed, onMounted, reactive, Ref, ref} from "vue";
import Cookies from "js-cookie";
import axios, {AxiosResponse} from "axios";
import {FormInstance, message} from "ant-design-vue";
import {jwtDecode} from "jwt-decode";
import dayjs from "dayjs";
import router from "@/router";
import {useStore} from "@/stores/stateStore";

interface InfoFormState {
  firstname: string;
  lastname: string;
  email: string;
  username: string;
}

interface Customer {
  name: string;
  username: string;
  email: string;
}

const infoFormState = reactive<InfoFormState>({
  firstname: '',
  lastname: '',
  email: '',
  username: '',
});
const store = useStore()
const token: Ref<string> = ref(Cookies.get('token'))
const refresh_token: Ref<string> = ref(Cookies.get('refresh_token'))
const loading: Ref<boolean> = ref(true)
const updateInfoLoading = ref(false)
const updateInfoDisabled = computed(() => {
  return infoFormState.email == '' || infoFormState.firstname == '' || infoFormState.lastname == ''
})

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
          axios.get('http://localhost:8080/api/v1/customers/me', {
            headers: {
              Authorization: 'Bearer ' + token.value
            },
          }).then((response: AxiosResponse<Customer>) => {
            if (response.status == 200) {
              const nameList = response.data.name.split(' ')
              infoFormState.firstname = nameList[0]
              infoFormState.lastname = nameList[1]
              infoFormState.email = response.data.email
              infoFormState.username = response.data.username
              loading.value = false
            }
          }).catch(() => {
            loading.value = false
          })
        } else {
          router.push({name: 'main'})
        }
      }).catch(() => {
        router.push({name: 'main'})
      })
    } else {
      router.push({name: 'main'})
    }
  } else {
    axios.get('http://localhost:8080/api/v1/customers/me', {
      headers: {
        Authorization: 'Bearer ' + token.value
      },
    }).then((response: AxiosResponse<Customer>) => {
      if (response.status == 200) {
        const nameList = response.data.name.split(' ')
        infoFormState.firstname = nameList[0]
        infoFormState.lastname = nameList[1]
        infoFormState.email = response.data.email
        infoFormState.username = response.data.username
        loading.value = false
      }
    }).catch(() => {
      loading.value = false
    })
  }
})

const onFinish = (value: InfoFormState) => {
  updateInfoLoading.value = true
  if (dayjs().isAfter(dayjs.unix(jwtDecode(token.value).exp))) {
    if (dayjs().isBefore(dayjs.unix(jwtDecode(refresh_token.value).exp))) {
      axios.post('http://localhost:8080/api/v1/refresh', refresh_token.value).then((response) => {
        if (response.status == 200) {
          token.value = response.data
        } else {
          router.push({name: 'main'})
        }
      }).catch(() => {
        router.push({name: 'main'})
      })
    } else {
      router.push({name: 'main'})
    }
  } else {
    axios.put('http://localhost:8080/api/v1/customers/update', {'name': value.firstname + " " + value.lastname, 'username': value.username, 'email': value.email}, {
      headers: {
        Authorization: 'Bearer ' + token.value
      },
    }).then((response) => {
      if (response.status == 200) {
        message.info('Update successfully!')
        router.push({name: 'success'})
      } else {
        message.error('Update failed!')
      }
      updateInfoLoading.value = false
    }).catch(() => {
      message.error('Update failed!')
      updateInfoLoading.value = false
    })
  }

}

const changePasswordOpen = ref(false)
const updatePassLoading = ref(false)
const changePasswordDisabled = computed(() => {
  return changePassFormState.oldPass == '' || changePassFormState.newPass == '' || changePassFormState.confirmPass == ''
})

const onChangePassword = () => {
  updatePassLoading.value = true
  changePassFormRef.value.validateFields().then(() => {
    axios.put('http://localhost:8080/api/v1/customers/update/password', {
          'oldPass': changePassFormState.oldPass,
          'newPass': changePassFormState.newPass,
        }, {
          headers: {
            Authorization: 'Bearer ' + token.value
          }
        }
    ).then((response) => {
      if (response.status == 200) {
        message.info('Change Successfully!')
        changePasswordOpen.value = false
        changePassFormState.newPass = ''
        changePassFormState.oldPass = ''
        changePassFormState.confirmPass = ''
      } else {
        message.error('Change Failed!')
      }
      updatePassLoading.value = false
    }).catch(() => {
      updatePassLoading.value = false
    })
  })
}

interface ChangePassFormState {
  oldPass: string,
  newPass: string,
  confirmPass: string
}

const changePassFormState = reactive<ChangePassFormState>({
  oldPass: '',
  newPass: '',
  confirmPass: '',
})
const changePassFormRef: Ref<FormInstance> = ref<FormInstance>();
</script>

<template>
  <a-modal :closable="false" :mask-closable="false" v-model:open="changePasswordOpen" title="Change Password">
    <template #footer>
      <a-button key="changePassBack" @click="()=>{changePasswordOpen = false}">Cancel</a-button>
      <a-button key="changePassSubmit" type="primary" :loading="updatePassLoading" :disabled="changePasswordDisabled"
                @click="onChangePassword">
        Change
      </a-button>
    </template>
    <a-form :model="changePassFormState" :label-col="{ span: 8 }"
            :wrapper-col="{ span: 14 }" ref="changePassFormRef">
      <a-form-item
          name="oldPass"
          label="Current Password"
          :rules="[{ required: true, message: 'Please input your current password!' }]"
      >
        <a-input-password v-model:value="changePassFormState.oldPass"></a-input-password>
      </a-form-item>
      <a-form-item
          name="newPass"
          label="New Password"
          :rules="[{ required: true, message: 'Please input your new password!' }]"
      >
        <a-input-password v-model:value="changePassFormState.newPass"></a-input-password>
      </a-form-item>
      <a-form-item
          name="confirmPass"
          label="Confirm Password"
          :rules="[{ required: true, message: 'Please input your new password again!' }]"
      >
        <a-input-password v-model:value="changePassFormState.confirmPass"></a-input-password>
      </a-form-item>
    </a-form>
  </a-modal>
  <a-form layout="vertical" style="margin: 30px" :model="infoFormState" @finish="onFinish">
    <a-row :gutter="24">
      <a-col :span="6">
        <a-form-item
            name="username"
            label="Username"
        >
          <a-input v-model:value="infoFormState.username" :disabled="true"></a-input>
        </a-form-item>
      </a-col>
    </a-row>
    <a-row :gutter="24">
      <a-col :span="6">
        <a-form-item
            name="firstname"
            label="First Name"
            :rules="[{ required: true, message: 'First name cannot be empty!' }]"
        >
          <a-input v-model:value="infoFormState.firstname"></a-input>
        </a-form-item>
      </a-col>
      <a-col :span="6">
        <a-form-item
            name="lastname"
            label="Last Name"
            :rules="[{ required: true, message: 'Last name cannot be empty!' }]"
        >
          <a-input v-model:value="infoFormState.lastname"></a-input>
        </a-form-item>
      </a-col>
    </a-row>

    <a-row :gutter="24">
      <a-col :span="6">
        <a-form-item
            name="email"
            label="Email"
            :rules="[{ required: true, message: 'Email cannot be empty' }, {type: 'email', message: 'Not a valid email!'}]"
        >
          <a-input v-model:value="infoFormState.email"></a-input>
        </a-form-item>
      </a-col>
    </a-row>
    <a-row :gutter="24">
      <a-space :size="350">
        <a-col>
          <a-button style="margin-top: 20px" @click="()=>{changePasswordOpen = true}">Change Password</a-button>
        </a-col>
        <a-col>
          <a-button style="margin-top: 20px" type="primary" html-type="submit" :loading="updateInfoLoading"
                    :disabled="updateInfoDisabled">Update
          </a-button>
        </a-col>
      </a-space>
    </a-row>
  </a-form>
</template>

<style scoped>

</style>