<script setup lang="ts">
import {useRoute, useRouter} from 'vue-router';
import axios from 'axios'
import {onMounted, ref} from "vue";
import type {Ref} from 'vue'

interface Room {
  roomType: string,
  fee: number,
}

const route = useRoute()
const router = useRouter()

const checkIn = route.query.checkIn
const checkOut = route.query.checkOut
const guests = route.query.guests

const loading = ref<boolean>(true)

const roomData: Ref<Room[]> = ref<Room[]>([])

const queryData = (checkInDate, checkOutDate) => {
  axios.get('http://localhost:8080/api/v1/rooms/available/type', {params: {checkInDate: checkInDate, checkOutDate: checkOutDate}})
      .then((response) => {
        if (response.status == 200) {
          roomData.value = response.data
          loading.value = false
          console.log(response)
        }
      })
}

onMounted(() => {
  queryData(checkIn, checkOut);
})
</script>

<template>
  <div style="position: relative">
    <img class="background_image" src="/regular.jpg" alt="Background Image"/>
    <div class="overlayer"/>
  </div>
  <div style="height: 30px"></div>

  <a-skeleton active :loading="loading">
    <a-list :data-source="roomData" style="display: flex;justify-content: center">
      <template #renderItem="{ item }">
        <a-list-item>
          <a-card style="width: 1000px">
            <a-row>
              <a-col>
                <img style="width: 320px;border-radius: 10px;margin-top: 20px" alt="Room Image"
                     :src="item.roomType == 'REGULAR'? 'regular.jpg': item.roomType == 'DELUXE'? 'deluxe.jpg':item.roomType == 'DOUBLE'? 'double.jpg':''"/>
              </a-col>
              <a-col style="margin-left: 30px">
                <a-row>
                  <div style="font-size: 30px;font-weight: bold">
                    {{
                      item.roomType == 'REGULAR' ? 'Regular Room' : item.roomType == 'DELUXE' ? 'Deluxe Room' : item.roomType == 'DOUBLE' ? 'Double Room' : ''
                    }}
                  </div>
                </a-row>
                <a-row style="margin-bottom: 160px">
                  <div style="font-size: 16px;width: 500px">
                    {{
                      item.roomType == 'REGULAR' ? 'Room with one queen bed. Amenities include a private washroom with shower.' : item.roomType == 'DELUXE' ? 'Larger room with one queen bed. Amenities include a private washroom with shower, a sofa, a working desk with chair.' : item.roomType == 'DOUBLE' ? 'Room with two queen beds. Amenities include a private washroom with shower and a sofa.' : ''
                    }}
                  </div>
                </a-row>
                <a-row style="position: absolute;bottom: 0">
                  <a-card>
                    <a-col>
                      <a-row style="font-size: 15px">
                        Starting at
                      </a-row>
                      <a-row style="font-size: 24px;font-weight: bold">
                        CA${{ item.fee }}
                      </a-row>
                      <a-row style="font-size: 15px">
                        Excluding taxes and fees
                      </a-row>
                    </a-col>
                  </a-card>
                </a-row>
              </a-col>
              <a-col style="display: flex;align-items: center;margin-left: 20px">
                <a-button type="primary" size="large"
                          @click="()=>{router.push({name:'book',query:{checkIn: checkIn, checkOut: checkOut, guests: guests, type: item.roomType}})}">
                  Book
                </a-button>
              </a-col>
            </a-row>
          </a-card>

        </a-list-item>
      </template>
    </a-list>
    <div style="height: 30px"></div>
  </a-skeleton>
</template>

<style scoped>
.background_image {
  width: 100%;
  height: 230px;
  object-fit: cover;
}

.overlayer {
  background: rgba(0, 0, 0, 0.4);
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>