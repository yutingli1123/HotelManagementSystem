<script setup lang="ts">
import {CheckCircleOutlined} from "@ant-design/icons-vue";
</script>

<template>
  <div style="position: relative">
    <img class="background_image" src="/regular.jpg" alt="Background Image"/>
    <div class="overlayer"/>
  </div>
  <div style="height: 30px"></div>
  <div style="display: flex;justify-content: center">
    <a-card style="width: 900px">
      <a-col style="display: flex;flex-direction:column;align-items: center">
        <a-row style="font-size: 80px"><CheckCircleOutlined /></a-row>
        <a-row style="font-size: 50px">Book Successfully</a-row>
        <a-row style="font-size: 24px">Thank you for choosing our hotel. We wish you a pleasant and comfortable stay. </a-row>
      </a-col>
    </a-card>
  </div>
  <div style="height: 30px;"/>
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