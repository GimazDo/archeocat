package com.github.gimazdo.archeocat.controller;

import com.github.gimazdo.archeocat.entity.Room;
import com.github.gimazdo.archeocat.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class FrontController {

    private final RoomService roomService;

    @GetMapping("/addRoom")
    public String roomPage(Model model){
        model.addAttribute("rooms", roomService.getAll());
        return "roomPage";
    }


 @PostMapping("/addRoom")
    public String addRoom(@RequestParam Long id,
                          @RequestParam String description,
                          Model model){
        Room room = new Room(id, description);
        roomService.add(room);
        model.addAttribute("rooms", roomService.getAll());
        return "roomPage";
    }



}
