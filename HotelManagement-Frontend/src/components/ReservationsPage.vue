<script setup lang="ts">
import {onMounted, ref, Ref} from "vue";
import Cookies from "js-cookie";
import axios from "axios";
import {jwtDecode} from "jwt-decode";
import {message} from "ant-design-vue";
import dayjs from "dayjs";
import {useRouter} from "vue-router";
import {useStore} from "@/stores/stateStore";

const token: Ref<string> = ref(Cookies.get('token'))
const refresh_token: Ref<string> = ref(Cookies.get('refresh_token'))

const router = useRouter()
const store = useStore()

interface Reservation {
  id: number;
  checkInDate: number;
  checkOutDate: number;
  roomType: string;
  fee: number;
}

const data: Ref<Reservation[]> = ref([])
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
          const username = jwtDecode(token.value).iss
          fetchReservations(username)
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
    const username = jwtDecode(token.value).iss
    fetchReservations(username)
  }
})

const fetchReservations = (username: string) => {
  axios.get('http://localhost:8080/api/v1/reservations/' + username, {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  }).then((response) => {
    if (response.status == 200) {
      data.value = response.data.map(item => {
        return {
          id: item.id,
          checkInDate: dayjs(item.checkInDate).format("MMM / DD / YYYY"),
          checkOutDate: dayjs(item.checkOutDate).format("MMM / DD / YYYY"),
          roomType: item.roomType,
          fee: 'CA$' + item.fee,
        }
      })
    } else {
      message.error('Get reservations failed!')
    }
  }).catch(() => {
    message.error('Get reservations failed!')
  })
}

const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    key: 'id',
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
    title: 'Room Type',
    dataIndex: 'roomType',
    key: 'roomType',
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

const checkActionVisibility = (column, record: Reservation) => {
  return column.key == 'action' && dayjs(record.checkInDate, 'MMM / DD / YYYY').subtract(3, 'day').isAfter(dayjs())
}

const cancelRoom = (id) => {
  axios.delete("http://localhost:8080/api/v1/reservations/cancel/" + id, {
    headers: {
      Authorization: 'Bearer ' + token.value
    },
  })
      .then((response) => {
        if (response.status == 200) {
          message.info('Cancel Successfully!')
          const username = jwtDecode(token.value).iss
          fetchReservations(username)
        } else {
          message.error("Cancel Failed!")
        }
      })
      .catch(() => message.error("Cancel Failed!"))
}
</script>

<template>
  <a-table :dataSource="data" :columns="columns">
    <template #bodyCell="{ column, record }">
      <template v-if="checkActionVisibility(column,record)">
        <a-space>
          <a-popconfirm
              title="Are you sure to cancel this reservation?"
              @confirm="() => cancelRoom(record.id)"
          >
            <a-button danger>Cancel</a-button>
          </a-popconfirm>

        </a-space>
      </template>
      <template v-else-if="column.key == 'action'">
        <a-space>
          <a-tooltip>
            <template #title>This reservation has exceeded the 72-hour deadline and cannot be canceled.</template>
            <a-button danger disabled>Cancel</a-button>
          </a-tooltip>
        </a-space>
      </template>
    </template>
  </a-table>

</template>

<style scoped>

</style>