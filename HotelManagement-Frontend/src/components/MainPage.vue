<script setup lang="ts">
import {ref} from "vue";
import type {Dayjs} from 'dayjs'
import dayjs from 'dayjs'

const today =dayjs()
const guest_number = ref(1)
const check_in_date = ref<Dayjs>(today)
const check_out_date = ref<Dayjs>(today.add(1, 'day'))

const disabled_check_in_date = (date: Dayjs) => {
  if (date.isBefore(today)) {
    return true
  }
}

const disabled_check_out_date = (date: Dayjs) => {
  if (date.subtract(1,'day').isBefore(check_in_date.value)) {
    return true
  }
}

const date_checker = (date: Dayjs) => {
  if (date.isAfter(check_out_date.value)) {
    check_out_date.value = date.add(1,'day')
  }
}
</script>

<template>
  <div class="container">
    <a-carousel class="carousel" :autoplay=true>
      <div class="img-container">

        <img src="../assets/home1.jpg" class="carousel-image"/>
        <div class="overlayer">
          <div class="img-info">EXPERIENCE <br/> <span class="primary">LUXURY</span> <br/> AND <br/> <span
              class="primary">COMFORT</span></div>

        </div>
      </div>
      <div class="img-container">
        <img src="../assets/home2.jpg" class="carousel-image"/>
        <div class="overlayer">
          <div class="img-info">YOUR <br/> <span class="primary">PERFECT <br/> GETAWAY </span> <br/> AWAITS</div>
        </div>
      </div>

    </a-carousel>
    <div class="overlayer-box">
      <a-row class="row">
        <a-col>
          <a-space direction="vertical" class="col" size="middle">
            <a-row>
              <div style="font-size: 16px">CHECK-IN</div>
            </a-row>
            <a-row>
              <a-date-picker size="large" format="MMM/DD/YYYY" v-model:value="check_in_date" :disabled-date="disabled_check_in_date" @change="date_checker"/>
            </a-row>
          </a-space>
        </a-col>
        <a-col>
          <a-space direction="vertical" class="col" size="middle">
            <a-row>
              <div style="font-size: 16px">CHECK-OUT</div>
            </a-row>
            <a-row>
              <a-date-picker size="large" format="MMM/DD/YYYY" v-model:value="check_out_date" :disabled-date="disabled_check_out_date"/>
            </a-row>
          </a-space>
        </a-col>
        <a-col class="col">
          <a-space direction="vertical" class="col" size="middle">
            <a-row>
              <div style="font-size: 16px">GUESTS</div>
            </a-row>
            <a-row>
              <a-input-number size="large" min="1" v-model:value="guest_number"/>
            </a-row>
          </a-space>
        </a-col>

      </a-row>
      <a-button class="full-button" type="text">CHECK <br/> AVAILABILITY</a-button>

    </div>
  </div>
</template>

<style scoped>

.carousel-image {
  width: 100%;
  height: 750px;
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

.img-info {
  color: white;
  font-size: 60px;
  text-align: center;
  font-family: "Times New Roman", serif;
  margin-bottom: 200px;
}

.img-container {
  position: relative;

}

.container {
  position: relative;
}

.primary {
  font-size: 80px;
  font-weight: bold;
}

.overlayer-box {
  width: 800px;
  height: 150px;
  background: white;
  position: absolute;
  bottom: 60px;
  left: 50%;
  transform: translate(-50%, 0);
  overflow: clip;
}

.row {
  display: flex;
  justify-content: space-evenly;
  align-items: center;
  height: 100%;
  margin-right: 170px;
}

.col {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.full-button {
  position: absolute;
  right: 0;
  top: -10%;
  bottom: 0;
  height: 120%;
  width: 180px;
  background: black;
  color: white;
  font-size: 18px;
}
</style>