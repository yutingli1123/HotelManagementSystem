<script setup lang="ts">
import {onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Ref} from "vue";
import Cookies from "js-cookie";
import router from "@/router";
import dayjs from "dayjs";

interface Reservation {
  id: number;
  username: string;
  checkInDate: number;
  checkOutDate: number;
  roomType: string;
  fee: number;
}

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


const deleteReservation = async (id) => {
  axios.delete("http://localhost:8080/api/v1/reservations/by-id/{" + id + "}", {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  })
      .then(() => {
        fetchReservations()
      })
      .catch(() => message.error("Delete Failed!"))
};

const openEditModal = (reservation) => {
  editFormData.value = {
    id: reservation.id,
    roomType: reservation.roomType,
    checkInDate: dayjs(reservation.checkInDate),
    checkOutDate: dayjs(reservation.checkOutDate),
  };
  isEditModalVisible.value = true;
};


const handleEditReservation = async () => {
  try {
    const response = await axios.post('http://localhost:8080/api/v1/reservations/update', editFormData.value, {
      headers: {
        Authorization: 'Bearer ' + token.value
      },
    });
    if (response.status === 200) {
      // Update the reservations array with the edited data
      fetchReservations();
      isEditModalVisible.value = false;
    }
  } catch (error) {
    console.error('Error updating reservation:', error);
  }
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

</script>

<template>
  <a-modal
      v-model:open="isEditModalVisible"
      title="Edit Reservation"
      @ok="handleEditReservation"
      @cancel="() => {isEditModalVisible = false}"
  >
    <a-form
        :model="editFormData"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 19 }"
    >
      <!-- Room Type Dropdown -->
      <a-form-item label="Room Type">
        <a-select v-model:value="editFormData.roomType" placeholder="Select a room type">
          <a-select-option value="REGULAR">Regular</a-select-option>
          <a-select-option value="DELUXE">Deluxe</a-select-option>
          <a-select-option value="DOUBLE">Luxury</a-select-option>
        </a-select>
      </a-form-item>

      <!-- Check-in Date Selector -->
      <a-form-item label="Check-in Date">
        <a-date-picker
            v-model:value="editFormData.checkInDate"
            format="MMM / DD / YYYY"
            placeholder="Select check-in date"
        />
      </a-form-item>

      <!-- Check-out Date Selector -->
      <a-form-item label="Check-out Date">
        <a-date-picker
            v-model:value="editFormData.checkOutDate"
            format="MMM / DD / YYYY"
            placeholder="Select check-out date"
        />
      </a-form-item>
    </a-form>
  </a-modal>
  <!-- Main Layout -->


  <!-- Reservation Table -->
  <a-row :gutter="16" style="margin-top: 20px;">
    <a-col :span="24">
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
    </a-col>
  </a-row>


  <!-- Edit Reservation Modal -->

</template>


<style scoped>

</style>