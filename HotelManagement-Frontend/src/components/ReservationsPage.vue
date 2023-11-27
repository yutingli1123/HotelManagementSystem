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
const loading: Ref<boolean> = ref(true)

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
          Cookies.set('token', token.value, {expires: new Date(new Date().getTime() + 15 *60 * 1000)});
          Cookies.set('refresh_token', refresh_token.value, {expires: new Date(new Date().getTime() + 20 * 60 * 1000)});
          store.changeToLogin()
          const username = jwtDecode(token.value).iss
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
                  fee: 'CA$'+item.fee,
                }
              })
            } else {
              message.error('Get reservations failed!')
            }
          }).catch(() => {
            message.error('Get reservations failed!')
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
    const username = jwtDecode(token.value).iss
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
            fee: 'CA$'+item.fee,
          }
        })
      } else {
        message.error('Get reservations failed!')
      }
    }).catch(() => {
      message.error('Get reservations failed!')
    })
  }
})

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
];
</script>

<template>
  <a-table :dataSource="data" :columns="columns"/>
</template>

<style scoped>

</style>