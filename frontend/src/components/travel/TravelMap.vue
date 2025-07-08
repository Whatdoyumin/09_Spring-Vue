<script setup>
import { reactive, ref } from 'vue';
import { KakaoMap, KakaoMapMarker } from 'vue3-kakao-maps';

// Props 정의 - 부모 컴포넌트에서 제목과 주소 전달받기
const props = defineProps({
  title: { type: String, required: true }, // 여행지 제목
  address: { type: String, required: true }, // 여행지 주소
});

// 서울 시청 좌표 (기본값)
const coordinate = reactive({
  lat: 37.566826, // 위도
  lng: 126.9786567, // 경도
});

const map = ref(); // 지도 객체 참조

// 지도 로드 완료 시 실행되는 함수
const onLoadKakaoMap = (mapRef) => {
  map.value = mapRef; // 지도 객체 저장

  // Geocoder 객체 생성
  const geocoder = new kakao.maps.services.Geocoder();

  // 주소로 GPS 좌표 검색
  geocoder.addressSearch(props.address, (result, status) => {
    // 검색 성공 시 좌표 업데이트
    if (status === kakao.maps.services.Status.OK) {
      coordinate.lat = result[0].y; // 위도
      coordinate.lng = result[0].x; // 경도
    }
  });
};

const visibleRef = ref(false); // 마커에 infoWindow 표시 여부

// 마커 클릭 시 infoWindow on/off 변경 함수
const onClickKakaoMapMarker = () => {
  visibleRef.value = !visibleRef.value;
};
</script>

<template>
  <div><i class="fa-solid fa-map-location-dot"></i> 주소: {{ address }}</div>
  <kakao-map
    :lat="coordinate.lat"
    :lng="coordinate.lng"
    :level="3"
    :draggable="true"
    style="width: 100%"
    @onLoadKakaoMap="onLoadKakaoMap"
  >
    >
    <kakao-map-marker
      :lat="coordinate.lat"
      :lng="coordinate.lng"
      :clickable="true"
      :infoWindow="{ content: title, visible: visibleRef }"
      @onClickKakaoMapMarker="onClickKakaoMapMarker"
    />
  </kakao-map>
</template>
