package com.alkemy.ong.util;

import com.alkemy.ong.dto.ActivityDTO;
import com.alkemy.ong.entity.Activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityMocksUtil {

    //****** Controller ******//

    public static ActivityDTO generateActivityDTO(){
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setName("Activity Test DTO");
        activityDTO.setImage("https://cohorte-junio-a192d78b.s3.amazonaws.com/1657325315906-tutorias.jpg");
        activityDTO.setContent("Es un programa destinado a jovenes a partir del tercer año de secundaria,\n" +
                " cuyo objetivo es garantizar... etc");

        return activityDTO;
    }

    public static List<ActivityDTO> generateRequestMissingMandatoryAttributes() throws Exception {
        List<ActivityDTO> requests = new ArrayList<>();
        ActivityDTO singleRequest;

        // CASE 1: Missing name
        singleRequest = generateActivityDTO();
        singleRequest.setName(null);
        requests.add(singleRequest);

        // CASE 2: Missing content
        singleRequest = generateActivityDTO();
        singleRequest.setContent(null);
        requests.add(singleRequest);

        // CASE 4: Missing file name
        singleRequest = generateActivityDTO();
        singleRequest.setImage(null);
        requests.add(singleRequest);

        return requests;
    }

    public static List<ActivityDTO> generateRequestWithBrokenAttributes() throws IOException {
        List<ActivityDTO> requests = new ArrayList<>();
        ActivityDTO singleRequest;

        // CASE 1: Blank name
        singleRequest = generateActivityDTO();
        singleRequest.setName("");
        requests.add(singleRequest);

        // CASE 2: Blank content
        singleRequest = generateActivityDTO();
        singleRequest.setContent("");
        requests.add(singleRequest);

        // CASE 3: Blank image file
        singleRequest = generateActivityDTO();
        singleRequest.setImage((""));
        requests.add(singleRequest);

        return requests;
    }

    //****** Service ******//

    public static Activity generateMockActivity(){
        Activity activityEntity = new Activity();
        activityEntity.setId("1");
        activityEntity.setName("Activity ");
        activityEntity.setContent("Activity content ");
        activityEntity.setImage("image.jpg ");

        return activityEntity;
    }

    public static ActivityDTO generateANewActivityDTO(){
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setName("Test save name");
        activityDTO.setContent("Test save content");
        activityDTO.setImage("testSave.jpg");

        return activityDTO;
    }
}