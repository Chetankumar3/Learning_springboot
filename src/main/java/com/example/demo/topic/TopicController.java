package com.example.demo.topic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class TopicController {

    @RequestMapping("/topics")
    public List<Topic> getAllTopics(){
        /* List is just an interface in Java. `ArrayList`, `LinkedList`, and many other concrete classes implement the `List` interface. `ArrayList` cannot initialize using multiple arguments because it takes size as an argument, so it would be uncertain about whether data size is length or element itself.
        But we can pass a concrete object implementing `List` to initialize. Below is the example of such an implementation. Note that the `List.of` inside here is a static function of List<E>. It returns a concrete object, ListN (or List1, List2). But this `ListN` object is an internal package-private class. So, if I do: 'System.out.println(data.getClass().getSimpleName());' ListN is printed.
        */
        var data = new ArrayList<>(List.of(
            new Topic("id1", "Topic1", "desc1"),
            new Topic("id2", "Topic2", "desc2"),
            new Topic("id3", "Topic3", "desc3")
        ));

        return data;
    }
}
