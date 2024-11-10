package ru.alishev.springcourse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.alishev.springcourse.dao.PersonDao;

@Controller
@RequestMapping("/test-batch-update")
public class BatchController {
    private final PersonDao personDao;

    @Autowired
    public BatchController(PersonDao personDao) {
        this.personDao = personDao;
    }

    @GetMapping()
    public String index(){
        return "batch/index";
    }

    @GetMapping("/without")
    public String withoutBatch(){
        personDao.testMultipleUpdate();
        return "redirect:/people";
    }

    @GetMapping("/with")
    public String withBatch(){
        personDao.testBatchUpdate();
        return "redirect:/people";
    }
}
