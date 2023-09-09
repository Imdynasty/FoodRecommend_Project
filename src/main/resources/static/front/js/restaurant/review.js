/**
* 후기 기능 관련
*
*/

window.addEventListener("DOMContentLoaded", function() {
    const { popup } = commonLib;
    const writeReviews = document.getElementsByClassName("write_review");
    for (const el of writeReviews) {
       el.addEventListener("click", function() {
          const id = this.dataset.id;
          if (!id) return;
          const url = `/board/write/review?extraLong1=${id}&popup=true`;
          popup.open(url, "후기 작성하기", 700, 700, true);
       });
    }

    const reviewLists = document.querySelectorAll(".review_list .subject");
    for (const el of reviewLists) {
        el.addEventListener("click", function() {
            const li = this.parentElement;
            li.classList.toggle("on");
        });
    }

});

function boardWriteCallback() {
    const { popup } = commonLib;
    popup.close();

    location.reload();
}


