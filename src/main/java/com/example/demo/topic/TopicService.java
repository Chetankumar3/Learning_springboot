package com.example.demo.topic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TopicService {
    List<Topic> topics =  new ArrayList<>(List.of(
        new Topic("id1", "Topic1", "desc1"),
        new Topic("id2", "Topic2", "desc2"),
        new Topic("id3", "Topic3", "desc3")
    ));

    public List<Topic> getAllTopics(){
        return topics;
    }

    public Topic getTopic(String id){
        return topics.stream().filter(t -> id.equals(t.getId())).findFirst().get();
    }

    public void addTopic(Topic data){
        this.topics.add(data);
    }

    public void updateTopic(String id, Topic data){
        deleteTopic(id);
        addTopic(data);
    }

    public void deleteTopic(String id){
        this.topics.removeIf(t -> t.getId().equals(id));
    }
}
