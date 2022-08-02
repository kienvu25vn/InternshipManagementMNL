package com.quanlisinhvienmnl.service.impl;

import com.quanlisinhvienmnl.countfunction.Function;
import com.quanlisinhvienmnl.entity.Position;
import com.quanlisinhvienmnl.entity.Users;
import com.quanlisinhvienmnl.repository.*;
import com.quanlisinhvienmnl.service.PositionService;
import com.quanlisinhvienmnl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.List;

@SessionAttributes("link")
@Service
public class PositionServiceImpl implements PositionService {


    @Autowired
    private UserService userService;

    @Autowired
    private PositionRepository positionRepository;



    @Override
    public void Position(Model model, String search, String sort, String orderby, Integer page, Integer size) {
        if(search == null)
            search = "";
        Function function = new Function();
        function.role(model ,userService);
        int total = 0;
        Pageable pageable = function.pageable(page , size , orderby , sort);
        Page<Position> positions =  positionRepository.findAllByDelAndNameContainingIgnoreCase(false ,search , pageable);

        for(Position p : positions){
            List<Users> users = new ArrayList<>();
            for(Users us : p.getUsers()){
                if(!us.isDel()){
                    users.add(us);
                }
            }
            p.setUsers(users);
            positionRepository.save(p);
        }

        total = (int) positions.getTotalElements();

        Integer numOfPages = function.numOfpages(total , size);
        List<Integer> pages = function.page_list(numOfPages);
        Integer numOfShow = function.numOfShow(total , size , page , numOfPages);

        model.addAttribute("positions" , positions);
        model.addAttribute("pages" , pages);
        model.addAttribute("numOfShow" , numOfShow);
        model.addAttribute("numOfPositions" , total);
        model.addAttribute("pageIndex" , page + 1);
        model.addAttribute("orderby" , orderby);
        model.addAttribute("search" , search);
        model.addAttribute("link" , "position manage");
    }

    @Override
    public void addPositionView(Model model) {
        Function function = new Function();
        function.role(model ,userService);
        model.addAttribute("position" , new Position());
        model.addAttribute("link" , "position manage");
    }

    @Override
    public void addPosition(Model model, Position position) {
        position.setDel(false);
        positionRepository.save(position);
    }

    @Override
    public void updatePositionView(Model model, Long id) {
        Function function = new Function();
        function.role(model ,userService);
        Position position = positionRepository.findById(id).get();
        model.addAttribute("position" , position);
        model.addAttribute("link" , "position manage");
    }

    @Override
    public void updatePosition(Model model, Long id, Position position) {
        Position current = positionRepository.findById(id).get();
        current.setName(position.getName());
        positionRepository.save(position);
    }

    @Override
    public void deletePosition(Long id) {
        Position position = positionRepository.findById(id).get();
        position.setDel(true);
        positionRepository.save(position);
    }
}
