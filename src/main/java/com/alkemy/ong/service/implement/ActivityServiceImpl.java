package com.alkemy.ong.service.implement;

import com.alkemy.ong.dto.ActivityDTO;
import com.alkemy.ong.entity.Activity;
import com.alkemy.ong.mapper.ActivityMapper;
import com.alkemy.ong.repository.ActivityRepository;
import com.alkemy.ong.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public ActivityDTO createActivity(ActivityDTO activityDTO) {
        Activity activity = activityMapper.activityDTO2Entity(activityDTO);
        Activity activitySave = activityRepository.save(activity);
        return activityMapper.activityEntity2DTO(activitySave);
    }
}
