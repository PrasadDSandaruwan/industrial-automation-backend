package com.industrialautomation.api.utilities;


import com.industrialautomation.api.dao.AlarmRepository;
import com.industrialautomation.api.dao.ConnectedMachineRepository;
import com.industrialautomation.api.dao.MachineRepository;
import com.industrialautomation.api.dao.RateRepository;
import com.industrialautomation.api.dto.response.DefaultResponseDTO;
import com.industrialautomation.api.dto.response.ResponseStatus;
import com.industrialautomation.api.dto.user.ChangeRateDTO;
import com.industrialautomation.api.dto.user.ChangeRatioAlarmDTO;
import com.industrialautomation.api.model.Alarm;
import com.industrialautomation.api.model.ConnectedMachine;
import com.industrialautomation.api.model.Machine;
import com.industrialautomation.api.model.Rates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class HandleChangeOfRate {

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private ConnectedMachineRepository connectedMachineRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private RateRepository rateRepository;

    @Autowired
     private AlarmRepository alarmRepository;


    public Object newHandleChaneOfRate(Long id,Float rate,Integer is_temp){

        String s_q_2 = "SELECT DISTINCT m FROM Machine  m WHERE m.productionLine.id=(SELECT p.productionLine.id FROM Machine p WHERE p.id =:machine_id)";
        Query query2 = em.createQuery(s_q_2);
        query2.setParameter("machine_id", id);
        List<Machine> line_machines = query2.getResultList();
        LinkedList<Long> all_id = new LinkedList<>();
        HashMap<Long,Object> not_set = new HashMap<>();
        for (Machine m: line_machines){
            not_set.put(m.getId(),new HashMap<Long,Object>());
            all_id.add(m.getId());
        }

        String sq = "SELECT DISTINCT m FROM ConnectedMachine m WHERE m.connectedMachine.id IN :machine_list OR m.machine.id IN :machine_list";
        Query query3 = em.createQuery(sq);
        query3.setParameter("machine_list",all_id);
        List<ConnectedMachine> all_connections = query3.getResultList();



        HashMap<Long,Object> results= getConnectedMachineList(all_connections,all_id,new LinkedList<>(),not_set);

        System.out.println("Key set size "+ results.keySet().size());

//        HashMap<Long,Object> find_tree = findHashTree(id,results,new HashMap<Long,Object>(),false);

        Set<Long> all_possible = new HashSet<>();

        for(Long key: results.keySet()){
            Set<Long> set = findHashId( (HashMap<Long, Object>) results.get(key), new HashSet<Long>());
            set.add(key);
            if(set.contains(id))
                all_possible.addAll(set);
        }




        List<Long> selected = new LinkedList<>(all_possible);

        String s= "SELECT DISTINCT m FROM Machine m WHERE m.id IN :id_list";
        Query query = em.createQuery(s);
        query.setParameter("id_list",selected);

        List<Machine> machines = query.getResultList();


        String s_q = "SELECT  a FROM Alarm a JOIN FETCH a.machine WHERE a.machine.id IN :id_list AND a.deleted IS NULL";

        Query query1 = em.createQuery(s_q);
        query1.setParameter("id_list",selected);

        List<Alarm> alarms = query1.getResultList();


        Machine machine= machineRepository.getMachineDetails(id);

        Float best_rate = machine.getInt_rate();
        Float best_temp = machine.getInt_tempe();


        Float precentage;
        List<Machine> updated = new LinkedList<>();
        List<Rates> new_rates = new LinkedList<>();

        List<ChangeRateDTO> machineDetailsDTOS = new LinkedList<>();
        List<ChangeRatioAlarmDTO> changeRatioAlarmDTOS = new LinkedList<>();



        if (rate<=0){
            for (Machine m:machines)
                machineDetailsDTOS.add(new ChangeRateDTO(
                        m.getId() ,
                        m.getName(),
                        "WARNING.. SHUTTING DOWN THE MACHINE",
                        "SOME THING WENT WRONG..",
                        0F,
                        0F,
                        0F,
                        0F,
                        "STOP"
                ));

            for (Alarm alarm: alarms){
                if (alarm.getMachine().getId() == machine.getId()){
                    changeRatioAlarmDTOS.add(
                            new ChangeRatioAlarmDTO(
                                    alarm.getId(),
                                    alarm.getAlarm_name(),
                                    "WARNING.. THIS MACHINE HAS STOPPED. ",
                                    "STOP"
                            )
                    );
                }else {
                    changeRatioAlarmDTOS.add(
                            new ChangeRatioAlarmDTO(
                                    alarm.getId(),
                                    alarm.getAlarm_name(),
                                    "WARNING.. SHUTTING DOWN THE MACHINE.. ",
                                    "STOP"
                            )
                    );
                }

            }

            return new DefaultResponseDTO(200, ResponseStatus.OK,"SHUTDOWN MACHINES",machineDetailsDTOS);
        }


        String command = "";
        String massage ="";
        String command_type="";

        if (is_temp == 0) {
            if (rate >= best_rate) {
                precentage = 1F;
            } else {
                precentage = rate / best_rate;
            }

            if (precentage == 1) {
                for (Machine m : machines) {
                    Float new_rate = m.getInt_rate();
                    Float new_temp = m.getInt_tempe();

                    m.setCurrent_rate(new_rate);
                    m.setCurrent_temp(new_temp);
                    updated.add(m);

                    massage="System Running At Its Best.";
                    command="INCREASE THE RATE TO INITIAL RATE.";
                    command_type="SUCCESS";


                    Rates r = new Rates(null, LocalDateTime.now(), new_rate, 0, m);
                    Rates r2 = new Rates(null, LocalDateTime.now(), new_temp, is_temp, m);
                    new_rates.add(r);
                    new_rates.add(r2);
                }
            } else {
                for (Machine m : machines) {

                    massage="Slow Down The Connected Path.";
                    command="DECREASE THE RATE TO GIVEN RATE.";
                    command_type="WARNING";

                    Float new_rate = m.getInt_rate() * precentage;
                    m.setCurrent_rate(new_rate);
                    updated.add(m);
                    Rates r = new Rates(null, LocalDateTime.now(), new_rate, 0, m);
                    new_rates.add(r);

                }

            }


        }

        if (is_temp != 0) {
            if (rate <= best_temp) {
                precentage = 1F;
            } else {
                precentage = best_temp / rate;
            }

            for (Machine m : machines) {
                if (precentage == 1) {

                    Float new_rate = m.getInt_rate();

                    m.setCurrent_rate(new_rate);
                    m.setCurrent_temp(rate);

                    updated.add(m);

                    Rates r = new Rates(null, LocalDateTime.now(), new_rate, 0, m);
                    Rates r2 = new Rates(null, LocalDateTime.now(), rate, is_temp, m);

                    massage="System Running At Its Best.";
                    command="INCREASE THE RATE TO INITIAL RATE.";
                    command_type="SUCCESS";

                    new_rates.add(r);
                    new_rates.add(r2);
                } else {
                    Float new_rate = m.getCurrent_rate() * precentage;

                    massage="Slow Down The Connected Path.";
                    command="DECREASE THE RATE TO GIVEN RATE.";
                    command_type="WARNING";


                    m.setCurrent_rate(new_rate);
                    m.setCurrent_temp(rate);
                    updated.add(m);

                    Rates r = new Rates(null, LocalDateTime.now(), new_rate, 0, m);
                    Rates r2 = new Rates(null, LocalDateTime.now(), rate, is_temp, m);
                    new_rates.add(r);
                    new_rates.add(r2);

                }


            }
        }
        machineRepository.saveAll(updated);
        rateRepository.saveAll(new_rates);

         machineDetailsDTOS = new LinkedList<>();

        for (Alarm alarm: alarms){
            changeRatioAlarmDTOS.add(
                    new ChangeRatioAlarmDTO(
                            alarm.getId(),
                            alarm.getAlarm_name(),
                            massage,
                            command_type
                    )
            );
        }

        for (Machine m:updated)
            machineDetailsDTOS.add(new ChangeRateDTO(
                    m.getId() ,
                    m.getName(),
                    command,
                    massage,
                    m.getCurrent_rate(),
                    m.getCurrent_temp(),
                    m.getInt_rate(),
                    m.getCurrent_temp(),
                    command_type
            ));

        Map<String,Object> data = new HashMap();
        data.put("machines",machineDetailsDTOS);
        data.put("alarms",changeRatioAlarmDTOS);



        return new DefaultResponseDTO(200,ResponseStatus.OK,"Change Accordingly",data);
    }


//    public Object handleChangeOfRate(Machine machine, Float rate, Integer is_temp) {
////
////        Long machine_id = machine.getId();
//        Float best_rate = machine.getInt_rate();
//        Float best_temp = machine.getInt_tempe();
////        Float current_temp = machine.getCurrent_temp();
////        Float current_rate = machine.getCurrent_rate();
//
////        String s_q = "SELECT m FROM ConnectedMachine m JOIN FETCH m.machine JOIN FETCH m.connectedMachine WHERE m.machine.id = :machine_id OR m.connectedMachine.id= :machine_id";
//        String s_q = "SELECT  DISTINCT m FROM ConnectedMachine m JOIN FETCH m.machine JOIN FETCH m.connectedMachine WHERE m.machine.productionLine.id=:id OR m.connectedMachine.productionLine.id=:id";
//
//        Query query = em.createQuery(s_q);
//        query.setParameter("id", machine.getProductionLine().getId());
////        List<ConnectedMachine> connected_machines = query.getResultList();
//        List<ConnectedMachine> c_machines = query.getResultList();
//
//        List<Machine> machines = new LinkedList<>();
//        List<Long> visited = new LinkedList<>();
//
//        for (ConnectedMachine c : c_machines) {
//            System.out.println(" ID LIST ");
//            System.out.println(c.getMachine().getId());
//            System.out.println(c.getConnectedMachine().getId());
//            if (!visited.contains(c.getMachine().getId())) {
//
//                visited.add(c.getMachine().getId());
//                machines.add(c.getMachine());
//            }
//
//            if (!visited.contains(c.getConnectedMachine().getId())) {
//                visited.add(c.getConnectedMachine().getId());
//
//                machines.add(c.getConnectedMachine());
//            }
//        }
//
//
////        List<Machine> machines = new LinkedList<>();
////        List<Float> cur_rates = new LinkedList<>();
////        List<Float> best_rates = new LinkedList<>();
////
////
////        List<Float> cur_temps = new LinkedList<>();
////        List<Float> best_temps = new LinkedList<>();
//
////        List<Long> m_ids = new LinkedList<>();
//
////        Float sum_rates = 0F;
//
////        for(ConnectedMachine m : connected_machines){
////            Long i = m.getMachine().getId();
////            Long c_i =m.getConnectedMachine().getId();
////            if( i!=machine_id && (!m_ids.contains(i))){
////                m_ids.add(i);
////                machines.add(m.getMachine());
////                sum_rates+=m.getMachine().getCurrent_rate();
////
////                cur_rates.add(m.getMachine().getCurrent_rate());
////                best_rates.add(m.getMachine().getInt_rate());
////
////                cur_temps.add(m.getMachine().getCurrent_temp());
////                best_temps.add(m.getMachine().getInt_tempe());
////
////            }
////            else if( c_i != machine_id && (! m_ids.contains(c_i))){
////                m_ids.add(c_i);
////                machines.add(m.getConnectedMachine());
////
////                sum_rates+=m.getConnectedMachine().getCurrent_rate();
////
////                cur_rates.add(m.getConnectedMachine().getCurrent_rate());
////                best_rates.add(m.getConnectedMachine().getInt_rate());
////
////                cur_temps.add(m.getConnectedMachine().getCurrent_temp());
////                best_temps.add(m.getConnectedMachine().getInt_tempe());
////
////            }
////
//
////        }
//
//
////        for (int i=0 ;i< cur_rates.size();i++){
////
////        }
//
//        Float precentage;
//        List<Machine> updated = new LinkedList<>();
//        List<Rates> new_rates = new LinkedList<>();
//        if (is_temp == 0) {
//            if (rate >= best_rate) {
//                precentage = 1F;
//            } else {
//                precentage = rate / best_rate;
//            }
//
//            if (precentage == 1) {
//                for (Machine m : machines) {
//                    Float new_rate = m.getInt_rate();
//                    Float new_temp = m.getInt_tempe();
//
//
//                    m.setCurrent_rate(new_rate);
//                    m.setCurrent_temp(new_temp);
//                    updated.add(m);
//
//
//                    Rates r = new Rates(null, LocalDateTime.now(), new_rate, 0, m);
//                    Rates r2 = new Rates(null, LocalDateTime.now(), new_temp, is_temp, m);
//                    new_rates.add(r);
//                    new_rates.add(r2);
//                }
//            } else {
//                for (Machine m : machines) {
//                    Float new_rate = m.getInt_rate() * precentage;
//                    m.setCurrent_rate(new_rate);
//                    updated.add(m);
//                    Rates r = new Rates(null, LocalDateTime.now(), new_rate, 0, m);
//                    new_rates.add(r);
//
//                }
//
//            }
//
//
//        }
//
//        if (is_temp != 0) {
//            if (rate <= best_temp) {
//                precentage = 1F;
//            } else {
//                precentage = best_temp / rate;
//            }
//
//            for (Machine m : machines) {
//                if (precentage == 1) {
//
//
//                    Float new_rate = m.getInt_rate();
//
//                    m.setCurrent_rate(new_rate);
//                    m.setCurrent_temp(rate);
//
//                    updated.add(m);
//
//                    Rates r = new Rates(null, LocalDateTime.now(), new_rate, 0, m);
//                    Rates r2 = new Rates(null, LocalDateTime.now(), rate, is_temp, m);
//
//                    new_rates.add(r);
//                    new_rates.add(r2);
//                } else {
//                    Float new_rate = m.getCurrent_rate() * precentage;
//
//
//                    m.setCurrent_rate(new_rate);
//                    m.setCurrent_temp(rate);
//                    updated.add(m);
//
//                    Rates r = new Rates(null, LocalDateTime.now(), new_rate, 0, m);
//                    Rates r2 = new Rates(null, LocalDateTime.now(), rate, is_temp, m);
//                    new_rates.add(r);
//                    new_rates.add(r2);
//
//                }
//
//
//            }
//        }
//
//        machineRepository.saveAll(updated);
//        rateRepository.saveAll(new_rates);
//
//        return updated;
//    }


    private List<Long> getConnectedMachineIdList(List<ConnectedMachine> connectedMachines, LinkedList<Long> to_visit, List<Long> rest, LinkedList<Long> visited) {

        if (!to_visit.isEmpty()) {
            Long i = to_visit.pop();

            if (!visited.contains(i)) {
                visited.add(i);
                for (ConnectedMachine m : connectedMachines) {

                    if (m.getMachine().getId() == i) {
                        to_visit.add(m.getConnectedMachine().getId());
                        rest.add(m.getConnectedMachine().getId());
                    } else if (m.getConnectedMachine().getId() == i) {
                        to_visit.add(m.getMachine().getId());
                        rest.add(m.getMachine().getId());
                    }

                }


            }

            return getConnectedMachineIdList(connectedMachines, to_visit, rest, visited);

        }



        return rest;
    }

    private HashMap<Long,Object> getConnectedMachineList(List<ConnectedMachine> connectedMachines, LinkedList<Long> to_visit, LinkedList<Long> visited, HashMap<Long,Object>not_set ) {
        if (!to_visit.isEmpty()) {
            Long i = to_visit.pop();

//            System.out.println(" First loop "+ i);


            if (!visited.contains(i)) {
                visited.add(i);

                for (ConnectedMachine m : connectedMachines) {
//                    System.out.println(" Second loop "+ m.getMachine().getId() + "  "+m.getConnectedMachine().getId());
                    if (m.getConnectedMachine().getId() == i) {

                        if(not_set.containsKey(i)){

                            HashMap<Long,Object> removed= (HashMap<Long,Object>) not_set.get(i);
                            not_set.remove(i);
                            Long id= m.getMachine().getId();;

                            if(not_set.containsKey(id)){
                                HashMap<Long,HashMap> new_hash= ( HashMap<Long,HashMap>) not_set.get(id);
                                new_hash.put(i,removed);
                                not_set.put(id,new_hash);
                            }else {
                                not_set= createHash(id,removed,i,not_set);
                            }
                        } else {
                            HashMap<Long,Object> removed = findHash(i,not_set,new HashMap<Long,Object>(),false);
                            not_set= createHash(m.getMachine().getId(), removed,i,not_set);
                        }

                        to_visit.add(m.getConnectedMachine().getId());

                    }
//
//                    System.out.println();
//                    System.out.println();

                }


            }
            return getConnectedMachineList(connectedMachines, to_visit, visited,not_set);

        }

        return not_set;



    }
    /**
     *
     *
     * {
     *    1: {
     *        2: {
     *             3;{
     *
     *             },
     *             4:{
     *
     *             },
     *             5:{
     *
     *             }
     *         },
     *
     *     }
     *     ,
     *     6 :{
     *
     *     }
     * }
     * */

    private  HashMap<Long,Object> createHash(Long id, HashMap<Long,Object> to_set, Long to_set_id, HashMap<Long,Object> original){


        for(Long key : original.keySet()){

            if(((HashMap<Long,Object>)original.get(key)).containsKey(id)){
                HashMap<Long,Object> new_hash =  (HashMap<Long,Object>) ((HashMap<Long,Object>)original.get(key)).get(id);
                new_hash.put(to_set_id,to_set);
                ((HashMap<Long,Object>)original.get(key)).put(id,new_hash);
            }else {
                original.put(key, createHash(id,to_set,to_set_id ,(HashMap<Long, Object>) original.get(key)));
            }
        }

        return original;

    }


    /**
     *
     *
     * {
     *    1: {
     *        2: {
     *             3;{
     *
     *             },
     *             4:{
     *
     *             },
     *             5:{
     *
     *             }
     *         },
     *
     *     }
     *     ,
     *     6 :{
     *
     *     }
     * }
     * */
    private  HashMap<Long,Object> findHash(Long to_set_id, HashMap<Long,Object> original, HashMap<Long,Object> selected ,boolean found){
        if(found)
            return selected;

        for(Long key : original.keySet()){
            if (((HashMap<Long,Object>) original.get(key)).containsKey(to_set_id))
                findHash (to_set_id,original, (HashMap<Long,Object>)  ((HashMap<Long,Object>) original.get(key)).get(to_set_id),true);
            else {
                findHash(to_set_id,(HashMap<Long,Object>) original.get(key), selected, false);
            }
        }
        return selected;

    }

    private  HashMap<Long,Object> findHashTree(Long to_set_id, HashMap<Long,Object> original, HashMap<Long,Object> selected ,boolean found){
        if(found)
            return selected;

        if(original.containsKey(to_set_id))
            return findHash(to_set_id,(HashMap<Long,Object>) original.get(to_set_id), (HashMap<Long,Object>) original.get(to_set_id), true);

        for(Long key : original.keySet()){
            if (((HashMap<Long,Object>) original.get(key)).containsKey(to_set_id))
                return findHash (to_set_id,original, (HashMap<Long,Object>)  ((HashMap<Long,Object>) original.get(key)).get(to_set_id),true);
            else {
                return findHash(to_set_id,(HashMap<Long,Object>) original.get(key), selected, false);
            }
        }
        return selected;

    }

    /**
     *
     *
     * {
     *    1: {
     *        2: {
     *             3;{
     *
     *             },
     *             4:{
     *
     *             },
     *             5:{
     *
     *             }
     *         },
     *
     *     }
     *     ,
     *     6 :{
     *
     *     }
     * }
     * */

    private  Set<Long> findHashId(HashMap<Long,Object> original, Set<Long> results){

        for(Long key : original.keySet()){
//            System.out.println("Inside loop "+ key);
            results.add(key);
            results = findHashId((HashMap<Long, Object>) original.get(key),results);
        }
        return results;

    }




}

