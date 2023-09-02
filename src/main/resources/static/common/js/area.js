var commonLib = commonLib || {};

commonLib.area = {
    /**
    * 시도 목록
    *
    */
    async getSido() {
        let result = [];
        try {
            const data = await commonLib.ajaxLoad("get", "/ajax/sido");
            if (data) result = JSON.parse(data);
        } catch (err) {
            console.log(err);
        }

        return result;
    },
    /**
    * 시구군 조회
    *
    */
    async getSigugun(sido) {
        let result = [];
        try {
            const data = await commonLib.ajaxLoad("get", "/ajax/sigugun/" + sido);
            if (data) result = JSON.parse(data);
        } catch (err) {
            console.log(err);
        }

        return result;
    }
};

window.addEventListener("DOMContentLoaded", function() {
    const { area } = commonLib;
    /** 시도 변경시 시구군 데이터 조회 및 반영 처리 S */
    const ajaxSidoEls = document.getElementsByClassName("ajax_sido");
    if (ajaxSidoEls.length > 0) {
        for (const el of ajaxSidoEls) {
            el.addEventListener("change", async function() {
                const parentEl = this.parentElement;
                const targetEl = parentEl.querySelector(".ajax_gigugun");
                if (!targetEl) return;
                const sido = this.value.trim();
                targetEl.innerHTML = "<option value=''>- 시구군 선택-</option>";
                if (!sido) return;

                let siguguns = await area.getSigugun(sido);
                if (!siguguns) return;
                targetEl.innerHTML = "";
                for (const sgg of siguguns) {
                    const option = document.createElement("option");
                    option.value = sgg;
                    const optionText = document.createTextNode(sgg);
                    option.appendChild(optionText);

                    targetEl.appendChild(option);
                }

            });
        }
    }

    /** 시도 변경시 시구군 데이터 조회 및 반영 처리 E */
});