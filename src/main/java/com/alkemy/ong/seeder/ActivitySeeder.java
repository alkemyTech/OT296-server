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
        activity1.setName("Consultoría Ambiental y RSE (Asesoramiento a medida)");
        activity1.setContent("Nuestro objetivo radica en colaborar con la construcción de un perfil de empresa sustentable que comience desde acciones propias. Promover un cambio de hábitos genuino a fin de poder consolidar la transición hacia un nuevo paradigma, acorde a la ética que demanda la época y a cumplir con los valores de conciencia y responsabilidad del sector privado para con el ambiente.");
        activity1.setImage("imagen/activity1");
        activityList.add(activity1);

        Activity activity2 = new Activity();
        activity2.setName("Taller de Huerta en Casa");
        activity2.setContent("La idea principal de este taller es introducirlos en el mundo de las hortalizas y brindar los conocimientos necesarios para realizar una producción agroecológica en el espacio que se disponga en casa: balcón, patio o terraza.");
        activity2.setImage("imagen/activity2");
        activityList.add(activity2);

        activityRepository.saveAll(activityList);
    }

}
