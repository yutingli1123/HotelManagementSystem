<script setup lang="ts">
import dayjs, {Dayjs} from "dayjs";
import {ref} from "vue";
import {TeamOutlined} from "@ant-design/icons-vue";
import SvgIcon from '@jamescoyle/vue-icon'
import {mdiShower, mdiWifi, mdiTelevisionClassic, mdiDesk, mdiSofaSingle} from '@mdi/js';

const today = dayjs()
const guest_number = ref(1)
const check_in_date = ref<Dayjs>(today)
const check_out_date = ref<Dayjs>(today.add(1, 'day'))

const disabled_check_in_date = (date: Dayjs) => {
  if (date.isBefore(today)) {
    return true
  }
}

const disabled_check_out_date = (date: Dayjs) => {
  if (date.subtract(1, 'day').isBefore(check_in_date.value)) {
    return true
  }
}

const date_checker = (date: Dayjs) => {
  if (date.add(1, 'day').isAfter(check_out_date.value)) {
    check_out_date.value = date.add(1, 'day')
  }
}
</script>

<template>
  <div style="position: relative">
    <img class="image" src="../assets/regular.jpg"/>
    <div class="overlayer">
      <div class="img-info">Rooms</div>
    </div>
  </div>
  <div class="divider"></div>
  <div class="grid">
    <a-col>
      <a-card class="book_box">
        <a-row class="evenly_grid">
          <a-col>
            <a-space direction="vertical" :size=20>
              <a-row class="inner_card">
                <a-col class="center">
                  <a-row>
                    <div style="font-size: 16px">CHECK-IN</div>
                  </a-row>
                  <a-row>
                    <a-date-picker class="picker" size="large" format="MMM/DD/YYYY" v-model:value="check_in_date"
                                   :disabled-date="disabled_check_in_date" @change="date_checker"/>
                  </a-row>
                </a-col>
              </a-row>
              <a-row class="inner_card">
                <a-col class="center">
                  <a-row>
                    <div style="font-size: 16px">GUESTS</div>
                  </a-row>
                  <a-row>
                    <a-input-number class="picker" size="large" min="1" v-model:value="guest_number"/>
                  </a-row>
                </a-col>
              </a-row>
            </a-space>
          </a-col>
          <a-col>
            <a-space direction="vertical" :size=20>
              <a-row class="inner_card">
                <a-col class="center">
                  <a-row>
                    <div style="font-size: 16px">CHECK-OUT</div>
                  </a-row>
                  <a-row>
                    <a-date-picker class="picker" size="large" format="MMM/DD/YYYY" v-model:value="check_out_date"
                                   :disabled-date="disabled_check_out_date"/>
                  </a-row>
                </a-col>

              </a-row>
              <a-row>
                <a-button class="button" type="text">CHECK <br/> AVAILABILITY</a-button>
              </a-row>
            </a-space>
          </a-col>
        </a-row>
      </a-card>
    </a-col>

    <a-col>
      <a-space direction="vertical" :size=50>
        <a-row>

          <a-card hoverable style="width: 350px">
            <template #cover>
              <img
                  src="../assets/regular.jpg"
              />
            </template>
            <template #actions>
              <svg-icon type="mdi" :path="mdiWifi" size="16"/>
              <svg-icon type="mdi" :path="mdiShower" size="16"/>
              <svg-icon type="mdi" :path="mdiTelevisionClassic" size="16"/>
            </template>
            <a-card-meta title="Regular Room">
              <template #description>
                <TeamOutlined/>
                2 GUESTS <br/>
                <div style="margin-top: 10px">Room with one queen bed. Amenities include a private washroom with
                  shower.
                </div>
              </template>
            </a-card-meta>
          </a-card>

        </a-row>
        <a-row>
          <a-card hoverable style="width: 350px">
            <template #cover>
              <img
                  src="../assets/double.jpg"
              />
            </template>
            <template #actions>
              <svg-icon type="mdi" :path="mdiWifi" size="16"/>
              <svg-icon type="mdi" :path="mdiShower" size="16"/>
              <svg-icon type="mdi" :path="mdiTelevisionClassic" size="16"/>
              <svg-icon type="mdi" :path="mdiSofaSingle" size="16"/>
            </template>
            <a-card-meta title="Double Room">
              <template #description>
                <TeamOutlined/>
                4 GUESTS <br/>
                <div style="margin-top: 10px">Room with two queen beds. Amenities include a private washroom with shower
                  and a sofa.
                </div>
              </template>
            </a-card-meta>
          </a-card>
        </a-row>
      </a-space>
    </a-col>
    <a-col>
      <a-card hoverable style="width: 350px">
        <template #cover>
          <img
              src="../assets/deluxe.jpg"
          />
        </template>
        <template #actions>
          <svg-icon type="mdi" :path="mdiWifi" size="16"/>
          <svg-icon type="mdi" :path="mdiShower" size="16"/>
          <svg-icon type="mdi" :path="mdiTelevisionClassic" size="16"/>
          <svg-icon type="mdi" :path="mdiDesk" size="16"/>
          <svg-icon type="mdi" :path="mdiSofaSingle" size="16"/>
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
  </div>
  <div style="height: 30px"/>
</template>

<style scoped>
.grid {
  display: flex;
  justify-content: space-evenly;
  padding-left: 80px;
  padding-right: 80px;
}

.book_box {
  background: rgba(0, 0, 0, 0.9);
  width: 435px;
  height: 280px;
  color: white;
}

.evenly_grid {
  display: flex;
  justify-content: space-between;
}

.inner_card {
  background: white;
  padding: 10px 15px 18px;
  color: black;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.center {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.button {
  height: 105px;
  width: 185px;
  background: rgba(255, 255, 255, 0.1);
  color: white;
  font-size: 18px;
}

.button:hover {
  background: rgba(255, 255, 255, 0.3);
  color: white;
}

.picker {
  margin-top: 10px;
}

.image {
  width: 100%;
  height: 400px;
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
}

.divider {
  position: relative;
  top: -3px;
  height: 80px;
  background: black;
  margin-bottom: 20px;
}
</style>