package com.alkemy.ong.mapper;

import com.alkemy.ong.dto.ActivityDTO;
import com.alkemy.ong.entity.Activity;
import org.springframework.stereotype.Component;

@Component
public class ActivityMapper {

    public Activity activityDTO2Entity (ActivityDTO activityDTO){
        Activity activity = new Activity();
        activity.setName(activityDTO.getName());
        activity.setContent(activityDTO.getContent());
        activity.setImage(activityDTO.getImage());
        return activity;
    }

    public ActivityDTO activityEntity2DTO (Activity activity){
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setName(activity.getName());
        activityDTO.setContent(activity.getContent());
        activityDTO.setImage(activity.getImage());
        return activityDTO;
    }
}
