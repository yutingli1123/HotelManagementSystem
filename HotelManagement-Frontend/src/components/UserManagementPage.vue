<script setup lang="ts">
import {computed, onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Ref} from "vue";
import Cookies from "js-cookie";
import router from "@/router";

interface User {
  id: number;
  accountType: string;
  name: string;
  username: string;
  email: string;
}

const users: Ref<User[]> = ref([])
const loading = ref(false)
const isEditModalVisible = ref(false)
const editFormData = ref({})

const token: Ref<string> = ref(Cookies.get('token'))
const refresh_token: Ref<string> = ref(Cookies.get('refresh_token'))


const columns = [
  {
    title: 'User ID',
    dataIndex: 'id',
    key: 'id',
  },
  {
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: 'Username',
    dataIndex: 'username',
    key: 'username',
  },
  {
    title: 'Account Type',
    dataIndex: 'accountType',
    key: 'accountType',
  },
  {
    title: 'Email',
    dataIndex: 'email',
    key: 'email',
  },

  {
    title: 'Action',
    key: 'action',
  },
];

const fetchUsers = () => {
  loading.value = true;
  axios.get('http://localhost:8080/api/v1/users', {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  }).then(response => {
    if (response.status == 200) {
      users.value = response.data;
    }
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
};


const deleteUser = (id, accountType) => {
  if (accountType == 'CUSTOMER') {
    axios.delete("http://localhost:8080/api/v1/customers/by-id/" + id, {
      headers: {
        Authorization: 'Bearer ' + token.value
      },
    })
        .then((response) => {
          if (response.status == 200) {
            message.info('Delete Successfully!')
            fetchUsers()
          } else {
            message.error("Delete Failed!")
          }
        })
        .catch(() => message.error("Delete Failed!"))
  } else if (accountType == 'EMPLOYEE') {
    axios.delete("http://localhost:8080/api/v1/employees/by-id/" + id, {
      headers: {
        Authorization: 'Bearer ' + token.value
      },
    })
        .then((response) => {
          if (response.status == 200) {
            message.info('Delete Successfully!')
            fetchUsers()
          } else {
            message.error("Delete Failed!")
          }
        })
        .catch(() => message.error("Delete Failed!"))
  } else if (accountType == 'OWNER') {
    axios.delete("http://localhost:8080/api/v1/owners/by-id/" + id, {
      headers: {
        Authorization: 'Bearer ' + token.value
      },
    })
        .then((response) => {
          if (response.status == 200) {
            message.info('Delete Successfully!')
            fetchUsers()
          } else {
            message.error("Delete Failed!")
          }
        })
        .catch(() => message.error("Delete Failed!"))
  } else {
    message.error("Delete Failed!")
  }
}

const openEditModal = (user: User) => {
  const nameList = user.name.split(' ')

  editFormData.value = {
    username: user.username,
    firstName: nameList[0],
    lastName: nameList[1],
    accountType: user.accountType,
    email: user.email,
  };
  isEditModalVisible.value = true;
};

const handleEditUser = () => {
  if (editFormData.value.accountType == 'CUSTOMER') {
    axios.put('http://localhost:8080/api/v1/customers/update', {name: editFormData.value.firstName + ' ' + editFormData.value.lastName, username: editFormData.value.username, email: editFormData.value.email}, {headers: {
        Authorization: 'Bearer ' + token.value
      },}).then(response => {
      if (response.status === 200) {
        // Update the reservations array with the edited data
        fetchUsers();
        message.info('Update Successfully!')
        isEditModalVisible.value = false;
        editFormData.value = {}
      } else {
        message.error('Update Failed!')
      }
    }).catch(() => {
      message.error('Update Failed!')
    })
  } else if (editFormData.value.accountType == 'EMPLOYEE') {
    axios.put('http://localhost:8080/api/v1/employees/update', {name: editFormData.value.firstName + ' ' + editFormData.value.lastName, username: editFormData.value.username, email: editFormData.value.email},{headers: {
        Authorization: 'Bearer ' + token.value
      },}).then(response => {
      if (response.status === 200) {
        // Update the reservations array with the edited data
        fetchUsers();
        message.info('Update Successfully!')
        isEditModalVisible.value = false;
        editFormData.value = {}
      } else {
        message.error('Update Failed!')
      }
    }).catch(() => {
      message.error('Update Failed!')
    })
  } else if (editFormData.value.accountType == 'OWNER') {
    axios.put('http://localhost:8080/api/v1/owners/update', {name: editFormData.value.firstName + ' ' + editFormData.value.lastName, username: editFormData.value.username, email: editFormData.value.email},{headers: {
        Authorization: 'Bearer ' + token.value
      },}).then(response => {
      if (response.status === 200) {
        // Update the reservations array with the edited data
        fetchUsers();
        message.info('Update Successfully!')
        isEditModalVisible.value = false;
        editFormData.value = {}
      } else {
        message.error('Update Failed!')
      }
    }).catch(() => {
      message.error('Update Failed!')
    })
  }
}

onMounted(() => {
  if (token.value == null) {
    if (refresh_token.value != null) {
      axios.post('http://localhost:8080/api/v1/refresh', refresh_token.value).then((response) => {
        if (response.status == 200) {
          token.value = response.data['token']
          refresh_token.value = response.data['refresh_token']
          Cookies.set('token', token.value, {expires: new Date(new Date().getTime() + 15 * 60 * 1000)});
          Cookies.set('refresh_token', refresh_token.value, {expires: new Date(new Date().getTime() + 20 * 60 * 1000)});
          store.changeToLogin()
          fetchUsers()
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
    fetchUsers();
  }
});
const submitDisabled = computed(() => {
  return editFormData.value.firstName == '' || editFormData.value.lastName == '' || editFormData.value.email == ''
})
</script>

<template>
  <a-modal
      v-model:open="isEditModalVisible"
      title="Edit Reservation"
      :closable="false"
      :mask-closable="false"
  >
    <template #footer>
      <a-button key="registerBack" @click="() => {isEditModalVisible = false}">Cancel</a-button>
      <a-button key="registerSubmit" type="primary" :loading="loading" :disabled="submitDisabled"
                @click="handleEditUser">Update
      </a-button>
    </template>
    <a-form
        :model="editFormData"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 8 }"
    >

      <a-form-item label="First Name">
        <a-input v-model:value="editFormData.firstName"></a-input>
      </a-form-item>

      <a-form-item label="Last Name">
        <a-input v-model:value="editFormData.lastName"></a-input>
      </a-form-item>

      <a-form-item label="Email">
        <a-input v-model:value="editFormData.email"/>
      </a-form-item>


    </a-form>
  </a-modal>


  <a-table
      :columns="columns"
      :rowKey="record => record.id"
      :dataSource="users"
      :loading="loading"
  >
    <template #bodyCell="{ column, record }">
      <template v-if="column.key === 'action'">
        <a-space>
          <a-button type="primary" @click="openEditModal(record)">Edit</a-button>
          <a-popconfirm
              title="Are you sure to delete this reservation?"
              @confirm="() => deleteUser(record.id, record.accountType)"
          >
            <a-button danger>Delete</a-button>
          </a-popconfirm>
        </a-space>
      </template>
    </template>
  </a-table>

</template>


<style scoped>

</style>