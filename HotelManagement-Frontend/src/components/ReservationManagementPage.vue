<script setup lang="ts">
import {onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import type {Ref} from "vue";
import Cookies from "js-cookie";
import router from "@/router";
import dayjs, {Dayjs} from "dayjs";
import {useStore} from "@/stores/stateStore";

interface Reservation {
  id: number;
  username: string;
  checkInDate: number;
  checkOutDate: number;
  roomType: string;
  fee: number;
}

const store = useStore()

const reservations: Ref<Reservation[]> = ref([]);
const loading = ref(false);
const isEditModalVisible = ref(false);
const editFormData = ref({
  id: 0,
  roomType: '',
  checkInDate: '',
  checkOutDate: '',
});

const token: Ref<string> = ref(Cookies.get('token'))
const refresh_token: Ref<string> = ref(Cookies.get('refresh_token'))
const isAddModalVisible = ref(false);
const addFormData = ref({
  roomType: '',
  checkInDate: '',
  checkOutDate: '',
  username: ''
});

const columns = [
  {
    title: 'Reservation ID',
    dataIndex: 'id',
    key: 'id',
  },
  {
    title: 'Username',
    dataIndex: 'username',
    key: 'username',
  },
  {
    title: 'Room Type',
    dataIndex: 'roomType',
    key: 'roomType',
  },
  {
    title: 'Check-In Date',
    dataIndex: 'checkInDate',
    key: 'checkInDate',
  },
  {
    title: 'Check-Out Date',
    dataIndex: 'checkOutDate',
    key: 'checkOutDate',
  },
  {
    title: 'Total Fee',
    dataIndex: 'fee',
    key: 'fee',
  },
  {
    title: 'Action',
    key: 'action',
  },
];

const fetchReservations = () => {
  loading.value = true;
  axios.get('http://localhost:8080/api/v1/reservations', {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  }).then(response => {
    if (response.status == 200) {
      reservations.value = response.data;
      reservations.value.forEach(reservation => {
        reservation.checkInDate = dayjs(reservation.checkInDate).format('MMM / DD / YYYY')
        reservation.checkOutDate = dayjs(reservation.checkOutDate).format('MMM / DD / YYYY')
      })
    }
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
};


const deleteReservation = (id) => {
  axios.delete("http://localhost:8080/api/v1/reservations/by-id/" + id, {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  })
      .then((response) => {
        if (response.status == 200) {
          message.info('Delete Successfully!')
          fetchReservations()
        } else {
          message.error("Delete Failed!")
        }
      })
      .catch(() => message.error("Delete Failed!"))
};

const openEditModal = (reservation) => {
  editFormData.value = {
    id: reservation.id,
    roomType: reservation.roomType,
    checkInDate: dayjs(reservation.checkInDate,'MMM / DD / YYYY'),
    checkOutDate: dayjs(reservation.checkOutDate,'MMM / DD / YYYY'),
  };
  isEditModalVisible.value = true;
};

const openAddModal = () => {
  addFormData.value = {
    roomType: 'REGULAR',
    checkInDate: dayjs(),
    checkOutDate: dayjs().add(1,'day'),
    username: ''
  };
  isAddModalVisible.value = true;
};

const handleEditReservation = () => {
  axios.put('http://localhost:8080/api/v1/reservations/update', editFormData.value, {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  }).then(response => {
    if (response.status === 200) {
      // Update the reservations array with the edited data
      fetchReservations();
      message.info('Update Successfully!')
      isEditModalVisible.value = false;
    } else {
      message.error('Update Failed!')
    }
  }).catch(() => {
    message.error('Update Failed!')
  })
};

const handleAddReservation = () => {
  console.log(addFormData.value)
  axios.post('http://localhost:8080/api/v1/reservations/add', addFormData.value, {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  }).then(response => {
    if (response.status === 200) {
      fetchReservations();
      message.info('Add Successfully!')
      isAddModalVisible.value = false;
    } else {
      message.error('Add Failed!')
    }
  }).catch(() => {
    message.error('Add Failed!')
  })
};

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
          fetchReservations()
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
    fetchReservations();
  }
});
const disabled_check_in_date = (date: Dayjs) => {
  if (date.isBefore(dayjs())) {
    return true
  }
}

