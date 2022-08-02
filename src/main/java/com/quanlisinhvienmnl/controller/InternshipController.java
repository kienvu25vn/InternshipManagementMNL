package com.quanlisinhvienmnl.controller;



import com.quanlisinhvienmnl.model.UserInternship;
import com.quanlisinhvienmnl.service.InternshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.text.ParseException;


@Controller
public class InternshipController {

    @Autowired
    private InternshipService internshipService;


    @GetMapping("/internship/new")
    public String addInternship(Model model){
       internshipService.internshipError(model);
        model.addAttribute("userinternship" , new UserInternship());
        return "addInternship";
    }

    @PostMapping("/internship")
    public String addNewInternship(Model model ,@RequestParam("uni") String uni , @RequestParam("position") String position , @RequestParam("bd") String bd, @Valid @ModelAttribute("userinternship") UserInternship userInternship ,BindingResult bindingResult

                                  ) throws ParseException {

        if(bindingResult.hasErrors()) {
            internshipService.internshipError(model);
            return "addInternship";
        }
        internshipService.addNewInternship(model , uni , position , bd , userInternship);
        return "redirect:/internship";
    }

    @DeleteMapping("/internship")
    public String deleteIntership(@RequestParam("id") String userid){
        internshipService.deleteByUserId(Long.parseLong(userid));
        return "redirect:/internship";
    }


    @GetMapping("/update")
    public String update(@RequestParam("id") Long id , Model model){
        internshipService.updateInternshipView(id , model);
        return "updateInternship";
    }

    @PutMapping("/internship")
    public String updateInternship(Model model ,@RequestParam("id") Long id ,
                                   @RequestParam("uni") String uni ,
                                   @RequestParam("position") String position ,
                                   @RequestParam("bd") String bd,
                                   @Valid @ModelAttribute("userinternship") UserInternship userInternship ,
                                   BindingResult bindingResult) throws ParseException{
        if(bindingResult.hasErrors()){
            internshipService.internshipError(model);
            return "updateInternship";
        }
        internshipService.updateInternship(model , id , uni , position , bd , userInternship);
        return "redirect:/internship";

    }

    @GetMapping("/internship/profile")
    public String profile(@Param("id") Long id , Model model){
        internshipService.internshipProfile(id , model);
        return "internshipProfile";
    }

    @PostMapping("/internship/assignmentor")
    public String assigninternship(@Param("userid") Long userid , @RequestParam("mentor") Long mentorId){
        internshipService.internshipAssignment(userid , mentorId);
        return "redirect:/internship/profile?id=" + userid;
    }

    @PostMapping("/internship/review")
    public String reviewInternship(@Param("userid") Long userid , @Param("reviewer") String reviewer , @RequestParam("content") String content){
        internshipService.internshipReview(userid , reviewer , content);
        return "redirect:/internship/profile?id=" + userid;
    }

    @GetMapping("/internship")
    public String searchInternship(@RequestParam(value = "search" , required = false , defaultValue = "") String search,
                                   Model model,
                                   @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                   @RequestParam(name = "size", required = false, defaultValue = "2") Integer size,
                                   @RequestParam(name = "orderby" , required = false, defaultValue = "id") String orderby,
                                   @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort){
        internshipService.Internship(model , search , sort , orderby , page ,size);
        return "internshipManage";
    }



}
