/**
 * 레이어 팝업
 *
 */
var commonLib = commonLib || {};
commonLib.popup = {
    callback : function() {}, // popup 오픈 후 실행될 콜백 함수
    /**
     * popup 오픈 후 실행될 콜백 함수 설정
     *
     * @param {Function} callback
     */
    setCallback : function(callback) {
        this.callback = callback;
        return this;
    },
    /**
     * 레이어 팝업 열기
     *
     * @param {String} url 팝업 URL
     * @param {String} title 팝업 제목
     * @param {int} width 팝업 너비
     * @param {int} height 팝업 높이
     * @param {boolean} isIframe iframe 형태 사용여부
     */
    open(url, title, width, height, isIframe) {
        if (!url)
            return;

        let isMobile = false;
        const bodyEl = document.querySelector("body");
        if (bodyEl && bodyEl.classList.contains("body-mobile")) {
            isMobile = true;
        }

        width = width || 300;
        height = height || 300;

        if (isMobile) {
            if (width > window.innerWidth) {
                width = window.innerWidth - 20;
            }

            if (height > window.inenerHeight) {
                height = window.innerHeight - 20;
            }
        }

        isIframe = isIframe?true:false;

        var left = Math.round((window.innerWidth - width) / 2);
        var top = Math.round((window.innerHeight - height) / 2);

        var layerDim = document.createElement("div");
        layerDim.id = 'layer_dim';
        layerDim.addEventListener("click", commonLib.popup.close);
        var layerPopup = document.createElement("div");
        layerPopup.id = 'layer_popup';
        layerPopup.style.width = width + "px";
        layerPopup.style.height = height + "px";
        layerPopup.style.top = top + "px";
        layerPopup.style.left = left + "px";

        /** 팝업 제목 처리 S */
        if (title) {
            var layerTitle = document.createElement("div");
            layerTitle.className = "popup_title";
            var titleNode = document.createTextNode(title);
            layerTitle.appendChild(titleNode);
            layerPopup.appendChild(layerTitle);
        }
        /** 팝업 제목 처리 E */

        /** 팝업 내용 영역 S */
        if (isIframe) {
            var h = height - 45;
            if (title) {
                h -= 31 + 20;
            }

            var layerContents = document.createElement("iframe");
            layerContents.src=url;
            layerContents.width=width - 40;
            layerContents.height=h;
            layerContents.setAttribute('frameborder', 0);
            layerContents.setAttribute('scrolling', 'auto');
            layerContents.name = 'ifrmPopup';
            layerContents.id = 'ifrmPopup';
            layerPopup.appendChild(layerContents);
        } else {
            var layerContents = document.createElement("div");
            layerContents.className = "popup_html";
            layerPopup.appendChild(layerContents);

            var xhr = new XMLHttpRequest();
            xhr.open("GET", url);
            xhr.onreadystatechange = function() {
            if (xhr.readyState == XMLHttpRequest.DONE && xhr.status == 200) {
                layerContents.innerHTML = xhr.responseText;
                if (typeof commonLib.popup.callback == 'function') {
                    typeof commonLib.popup.callback();
                }
            }
            };
            xhr.send(null);
        }
        /** 팝업 내용 영역 E */

        /** 팝업 닫기 버튼 S */
        var layerClose = document.createElement("i");
        layerClose.className = "xi-close-thin layer_close";
        layerPopup.appendChild(layerClose);
        layerClose.addEventListener("click", commonLib.popup.close);
        /** 팝업 닫기 버튼 E */

        document.body.appendChild(layerDim);
        document.body.appendChild(layerPopup);
    },
    /**
     * 팝업 닫기
     *
     */
    close() {
        var layerDim = document.getElementById("layer_dim");
        var layerPopup = document.getElementById("layer_popup");
        if (layerPopup) {
            document.body.removeChild(layerPopup);
        }

        if (layerDim) {
            document.body.removeChild(layerDim);
        }
    }
};

window.addEventListener("DOMContentLoaded", function() {
    /** 팝업 열기 처리 S */
    var layerPopups = document.getElementsByClassName("layer_popup")
    if (layerPopups.length > 0) {
        for (var i = 0; i < layerPopups.length; i++) {
            layerPopups[i].addEventListener("click", function(e) {
                var dataset = e.currentTarget.dataset;
                var excepts = ['width', 'height', 'url', 'title'];
                var url = dataset.url;
                var width = dataset.width || 300;
                var height = dataset.height || 300;
                var title = dataset.title;
                var qs = [];
                for (key in dataset) {
                    if (excepts.indexOf(key) != -1)
                        continue;

                    qs.push(key + "=" + dataset[key]);

                }
                if (qs.length > 0) {
                    if (url.indexOf("?") == -1) url += "?";
                    else url += "&";
                    url += qs.join("&");
                }

                commonLib.popup.open(url, title, Number(width), Number(height));
            });

        }
    }
    /** 팝업 열기 처리 E */

    /** 팝업 닫기 처리 S */
    var layerCloses = document.getElementsByClassName("layer_close");
    if (layerCloses.length > 0) {
        for (var i = 0; i < layerCloses.length; i++) {
            layerCloses[i].addEventListener("click", commonLib.popup.close);
        }
    }
    /** 답업 닫기 처리 E */
});