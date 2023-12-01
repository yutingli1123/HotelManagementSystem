<script setup lang="ts">
import {ref} from "vue";
import type {Dayjs} from 'dayjs'
import dayjs from 'dayjs'
import SvgIcon from '@jamescoyle/vue-icon'
import {mdiDesk, mdiShower, mdiSofaSingle, mdiTelevisionClassic, mdiWifi} from "@mdi/js";
import {TeamOutlined} from "@ant-design/icons-vue";
import {useRouter} from "vue-router";

const router = useRouter()
const today = dayjs()
const guest_number = ref(1)
const check_in_date = ref<Dayjs>(today)
const check_out_date = ref<Dayjs>(today.add(1, 'day'))

const disabled_check_in_date = (date: Dayjs) => {
  return date.isBefore(today)
}

const disabled_check_out_date = (date: Dayjs) => {
  return date.subtract(1, 'day').isBefore(check_in_date.value)
}

const date_checker = (date: Dayjs) => {
  if (date.add(1, 'day').isAfter(check_out_date.value)) {
    check_out_date.value = date.add(1, 'day')
  }
}

const submit = () => {
  router.push({name:'result',query:{checkIn: check_in_date.value.format('YYYY-MM-DD'), checkOut: check_out_date.value.format('YYYY-MM-DD'), guests: guest_number.value}})
}

</script>

