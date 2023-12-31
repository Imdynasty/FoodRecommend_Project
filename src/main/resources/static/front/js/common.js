var commonLib = commonLib || {};

/**
* 현재 위치 관련
*
*/
commonLib.geolocation = {
    async init() {
        await this.save();
        const my = this.getMy();
        let searchRestauranLinkQs = "?";
        if (frmSearch && frmSearch.sido && frmSearch.sido.value == "" && my.sido && my.sido.trim()) {
            frmSearch.sido.value = my.sido;
            searchRestauranLinkQs += `sido=${my.sido}`;
        }

        if (frmSearch && frmSearch.sigugun && frmSearch.sigugun.value == "") {
            const { area } = commonLib;
            const siguguns = await area.getSigugun(my.sido);
            if (!siguguns) return;
            const targetEl = frmSearch.sigugun;
            targetEl.innerHTML = "<option value=''>- 시구군 선택-</option>";
            for (const sgg of siguguns) {
                const option = document.createElement("option");
                option.value = sgg;
                const optionText = document.createTextNode(sgg);
                option.appendChild(optionText);
                if (sgg == my.sigugun) {
                    option.selected = true;
                }
                targetEl.appendChild(option);
            }

            if (my.sigugun) searchRestauranLinkQs += `&sigugun=${my.sigugun}`;
        }

        const el = document.querySelector(".search_restaurant_link");
        if (el) el.href = el.href + searchRestauranLinkQs;
    },
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

                   //if (frmSearch && frmSearch.address) frmSearch.address.value = pos.address;
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
    init() {
       this.save();
       this.getMyFavorites(10);
       this.loadRecentKeywords();
    },
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
    * 개인 검색 순위별, 현재 위치별 검색 데이터 조회
    *
    */
    async getMyFavorites(limit) {
        const myFavoriteEls = document.getElementsByClassName("my_favorite");
        if (myFavoriteEls.length == 0) return;

        const { geolocation, ajaxLoad } = commonLib;
        const my = geolocation.getMy();

        limit = limit || 10;
        const keywords = this.getRanks().map(r => `keywords=${r.key}`).join("&");

        if (!keywords || !my.sido || !my.sido.trim() || !my.sigugun || !my.sigugun.trim())
            return;

        let url = `/restaurant/my?limit=${limit}`;
        if (keywords) url += "&" + keywords;
        if (my.sido && my.sido.trim()) url += `&sido=${my.sido.trim()}`;
        if (my.sigugun && my.sigugun.trim()) url += `&sigugun=${my.sigugun.trim()}`;


        ajaxLoad("GET", url)
            .then((res) => {

                for (const el of myFavoriteEls) {
                    el.innerHTML = res;
                }
            })
            .catch((err) => console.error(err));
    },
    async loadRecentKeywords(e) {
        const { ajaxLoad } = commonLib;
        const formData = new FormData(frmSearch);
        const searchType = formData.get("searchType");

        const targetEl = document.querySelector(".frm_search .keywords");
        if (!targetEl) return;
        targetEl.innerHTML = "";
        try {
            let url = '/search/keywords';
            const list = await ajaxLoad("POST", url, formData, "json");
            if (!list) return;

            for (const key of list) {
                const liEl = document.createElement("li");
                const textNode = document.createTextNode(key);
                const spanEl = document.createElement("span");
                spanEl.appendChild(textNode);
                liEl.appendChild(spanEl);
                // 최근 검색일때 삭제 버튼 추가 및 처리 S
                if (searchType == 'recent') {
                      const removeEl = document.createElement("i");
                      removeEl.className="xi-close remove";
                      liEl.appendChild(removeEl);
                      removeEl.addEventListener("click", function() {
                        commonLib.search.removeKeyword(key);
                        liEl.parentElement.removeChild(liEl);
                      });
                }
                // 최근 검색일때 삭제 버튼 추가 및 처리 E

                targetEl.appendChild(liEl);

                spanEl.addEventListener("click", function() {
                    frmSearch.skey.value = key;
                    frmSearch.submit();
                });
            }
        } catch (err) {
            console.error(err);
        }
    },
    /**
    * 드롭 다운 메뉴 보이기
    */
    showDropDown() {
        const searchDropdown = document.querySelector(".search_dropdown");
        if (!searchDropdown) return;
        searchDropdown.classList.remove("dn");

        commonLib.search.loadRecentKeywords();
    },
    /**
    * 드롭다운 메뉴 감추기
    *
    */
    closeDropDown() {
        const searchDropdown = document.querySelector(".search_dropdown");
        if (!searchDropdown) return;
        searchDropdown.classList.remove("dn");
        searchDropdown.classList.add("dn");
    },
    /**
    * 키워드 삭제
    *
    */
    removeKeyword(keyword) {
        const { ajaxLoad } = commonLib;
        if (!keyword || !keyword.trim()) return;

        keyword = keyword.trim();

        const url = `/search/remove/${encodeURIComponent(keyword)}`;

        ajaxLoad("GET", url);
    }
};



window.addEventListener("DOMContentLoaded", function() {
    const { geolocation, search, ajaxLoad } = commonLib;
    geolocation.init();
    search.init();

    /** 찜하기 버튼 클릭 이벤트 처리 S */
    const wishToggles = document.getElementsByClassName("toggle_wish");
    for (const el of wishToggles) {
        el.addEventListener("click", function() {
            const id = this.dataset.id;
            if (!id) return;
            let url = "/wish/";
            url = this.checked ?  url += "save" : url += "delete";
            url += `?id=${id}`;
            console.log(this.checked, url);
            ajaxLoad("GET", url)
                .then((res) => {
                    el.classList.contains("on") ? el.classList.remove("on") : el.classList.add("on");
                })
                .catch((err) => console.error(err));
        });
    }
    /** 찜하기 버튼 클릭 이벤트 처리 E */

    /** 키워드 입력시 자동 완성 처리 S */
    const skeyEl = document.querySelector(".frm_search input[name='skey']");
    if (skeyEl) {
        skeyEl.addEventListener("focus", search.showDropDown);
        skeyEl.addEventListener("keyup", search.showDropDown);
    }

    const searchTypeEls = document.querySelectorAll(".frm_search input[name='searchType']");
    for (const el of searchTypeEls) {
        el.addEventListener("click", search.loadRecentKeywords);
    }


    const closePopupEl = document.querySelector(".close_popup");
    if (closePopupEl) {
        closePopupEl.addEventListener("click", search.closeDropDown);
    }
    /** 키워드 입력시 자동 완성 처리 E */

    /** 클릭 색 변경 이벤트*/


});