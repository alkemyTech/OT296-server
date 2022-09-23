package com.alkemy.ong.seeder;

import com.alkemy.ong.entity.Activity;
import com.alkemy.ong.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ActivitySeeder implements CommandLineRunner {
    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public void run(String... args) throws Exception {
        seedActivityTable();
    }
    private void seedActivityTable(){
        if(activityRepository.count() == 0){
            createActivity();
        }
    }
    private void createActivity(){
        List<Activity> activityList = new ArrayList<>();

        Activity activity1 = new Activity();
        activity1.setName("nombre activity1");
        activity1.setContent("content activity1");
        activity1.setImage("images/activity1.png");
        activityList.add(activity1);

        Activity activity2 = new Activity();
        activity2.setName("nombre activity2");
        activity2.setContent("content activity2");
        activity2.setImage("images/activity2.png");
        activityList.add(activity2);

        activityRepository.saveAll(activityList);
    }

}
