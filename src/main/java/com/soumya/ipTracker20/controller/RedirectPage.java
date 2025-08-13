package com.soumya.ipTracker20.controller;

//import ch.qos.logback.core.model.Model;
import com.soumya.ipTracker20.entity.User;
import com.soumya.ipTracker20.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.soumya.ipTracker20.entity.IpLog;
import com.soumya.ipTracker20.repository.IpLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
//@Controller
@RequestMapping("/home")
public class RedirectPage {

    @Autowired
    private IpLogRepository ipLogRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/ip")
    public List<IpLog> redPage(HttpServletRequest request){


       return  ipLogRepository.findAll();

    }

    @PostMapping("/ip")
    public IpLog postIp(HttpServletRequest request){
        LocalDateTime now= LocalDateTime.now();

//        for deployment
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            // X-Forwarded-For can be a comma-separated list — take the first one
            ip = ip.split(",")[0].trim();
        } else {
            ip = request.getRemoteAddr();
        }

//        for testing
        // String ip="204.84.125.89";

        // Extract username and link from request parameters
        String username = request.getParameter("username");
        String link = request.getParameter("link");
        String redirect = request.getParameter("redirect");

        IpLog ipLog=new IpLog();
//        ipLog.setSno(1);
        ipLog.setDate(now.toLocalDate().toString());
        ipLog.setTime(now.toLocalTime().toString());
        ipLog.setIp(ip);
        ipLog.setUsername(username != null ? username : "unknown");
        ipLog.setLink(redirect != null ? redirect : (link != null ? link : "direct_access"));

        String url="http://ip-api.com/json/"+ip;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> geoData = response.getBody();

            ipLog.setCountry(geoData.get("country").toString());
            ipLog.setRegion(geoData.get("regionName").toString());
            ipLog.setCity(geoData.get("city").toString());
            ipLog.setZip(geoData.get("zip").toString());
            ipLog.setLat((double)geoData.get("lat"));
            ipLog.setLon((double)geoData.get("lon"));
            ipLog.setTimezone(geoData.get("timezone").toString());
            ipLog.setIsp(geoData.get("isp").toString());

        }

        return ipLogRepository.save(ipLog);
    }

    @GetMapping
    public void redirectHome(@RequestParam(required = false) String redirect,
                             HttpServletResponse response) {
        try {
            if (redirect != null && !redirect.isEmpty()) {
                // Pass redirect param to home.html
                String encodedRedirect = java.net.URLEncoder.encode(redirect, java.nio.charset.StandardCharsets.UTF_8);
                response.sendRedirect("/home.html?redirect=" + encodedRedirect);
            } else {
                response.sendRedirect("/home.html");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @PostMapping("/ipl")
//    public String ipLocation(){
//
//    }

    @GetMapping("/map")
    public void redirectmap(HttpServletResponse response) throws IOException {
        response.sendRedirect("/map.html");
    }

    @GetMapping("/latest-ip")
    public ResponseEntity<IpLog> getLatestIp() {
        Optional<IpLog> latest = ipLogRepository.findTopByOrderBySnoDesc();
        return latest.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/api/ip-lookup")
    public ResponseEntity<?> lookupIp(@RequestParam String ip) {
        String url = "http://ip-api.com/json/" + ip;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> geoData = response.getBody();
            return ResponseEntity.ok(geoData);
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to get IP info");
        }
    }

//    @PostMapping("/signup")
//    public ResponseEntity<?> createUser(@RequestParam String email, @RequestParam String password) {
//        if (userRepository.existsByEmail(email)) {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body(Map.of("error", "Email already registered"));
//        }
//
//        User user = new User(email, password);
//        User savedUser = userRepository.save(user);
//
//        String generatedUsername = "tuou" + savedUser.getUid();
//
//        while(userRepository.existsByUserName(generatedUsername)) {
//            generatedUsername += (int)(Math.random() * 10);
//        }
//
//        user.setUserName(generatedUsername);
//        userRepository.save(user);
//
//        return ResponseEntity.ok(Map.of("message", "User created successfully"));
//    }

    @GetMapping("/user")
    public List<User> allUser() {
        return userRepository.findAll();
    }







}
