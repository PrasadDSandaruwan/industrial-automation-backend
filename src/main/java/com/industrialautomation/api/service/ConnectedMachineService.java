package com.industrialautomation.api.service;


import com.industrialautomation.api.dao.ConnectedMachineRepository;
import com.industrialautomation.api.dao.MachineRepository;
import com.industrialautomation.api.dto.admin.MachineDetailsDTO;
import com.industrialautomation.api.dto.response.DefaultResponseDTO;
import com.industrialautomation.api.dto.response.ResponseStatus;
import com.industrialautomation.api.model.ConnectedMachine;
import com.industrialautomation.api.model.ConnectedMachineKey;
import com.industrialautomation.api.model.Machine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ConnectedMachineService {

    @Autowired
    private ConnectedMachineRepository connectedMachineRepository;

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private EntityManager em;

//    private HashMap<Long,Object> original;


    @Transactional
    public Object editConnectedMachine(Long machine_id, Long connected_machine_id, double rate) {

        Machine machine = machineRepository.getMachineDetails(machine_id);
        Machine connected_machine = machineRepository.getMachineDetails(connected_machine_id);

        if (machine == null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS, "No such machine exists.");

        if (connected_machine == null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS, "Invalid connected machine id.");

        if (connected_machine.getProductionLine().getId() != machine.getProductionLine().getId())
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS, "Both machines not in same production Line.");

        Float f_rate = (float) rate;

        if (machine.getInt_rate()< f_rate)
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Line Rate cannot be higher than INITIAL RATE.");
        if(f_rate<=0)
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Cannot rate be negative or Zero");

        if (machine_id == connected_machine_id)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS, "Cannot have same id.");

//        machine.setInt_tempe((float) temp);

        ConnectedMachineKey connectedMachineKey = new ConnectedMachineKey();
        connectedMachineKey.setConnected_machine_id(connected_machine_id);
        connectedMachineKey.setMachine_id(machine_id);

        ConnectedMachineKey rev = new ConnectedMachineKey();
        rev.setConnected_machine_id(machine_id);
        rev.setMachine_id(connected_machine_id);

        ConnectedMachine connectedMachine = connectedMachineRepository.getByDoubleId(connectedMachineKey, rev);
//        Float f_rate = (float) rate;

        if (connectedMachine == null) {
            connectedMachine = new ConnectedMachine();
            connectedMachine.setId(connectedMachineKey);

            connectedMachine.setRate(f_rate);
            connectedMachine.setMachine(machine);
            connectedMachine.setConnectedMachine(connected_machine);
            connectedMachine.setCurrent_rate(f_rate);

//            float current_rate;
//            if (machine.getInt_rate() == null) {
//                current_rate = f_rate;
//            } else {
//                current_rate = machine.getInt_rate() + f_rate;
//            }
//
//            machine.setInt_rate(current_rate);
        } else {

            connectedMachine.setRate(f_rate);
            connectedMachine.setCurrent_rate(f_rate);
            connectedMachine.setMachine(machine);
            connectedMachine.setConnectedMachine(connected_machine);

//            float cur_rate = machine.getInt_rate();
//            cur_rate = cur_rate - connectedMachine.getRate() + f_rate;
//            machine.setInt_rate(cur_rate);
        }
