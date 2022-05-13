package com.github.gimazdo.archeocat.controller;

import com.github.gimazdo.archeocat.entity.Answer;
import com.github.gimazdo.archeocat.entity.Image;
import com.github.gimazdo.archeocat.entity.Question;
import com.github.gimazdo.archeocat.entity.Room;
import com.github.gimazdo.archeocat.service.AnswerService;
import com.github.gimazdo.archeocat.service.ImageService;
import com.github.gimazdo.archeocat.service.QuestionService;
import com.github.gimazdo.archeocat.service.RoomService;
import com.github.gimazdo.archeocat.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FrontController {

    private final RoomService roomService;
    private final ImageService imageService;
    private final QuestionService questionService;

    private final AnswerService answerService;

    @GetMapping("/rooms")
    public String roomPage(Model model) {
        model.addAttribute("rooms", roomService.getAll());
        model.addAttribute("imgUtil", new ImageUtil());
        return "roomPage";
    }


    @PostMapping(value = "/rooms")
    public String addRoom(@RequestParam Long id,
                          @RequestParam String description,
                          @RequestParam MultipartFile file,
                          Model model) throws IOException {
        Room room;
        if (file != null) {
            Image image = new Image();
            image.setImage(file.getBytes());
            image.setMimeType(file.getContentType());
            image = imageService.add(image);
            room = new Room(id, description, image);
        } else {
            room = new Room(id, description, null);
        }
        roomService.add(room);
        return "redirect:/rooms";
    }


    @GetMapping("/quest")
    public String questionPage(Model model) {
        model.addAttribute("imgUtil", new ImageUtil());
        List<Question> questionList = questionService.getAll();
        model.addAttribute("questions", questionList);
        return "questPage";
    }

    @PostMapping("/quest")
    public String addQuestion(
            @RequestParam Long index,
            @RequestParam String description,
            @RequestParam MultipartFile image,
            @RequestParam MultipartFile imageWin,
            @RequestParam(required = false) boolean textAnswer,
            @RequestParam String textIfWin) throws IOException {
        Question question = new Question();
        question.setDescription(description);
        question.setIndex(index);
        question.setTextIfWin(textIfWin);
        question.setTextAnswer(textAnswer);
        if (image != null) {
            Image imag = new Image(null, image.getBytes(), image.getContentType());
            imageService.add(imag);
            question.setImage(imag);
        }
        if (image != null) {
            Image imag = new Image(null, imageWin.getBytes(), imageWin.getContentType());
            imageService.add(imag);
            question.setImageWin(imag);
        }
        questionService.add(question);
        return "redirect:/quest";
    }

    @PostMapping("/addAnswer")
    public String addAnswer(
            @RequestParam("question_id") Long questionId,
            @RequestParam String text,
            @RequestParam(required = false) boolean correct) {
        Answer answer;
        Question question = questionService.getById(questionId);
        answer = new Answer(null, text, correct, question);

        answerService.add(answer);
        question.getAnswerSet().add(answer);
        questionService.add(question);
        return "redirect:/question?id=" + questionId;
    }

    @GetMapping("/question")
    public String getQuestionPage(@RequestParam Long id, Model model) {
        model.addAttribute("imgUtil", new ImageUtil());
        Question question = questionService.getById(id);
        model.addAttribute("question", question);
        return "question";
    }


    @PostMapping("/deleteQuestion")
    public String deleteQuestion(@RequestParam("question_id") Long questionId) {
        Question question = questionService.getById(questionId);
        questionService.delete(question);
        return "redirect:/quest";
    }

    @PostMapping("/deleteAnswer")
    public String deleteQuestion(@RequestParam("question_id") Long questionId,
                                 @RequestParam("answer_id") Long answerId) {
        Answer answer = answerService.getById(answerId);
        answerService.delete(answer);
        return "redirect:/question?id="+questionId;
    }
    @PostMapping("/deleteRoom")
    public String deleteRoom(@RequestParam("room_id") Long roomId) {
        Room room = roomService.getById(roomId);
        roomService.delete(room);
        return "redirect:/rooms";
    }
}
