package com.alkemy.ong.service;

import com.alkemy.ong.dto.ActivityDTO;

public interface ActivityService {

    ActivityDTO createActivity(ActivityDTO activityDTO);

    ActivityDTO updateActivity(ActivityDTO activityDTO, String id);
}
