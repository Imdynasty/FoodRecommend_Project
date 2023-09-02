window.addEventListener("DOMContentLoaded", function() {
    /** 양식 공통 처리 S */
    const formActions = document.getElementsByClassName("form_action");
    for (const el of formActions) {
        el.addEventListener("click", function() {
            const dataset = this.dataset;
            const mode = dataset.mode;
            const targetName = dataset.targetName;
            const target = document[targetName];
            if (!target) return;

            target.mode.value = mode;
            if (mode == 'delete' && !confirm('정말 삭제하시겠습니까?')) {
                return;
            }

            target.submit();
        });
    }
    /** 양식 공통 처리 E */

    /** 전체 선택 처리 S */
    const checkalls = document.getElementsByClassName("checkall");
    for (const el of checkalls) {
        el.addEventListener("click", function() {
            const targetName = this.dataset.targetName;
            if (!targetName) return;

            const chkEls = document.getElementsByName(targetName);
            for (const chk of chkEls) {
                chk.checked = this.checked;
            }
        });
    }
    /** 전체 선택 처리 E */
});