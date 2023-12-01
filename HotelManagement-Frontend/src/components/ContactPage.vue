<script setup lang="ts">

</script>

<template>
  <div style="position: relative">
    <img class="background_image" src="/contact_background.jpg" alt="Background Image"/>
    <div class="overlayer">
      <div class="img-info">Contact</div>
    </div>
  </div>
  <div style="height: 30px;"></div>
  <a-row :gutter=[40,30] class="center">
    <a-col>
      <iframe
          src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d699.0258935321053!2d-73.55248643027768!3d45.50799359819216!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x4cc91a55d6840017%3A0xff4f8b3ee2654f54!2s339%20Rue%20de%20la%20Commune%20E%2C%20Montr%C3%A9al%2C%20QC%20H2Y%201J3!5e0!3m2!1sen-US!2sca!4v1700893583833!5m2!1sen-US!2sca"
          width="600" height="450" style="border:0;" allowfullscreen="false" loading="lazy"
          referrerpolicy="no-referrer-when-downgrade"></iframe>
    </a-col>
    <a-col>
        <a-card style="padding: 10px">
          <a-row class="info-title">
            Address
          </a-row>
          <a-row class="info">
            339 Rue de la Commune E, Montréal, QC, Canada H2Y 1J3
          </a-row>
          <a-divider/>
          <a-row>
            <a-col>
              <a-row class="info-title">
                Email
              </a-row>
              <a-row class="info">
                <a href="mailto: info@thehotel.ca">info@thehotel.ca</a>
              </a-row>
            </a-col>
            <a-divider type="vertical" style="height: 65px;margin-left: 80px;margin-right: 20px"/>
            <a-col>
              <a-row class="info-title">
                Telephone
              </a-row>
              <a-row class="info">
                <a href="tel:(438) 123-4567">(438) 123-4567</a>
              </a-row>
            </a-col>
          </a-row>

        </a-card>
    </a-col>
  </a-row>
  <div style="height: 30px"/>
</template>

<style scoped>
.img-info {
  color: white;
  font-size: 60px;
  text-align: center;
  font-family: "Times New Roman", serif;
}

.info {
  font-size: 16px;
}

.info-title {
  font-size: 20px;
  font-weight: bold;
}

.center {
  display: flex;
  justify-content: center;

}
</style>