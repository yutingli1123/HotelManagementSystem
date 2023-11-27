<script lang="ts" setup>

import {ref} from "vue";

const userModalOpen = ref(false)
const taskModalOpen = ref(false)
const reservationModalOpen = ref(false)
</script>

<template>
  <!-- User Management Modal -->
  <a-modal v-model:open="userModalOpen" title="Manage User" :mask-closable=false :closable=false>
    <template #footer>
      <a-button key="userBack" @click="handleUserCancel">Cancel</a-button>
      <a-button key="userSubmit" type="primary" :loading="userLoading" @click="onManageUser">
        Submit
      </a-button>
    </template>
    <div style="height: 20px"></div>
    <a-form :model="userFormState"
            ref="userFormRef"
            autocomplete="off"
            :label-col="{ span: 8 }"
            :wrapper-col="{ span: 12 }"
    >
      <a-form-item
          label="Name"
          name="name"
          :rules="[{ required: true, message: 'Please input user name!' }]"
      >
        <a-input v-model:value="userFormState.name"/>
      </a-form-item>

      <a-form-item
          label="Email"
          name="email"
          :rules="[{ required: true, message: 'Please input email!' }, { type: 'email', message: 'Invalid email format' }]"
      >
        <a-input v-model:value="userFormState.email"/>
      </a-form-item>

      <a-form-item
          label="Role"
          name="role"
      >
        <a-select v-model:value="userFormState.role">
          <a-select-option value="owner">Admin</a-select-option>
          <a-select-option value="user">User</a-select-option>
          <a-select-option value="employee">User</a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
  </a-modal>

  <!-- Task Management Modal -->
  <a-modal v-model:open="taskModalOpen" title="Manage Task" :mask-closable=false :closable=false>
    <template #footer>
      <a-button key="taskBack" @click="handleTaskCancel">Cancel</a-button>
      <a-button key="taskSubmit" type="primary" :loading="taskLoading" @click="onManageTask">
        Submit
      </a-button>
    </template>
    <div style="height: 20px"></div>
    <a-form :model="taskFormState"
            ref="taskFormRef"
            autocomplete="off"
            :label-col="{ span: 8 }"
            :wrapper-col="{ span: 12 }"
    >
      <a-form-item
          label="Task Description"
          name="description"
          :rules="[{ required: true, message: 'Please input task description!' }]"
      >
        <a-input v-model:value="taskFormState.description"/>
      </a-form-item>

      <a-form-item
          label="Assignee"
          name="assignee"
          :rules="[{ required: true, message: 'Please select an assignee!' }]"
      >
        <a-select v-model:value="taskFormState.assignee">
          <a-select-option value="employee1">Employee 1</a-select-option>
          <a-select-option value="employee2">Employee 2</a-select-option>

        </a-select>
      </a-form-item>

      <a-form-item
          label="Due Date"
          name="dueDate"
      >
        <a-date-picker v-model:value="taskFormState.dueDate"/>
      </a-form-item>
    </a-form>
  </a-modal>

  <!-- Reservation Management Modal -->
  <a-modal v-model:open="reservationModalOpen" title="Manage Reservation" :mask-closable=false :closable=false>
    <template #footer>
      <a-button key="reservationBack" @click="handleReservationCancel">Cancel</a-button>
      <a-button key="reservationSubmit" type="primary" :loading="reservationLoading" @click="onManageReservation">
        Submit
      </a-button>
    </template>
    <div style="height: 20px"></div>
    <a-form :model="reservationFormState"
            ref="reservationFormRef"
            autocomplete="off"
            :label-col="{ span: 8 }"
            :wrapper-col="{ span: 12 }"
    >
      <a-form-item
          label="Guest Name"
          name="guestName"
          :rules="[{ required: true, message: 'Please input guest name!' }]"
      >
        <a-input v-model:value="reservationFormState.guestName"/>
      </a-form-item>

      <a-form-item
          label="Room"
          name="room"
          :rules="[{ required: true, message: 'Please select a room!' }]"
      >
        <a-select v-model:value="reservationFormState.room">
          <a-select-option value="regular">Room 1</a-select-option>
          <a-select-option value="deluex">Room 2</a-select-option>
          <a-select-option value="luxury">Room 2</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item
          label="Check-in Date"
          name="checkInDate"
          :rules="[{ required: true, message: 'Please select check-in date!' }]"
      >
        <a-date-picker v-model:value="reservationFormState.checkInDate"/>
      </a-form-item>

      <a-form-item
          label="Check-out Date"
          name="checkOutDate"
          :rules="[{ required: true, message: 'Please select check-out date!' }]"
      >
        <a-date-picker v-model:value="reservationFormState.checkOutDate"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<style scoped>
.management-modal .a-modal-content {
  padding: 24px;
  background-color: #f5f5f5;
  border-radius: 8px;
}

.management-modal .a-modal-header {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  text-align: center;
  border-bottom: 1px solid #ddd;
  padding-bottom: 15px;
}

.management-modal .a-modal-body {
  padding-top: 20px;
}

.management-modal .a-form-item-label {
  text-align: left;
  font-size: 16px;
  color: #666;
}

.management-modal .a-form-item-control {
  padding-top: 4px;
}

.management-modal .a-input,
.management-modal .a-select,
.management-modal .a-date-picker {
  border-radius: 4px;
}

.management-modal .footer-buttons {
  margin-top: 30px;
  text-align: right;
}

.management-modal .footer-buttons a-button {
  margin-left: 10px;
}

</style>