package com.example.demo.topic;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/topics")
@RestController 
public class TopicController {
    // 'final' for Compilte-time saftey
    private final TopicService topicService;

    // Constructor Injection
    TopicController(TopicService topicService){
        this.topicService = topicService;
    }

    @RequestMapping
    public List<Topic> getAllTopics(){
        return topicService.getAllTopics();
    }

    @RequestMapping("/{id}")
    public Topic getTopic(@PathVariable String id){
        return topicService.getTopic(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addTopic(@RequestBody Topic data){
        topicService.addTopic(data);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateTopic(@PathVariable String id, @RequestBody Topic data){
        topicService.updateTopic(id, data);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteTopic(@PathVariable String id){
        topicService.deleteTopic(id);
    }
}
