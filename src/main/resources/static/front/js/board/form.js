/**
* 게시판 작성, 수정 양식 공통 스크립트
*
*/

window.addEventListener("DOMContentLoaded", function() {
    try {
        CKEDITOR.replace("content", {
            height: 350,
        });
    } catch(e) {}

    const insertEditors = document.getElementsByClassName("insert_editor");
        for (const el of insertEditors) {
            el.addEventListener("click", function() {
                insertEditor(this.dataset.url);
            });
        }
});


/**
* 파일 업로드 콜백
*
*/
function fileUploadCallback(files) {
    if (!files || files.length == 0) return;
    const editorImages = document.getElementById("attachedFiles_editor");
    const attachFiles = document.getElementById("attachedFiles_files");
    const tpl1 = document.getElementById("tpl_editor").innerHTML;
    const tpl2 = document.getElementById("tpl_file").innerHTML;
    const domParser = new DOMParser();
    for (const file of files) {
        const location = file.location;
        let html = location == 'editor'? tpl1 : tpl2;
        let target = location == 'editor'? editorImages : attachFiles;
        html = html.replace(/\[id\]/g, file.id)
                        .replace(/\[url\]/g, file.thumbsUrl[0])
                        .replace(/\[orgUrl\]/g, file.fileUrl)
                        .replace(/\[fileName\]/g, file.fileName);
        const dom = domParser.parseFromString(html, "text/html");
        const el = dom.querySelector(".file_items");

        if (location == 'editor') {
           const imgTag = `<img src='${file.fileUrl}'>`;
           CKEDITOR.instances.content.insertHtml(imgTag);

           const insertEditorEl = dom.querySelector(".insert_editor");
           insertEditorEl.addEventListener("click", function() {
                insertEditor(this.dataset.url);
           });
       }

       target.appendChild(el);
   }
}

/**
* 파일 삭제 성공시 콜백 처리
*
* @param fileId : 파일 등록 번호
*/
function fileDeleteCallback(fileId) {
    if (!fileId) return;

    const el = document.getElementById(`file_${fileId}`);
    if (el) el.parentElement.removeChild(el);
}

/**
* 이미지 에디터 본문 첨부
*
*/
function insertEditor(url) {
    const imgTag = `<img src='${url}'>`;
    CKEDITOR.instances.content.insertHtml(imgTag);
}
