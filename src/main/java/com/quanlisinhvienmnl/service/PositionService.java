package com.quanlisinhvienmnl.service;

import com.quanlisinhvienmnl.entity.Position;
import org.springframework.ui.Model;

public interface PositionService {
    void Position(Model model , String search , String sort , String orderby , Integer page , Integer size);

    void addPositionView(Model model);

    void addPosition(Model model , Position position);

    void updatePositionView(Model model , Long id);


    void updatePosition(Model model, Long id, Position position);

    void deletePosition(Long id);
}
