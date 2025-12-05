package com.kosa2.hospital.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kosa2.hospital.dto.HomeDto;
import com.kosa2.hospital.service.HomeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    // 홈 화면 데이터 조회
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

        return "home/home";
    }

    // 현재는 환자or의료진 이름이 포함된 예약 정보만 검색 가능
    @GetMapping("/search")
    public String search(@RequestParam(value = "keyword", required = false) String keyword, Model model) {

        if (keyword != null && !keyword.trim().isEmpty()) {
            List<Map<String, Object>> searchList = homeService.search(keyword);
            model.addAttribute("searchList", searchList);
        }

        model.addAttribute("keyword", keyword);
        return "home/search";
    }
}
