<script setup>

import ReviewList from "@/components/ReviewListComponent.vue";
import {ref, onMounted} from 'vue';
import axios from 'axios';
import {useRoute} from "vue-router";

const event = ref(null);
const route = useRoute(); // useRoute()를 사용하여 현재 라우팅 정보를 가져옴

const fetchEventDetail = async () => {
  const eventId = Number(route.params.eventId); // 이벤트 ID를 Long으로 변환
  try {
    const response = await axios.get(`http://localhost:8080/events/${eventId}`);
    event.value = response.data;
  } catch (error) {
    console.error('이벤트 상세 정보를 불러오는 동안 오류가 발생했습니다:', error);
  }
};

onMounted(() => {
  const eventId = route.params.eventId; // 라우팅 정보에서 eventId를 가져옴
  fetchEventDetail(eventId); // 받아온 이벤트 ID 값을 사용하여 이벤트 상세 정보 호출
});
</script>

<template>
  <div class="event-detail" v-if="event">
    <div
        style="width: 100%; height: 60px; background-color: #392365; border-radius: 10px; margin: auto auto 10px auto; text-align: center">
      <h2 style="color: white; font-weight: bold">{{ event.title }}</h2>
    </div>
    <div class="이미지포함이벤트설명" style="display: flex; margin: auto auto 10px auto;">
      <v-img class="img" :src="event.posterImage" cover alt="Event Poster">
      </v-img>
      <!--          공연 정보를 담는 디브-->
      <div
          style="width: 800px; height: 500px; background-color: #392365; border-radius: 10px; margin: auto auto auto 10px;">
        <!--            안에 들어가는 정보들-->
        <div class="공연정보담고있는디브">
          <div class="공연정보1">
            <h5></h5>
          </div>
          <div class="공연정보2">
            <h5></h5>
          </div>
        </div>
        <div class="공연정보담고있는디브">
          <div class="공연정보1">
            <h5>장소</h5>
          </div>
          <div class="공연정보2">
            <h5>{{ event.place.name }}</h5>
          </div>
        </div>
        <div class="공연정보담고있는디브">
          <div class="공연정보1">
            <h5>기간</h5>
          </div>
          <div class="공연정보2">
            <h5>{{ event.startDate }} ~ {{ event.endDate }}</h5>
          </div>
        </div>
        <div class="공연정보담고있는디브">
          <div class="공연정보1">
            <h5>정보</h5>
          </div>
          <div class="공연정보2">
            <h5>{{ event.eventInfo }}</h5>
          </div>
        </div>
        <div class="공연정보담고있는디브">
          <div class="공연정보1">
            <h5>평점</h5>
          </div>
          <div class="공연정보2">
            <h5>{{ event.averageRating }}</h5>
          </div>
        </div>
        <div class="공연정보담고있는디브">
          <div class="공연정보1">
            <h5>가격</h5>
          </div>
          <div class="공연정보2">
            <h5>R 석 - {{ event.price.seatRPrice }}</h5>
            <h5>S 석 - {{ event.price.seatSPrice }}</h5>
            <h5>A 석 - {{ event.price.seatAPrice }}</h5>
          </div>
        </div>
      </div>
    </div>
    <div style="; width: 100%; height: 80px; display: flex">
      <h3 style="color: white;text-align: center; width: 55px; height: 40px; margin-right: 10px; background-color: #392365; border-radius: 20px; ">0</h3>
      <button type="button" style="width: 110px; height: 40px; background-color: #392365; border-radius: 20px; ">
        <h5 style="color: white">좋아요</h5>
      </button>
    </div>


    <!--        여기 아래로는 리뷰들-->
    <div class="리뷰를담은디브">
      <div style="height: 60px; background-color: #392365; border-radius: 10px">
        <div style="display: flex;">
          <h5 style="margin: 17px 10px auto auto; color: #aa98ba;">리뷰 개수 :</h5>
          <h5 style="margin: 17px auto auto 10px; color: white;">{{ event.reviewCount }}</h5>
        </div>
      </div>
      <!--리뷰 추가될때마다 1개씩 추가-->
      <ReviewList/>
    </div>
  </div>
</template>

<style scoped>
@import "../css/styles/TestStyle.css";

.리뷰를담은디브 {
  width: 100%;
}

.공연정보담고있는디브 {
  height: 60px;
  display: flex;
}

.공연정보1 {
  color: #aa98ba;
  width: 160px;
  height: 100%;
  text-align: right;
}

.공연정보2 {
  color: white;
  width: 530px;
  height: 100%;
  margin-left: 40px
}

.event-detail {
  background-color: #aa98ba;
  border-radius: 20px;
  height: 2000px;
  width: 1200px;
  padding: 50px;
  margin: 100px;
}

.img {
  border-radius: 10px;
  width: 300px;
  height: 500px;
}
</style>