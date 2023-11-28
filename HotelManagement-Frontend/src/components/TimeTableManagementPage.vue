<script setup lang="ts">
import {onMounted, ref} from 'vue';
import axios, {AxiosResponse} from 'axios';
import {message} from "ant-design-vue";
import {Ref} from "vue";
import Cookies from "js-cookie";
import router from "@/router";

interface TimeTable {
  id:number;
  timeTableName:string;
  taskIds:number[];
  employeeIds:number[];
}

const timeTables: Ref<TimeTable[]> = ref([])
const loading = ref(false)
const addLoading = ref(false)
const isEditModalVisible = ref(false)
const isAddModalVisible = ref(false)
const editFormData: Ref<TimeTable> = ref({})
const addFormData: Ref<TimeTable> = ref({})

const token: Ref<string> = ref(Cookies.get('token'))
const refresh_token: Ref<string> = ref(Cookies.get('refresh_token'))

const columns = [
  {
    title: 'TimeTable ID',
    dataIndex: 'id',
    key: 'id',
  },
  {
    title: 'TimeTable Name',
    dataIndex: 'timeTableName',
    key: 'timeTableName',
  },
  {
    title: 'Task IDs',
    dataIndex: 'taskIds',
    key: 'taskIds',
  },
  {
    title: 'Employee IDs',
    dataIndex: 'employeeIds',
    key: 'employeeIds',
  },
  {
    title: 'Action',
    key: 'action',
  },
];

const fetchTimeTables = () => {
  loading.value = true;
  axios.get('http://localhost:8080/api/v1/timeTables', {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  }).then(response => {
    if (response.status == 200) {
      console.log(response.data)
      timeTables.value = response.data;
    }
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
};


const deleteTimeTable = (id) => {
  axios.delete("http://localhost:8080/api/v1/timeTables/by-id/" + id, {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  })
      .then((response) => {
        if (response.status == 200) {
          message.info('Delete Successfully!')
          fetchTimeTables()
        } else {
          message.error("Delete Failed!")
        }
      })
      .catch(() => message.error("Delete Failed!"))
}

const openEditModal = (timeTable: TimeTable) => {
  editFormData.value = {
    id: timeTable.id,
    timeTableName: timeTable.timeTableName,
    taskIds: timeTable.taskIds,
    employeeIds: timeTable.employeeIds,
  };
  isEditModalVisible.value = true;
};

const openAddModal = () => {
  addFormData.value = {
    id: 0,
    timeTableName: '',
    taskIds: [],
    employeeIds: [],
  };
  isAddModalVisible.value = true;
};
const handleEditTimeTable = () => {
  loading.value = true
  axios.put('http://localhost:8080/api/v1/timeTables/update', editFormData.value, {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  }).then(response => {
    if (response.status === 200) {
      // Update the timetables array with the edited data
      fetchTimeTables();
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

const handleAddTimeTable = () => {
  addLoading.value = true
  axios.post('http://localhost:8080/api/v1/timeTables/add', {timeTableName: addFormData.value.timeTableName}, {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  }).then(response => {
    if (response.status === 200) {
      // Update the timetables array with the edited data
      fetchTimeTables();
      message.info('Add Successfully!')
      isAddModalVisible.value = false;
      addFormData.value = {}
    } else {
      message.error('Add Failed!')
    }
    addLoading.value = false
  }).catch(() => {
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
          fetchTimeTables()
          fetchAllEmployeeIds()
          fetchAllTaskIds()
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
    fetchTimeTables();
    fetchAllEmployeeIds()
    fetchAllTaskIds()
  }
});
const employees = ref([])
const fetchAllEmployeeIds = () => {
  axios.get('http://localhost:8080/api/v1/employees/ids', {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  }).then((response: AxiosResponse) => {
    if (response.status === 200) {
      response.data.forEach(data=>{
        employees.value.push({key:data.id, title: data.name})
      })

    }
  })
}
const tasks = ref([])
const fetchAllTaskIds = () => {
  axios.get('http://localhost:8080/api/v1/tasks/ids', {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  }).then((response: AxiosResponse) => {
    if (response.status === 200) {
      response.data.forEach(data=>{
        tasks.value.push({key:data, title: data})
      })
    }
  })
}

</script>

<template>
  <a-modal
      v-model:open="isEditModalVisible"
      title="Edit TimeTable"
      :closable="false"
      :mask-closable="false"
  >
    <template #footer>
      <a-button key="registerBack" @click="() => {isEditModalVisible = false}">Cancel</a-button>
      <a-button key="registerSubmit" type="primary" :loading="loading"
                @click="handleEditTimeTable">Update
      </a-button>
    </template>
    <a-form
        :model="editFormData"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 20 }"
        layout="vertical"
    >
      <a-form-item label="TimeTable Name">
        <a-input v-model:value="editFormData.timeTableName"></a-input>
      </a-form-item>
      <a-form-item label="Task">
        <a-transfer
            v-model:target-keys="editFormData.taskIds"
            :data-source="tasks"
            :titles="['Available', 'Selected']"
            :render="item => item.title"
        />
      </a-form-item>
      <a-form-item label="Employee">
      <a-transfer
          v-model:target-keys="editFormData.employeeIds"
          :data-source="employees"
          :titles="['Available', 'Selected']"
          :render="item => '['+item.key+'] '+item.title"
      />
      </a-form-item>
    </a-form>
  </a-modal>

  <a-modal
      v-model:open="isAddModalVisible"
      title="Add Timetable"
      :closable="false"
      :mask-closable="false"
  >
    <template #footer>
      <a-button key="addBack" @click="() => {isAddModalVisible = false}">Cancel</a-button>
      <a-button key="addSubmit" type="primary" :loading="addLoading"
                @click="handleAddTimeTable">Add
      </a-button>
    </template>
    <a-form
        :model="addFormData"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 20 }"
        layout="vertical"
    >
      <a-form-item label="TimeTable Name">
        <a-input v-model:value="addFormData.timeTableName"></a-input>
      </a-form-item>
    </a-form>
  </a-modal>
  <a-button type="primary" @click="openAddModal()" style="margin-left: 5px">Add</a-button>
  <div style="height: 20px"/>
  <a-table
      :columns="columns"
      :rowKey="record => record.id"
      :dataSource="timeTables"
      :loading="loading"
  >
    <template #bodyCell="{ column, record }">
      <template v-if="column.key === 'employeeIds'">
        {{ record.employeeIds.join(', ') }}
      </template>
      <template v-if="column.key === 'taskIds'">
        {{ record.taskIds.join(', ') }}
      </template>
      <template v-if="column.key === 'action'">
        <a-space>
          <a-button type="primary" @click="openEditModal(record)">Edit</a-button>
          <a-popconfirm
              title="Are you sure to delete this reservation?"
              @confirm="() => deleteTimeTable(record.id)"
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