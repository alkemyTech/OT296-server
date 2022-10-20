package com.alkemy.ong.service.implement;

import com.alkemy.ong.dto.ActivityDTO;
import com.alkemy.ong.entity.Activity;
import com.alkemy.ong.mapper.ActivityMapper;
import com.alkemy.ong.repository.ActivityRepository;
import com.alkemy.ong.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
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

    @Override
    public ActivityDTO updateActivity(ActivityDTO activityDTO,String id) {
        Activity activity = activityRepository.findById(id).orElse(null);
        assert activity != null;
        activity.setName(activityDTO.getName());
        activity.setContent(activityDTO.getContent());
        activity.setImage(activityDTO.getImage());
        Activity activitySave = activityRepository.save(activity);
        return activityMapper.activityEntity2DTO(activitySave);
    }
}
