package com.example.demo.topic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class TopicController {
    private final TopicService topicService;

    TopicController(TopicService topicService){
        this.topicService = topicService;
    }

    @RequestMapping("/topics")
    public List<Topic> getAllTopics(){
        return topicService.getAllTopics();
    }

    @RequestMapping("/topics/{id}")
    public Topic getTopic(@PathVariable String id){
        return topicService.getTopic(id);
    }

    @RequestMapping(value = "/topics", method = RequestMethod.POST)
    public void addTopic(@RequestBody Topic data){
        topicService.addTopic(data);
    }

    @RequestMapping(value = "/topics/{id}", method = RequestMethod.PUT)
    public void updateTopic(@PathVariable String id, @RequestBody Topic data){
        topicService.updateTopic(id, data);
    }

    @RequestMapping(value = "/topics/{id}", method = RequestMethod.DELETE)
    public void updateTopic(@PathVariable String id){
        topicService.deleteTopic(id);
    }
}
