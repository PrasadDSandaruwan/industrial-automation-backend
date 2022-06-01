package com.industrialautomation.api.seed;


import com.industrialautomation.api.dao.*;
import com.industrialautomation.api.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


@Component
public class Seed implements CommandLineRunner {

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private MachineTypeRepository machineTypeRepository;

    @Autowired
    private ProductionLineRepository productionLineRepository;

    @Autowired
    private CommandTypeRepository commandTypeRepository;

    @Autowired
    private RateRepository rateRepository;

    @Autowired AlarmRepository alarmRepository;

    @Autowired ConnectedMachineRepository connectedMachineRepository;


    @Override
    public void run(String... args) throws Exception {
        addUserTypes();
        addUsers();
        addMachineType();
        addProductionLine();
        addMachine();
        addCommandType();
        addAlarm();
        addRates();
        addConnectedMachines();
    }

    private void addConnectedMachines() {


        if (connectedMachineRepository.count()==0){

            Long[] from = {1L,2L,3L,4L,1L};
            Long[] to ={2L,3L, 4L, 6L,5L};

            for (int i=0 ;i<from.length ;i++){
                ConnectedMachineKey key = new ConnectedMachineKey();
                key.setMachine_id(from[i]);
                key.setConnected_machine_id(to[i]);

                ConnectedMachine machine = new ConnectedMachine();

                machine.setMachine(new Machine(from[i]));
                machine.setConnectedMachine(new Machine(to[i]));

                machine.setRate(20F);
                machine.setCurrent_rate(20F);

                machine.setId(key);

                connectedMachineRepository.save(machine);
            }

        }
    }

    private void addRates() {

        if (rateRepository.count()==0){
            Float min = 10F;
            Float max = 60F;

            Random random = new Random();
            List<Float> rate = new LinkedList<>();

            List<Integer> is_temp = new LinkedList<>();


            for(int i=0;i<100;i++){
                Float rand = random.nextFloat() * (max - min) + min;
                rate.add(rand);
                if (i>50)
                    is_temp.add(1);
                else
                    is_temp.add(0);

            }

            Long[] machine = {1L,4L,6L};

            List<Rates> ratesList = new LinkedList<>();

            for (int j=0;j<machine.length;j++){
                for (int i=0; i< rate.size();i++){
                    Rates rates = new Rates(
                            null,
                            LocalDateTime.now().plusMinutes(i),
                            rate.get(i),
                            is_temp.get(i),
                            new Machine( machine[j])

                    );

                    ratesList.add(rates);


                }
            }

            rateRepository.saveAll(ratesList);
        }



    }

    private void addAlarm() {
        if (alarmRepository.count()==0){
            String [] name ={ "Alarm 01 M01","Alarm 01 M02","Alarm 01 M04","Alarm 01 M06","Alarm 01 M08","Alarm 01 M10"};
            String [] slug = { "Alarm 01 L01","Alarm 02 L01","Alarm 03 L01","Alarm 04 L01","Alarm 05 L01","Alarm 06 L01"
                    ,"Alarm 01 L02","Alarm 02 L02","Alarm 03 L02","Alarm 01 L03","Alarm 02 L03"};
            Long [] machine = {1L,2L,4L,6L,8L,10L,2L,2L,2L,3L,3L,3L};

            Alarm alarm;
            for (Integer i = 0; i < name.length; i++) {
                alarm = new Alarm(
                        null,
                        name[i],
                        slug[i],
                        LocalDateTime.now(),
                        null,
                        new Machine(machine[i])

                );
                alarmRepository.save(alarm);
            }
        }



    }

    private void addCommandType() {
        if (commandTypeRepository.count() == 0) {
            CommandType commandType = new CommandType(1L, "test type", "ct1");
            commandTypeRepository.save(commandType);
            commandType = new CommandType(2L, "test type 2", "ct2");
            commandTypeRepository.save(commandType);
            commandType = new CommandType(3L, "test type 3", "ct3");
            commandTypeRepository.save(commandType);
            commandType = new CommandType(4L, "test type 4", "ct4");
            commandTypeRepository.save(commandType);
        }
    }

    private void addProductionLine() {
        if (productionLineRepository.count() == 0) {
            ProductionLine productionLine = new ProductionLine(1L, "Production Line 01", "line_01");
            productionLineRepository.save(productionLine);
            productionLine = new ProductionLine(2l, "Production Line 02", "line_02");
            productionLineRepository.save(productionLine);
            productionLine = new ProductionLine(3l, "Production Line 03", "line_03");
            productionLineRepository.save(productionLine);
        }
    }

