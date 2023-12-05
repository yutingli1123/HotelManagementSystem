<script setup lang="ts">
import {computed, onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Ref} from "vue";
import Cookies from "js-cookie";
import router from "@/router";
import dayjs, {Dayjs} from "dayjs";

interface Task {
  id: number;
  startTime: number;
  endTime: number;
  dayOfTheWeek: string;
  taskName: string;
  taskDescription: string;
}

const tasks: Ref<Task[]> = ref([])
const loading = ref(false)
const addLoading = ref(false)
const isEditModalVisible = ref(false)
const isAddModalVisible = ref(false)
const editFormData: Ref<Task> = ref({})
const addFormData: Ref<Task> = ref({})


const token: Ref<string> = ref(Cookies.get('token'))
const refresh_token: Ref<string> = ref(Cookies.get('refresh_token'))

const columns = [
  {
    title: 'Task ID',
    dataIndex: 'id',
    key: 'id',
  },
  {
    title: 'Task Name',
    dataIndex: 'taskName',
    key: 'taskName',
  },
  {
    title: 'Task Description',
    dataIndex: 'taskDescription',
    key: 'taskDescription',
  },
  {
    title: 'Day',
    dataIndex: 'dayOfTheWeek',
    key: 'dayOfTheWeek',
  },
  {
    title: 'Start Time',
    dataIndex: 'startTime',
    key: 'startTime',
  },
  {
    title: 'End Time',
    dataIndex: 'endTime',
    key: 'endTime',
  },
  {
    title: 'Action',
    key: 'action',
  },
];

