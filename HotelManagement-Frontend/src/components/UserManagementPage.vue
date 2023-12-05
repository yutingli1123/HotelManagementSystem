<script setup lang="ts">
import {computed, onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Ref} from "vue";
import Cookies from "js-cookie";
import {useRouter} from "vue-router";

const router = useRouter()

interface User {
  id: number;
  accountType: string;
  name: string;
  username: string;
  email: string;
  salary: number;
  password: string;
}

const users: Ref<User[]> = ref([])
const loading = ref(false)
const addLoading = ref(false)
const isEditModalVisible = ref(false)
const isAddModalVisible = ref(false)
const editFormData: Ref<User> = ref({})
const addFormData: Ref<User> = ref({})

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
    title: 'Salary',
    dataIndex: 'salary',
    key: 'salary',
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
    users.value.sort((a, b) => {
      return a.id - b.id
    })
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
    salary: user.salary,
    email: user.email,
  };
  isEditModalVisible.value = true;
};

const openAddModal = () => {
  addFormData.value = {
    username: '',
    firstName: '',
    lastName: '',
    accountType: '',
    email: '',
    salary: '',
    password: '',
  };
  isAddModalVisible.value = true;
}

const handleEditUser = () => {
  loading.value = true;
  if (editFormData.value.accountType == 'CUSTOMER') {
    axios.put('http://localhost:8080/api/v1/customers/update', {name: editFormData.value.firstName + ' ' + editFormData.value.lastName, username: editFormData.value.username, email: editFormData.value.email}, {
      headers: {
        Authorization: 'Bearer ' + token.value
      },
    }).then(response => {
      if (response.status === 200) {
        // Update the reservations array with the edited data
        fetchUsers();
        message.info('Update Successfully!')
        isEditModalVisible.value = false;
        editFormData.value = {}
      } else {
        message.error('Update Failed!')
      }
      loading.value = false
    }).catch(() => {
      message.error('Update Failed!')
      loading.value = false

    })
  } else if (editFormData.value.accountType == 'EMPLOYEE') {
    axios.put('http://localhost:8080/api/v1/employees/update', {name: editFormData.value.firstName + ' ' + editFormData.value.lastName, username: editFormData.value.username, email: editFormData.value.email, salary: editFormData.value.salary}, {
      headers: {
        Authorization: 'Bearer ' + token.value
      },
    }).then(response => {
      if (response.status === 200) {
        // Update the reservations array with the edited data
        fetchUsers();
        message.info('Update Successfully!')
        isEditModalVisible.value = false;
        editFormData.value = {}
      } else {
        message.error('Update Failed!')
      }
      loading.value = false

    }).catch(() => {
      message.error('Update Failed!')
      loading.value = false
    })
  } else if (editFormData.value.accountType == 'OWNER') {
    axios.put('http://localhost:8080/api/v1/owners/update', {name: editFormData.value.firstName + ' ' + editFormData.value.lastName, username: editFormData.value.username, email: editFormData.value.email}, {
      headers: {
        Authorization: 'Bearer ' + token.value
      },
    }).then(response => {
      if (response.status === 200) {
        // Update the reservations array with the edited data
        fetchUsers();
        message.info('Update Successfully!')
        isEditModalVisible.value = false;
        editFormData.value = {}
      } else {
        message.error('Update Failed!')
      }
      loading.value = false
    }).catch(() => {
      message.error('Update Failed!')
      loading.value = false
    })
  }
}

const handleAddUser = () => {
  addLoading.value = true
  axios.post('http://localhost:8080/api/v1/users/add', {name: addFormData.value.firstName + ' ' + addFormData.value.lastName, username: addFormData.value.username, email: addFormData.value.email, accountType: addFormData.value.accountType, password: addFormData.value.password, salary: addFormData.value.salary}, {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  }).then(response => {
    if (response.status === 200) {
      // Update the users array with the edited data
      fetchUsers();
      message.info('Add Successfully!')
      isAddModalVisible.value = false;
      addFormData.value = {}
    } else {
      message.error('Add Failed!')
    }
    addLoading.value = false
  }).catch((err) => {
    console.log(err)
    message.error('Add Failed!')
    addLoading.value = false
  })
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
  if (editFormData.value.accountType != 'EMPLOYEE') {
    return editFormData.value.firstName == '' || editFormData.value.lastName == '' || editFormData.value.email == ''
  } else {
    return editFormData.value.firstName == '' || editFormData.value.lastName == '' || editFormData.value.email == '' || editFormData.value.salary == ''
  }
})
const addSubmitDisabled = computed(() => {
  if (addFormData.value.accountType != 'EMPLOYEE') {
    return addFormData.value.firstName == '' || addFormData.value.lastName == '' || addFormData.value.email == '' || addFormData.value.username == '' || addFormData.value.password == ''
  } else {
    return addFormData.value.firstName == '' || addFormData.value.lastName == '' || addFormData.value.email == '' || addFormData.value.username == '' || addFormData.value.password == '' || addFormData.value.salary == ''
  }
})