//
//        machineRepository.save(machine);
        connectedMachineRepository.save(connectedMachine);
        return new DefaultResponseDTO(200, ResponseStatus.OK, "Successfully Added.");

    }

    public Object getPossibleMachines(Long id) {

        Machine machine = machineRepository.getMachineDetails(id);

        if(machine==null)
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"No such machine");


        String s_q = "SELECT DISTINCT m FROM Machine m JOIN  FETCH  m.productionLine JOIN FETCH m.machineType " +
                " WHERE m.deleted IS NULL AND (NOT m.id=:machine_id)" +
                " AND m.productionLine.id=(SELECT p.productionLine.id FROM Machine p WHERE p.id=:machine_id) ";

        if(machine.getMachineType().getSlug().equals("production")){
            s_q += "AND (NOT m.machineType.slug='ingredient' )";
        }
        if (machine.getMachineType().getSlug().equals("packing")){
            s_q += "AND  m.machineType.slug='packing' ";
        }

        Query query = em.createQuery(s_q);
        query.setParameter("machine_id", id);
        List<Machine> machines = query.getResultList();

        String s = " SELECT DISTINCT m FROM ConnectedMachine m JOIN FETCH m.machine JOIN FETCH m.connectedMachine " +
                " WHERE m.machine.id in :machine_id OR m.connectedMachine.id in :machine_id";
        Query query1 = em.createQuery(s);

        List<Long> machine_id = new LinkedList<>();

        for (Machine m : machines) {
            System.out.println(m.getId());
            machine_id.add(m.getId());
        }

        query1.setParameter("machine_id", machine_id);

        List<ConnectedMachine> connectedMachines = query1.getResultList();

        LinkedList<Long> to_visit = new LinkedList<Long>() {{
            add(id);
        }};

        LinkedList<Long> visited = new LinkedList<>();
        List<Long> not_suitable = getConnectedMachineIdList(connectedMachines, to_visit, new LinkedList<>(), visited);

        not_suitable.add(id);



        List<Machine> selected = new LinkedList<>();

        for (Machine m : machines) {
            if (!not_suitable.contains(m.getId())){
                selected.add(m);
            }



        }

        String s_q_2 = "SELECT DISTINCT m FROM Machine  m WHERE m.productionLine.id=(SELECT p.productionLine.id FROM Machine p WHERE p.id =:machine_id) AND m.deleted IS NULL ";
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


        List<MachineDetailsDTO> machineDetailsDTOS = new LinkedList<>();

        for (Machine m : selected)
            machineDetailsDTOS.add(new MachineDetailsDTO(m));

        HashMap<String,Object> res = new HashMap<>();
        res.put("data",machineDetailsDTOS);
        res.put("connections", results);
        return new DefaultResponseDTO(200, ResponseStatus.OK, "Data fetched successfully.", res);
    }

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

    private HashMap<Long,Object> getConnectedMachineList(List<ConnectedMachine> connectedMachines, LinkedList<Long> to_visit, LinkedList<Long> visited,HashMap<Long,Object>not_set ) {
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

    public Object getRate(Long machine_id, Long connected_machine_id) {

        ConnectedMachineKey connectedMachineKey = new ConnectedMachineKey();
        connectedMachineKey.setConnected_machine_id(connected_machine_id);
        connectedMachineKey.setMachine_id(machine_id);

        ConnectedMachineKey rev = new ConnectedMachineKey();
        rev.setConnected_machine_id(machine_id);
        rev.setMachine_id(connected_machine_id);

        ConnectedMachine connectedMachine = connectedMachineRepository.getByDoubleId(connectedMachineKey, rev);

        Machine machine = machineRepository.getMachineDetails(machine_id);
        if (machine == null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS, "Machine details Not exists");

        Map<String, Float> results = new HashMap<>();

        if (connectedMachine == null) {
            if (machine.getInt_tempe() == null) {
                results.put("temp", 0F);
                results.put("rate", 0F);
            } else {
                results.put("temp", machine.getInt_tempe());
                results.put("rate", 0F);
            }

            return new DefaultResponseDTO(200, ResponseStatus.OK, "Data Fetched successfully.", results);
        } else {

            if (machine.getInt_tempe() == null) {
                results.put("temp", 0F);
                results.put("rate", connectedMachine.getRate());
            } else {
                results.put("temp", machine.getInt_tempe());
                results.put("rate", connectedMachine.getRate());
            }

        }

        return new DefaultResponseDTO(200, ResponseStatus.OK, "data fetched successfully.", results);

    }
}
