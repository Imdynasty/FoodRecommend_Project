/**
* 주소 API 처리
*
*/
var commonLib = commonLib || {};
commonLib.address = {
    /**
    * 주소 검색 처리
    *
    */
    search(e) {
        const dataset = e.currentTarget.dataset;

        const zipcodeName = dataset.zipcodeName;
        const addressName = dataset.addressName;
        new daum.Postcode({
               oncomplete: function(data) {
                   document.querySelector(`input[name='${zipcodeName}']`).value = data.zonecode;
                   document.querySelector(`input[name='${addressName}']`).value = data.address;
               }
       }).open();
    }
};

window.addEventListener("DOMContentLoaded", function() {
    /** 주소 API 동적 로딩 */
    const apiURL = "//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js";
    const script = document.createElement("script");
    script.src=apiURL;
    document.head.insertBefore(script, document.head.children[0]);

    const searchAddress = document.getElementsByClassName("search_address");
    for (const el of searchAddress) {
        el.addEventListener("click", commonLib.address.search);
    }
});