<template>
  <div class="container">
    <a-carousel class="carousel" :autoplay=true>
      <div class="img-container">

        <img src="/home1.jpg" class="carousel-image" alt="Home Page Image 1"/>
        <div class="overlayer">
          <div class="img-info">EXPERIENCE <br/> <span class="primary">LUXURY</span> <br/> AND <br/> <span
              class="primary">COMFORT</span></div>

        </div>
      </div>
      <div class="img-container">
        <img src="/home2.jpg" class="carousel-image" alt="Home Page Image 2"/>
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
              <a-date-picker size="large" format="MMM/DD/YYYY" v-model:value="check_in_date"
                             :disabled-date="disabled_check_in_date" @change="date_checker"/>
            </a-row>
          </a-space>
        </a-col>
        <a-divider type="vertical" style="height: 80px"></a-divider>

        <a-col>
          <a-space direction="vertical" class="col" size="middle">
            <a-row>
              <div style="font-size: 16px">CHECK-OUT</div>
            </a-row>
            <a-row>
              <a-date-picker size="large" format="MMM/DD/YYYY" v-model:value="check_out_date"
                             :disabled-date="disabled_check_out_date"/>
            </a-row>
          </a-space>
        </a-col>
        <a-divider type="vertical" style="height: 80px"></a-divider>
        <a-col class="col" style="margin-right: 10px">
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
      <a-button class="full-button" type="text" @click="submit">CHECK <br/> AVAILABILITY</a-button>
    </div>
  </div>
  <div style="height: 20px;"/>
  <div class="center">
    <a-row>
      <a-col style="margin-top: 35px">
        <img class="img-welcome" src="/reception.jpg" alt="Reception Image">
      </a-col>
      <a-col class="message">
        <div class="banner"><p>Welcome To <span style="font-style: italic">Marwan<span
            class="text-primary">Continental</span></span>
        </p></div>
        <a-typography>
          <a-typography-paragraph class="welcome-message">
            Here, we are committed to providing you with an unparalleled lodging experience. The comfort and
            satisfaction
            of
            every guest is our greatest pursuit. Whether you are here for business or leisure, our team of professionals
            will
            be at your service wholeheartedly.
          </a-typography-paragraph>
          <a-typography-paragraph class="welcome-message">
            Our hotel features elegant and comfortable guest rooms equipped with state-of-the-art facilities designed to
            meet
            all your needs. Here you can enjoy peace and relaxation, away from the hustle and bustle of everyday life.
            We also offer a diverse range of dining options, including authentic local cuisine and international
            delicacies
            to
            satisfy your taste buds. In addition, the hotel is equipped with a fitness center, spa and meeting rooms, so
            whether you are looking for a physical workout or for business needs, we are here to serve you.
          </a-typography-paragraph>
          <a-typography-paragraph class="welcome-message">
            Thank you for choosing our hotel and we look forward to meeting you and creating unforgettable memories
            together
            in this wonderful place!
          </a-typography-paragraph>
        </a-typography>
      </a-col>
    </a-row>
  </div>
  <a-divider/>
  <a-row :wrap=true :gutter=[40,40] justify="center" style="margin-bottom: 30px;width: 100%">
    <a-col>
      <a-card hoverable style="width: 350px">
        <template #cover>
          <img
              src="/regular.jpg" alt="Regular Room"
          />
        </template>
        <template #actions>
          <a-tooltip title="Wi-Fi">
            <svg-icon type="mdi" :path="mdiWifi" size="16"/>
          </a-tooltip>
          <a-tooltip title="Shower">
            <svg-icon type="mdi" :path="mdiShower" size="16"/>
          </a-tooltip>
          <a-tooltip title="Television">
            <svg-icon type="mdi" :path="mdiTelevisionClassic" size="16"/>
          </a-tooltip>
        </template>
        <a-card-meta title="Regular Room">
          <template #description>
            <TeamOutlined/>
            2 GUESTS <br/>
            <div style="margin-top: 10px;margin-bottom: 25px">Room with one queen bed. Amenities include a private
              washroom with
              shower.
            </div>
          </template>
        </a-card-meta>
      </a-card>
    </a-col>
    <a-col>
      <a-card hoverable style="width: 350px">
        <template #cover>
          <img
              src="/deluxe.jpg" alt="Deluxe Room"
          />
        </template>
        <template #actions>
          <a-tooltip title="Wi-Fi">
            <svg-icon type="mdi" :path="mdiWifi" size="16"/>
          </a-tooltip>
          <a-tooltip title="Shower">
            <svg-icon type="mdi" :path="mdiShower" size="16"/>
          </a-tooltip>
          <a-tooltip title="Television">
            <svg-icon type="mdi" :path="mdiTelevisionClassic" size="16"/>
          </a-tooltip>
          <a-tooltip title="Desk">
            <svg-icon type="mdi" :path="mdiDesk" size="16"/>
          </a-tooltip>
          <a-tooltip title="Sofa">
            <svg-icon type="mdi" :path="mdiSofaSingle" size="16"/>
          </a-tooltip>
        </template>
        <a-card-meta title="Deluxe Room">
          <template #description>
            <TeamOutlined/>
            2 GUESTS <br/>
            <div style="margin-top: 10px"> Larger room with one queen bed. Amenities include a private washroom with
              shower, a sofa, a working desk with chair.
            </div>
          </template>
        </a-card-meta>
      </a-card>
    </a-col>
    <a-col>
      <a-card hoverable style="width: 350px">
        <template #cover>
          <img
              src="/double.jpg" alt="Double Room"
          />
        </template>
        <template #actions>
          <a-tooltip title="Wi-Fi">
            <svg-icon type="mdi" :path="mdiWifi" size="16"/>
          </a-tooltip>
          <a-tooltip title="Shower">
            <svg-icon type="mdi" :path="mdiShower" size="16"/>
          </a-tooltip>
          <a-tooltip title="Television">
            <svg-icon type="mdi" :path="mdiTelevisionClassic" size="16"/>
          </a-tooltip>
          <a-tooltip title="Sofa">
            <svg-icon type="mdi" :path="mdiSofaSingle" size="16"/>
          </a-tooltip>
        </template>
        <a-card-meta title="Double Room">
          <template #description>
            <TeamOutlined/>
            4 GUESTS <br/>
            <div style="margin-top: 10px;margin-bottom: 25px">Room with two queen beds. Amenities include a private
              washroom with shower
              and a sofa.
            </div>
          </template>
        </a-card-meta>
      </a-card>
    </a-col>
  </a-row>
</template>

<style scoped>
.banner {
  font-size: 32px;
  font-family: "Times New Roman", serif;
}

.center {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.welcome-message {
  font-size: 18px;
  font-family: "Times New Roman", serif;
  width: 700px;
  color: rgba(0, 0, 0, 0.7);
}

.img-welcome {
  width: 370px;
  height: 400px;
  object-fit: cover;
  margin-right: 50px;
}

.message {
  align-items: start;
}

.carousel-image {
  width: 100%;
  height: 750px;
  object-fit: cover;
}

.overlayer {
  background: rgba(0, 0, 0, 0.4);
  position: absolute;
  top: 0;
  bottom: -3px;
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
  height: 130px;
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