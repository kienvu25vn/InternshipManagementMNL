package com.quanlisinhvienmnl.controller;


import com.quanlisinhvienmnl.entity.Internship;
import com.quanlisinhvienmnl.repository.TimesheetRepository;
import com.quanlisinhvienmnl.service.TimesheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;

@Controller
@SessionAttributes({"link", "day"})
public class TimesheetController {


    @Autowired
    private TimesheetService timesheetService;

    @Autowired
    private TimesheetRepository timesheetRepository;

    @GetMapping("/timesheet")
    public String timesheetManage(Model model ,@ModelAttribute("day") LocalDate instanceDay , @RequestParam(name = "status" , defaultValue = "now" , required = false) String status,
    @RequestParam(name = "search"  , required = false) String search , @RequestParam(name = "id" , required = false) Long id) throws ParseException {
        timesheetService.InternshipTimesheet(model , instanceDay , status , search , id);
        return "timesheetManage";
    }

    @GetMapping("/timesheet/new")
    public String addTimesheetView(Model model , @RequestParam(name = "id" , required = false) Long id) throws ParseException {
        timesheetService.addTimesheetView(model , id);
        return "addTimesheet";
    }

    @PostMapping("/timesheet")
    public String addTimesheet(@RequestParam(name = "workDate") String workDate , @RequestParam(name = "workTime") String workTime , @RequestParam(name = "id" , required = false)Long id) throws ParseException {

        timesheetService.addTimesheet(workDate ,workTime , id);
        if(id != null)
            return "redirect:/timesheet/new?id=" + id;
        return "redirect:/timesheet/new";
    }

    @DeleteMapping("/timesheet")
    public String deleteTimesheet(@RequestParam("id") Long id){
        timesheetService.deleteTimesheetById(id);
        Internship internship = timesheetRepository.findById(id).get().getInternship();

        return "redirect:/timesheet?id=" + internship.getUserid();
    }

    @GetMapping("/timesheet/update")
    public String updateTimesheetView(Model model , @RequestParam("id") Long id , @RequestParam("workDay") String workDay ) throws ParseException {
        timesheetService.updateTimesheetView(model, id , workDay);
        return "updateTimesheet";
    }

    @PutMapping("/timesheet")
    public String updateTimesheet(@RequestParam("id") Long id , @RequestParam("workTime") String workTime , @RequestParam("workDate") String workDate) throws ParseException {
        timesheetService.updateTimesheet(id,workTime , workDate);
        return "redirect:/timesheet?id=" + id;
    }

}
