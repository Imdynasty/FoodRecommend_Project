var commonLib = commonLib || {};

/**
* 현재 위치 관련
*
*/
commonLib.geolocation = {
    /**
    * 현재 위치 가져오기
    *
    */
    getLocation() {
        const options = {
            enableHighAccuracy: true,
            timeout: 5000,
            maximumAge: 0,
        };
        return new Promise((resolve, reject) => {
            navigator.geolocation.getCurrentPosition(
                (pos) => resolve({lat : pos.coords.latitude , lng : pos.coords.longitude }),
                (err) => reject(err));
        });
    },
    /**
    * 현재 위치 로컬 스토리지에 저장하기
    *
    */
    async save() {
        try {
            const pos = await this.getLocation();
            /** 좌표로 주소 정보 조회 및 localStorage 저장처리 */
            const geocoder = new kakao.maps.services.Geocoder();
            geocoder.coord2RegionCode(pos.lng, pos.lat, function(result, status) {
                if (status === kakao.maps.services.Status.OK) {
                   const data=result[0];
                   pos.address=data.address_name;
                   pos.sido=data.region_1depth_name;
                   pos.sigugun=data.region_2depth_name;
                   pos.dong=data.region_3depth_name;
                   localStorage.setItem("myLocation", JSON.stringify(pos));
                }
            });
        } catch (err) {
            console.warn(err);
        }
    },
    /**
    * 현재 나의 위치 정보
    *
    */
    getMy() {
        let data = localStorage.getItem("myLocation");
        data = data ? JSON.parse(data) : {};
        return data;
    }
}

/**
* 위치 + 최근 검색어 기반 데이터 조회
*
*/
commonLib.search = {
    hours : 3,
    /**
    * 최근 검색어 및 검색어 순위 저장
    *
    */
    save() {
        const { ajaxLoad } = commonLib;
        const params = new URLSearchParams(location.search);
        const skey = params.get('skey');
        /** 최근 검색어 DB 저장처리 S */
        if (skey) {
            const formData = new FormData();
            formData.append("keyword", skey);
            ajaxLoad("POST", "/search/keyword/save", formData);
        }
        /** 최근 검색어 DB 저장처리 E */

        let searches = localStorage.getItem("searches");
        searches = searches ? JSON.parse(searches) : {};
        searches[skey] = searches[skey] || { count : 0, stamp : Date.now() };
        searches[skey].count++;
        searches[skey].stamp = Date.now();

        /* hours에 지정된 시간만큼 경과된 키워드 삭제 처리 */
        let searchRanks = [];
        for (key in searches) {
            const data = searches[key];
            if (!key || key == 'null' || data.stamp < Date.now() - 1000 * 60 * 60 * this.hours) {
                delete searches[key];
            } else {
                searchRanks.push({...searches[key], key })
            }
        }

        /* 인기순 정렬 */
        searchRanks = searchRanks.sort((s1, s2) => s2.count - s1.count);

        localStorage.setItem("searches", JSON.stringify(searches));
        localStorage.setItem("searchRanks", JSON.stringify(searchRanks));
    },
    /**
    * 검색 순위별 데이터 조회
    *
    */
    getRanks() {
        let ranks = localStorage.getItem("searchRanks");
        ranks = ranks ? JSON.parse(ranks):[];

        return ranks;
    },
    /**
    * 검색 순위별, 검색 데이터 조회
    *
    */
    getMyFavorites(limit) {

    }
};


window.addEventListener("DOMContentLoaded", function() {
    const { geolocation, search } = commonLib;
    geolocation.save();
    search.save();
});