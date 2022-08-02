package com.quanlisinhvienmnl.service.impl;

import com.quanlisinhvienmnl.config.DateUtils;
import com.quanlisinhvienmnl.countfunction.Function;
import com.quanlisinhvienmnl.entity.Internship;
import com.quanlisinhvienmnl.entity.InternshipTimesheet;
import com.quanlisinhvienmnl.entity.Users;
import com.quanlisinhvienmnl.repository.InternshipRepository;
import com.quanlisinhvienmnl.repository.TimesheetRepository;
import com.quanlisinhvienmnl.service.InternshipService;
import com.quanlisinhvienmnl.service.TimesheetService;
import com.quanlisinhvienmnl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SessionAttributes({"link", "day"})
@Service
public class TimesheetServiceImpl implements TimesheetService {

    @Autowired
    private TimesheetRepository timesheetRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private InternshipService internshipService;


    @Override
    public List<InternshipTimesheet> findAllInWeek(Internship internship, boolean del, Date start, Date end) {
        return timesheetRepository.findAllByInternshipAndDelAndWorkingDayGreaterThanEqualAndWorkingDayLessThanEqual(internship , del , start ,end);
    }

    @Override
    public InternshipTimesheet findByDay(Internship internship , Date day , boolean del) {
        return timesheetRepository.findByInternshipAndWorkingDayAndDel(internship ,day ,del);
    }

    @Override
    public void InternshipTimesheet(Model model, LocalDate instanceDay, String status, String search , Long id) throws ParseException {
        Function function = new Function();
        function.role(model , userService);
        DateUtils dateUtils = new DateUtils();
        Internship internship = new Internship();
        if(id == null) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Users users = userService.findByUsername(user.getUsername());
            internship = internshipService.findByUserId(users.getId());
        }else{
            Users users = userService.findById(id);
            internship = internshipService.findByUserId(id);
            model.addAttribute("internship" , users);
        }
        List<InternshipTimesheet> timesheets = new ArrayList<>();

        List<Date> dates = new ArrayList<>();
        int totalHours = 0;

        if(search != null){
            if(findByDay(internship, new SimpleDateFormat("yyyy-MM-dd").parse(search) , false ) != null) {
                timesheets.add(findByDay(internship, new SimpleDateFormat("yyyy-MM-dd").parse(search), false));
            }else{
                timesheets.add(new InternshipTimesheet(new SimpleDateFormat("yyyy-MM-dd").parse(search) , 0));
            }
            totalHours = timesheets.get(0).getTotalHours();
            model.addAttribute("search" , search);
        }
        else{
            if(status.equals("now")) {
                model.addAttribute("day" , LocalDate.now());
                Calendar cal = Calendar.getInstance();
                for (int i = Calendar.MONDAY; i <= Calendar.FRIDAY; i++) {
                    cal.set(Calendar.DAY_OF_WEEK, i);
                    dates.add(cal.getTime());
                }
            }
            else{
                Date check;
                if(status.equals("previous")){
                    check = dateUtils.asDate(instanceDay.minusDays(7));
                }
                else{
                    check = dateUtils.asDate(instanceDay.plusDays(7));
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(check);
                for (int i = Calendar.MONDAY; i <= Calendar.FRIDAY; i++) {
                    cal.set(Calendar.DAY_OF_WEEK, i);
                    dates.add(cal.getTime());
                }
                model.addAttribute("day" , dateUtils.asLocalDate(check));
            }

            for(Date date  : dates){
                String dateString = new SimpleDateFormat("yyyy-MM-dd").format(date);
                Date day = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
                if(findByDay(internship , day , false) == null){
                    timesheets.add(new InternshipTimesheet(date , 0));
                }
                else{
                    timesheets.add(findByDay(internship , day, false));
                    totalHours += findByDay(internship , day, false).getTotalHours();
                }
            }
            model.addAttribute("startDay" , dates.get(0));
            model.addAttribute("endDay" , dates.get(dates.size() - 1));
        }

        model.addAttribute("link" , "timesheet manage");
        model.addAttribute("totalHours" , totalHours);
        model.addAttribute("timesheets" , timesheets);
    }

    @Override
    public void addTimesheetView(Model model , Long id) throws ParseException {
        model.addAttribute("link" , "timesheet manage");
        Function function = new Function();
        function.role(model , userService);
        List<Date> dates = new ArrayList<>();
        DateUtils dateUtils = new DateUtils();
        List<InternshipTimesheet> timesheets = new ArrayList<>();
        Internship internship = new Internship();
        if(id == null) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Users users = userService.findByUsername(user.getUsername());
            internship = internshipService.findByUserId(users.getId());
        }else{
            Users users = userService.findById(id);
            internship = internshipService.findByUserId(id);
            model.addAttribute("internship" , users);
        }
        Calendar cal = Calendar.getInstance();
        for (int i = Calendar.MONDAY; i <= Calendar.FRIDAY; i++) {
            cal.set(Calendar.DAY_OF_WEEK, i);
            dates.add(cal.getTime());
        }


