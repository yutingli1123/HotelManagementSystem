<script setup lang="ts">
import {onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";

const reservations = ref([]);
const loading = ref(false);
const isEditModalVisible = ref(false);
const editFormData = ref({
  roomType: '',
  checkInDate: '',
  checkOutDate: '',
});

const columns = [
  {
    title: 'Reservation ID',
    dataIndex: 'id',
    key: 'id',
  },
  {
    title: 'User Name',
    dataIndex: 'userName',
    key: 'userName',
  },
  {
    title: 'Room Type',
    dataIndex: 'roomType',
    key: 'roomType',
  },
  {
    title: 'Check-in Date',
    dataIndex: 'checkInDate',
    key: 'checkInDate',
  },
  {
    title: 'Check-out Date',
    dataIndex: 'checkOutDate',
    key: 'checkOutDate',
  },
];

const fetchReservations = async (searchText = '') => {
  loading.value = true;
  try {
    const response = await axios.get(`http://localhost:8080/api/v1/reservations`);
    reservations.value = response.data;
  } catch (error) {
    console.error('Failed to fetch reservations:', error);
  } finally {
    loading.value = false;
  }
};


const deleteReservation = async (id) => {
  axios.delete("http://localhost:8080/api/v1/reservations/by-id/{" + id + "}")
      .then(() => {fetchReservations()})
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
    const response = await axios.post(`http://localhost:8080/api/v1/reservations/${editFormData.value}`);
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
  fetchReservations();
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
                  <a-button type="danger">Delete</a-button>
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

.a-layout-header {
  background-color: #1890ff;
}

.a-layout-footer {
  background-color: #f0f2f5;
}
</style>