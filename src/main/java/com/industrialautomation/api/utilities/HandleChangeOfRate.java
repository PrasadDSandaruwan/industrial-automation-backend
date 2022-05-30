package com.industrialautomation.api.utilities;


import com.industrialautomation.api.dao.ConnectedMachineRepository;
import com.industrialautomation.api.dao.MachineRepository;
import com.industrialautomation.api.dao.RateRepository;
import com.industrialautomation.api.model.ConnectedMachine;
import com.industrialautomation.api.model.Machine;
import com.industrialautomation.api.model.Rates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

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


    public Object handleChangeOfRate(Machine machine, Float rate, Integer is_temp) {

        Long machine_id = machine.getId();
        Float best_rate = machine.getInt_rate();
        Float best_temp = machine.getInt_tempe();
//        Float current_temp = machine.getCurrent_temp();
//        Float current_rate = machine.getCurrent_rate();

//        String s_q = "SELECT m FROM ConnectedMachine m JOIN FETCH m.machine JOIN FETCH m.connectedMachine WHERE m.machine.id = :machine_id OR m.connectedMachine.id= :machine_id";
        String s_q = "SELECT  DISTINCT m FROM ConnectedMachine m JOIN FETCH m.machine JOIN FETCH m.connectedMachine WHERE m.machine.productionLine.id=:id OR m.connectedMachine.productionLine.id=:id";

        Query query = em.createQuery(s_q);
        query.setParameter("id", machine.getProductionLine().getId());
//        List<ConnectedMachine> connected_machines = query.getResultList();
        List<ConnectedMachine> c_machines = query.getResultList();

        List<Machine> machines = new LinkedList<>();
        List<Long> visited = new LinkedList<>();

        for(ConnectedMachine c: c_machines){
            System.out.println(" ID LIST ");
            System.out.println(c.getMachine().getId());
            System.out.println(c.getConnectedMachine().getId());
            if(!visited.contains(c.getMachine().getId() )){

                visited.add(c.getMachine().getId());
                machines.add(c.getMachine());
            }

            if(!visited.contains(c.getConnectedMachine().getId() )){
                visited.add(c.getConnectedMachine().getId());

                machines.add(c.getConnectedMachine());
            }
        }


//        List<Machine> machines = new LinkedList<>();
//        List<Float> cur_rates = new LinkedList<>();
//        List<Float> best_rates = new LinkedList<>();
//
//
//        List<Float> cur_temps = new LinkedList<>();
//        List<Float> best_temps = new LinkedList<>();

//        List<Long> m_ids = new LinkedList<>();

//        Float sum_rates = 0F;

//        for(ConnectedMachine m : connected_machines){
//            Long i = m.getMachine().getId();
//            Long c_i =m.getConnectedMachine().getId();
//            if( i!=machine_id && (!m_ids.contains(i))){
//                m_ids.add(i);
//                machines.add(m.getMachine());
//                sum_rates+=m.getMachine().getCurrent_rate();
//
//                cur_rates.add(m.getMachine().getCurrent_rate());
//                best_rates.add(m.getMachine().getInt_rate());
//
//                cur_temps.add(m.getMachine().getCurrent_temp());
//                best_temps.add(m.getMachine().getInt_tempe());
//
//            }
//            else if( c_i != machine_id && (! m_ids.contains(c_i))){
//                m_ids.add(c_i);
//                machines.add(m.getConnectedMachine());
//
//                sum_rates+=m.getConnectedMachine().getCurrent_rate();
//
//                cur_rates.add(m.getConnectedMachine().getCurrent_rate());
//                best_rates.add(m.getConnectedMachine().getInt_rate());
//
//                cur_temps.add(m.getConnectedMachine().getCurrent_temp());
//                best_temps.add(m.getConnectedMachine().getInt_tempe());
//
//            }
//

//        }


//        for (int i=0 ;i< cur_rates.size();i++){
//
//        }

        Float precentage;
        List<Machine> updated = new LinkedList<>();
        List<Rates> new_rates = new LinkedList<>();
        if(is_temp==0){
            if(rate>=best_rate){
                precentage =1F;
            }else {
                precentage = rate/best_rate;
            }

            if(precentage==1){
                for (Machine m: machines){
                    Float new_rate = m.getInt_rate();
                    Float new_temp = m.getInt_tempe();


                    m.setCurrent_rate(new_rate);
                    m.setCurrent_temp(new_temp);
                    updated.add(m);


                    Rates r = new Rates(null, LocalDateTime.now(),new_rate,0,m);
                    Rates r2 = new Rates(null, LocalDateTime.now(),new_temp,is_temp,m);
                    new_rates.add(r);
                    new_rates.add(r2);
                }
            }else {
                for (Machine m: machines){
                    Float new_rate = m.getInt_rate()*precentage;
                    m.setCurrent_rate(new_rate);
                    updated.add(m);
                    Rates r = new Rates(null, LocalDateTime.now(),new_rate,0,m);
                    new_rates.add(r);

                }

            }


        }

        if(is_temp!=0){
            if(rate <= best_temp){
                precentage =1F;
            }else {
                precentage = best_temp/rate;
            }

            for (Machine m: machines){
                if(precentage==1){


                    Float new_rate = m.getInt_rate();

                    m.setCurrent_rate(new_rate);
                    m.setCurrent_temp(rate);

                    updated.add(m);

                    Rates r = new Rates(null, LocalDateTime.now(),new_rate,0,m);
                    Rates r2 = new Rates(null, LocalDateTime.now(),rate,is_temp,m);

                    new_rates.add(r);
                    new_rates.add(r2);
                }
                else {
                    Float new_rate = m.getCurrent_rate()*precentage;


                    m.setCurrent_rate(new_rate);
                    m.setCurrent_temp(rate);
                    updated.add(m);

                    Rates r = new Rates(null, LocalDateTime.now(),new_rate,0,m);
                    Rates r2 = new Rates(null, LocalDateTime.now(),rate,is_temp,m);
                    new_rates.add(r);
                    new_rates.add(r2);

                }


            }
        }

        machineRepository.saveAll(updated);
        rateRepository.saveAll(new_rates);

        return updated;
    }




}

