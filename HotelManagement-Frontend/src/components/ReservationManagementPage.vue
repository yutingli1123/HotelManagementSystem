<script setup lang="ts">
import {onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Ref} from "vue/dist/vue";
import Cookies from "js-cookie";
import {AxiosResponse} from "axios";
import router from "@/router";

const reservations = ref([]);
const loading = ref(false);
const isEditModalVisible = ref(false);
const editFormData = ref({
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
];

const fetchReservations = () => {
  loading.value = true;
  axios.get('http://localhost:8080/api/v1/reservations', {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  }).then(response => {
    if (response.status == 200) {
      console.log(response.data)
      reservations.value = response.data;
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
      .catch((err) => message.error("delete failed"))
};

const openEditModal = (reservation) => {
  editFormData.value = {
    roomType: reservation.roomType,
    checkInDate: reservation.checkInDate,
    checkOutDate: reservation.checkOutDate,
  };
  isEditModalVisible.value = true;
};


const handleEditReservation = async () => {
  try {
    const response = await axios.post('http://localhost:8080/api/v1/reservations', editFormData.value, {
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
              fetchReservations();
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
    fetchReservations();
  }
});

</script>

<template>
  <!-- Main Layout -->
  <a-layout class="layout">
    <a-layout-header>
      <!-- Header Section -->
      <div class="header-content">Reservation Management</div>
    </a-layout-header>

    <a-layout-content style="padding: 50px">

      <!-- Reservation Table -->
      <a-row :gutter="16" style="margin-top: 20px;">
        <a-col :span="24">
          <a-table
              :columns="columns"
              :rowKey="record => record.id"
              :dataSource="reservations"
              :loading="loading"
          >
            <template #actions="{ record }">
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
          </a-table>
        </a-col>
      </a-row>
    </a-layout-content>

    <a-layout-footer style="text-align: center">
      Reservation Management System ©2023
    </a-layout-footer>
  </a-layout>

  <!-- Edit Reservation Modal -->
  <a-modal
      v-model:visible="isEditModalVisible"
      title="Edit Reservation"
      @ok="handleEditReservation"
      @cancel="() => isEditModalVisible = false"
  >
    <a-form
        :model="editFormData"
        label-col="{ span: 5 }"
        wrapper-col="{ span: 19 }"
    >
      <!-- Room Type Dropdown -->
      <a-form-item label="Room Type">
        <a-select v-model:value="editFormData.roomType" placeholder="Select a room type">
          <a-select-option value="regular">Regular</a-select-option>
          <a-select-option value="deluxe">Deluxe</a-select-option>
          <a-select-option value="luxury">Luxury</a-select-option>
        </a-select>
      </a-form-item>

      <!-- Check-in Date Selector -->
      <a-form-item label="Check-in Date">
        <a-date-picker
            v-model:value="editFormData.checkInDate"
            format="YYYY-MM-DD"
            placeholder="Select check-in date"
        />
      </a-form-item>

      <!-- Check-out Date Selector -->
      <a-form-item label="Check-out Date">
        <a-date-picker
            v-model:value="editFormData.checkOutDate"
            format="YYYY-MM-DD"
            placeholder="Select check-out date"
        />
      </a-form-item>
    </a-form>
  </a-modal>
</template>


<style scoped>
.layout {
  background: #f0f2f5;
}

.header-content {
  color: #fff;
  font-size: 20px;
}

.header-content {
  color: #fff;
  font-size: 20px;
}
</style>