        for(Date date : dates){
            String dateString = new SimpleDateFormat("yyyy-MM-dd").format(date);
            Date day = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            if(day.before(dateUtils.asDate(LocalDate.now()))) continue;
            if(findByDay(internship , day , false) == null){
                timesheets.add(new InternshipTimesheet(date , 0));
            }
            else{
                continue;
            }
        }

        if(timesheets.size() == 0){
            model.addAttribute("message" , "you have created timesheet for this week!");
        }
        model.addAttribute("dayStart" , dates.get(0));
        model.addAttribute("dayEnd" , dates.get(dates.size()-1));
        model.addAttribute("timesheets" , timesheets);
        model.addAttribute("timesheet" , new InternshipTimesheet());
    }

    @Override
    public void addTimesheet(String workDate, String workTime , Long id) throws ParseException {
        Internship internship = new Internship();
        if(id == null) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Users users = userService.findByUsername(user.getUsername());
            internship = internshipService.findByUserId(users.getId());
        }else{
            Users users = userService.findById(id);
            internship = internshipService.findByUserId(id);
        }
        InternshipTimesheet internshipTimesheet = new InternshipTimesheet();
        internshipTimesheet.setWorkingDay(new SimpleDateFormat("yyyy-MM-dd").parse(workDate));
        if(workTime.equals("morning")){
            internshipTimesheet.setTime("morning");
            internshipTimesheet.setTotalHours(4);
        } else if (workTime.equals("afternoon")) {
            internshipTimesheet.setTime("afternoon");
            internshipTimesheet.setTotalHours(4);
        }
        else {
            internshipTimesheet.setTime("fulltime");
            internshipTimesheet.setTotalHours(8);
        }
        internshipTimesheet.setDel(false);
        internshipTimesheet.setInternship(internship);
        timesheetRepository.save(internshipTimesheet);
    }

    @Override
    public void deleteTimesheetById(Long id) {
        InternshipTimesheet internshipTimesheet = timesheetRepository.findById(id).get();
        internshipTimesheet.setDel(true);
        timesheetRepository.save(internshipTimesheet);
    }

    @Override
    public void updateTimesheetView(Model model, Long id , String workDay) throws ParseException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        model.addAttribute("link" , "timesheet manage");
        Function function = new Function();
        function.role(model , userService);
        InternshipTimesheet internshipTimesheet = new InternshipTimesheet();
        Users internship = userService.findById(id);
        Internship intern = internshipService.findByUserId(internship.getId());
        if(findByDay(intern , fmt.parse(workDay) , false) != null){
            internshipTimesheet = findByDay(intern , fmt.parse(workDay) , false);
        }else{
            internshipTimesheet = new InternshipTimesheet(fmt.parse(workDay) , 0);

        }

        model.addAttribute("timesheet" , internshipTimesheet);
        model.addAttribute("internship" , internship);
    }

    @Override
    public void updateTimesheet(Long id, String workTime , String workDate) throws ParseException {

        Internship internship = new Internship();
        Users users = userService.findById(id);
        internship = internshipService.findByUserId(id);

        InternshipTimesheet internshipTimesheet = new InternshipTimesheet();
        if(findByDay(internship ,new SimpleDateFormat("yyyy-MM-dd").parse(workDate) , false ) == null) {
            internshipTimesheet.setWorkingDay(new SimpleDateFormat("yyyy-MM-dd").parse(workDate));
            if (workTime.equals("morning")) {
                internshipTimesheet.setTime("morning");
                internshipTimesheet.setTotalHours(4);
            } else if (workTime.equals("afternoon")) {
                internshipTimesheet.setTime("afternoon");
                internshipTimesheet.setTotalHours(4);
            } else {
                internshipTimesheet.setTime("fulltime");
                internshipTimesheet.setTotalHours(8);
            }
            internshipTimesheet.setDel(false);
        }else{
            internshipTimesheet = findByDay(internship ,new SimpleDateFormat("yyyy-MM-dd").parse(workDate) , false );
            internshipTimesheet.setTime(workTime);
        }
        internshipTimesheet.setInternship(internship);
        timesheetRepository.save(internshipTimesheet);
    }


}
