package com.quanlisinhvienmnl.controller;


import com.quanlisinhvienmnl.countfunction.Function;
import com.quanlisinhvienmnl.entity.Position;
import com.quanlisinhvienmnl.service.PositionService;
import com.quanlisinhvienmnl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class PositionController {

    @Autowired
    private UserService userService;

    @Autowired
    private PositionService positionService;

    @GetMapping("/position")
    public String positionManage(@RequestParam(value = "search" , required = false , defaultValue = "") String search,
                                 Model model,
                                 @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                 @RequestParam(name = "size", required = false, defaultValue = "2") Integer size,
                                 @RequestParam(name = "orderby" , required = false, defaultValue = "id") String orderby,
                                 @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort){

        positionService.Position(model , search , sort , orderby , page , size);
        return "positionManage";
    }

    @GetMapping("/position/new")
    public String addPositionView(Model model){
        positionService.addPositionView(model);
        return "addPosition";
    }
    @PostMapping("/position")
    public String addPosition(Model model , @Valid @ModelAttribute("position") Position position, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            Function function = new Function();
            function.role(model ,userService);
            return "addPosition";
        }
        positionService.addPosition(model , position);
        return "redirect:/position";
    }

    @GetMapping("/position/update")
    public String updatePositionView(Model model , @RequestParam("id") Long id){
        positionService.updatePositionView(model , id);
        return "editPosition";
    }

    @PutMapping("/position")
    public String updatePosition(Model model ,@RequestParam("id") Long id , @Valid @ModelAttribute("position") Position position , BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            Function function = new Function();
            function.role(model ,userService);
            return "editPosition";
        }
        positionService.updatePosition(model , id , position);
        return "redirect:/position";
    }

    @DeleteMapping("/position")
    public String deletePosition(@RequestParam("id") Long id){
        positionService.deletePosition(id);
        return "redirect:/position";
    }
}