const fetchTasks = () => {
  loading.value = true;
  axios.get('http://localhost:8080/api/v1/tasks', {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  }).then(response => {
    if (response.status == 200) {
      tasks.value = response.data;
      tasks.value.forEach(task => {
        task.startTime = dayjs(task.startTime, 'HH:mm:ss').format('h:mm A')
        task.endTime = dayjs(task.endTime, 'HH:mm:ss').format('h:mm A')
      })
      tasks.value.sort((a, b) => a.id - b.id)
    }
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
};


const deleteTask = (id) => {
  axios.delete("http://localhost:8080/api/v1/tasks/by-id/" + id, {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  })
      .then((response) => {
        if (response.status == 200) {
          message.info('Delete Successfully!')
          fetchTasks()
        } else {
          message.error("Delete Failed!")
        }
      })
      .catch(() => message.error("Delete Failed!"))
}

const openEditModal = (task: Task) => {
  editFormData.value = {
    id: task.id,
    startTime: dayjs(task.startTime, 'h:mm A'),
    endTime: dayjs(task.endTime, 'h:mm A'),
    dayOfTheWeek: task.dayOfTheWeek,
    taskName: task.taskName,
    taskDescription: task.taskDescription,
  };
  isEditModalVisible.value = true;
};

const openAddModal = () => {
  addFormData.value = {
    startTime: dayjs(),
    endTime: dayjs().add(1,'minute'),
    dayOfTheWeek: 'Mon',
    taskName: '',
    taskDescription: '',
  }
  isAddModalVisible.value = true
}

const handleEditTask = () => {
  loading.value = true
  axios.put('http://localhost:8080/api/v1/tasks/update', {
    id: editFormData.value.id,
    startTime: editFormData.value.startTime.format('HH:mm:ss'),
    endTime: editFormData.value.endTime.format('HH:mm:ss'),
    dayOfTheWeek: editFormData.value.dayOfTheWeek,
    taskName: editFormData.value.taskName,
    taskDescription: editFormData.value.taskDescription,
  }, {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  }).then(response => {
    if (response.status === 200) {
      // Update the tasks array with the edited data
      fetchTasks();
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

const handleAddTask = () => {
  addLoading.value = true
  axios.post('http://localhost:8080/api/v1/tasks/add', {
    startTime: addFormData.value.startTime.format('HH:mm:ss'),
    endTime: addFormData.value.endTime.format('HH:mm:ss'),
    dayOfTheWeek: addFormData.value.dayOfTheWeek,
    taskName: addFormData.value.taskName,
    taskDescription: addFormData.value.taskDescription,
  }, {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  }).then(response => {
    if (response.status === 200) {
      // Update the tasks array with the edited data
      fetchTasks();
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
          fetchTasks()
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
    fetchTasks();
  }
});
const submitDisabled = computed(() => {
  return editFormData.value.taskDescription == '' || editFormData.value.taskName == ''
})

const addSubmitDisabled = computed(() => {
  return addFormData.value.taskDescription == '' || addFormData.value.taskName == ''
})

const date_checker = (time: Dayjs) => {
  if (time.add(1, 'minute').isAfter(editFormData.value.endTime)) {
    editFormData.value.endTime = time.add(1, 'minute')
  }
}

const disabledEndTime = () => {
  const selectedStartTime = editFormData.value.startTime;
  const selectedHour = selectedStartTime.hour();
  const selectedMinute = selectedStartTime.minute();

  return {
    disabledHours: () => {
      const hours = [];
      for (let i = 0; i < selectedHour; i++) {
        hours.push(i);
      }
      return hours;
    },
    disabledMinutes: (selectedHour) => {
      const minutes = [];
      if (selectedHour === selectedStartTime.hour()) {
        for (let i = 0; i < selectedMinute + 1; i++) {
          minutes.push(i);
        }
      }
      return minutes;
    },
  };
}

const add_date_checker = (time: Dayjs) => {
  if (time.add(1, 'minute').isAfter(addFormData.value.endTime)) {
    addFormData.value.endTime = time.add(1, 'minute')
  }
}

const addDisabledEndTime = () => {
  const selectedStartTime = addFormData.value.startTime;
  const selectedHour = selectedStartTime.hour();
  const selectedMinute = selectedStartTime.minute();

  return {
    disabledHours: () => {
      const hours = [];
      for (let i = 0; i < selectedHour; i++) {
        hours.push(i);
      }
      return hours;
    },
    disabledMinutes: (selectedHour) => {
      const minutes = [];
      if (selectedHour === selectedStartTime.hour()) {
        for (let i = 0; i < selectedMinute + 1; i++) {
          minutes.push(i);
        }
      }
      return minutes;
    },
  };
}
</script>

<template>
  <a-modal
      v-model:open="isEditModalVisible"
      title="Edit Task"
      :closable="false"
      :mask-closable="false"
  >
    <template #footer>
      <a-button key="registerBack" @click="() => {isEditModalVisible = false}">Cancel</a-button>
      <a-button key="registerSubmit" type="primary" :loading="loading" :disabled="submitDisabled"
                @click="handleEditTask">Update
      </a-button>
    </template>
    <a-form
        :model="editFormData"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
    >
      <a-form-item label="Task Id">
        <a-input-number v-model:value="editFormData.taskName" disabled></a-input-number>
      </a-form-item>

      <a-form-item label="Start Time">
        <a-time-picker v-model:value="editFormData.startTime" format="h:mm A" :use12-hours="true"
                       @change="date_checker"/>
      </a-form-item>

      <a-form-item label="End Time">
        <a-time-picker v-model:value="editFormData.endTime" format="h:mm A" :disabled-time="disabledEndTime"
                       :use12-hours="true"/>
      </a-form-item>
      <a-form-item label="Day">
        <a-select v-model:value="editFormData.dayOfTheWeek">
          <a-select-option value="Mon">Mon</a-select-option>
          <a-select-option value="Tue">Tue</a-select-option>
          <a-select-option value="Wen">Wen</a-select-option>
          <a-select-option value="Thu">Thu</a-select-option>
          <a-select-option value="Fri">Fri</a-select-option>
          <a-select-option value="Sat">Sat</a-select-option>
          <a-select-option value="Sun">Sun</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="Task Name">
        <a-input v-model:value="editFormData.taskName"></a-input>
      </a-form-item>

      <a-form-item label="Task Description">
        <a-input v-model:value="editFormData.taskDescription"></a-input>
      </a-form-item>

    </a-form>
  </a-modal>

  <a-modal
      v-model:open="isAddModalVisible"
      title="Add Task"
      :closable="false"
      :mask-closable="false"
  >
    <template #footer>
      <a-button key="addBack" @click="() => {isAddModalVisible = false}">Cancel</a-button>
      <a-button key="addSubmit" type="primary" :loading="addLoading" :disabled="addSubmitDisabled"
                @click="handleAddTask">Add
      </a-button>
    </template>
    <a-form
        :model="addFormData"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
    >

      <a-form-item label="Start Time">
        <a-time-picker v-model:value="addFormData.startTime" format="h:mm A" :use12-hours="true"
                       @change="add_date_checker"/>
      </a-form-item>

      <a-form-item label="End Time">
        <a-time-picker v-model:value="addFormData.endTime" format="h:mm A" :disabled-time="addDisabledEndTime"
                       :use12-hours="true"/>
      </a-form-item>
      <a-form-item label="Day">
        <a-select v-model:value="addFormData.dayOfTheWeek">
          <a-select-option value="Mon">Mon</a-select-option>
          <a-select-option value="Tue">Tue</a-select-option>
          <a-select-option value="Wen">Wen</a-select-option>
          <a-select-option value="Thu">Thu</a-select-option>
          <a-select-option value="Fri">Fri</a-select-option>
          <a-select-option value="Sat">Sat</a-select-option>
          <a-select-option value="Sun">Sun</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="Task Name">
        <a-input v-model:value="addFormData.taskName"></a-input>
      </a-form-item>

      <a-form-item label="Task Description">
        <a-input v-model:value="addFormData.taskDescription"></a-input>
      </a-form-item>

    </a-form>
  </a-modal>

  <a-button type="primary" @click="openAddModal" style="margin-left: 5px">Add</a-button>
  <div style="height: 20px"/>
  <a-table
      :columns="columns"
      :rowKey="record => record.id"
      :dataSource="tasks"
      :loading="loading"
  >
    <template #bodyCell="{ column, record }">
      <template v-if="column.key === 'action'">
        <a-space>
          <a-button type="primary" @click="openEditModal(record)">Edit</a-button>
          <a-popconfirm
              title="Are you sure to delete this reservation?"
              @confirm="() => deleteTask(record.id)"
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