const changePassData = ref({})
const isChangePassModalVisible = ref(false)
const changePassLoading = ref(false)
const openChangePassModal = () => {
  changePassData.value = {
    username: editFormData.value.username,
    newPass: ''
  }
  isChangePassModalVisible.value = true;
}

const handleChangePass = () => {
  changePassLoading.value = true;
  if (editFormData.value.accountType == 'CUSTOMER') {
    axios.put('http://localhost:8080/api/v1/customers/update/password', changePassData.value, {
      headers: {
        Authorization: 'Bearer ' + token.value
      },
    }).then(response => {
      if (response.status === 200) {
        // Update the reservations array with the edited data
        message.info('Update Successfully!')
        isChangePassModalVisible.value = false;
        changePassData.value = {}
      } else {
        message.error('Update Failed!')
      }
      changePassLoading.value = false
    }).catch(() => {
      message.error('Update Failed!')
      changePassLoading.value = false
    })
  } else if (editFormData.value.accountType == 'EMPLOYEE') {
    axios.put('http://localhost:8080/api/v1/employees/update/password', changePassData.value, {
      headers: {
        Authorization: 'Bearer ' + token.value
      },
    }).then(response => {
      if (response.status === 200) {
        // Update the reservations array with the edited data
        message.info('Update Successfully!')
        isChangePassModalVisible.value = false;
        changePassData.value = {}
      } else {
        message.error('Update Failed!')
      }
      changePassLoading.value = false
    }).catch(() => {
      message.error('Update Failed!')
      changePassLoading.value = false
    })
  } else if (editFormData.value.accountType == 'OWNER') {
    axios.put('http://localhost:8080/api/v1/owners/update/password', changePassData.value, {
      headers: {
        Authorization: 'Bearer ' + token.value
      },
    }).then(response => {
      if (response.status === 200) {
        // Update the reservations array with the edited data
        message.info('Update Successfully!')
        isChangePassModalVisible.value = false;
        changePassData.value = {}
      } else {
        message.error('Update Failed!')
      }
      changePassLoading.value = false
    }).catch(() => {
      message.error('Update Failed!')
      changePassLoading.value = false
    })
  }
}

const changePassSubmitDisabled = computed(() => {
  return changePassData.value.newPass == ''
})
</script>

<template>

  <a-modal
      v-model:open="isChangePassModalVisible"
      title="Change Password"
      :closable="false"
      :mask-closable="false"
  >
    <template #footer>
      <a-button key="changePassBack" @click="() => {isChangePassModalVisible = false}">Cancel</a-button>
      <a-button key="changePassSubmit" type="primary" :loading="changePassLoading" :disabled="changePassSubmitDisabled"
                @click="handleChangePass">Update
      </a-button>
    </template>
    <a-form
        :model="changePassData"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 8 }"
    >

      <a-form-item label="New Password">
        <a-input v-model:value="changePassData.newPass"></a-input>
      </a-form-item>

    </a-form>

  </a-modal>
  <a-modal
      v-model:open="isEditModalVisible"
      title="Edit User"
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

      <a-form-item label="Salary" v-if="editFormData.accountType == 'EMPLOYEE'">
        <a-input-number v-model:value="editFormData.salary"/>
      </a-form-item>

    </a-form>
    <a-button style="margin-left: 30px" @click="openChangePassModal">Change Password</a-button>

  </a-modal>

  <a-modal
      v-model:open="isAddModalVisible"
      title="Add User"
      :closable="false"
      :mask-closable="false"
  >
    <template #footer>
      <a-button key="addBack" @click="() => {isAddModalVisible = false}">Cancel</a-button>
      <a-button key="addSubmit" type="primary" :loading="addLoading" :disabled="addSubmitDisabled"
                @click="handleAddUser">Add
      </a-button>
    </template>
    <a-form
        :model="addFormData"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 8 }"
    >

      <a-form-item label="First Name">
        <a-input v-model:value="addFormData.firstName"></a-input>
      </a-form-item>

      <a-form-item label="Last Name">
        <a-input v-model:value="addFormData.lastName"></a-input>
      </a-form-item>

      <a-form-item label="User Name">
        <a-input v-model:value="addFormData.username"></a-input>
      </a-form-item>

      <a-form-item label="Account Type">
        <a-select v-model:value="addFormData.accountType">
          <a-select-option value="CUSTOMER">Customer</a-select-option>
          <a-select-option value="EMPLOYEE">Employee</a-select-option>
          <a-select-option value="OWNER">Owner</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="Email">
        <a-input v-model:value="addFormData.email"/>
      </a-form-item>

      <a-form-item label="Password">
        <a-input-password v-model:value="addFormData.password"/>
      </a-form-item>

      <a-form-item label="Salary" v-if="addFormData.accountType == 'EMPLOYEE'">
        <a-input-number v-model:value="addFormData.salary"/>
      </a-form-item>
    </a-form>
  </a-modal>

  <a-button type="primary" @click="openAddModal()" style="margin-left: 5px">Add User</a-button>
  <div style="height: 20px"/>
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