const disabled_check_out_date = (date: Dayjs) => {
  if (date.subtract(1, 'day').isBefore(dayjs(editFormData.value.checkInDate))) {
    return true
  }
}

const date_checker = (date: Dayjs) => {
  if (date.add(1, 'day').isAfter(dayjs(editFormData.value.checkOutDate))) {
    editFormData.value.checkOutDate = date.add(1, 'day')
  }
}

const add_disabled_check_in_date = (date: Dayjs) => {
  if (date.isBefore(dayjs())) {
    return true
  }
}

const add_disabled_check_out_date = (date: Dayjs) => {
  if (date.subtract(1, 'day').isBefore(dayjs(addFormData.value.checkInDate))) {
    return true
  }
}

const add_date_checker = (date: Dayjs) => {
  if (date.add(1, 'day').isAfter(dayjs(addFormData.value.checkOutDate))) {
    addFormData.value.checkOutDate = date.add(1, 'day')
  }
}
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
      <a-button key="registerSubmit" type="primary" :loading="loading"
                @click="handleEditReservation">Update
      </a-button>
    </template>
    <a-form
        :model="editFormData"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 8 }"
    >
      <a-form-item label="Room Type">
        <a-select v-model:value="editFormData.roomType" placeholder="Select a room type">
          <a-select-option value="REGULAR">Regular</a-select-option>
          <a-select-option value="DELUXE">Deluxe</a-select-option>
          <a-select-option value="DOUBLE">Double</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="Check-in Date">
        <a-date-picker
            v-model:value="editFormData.checkInDate"
            format="MMM / DD / YYYY"
            placeholder="Select check-in date"
            :disabled-date="disabled_check_in_date" @change="date_checker"
        />
      </a-form-item>

      <a-form-item label="Check-out Date">
        <a-date-picker
            v-model:value="editFormData.checkOutDate"
            format="MMM / DD / YYYY"
            placeholder="Select check-out date"
            :disabled-date="disabled_check_out_date"
        />
      </a-form-item>
    </a-form>
  </a-modal>

  <a-modal
      v-model:open="isAddModalVisible"
      title="Add Reservation"
      :closable="false"
      :mask-closable="false"
  >
    <template #footer>
      <a-button key="addBack" @click="() => {isAddModalVisible = false}">Cancel</a-button>
      <a-button key="addSubmit" type="primary" :loading="loading"
                @click="handleAddReservation">Add
      </a-button>
    </template>
    <a-form
        :model="addFormData"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 8 }"
    >
      <a-form-item label="Username">
        <a-input v-model:value="addFormData.username" placeholder="Enter user name" />
      </a-form-item>

      <a-form-item label="Room Type">
        <a-select v-model:value="addFormData.roomType" placeholder="Select a room type">
          <a-select-option value="REGULAR">Regular</a-select-option>
          <a-select-option value="DELUXE">Deluxe</a-select-option>
          <a-select-option value="DOUBLE">Double</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="Check-in Date">
        <a-date-picker
            v-model:value="addFormData.checkInDate"
            format="MMM / DD / YYYY"
            placeholder="Select check-in date"
            :disabled-date="add_disabled_check_in_date" @change="add_date_checker"
        />
      </a-form-item>

      <a-form-item label="Check-out Date">
        <a-date-picker
            v-model:value="addFormData.checkOutDate"
            format="MMM / DD / YYYY"
            placeholder="Select check-out date"
            :disabled-date="add_disabled_check_out_date"
        />
      </a-form-item>
    </a-form>
  </a-modal>

  <a-button type="primary" style="margin-left: 5px" @click="openAddModal">Add</a-button>
<div style="height: 20px"/>
  <a-table
      :columns="columns"
      :rowKey="record => record.id"
      :dataSource="reservations"
      :loading="loading"
  >
    <template #bodyCell="{ column, record }">
      <template v-if="column.key === 'action'">
        <a-space>
          <a-button type="primary" @click="openEditModal(record)">Edit</a-button>
          <a-popconfirm
              title="Are you sure to delete this reservation?"
              @confirm="() => deleteReservation(record.id)"
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