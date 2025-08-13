package com.soumya.ipTracker20.controller;

import com.soumya.ipTracker20.entity.IpLog;
import com.soumya.ipTracker20.entity.User;
import com.soumya.ipTracker20.repository.IpLogRepository;
import com.soumya.ipTracker20.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class DynamicController {

    @Autowired
    private IpLogRepository ipLogRepository;

    @Autowired
    private UserRepository userRepository;

    // Endpoint to show all logged IPs
    @GetMapping("/tracker/log")
    public String trackerHome(HttpSession session, HttpServletResponse response, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null || username.isEmpty()) {
            return "redirect:/login";
        }
        
        // Prevent back button access
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
        // Get logs filtered by username
        List<IpLog> ipLogs = ipLogRepository.findByUsernameOrderBySnoDesc(username);
        model.addAttribute("ipLogs", ipLogs);
        model.addAttribute("username", username);
        return "tracker-log";  // Thymeleaf template to show IP logs
    }

    // IP logging and showing "My IP" page with activePage = "my-ip"
    @GetMapping("/my-ip")
    public String myIpPage(HttpServletRequest request, HttpSession session, HttpServletResponse response, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null || username.isEmpty()) {
            return "redirect:/login";
        }
        
        // Prevent back button access
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
        String clientIp = request.getHeader("X-FORWARDED-FOR");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = request.getRemoteAddr();
        }

        model.addAttribute("clientIp", clientIp);  // Used to display the IP in the view
        model.addAttribute("activePage", "my-ip"); // Used for conditional rendering
        model.addAttribute("username", username);
        return "main-template";  // The shared Thymeleaf template
    }

    // For navigating between other dynamic pages
    @GetMapping({"/track-via-link", "/ip-lookup"})
    public String renderPage(HttpServletRequest request, HttpSession session, HttpServletResponse response, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null || username.isEmpty()) {
            return "redirect:/login";
        }
        
        // Prevent back button access
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
        String uri = request.getRequestURI();
        String activePage = uri.contains("ip-lookup") ? "ip-lookup" : "track-via-link";

        model.addAttribute("activePage", activePage);
        model.addAttribute("username", username);
        return "main-template";
    }

//    @PostMapping("/signup")
//    public String handleSignup(@RequestParam String email,
//                               @RequestParam String password,
//                               Model model) {
//        if (userRepository.existsByEmail(email)) {
//            model.addAttribute("error", "Email already registered");
//            return "signup";
//        }
//
//        User user = new User(email, password);
//        User savedUser = userRepository.save(user);
//
//        String generatedUsername = "tuou" + savedUser.getUid();
//        while (userRepository.existsByUserName(generatedUsername)) {
//            generatedUsername += (int)(Math.random() * 10);
//        }
//        user.setUserName(generatedUsername);
//        userRepository.save(user);
//
//        model.addAttribute("message", "User created successfully! Please log in.");
//        return "done";
//    }

    @PostMapping("/signup")
    public String createUser(@RequestParam String email , @RequestParam String password ,Model model ){
        User newuser=new User();
        newuser.setEmail(email);
        newuser.setPassword(password);
        newuser.setUserName("tuou"+(int)(Math.random() * 100) + (int)(Math.random() * 100));
        userRepository.save(newuser);
        return "login";
    }

    @PostMapping("/login")
    public String validateUser(@RequestParam String email , @RequestParam String password, HttpSession session, HttpServletResponse response, Model model){
        User user = userRepository.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("username", user.getUserName());
            session.setAttribute("userId", user.getUid());
            
            // Prevent back button access
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            
            return "redirect:/my-ip";
        }

        model.addAttribute("error", "Invalid email or password");
        return "login";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session){
        if (session.getAttribute("username") != null) {
            return "redirect:/my-ip";
        }
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage(){
        return "signup";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }





}
