package com.alkemy.ong.seeder;

import com.alkemy.ong.entity.Role;
import com.alkemy.ong.entity.Users;
import com.alkemy.ong.repository.RoleRepository;
import com.alkemy.ong.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsersSeeder implements CommandLineRunner {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String PHOTO ="default.jpg";

    @Override
    public void run(String...args)throws Exception{
        loadRole();
        loadUser();
    }
    //crear roles si no existen
    private void loadRole(){
        if(roleRepository.count() == 0){
            loadRoleSeed();
        }
    }
    private void loadRoleSeed(){
        roleRepository.save(buildRole(
                "ROLE_ADMIN","Has all the privileges from both roles"));
        roleRepository.save(buildRole("ROLE_USER",
                "Privileges limited to only modifying and viewing your data"));
    }
    private Role buildRole(String name, String description){
        return new Role(name,description);
    }
    //create users if they don't exist
    private void loadUser(){
        if(usersRepository.count() == 0){
            loadUserSeed();
        }
    }
    private void loadUserSeed(){
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
        usersRepository.save(buildUser("Caroline","Gomez","gocaroline@admin.com","OTLZQDLdddfx",roleAdmin));
        usersRepository.save(buildUser("Ezequiel","Ezealva","ezealva@admin.com","DSFDSFFguynf",roleAdmin));
        usersRepository.save(buildUser("Gabriel","Bosio","gabosio@admin.com","GHSFKGDadffa",roleAdmin));
        usersRepository.save(buildUser("Flavio","Ambroggio","flaambroggio@admin.com","DADFTJFFasdfa",roleAdmin));
        usersRepository.save(buildUser("Joel","Tejerina","jotejerina@admin.com","DSFDSFFguynf",roleAdmin));
        usersRepository.save(buildUser("Felipe","Munevar","femunevar@admin.com","ADIDGDOadyjf",roleAdmin));
        usersRepository.save(buildUser("Brian","Nieto","brinieto@admin.com","PAEGSATnhyfj",roleAdmin));
        usersRepository.save(buildUser("Rosario","Arma","roarma@admin.com","OYRODYDoyfgd",roleAdmin));
        usersRepository.save(buildUser("Lucia","Cora","luciacorona@admin.com","PIYFGYDhyosf",roleAdmin));
        usersRepository.save(buildUser("Brian","Nieto","brinieto@admin.com","WRTRYFAbncou",roleAdmin));

        Role roleUser= roleRepository.findByName("ROLE_USER");
        usersRepository.save(buildUser("Misha","Fossord","mfossord9@gmail.com","3q3MgFh2Ps5", roleUser));
        usersRepository.save(buildUser("Charissa","MacGillreich","cmacgillreicha@gmail.com","7MOQwvVOHz2p", roleUser));
        usersRepository.save(buildUser("Yasmin","Lapsley","yjoicey8@gmail.com","r79Sfnpxxsx", roleUser));
        usersRepository.save(buildUser("Sharity","Penreth","spenreth7@gmail.com","7QWax89xxs", roleUser));
        usersRepository.save(buildUser("Heidi","Saffill","hsaffill6@gmail.com","DZfgtHxxxs", roleUser));
        usersRepository.save(buildUser("Skylar","Larter","slarter5@gmail.com","olpArkfRD2", roleUser));
        usersRepository.save(buildUser("Will","MacRonald","wmacronald3@gmail.com","KW1LpoXehdo", roleUser));
        usersRepository.save(buildUser("Odele","Clynmans","oclynmans2@gmail.com","DTUMZGlDjxdd", roleUser));
        usersRepository.save(buildUser("Cristian","Inkpen","cinkpen0@joomla.org","4VHcXxp86sB", roleUser));
        usersRepository.save(buildUser("Cristiano","Foston","cfoston1@japan.org","9wf9KkPkxsx", roleUser));
    }
    private Users buildUser (String firsTName , String lastName, String email, String password,Role role){
        return new Users(firsTName,lastName,email,passwordEncoder.encode(password), UsersSeeder.PHOTO,role);
    }
}