    private void addMachineType() {
        if (machineTypeRepository.count() == 0) {
            MachineType machineType = new MachineType(1l, "Production Machine", "production");
            machineTypeRepository.save(machineType);
            machineType = new MachineType(2l, "Ingredient Machine", "ingredient");
            machineTypeRepository.save(machineType);
            machineType = new MachineType(3l, "Packing Machine", "packing");
            machineTypeRepository.save(machineType);

        }
    }

    private void addMachine() {
        if (machineRepository.count() == 0) {

            String [] name ={ "Machine 01 L01","Machine 02 L01","Machine 03 L01","Machine 04 L01","Machine 05 L01","Machine 06 L01"
                    ,"Machine 01 L02","Machine 02 L02","Machine 03 L02","Machine 01 L03","Machine 02 L03"};
            String [] slug = { "Machine 01 L01","Machine 02 L01","Machine 03 L01","Machine 04 L01","Machine 05 L01","Machine 06 L01"
                    ,"Machine 01 L02","Machine 02 L02","Machine 03 L02","Machine 01 L03","Machine 02 L03"};
            Long [] p_line = {1L,1L,1L,1L,1L,1L,2L,2L,2L,3L,3L,3L};
            Long [] m_type = {1L,1L,1L,2L,2L,3L,1L,2L,3L,1L,3L};

            Float[] temp =  {50F,20F,50F,60F,50F,70F,50F,80F,50F,50F,50F};

            Float[] rate =  {40F,20F,50F,60F,50F,70F,40F,80F,50F,50F,50F};;

            Machine machine;
            for (Integer i = 0; i < name.length; i++) {
                machine = new Machine(
                        null,
                        name[i],
                        slug[i],
                        i.toString() ,
                        LocalDateTime.now(),
                        1,
                        null,
                        temp[i],
                        rate[i],
                        temp[i],
                        rate[i],
                        new ProductionLine(p_line[i]),
                        new MachineType(m_type[i])
                );
                machineRepository.save(machine);
            }
        }
    }

    private void addUserTypes() {
        if (userTypeRepository.count() == 0) {
            String[] type = {"Admin", "User"};
            String[] slug = {"ADMIN", "USER"};

            for (int i = 0; i < type.length; i++) {
                UserType userType = new UserType();
                userType.setUser_type_name(type[i]);
                userType.setSlug(slug[i]);
                userTypeRepository.save(userType);
            }
        }
    }

    private void addUsers() {
        if (userRepository.count() == 0) {

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String[] fName = {"Admin", "Admin", "Staff", "Staff", "User", "User", "Manager"};

            String[] lName = {"Rathnayake", "Mudiyanshe", "Gunawardhana", "Gnanathilaka", "Somathilaka", "Karunarathna", "Dasun"};

            String[] email = {"admin1@gmail.com", "admin2@gmail.com", "admin3@gmail.com", "admin4@gmail.com", "user1@gmail.com", "user2@gmail.com", "user4@gmail.com"};

            String[] nic = {"9812234234v", "1291281273v", "1219968826v", "997388162v", "123456765v", "12456765v", "124587V"};

            String[] contacts = {"0771213661", "0917768864", "0779875434", "0917654876", "3456789976", "3456789976", "074587941"};

            String[] password = {"Industry@Admin1", "Industry@Admin2", "Industry@Staff1", "Industry@Staff2", "Industry@User1", "Industry@User2", "Industry@Manager1"};

            Long[] typeID = {1l, 1l, 1l, 1l, 2l, 2l, 2l};
            for (int i = 0; i < fName.length; i++) {
                User user = new User();
                user.setFirst_name(fName[i]);
                user.setLast_name(lName[i]);
                user.setEmail(email[i]);
                user.setNic(nic[i]);
                user.setBirthday(LocalDate.now());
                user.setContact_no(contacts[i]);
                user.setForce_password_change_flag(LocalDateTime.now());

                UserType userType = new UserType();
                userType.setId(typeID[i]);
                user.setUserType(userType);
                user.setAdded_at(LocalDateTime.now());


                Auth auth = new Auth();
                auth.setPassword(encoder.encode(password[i]));
                user.setAuthDetails(auth);
                auth.setUser(user);
                authRepository.save(auth);

            }

        }
    }

}
