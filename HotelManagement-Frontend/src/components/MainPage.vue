<script setup lang="ts">

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
              <a-date-picker size="large"/>
            </a-row>
          </a-space>
        </a-col>
        <a-col>
          <a-space direction="vertical" class="col" size="middle">
            <a-row>
              <div style="font-size: 16px">CHECK-OUT</div>
            </a-row>
            <a-row>
              <a-date-picker size="large"/>
            </a-row>
          </a-space>
        </a-col>
        <a-col class="col">
          <a-space direction="vertical" class="col" size="middle">
            <a-row>
              <div style="font-size: 16px">GUESTS</div>
            </a-row>
            <a-row>
              <a-input-number size="large"/>
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
  height: 1200px;
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
  bottom: 140px;
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