<script setup lang="ts">
import {onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import type {Ref} from "vue";
import Cookies from "js-cookie";
import router from "@/router";
import {useStore} from "@/stores/stateStore";

interface Room {
  id: number;
  type: string;
  fee: number;
}

interface RoomBatch {
  roomType: string;
  fee: number;
  number: number;
}

const rooms: Ref<Room[]> = ref([])
const loading = ref(false)
const addLoading =ref(false)
const addBatchLoading =ref(false)
const isEditModalVisible = ref(false)
const isAddModalVisible = ref(false)
const isAddBatchModalVisible = ref(false)
const editFormData: Ref<Room> = ref({})
const addFormData: Ref<Room> = ref({})
const addBatchFormData: Ref<RoomBatch> = ref({})

const token: Ref<string> = ref(Cookies.get('token'))
const refresh_token: Ref<string> = ref(Cookies.get('refresh_token'))

const store = useStore()

const columns = [
  {
    title: 'Room ID',
    dataIndex: 'id',
    key: 'id',
  },
  {
    title: 'Room Type',
    dataIndex: 'type',
    key: 'type',
  },

  {
    title: 'Fee',
    dataIndex: 'fee',
    key: 'fee',
  },
  {
    title: 'Action',
    key: 'action',
  },
];

const fetchRooms = () => {
  loading.value = true;
  axios.get('http://localhost:8080/api/v1/rooms', {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  }).then(response => {
    if (response.status == 200) {
      rooms.value = response.data;
    }
    rooms.value.sort((a, b)=>a.id-b.id)
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
};


const deleteRoom = (id) => {
  axios.delete("http://localhost:8080/api/v1/rooms/by-id/" + id, {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  })
      .then((response) => {
        if (response.status == 200) {
          message.info('Delete Successfully!')
          fetchRooms()
        } else {
          message.error("Delete Failed!")
        }
      })
      .catch(() => message.error("Delete Failed!"))
}

const openEditModal = (task: Room) => {
  editFormData.value = {
    id: task.id,
    type: task.type,
    fee: task.fee,
  };
  isEditModalVisible.value = true;
};

const openAddModal = () => {
  addFormData.value = {
    type: '',
    fee: null,
  }
  isAddModalVisible.value = true;
}

const openAddBatchModal = () => {
  addBatchFormData.value = {
    roomType: '',
    fee: null,
    number: null,
  }
  isAddBatchModalVisible.value = true;
}

const handleEditRoom = () => {
  loading.value = true
  axios.put('http://localhost:8080/api/v1/rooms/update', editFormData.value, {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  }).then(response => {
    if (response.status === 200) {
      // Update the rooms array with the edited data
      fetchRooms();
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

const handleAddRoom = () => {
  addLoading.value = true
  axios.post('http://localhost:8080/api/v1/rooms/add', addFormData.value, {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  }).then(response => {
    if (response.status === 200) {
      // Update the rooms array with the edited data
      fetchRooms();
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
const handleAddRoomBatch = () => {
  addBatchLoading.value = true
  axios.post('http://localhost:8080/api/v1/rooms/batch', addBatchFormData.value, {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  }).then(response => {
    if (response.status === 200) {
      // Update the rooms array with the edited data
      fetchRooms();
      message.info('Add Successfully!')
      isAddBatchModalVisible.value = false;
      addBatchFormData.value = {}
    } else {
      message.error('Add Failed!')
    }
    addBatchLoading.value = false
  }).catch(() => {
    message.error('Add Failed!')
    addBatchLoading.value = false
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
          fetchRooms()
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
    fetchRooms();
  }
});
</script>

<template>
  <a-modal
      v-model:open="isEditModalVisible"
      title="Edit Room"
      :closable="false"
      :mask-closable="false"
  >
    <template #footer>
      <a-button key="registerBack" @click="() => {isEditModalVisible = false}">Cancel</a-button>
      <a-button key="registerSubmit" type="primary" @click="handleEditRoom" :disabled="editFormData.fee == null|| addFormData.fee == 0" :loading="loading">Update</a-button>
    </template>
    <a-form
        :model="editFormData"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 12 }"
    >
      <a-form-item label="Reservation ID">
        <a-input-number v-model:value="editFormData.id" disabled />
      </a-form-item>

      <a-form-item label="Room Type">
        <a-select v-model:value="editFormData.type">
          <a-select-option value="REGULAR">Regular</a-select-option>
          <a-select-option value="DELUXE">Deluxe</a-select-option>
          <a-select-option value="DOUBLE">Double</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="Fee">
        <a-input-number v-model:value="editFormData.fee"></a-input-number>
      </a-form-item>

    </a-form>
  </a-modal>

  <a-modal
      v-model:open="isAddModalVisible"
      title="Add Room"
      :closable="false"
      :mask-closable="false"
  >
    <template #footer>
      <a-button key="addBack" @click="() => {isAddModalVisible = false}">Cancel</a-button>
      <a-button key="addSubmit" type="primary" @click="handleAddRoom" :disabled="addFormData.fee == null || addFormData.fee == 0 || addFormData.type == ''" :loading="addLoading">Add</a-button>
    </template>
    <a-form
        :model="addFormData"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 12 }"
    >
      <a-form-item label="Room Type">
        <a-select v-model:value="addFormData.type">
          <a-select-option value="REGULAR">Regular</a-select-option>
          <a-select-option value="DELUXE">Deluxe</a-select-option>
          <a-select-option value="DOUBLE">Double</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="Fee">
        <a-input-number v-model:value="addFormData.fee"></a-input-number>
      </a-form-item>
    </a-form>
  </a-modal>

  <a-modal
      v-model:open="isAddBatchModalVisible"
      title="Add Rooms"
      :closable="false"
      :mask-closable="false"
  >
    <template #footer>
      <a-button key="addBack" @click="() => {isAddBatchModalVisible = false}">Cancel</a-button>
      <a-button key="addSubmit" type="primary" @click="handleAddRoomBatch" :disabled="addBatchFormData.roomType == '' || addBatchFormData.fee == null || addBatchFormData.fee == 0||addBatchFormData.number == null || addBatchFormData.number == 0" :loading="addBatchLoading">Add</a-button>
    </template>
    <a-form
        :model="addBatchFormData"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 12 }"
    >
      <a-form-item label="Room Type">
        <a-select v-model:value="addBatchFormData.roomType">
          <a-select-option value="REGULAR">Regular</a-select-option>
          <a-select-option value="DELUXE">Deluxe</a-select-option>
          <a-select-option value="DOUBLE">Double</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="Fee">
        <a-input-number v-model:value="addBatchFormData.fee"></a-input-number>
      </a-form-item>

      <a-form-item label="Number">
        <a-input-number v-model:value="addBatchFormData.number"></a-input-number>
      </a-form-item>
    </a-form>
  </a-modal>

  <a-button type="primary" @click="openAddModal" style="margin-left: 5px">Add</a-button>
  <a-button type="primary" @click="openAddBatchModal" style="margin-left: 5px">Add Batch</a-button>
  <div style="height: 20px"/>

  <a-table
      :columns="columns"
      :rowKey="record => record.id"
      :dataSource="rooms"
      :loading="loading"
  >
    <template #bodyCell="{ column, record }">
      <template v-if="column.key === 'action'">
        <a-space>
          <a-button type="primary" @click="openEditModal(record)">Edit</a-button>
          <a-popconfirm
              title="Are you sure to delete this reservation?"
              @confirm="() => deleteRoom(record.id)"
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