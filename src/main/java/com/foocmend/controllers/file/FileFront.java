package com.foocmend.controllers.file;

import com.foocmend.services.file.DeleteFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileFront {
    private final DeleteFileService deleteService;

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        deleteService.delete(id);

        /** 파일 삭제 성공시 콜백 함수 처리 */
        String script = String.format("if(typeof parent.fileDeleteCallback == 'function') parent.fileDeleteCallback(%d);", id);
        model.addAttribute("script", script);
        return "commons/execute_script";
    }

    @ExceptionHandler(Exception.class)
    public String errorHandler(Exception e, Model model) {

        e.printStackTrace();

        String script = String.format("alert('%s');", e.getMessage());
        model.addAttribute("script", script);
        return "commons/execute_script";
    }
}
