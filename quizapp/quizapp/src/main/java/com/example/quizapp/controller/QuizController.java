package com.example.quizapp.controller;

import com.example.quizapp.model.Score;
import com.example.quizapp.repository.ScoreRepository;
import com.example.quizapp.repository.StudentRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class QuizController {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private ScoreRepository scoreRepo;

    @GetMapping("/")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/start-quiz")
    public String startQuiz(@RequestParam String studentId, HttpSession session, Model model) {
        if (studentRepo.existsByStudentId(studentId)) {
            session.setAttribute("studentId", studentId);
            return "quiz";
        } else {
            model.addAttribute("error", "অবৈধ ছাত্র আইডি");
            return "login";
        }
    }

    @PostMapping("/submit-quiz")
    public String submitQuiz(@RequestParam("q1") String answer1,
                             @RequestParam("q2") String answer2,
                             HttpSession session,
                             Model model) {

        String studentId = (String) session.getAttribute("studentId");

        int correct = 0;
        if ("ক".equals(answer1)) correct++;  // Q1: প্রশান্ত মহাসাগর
        if ("ক".equals(answer2)) correct++;  // Q2: ৮টি বিভাগ

        int totalScore = correct * 47;

        // Save to database
        scoreRepo.save(new Score(studentId, totalScore));

        // Send score to view
        model.addAttribute("score", totalScore);
        return "result";
    }


}
