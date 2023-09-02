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
            localStorage.setItem("myLocation", JSON.stringify(pos));
        } catch (err) {
            console.warn(err);
        }
    },

}

window.addEventListener("DOMContentLoaded", function() {
       commonLib.geolocation.save();
});