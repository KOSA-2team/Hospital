package com.kosa2.hospital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kosa2.hospital.dto.HomeDto;
import com.kosa2.hospital.service.HomeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    
    @GetMapping("/")
    public String home(Model model) {
        HomeDto home = homeService.getHomeData();
        
        model.addAttribute("todayDate", home.getTodayDate());
        model.addAttribute("totalCount", home.getTotalCount());
        model.addAttribute("waitingCount", home.getWaitingCount());
        model.addAttribute("newPatientCount", home.getNewPatientCount());
        model.addAttribute("scheduleList", home.getScheduleList());
        model.addAttribute("doctorList", home.getDoctorList());
        model.addAttribute("monthlyNewCount", home.getMonthlyNewCount());
        
        return "home";
    }

    @GetMapping("/search")
    public String search() {
        return "search";
    }
}
