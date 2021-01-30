package com.cnam.nfa036projet.service;

import com.cnam.nfa036projet.form.TempDuJour;
import com.cnam.nfa036projet.model.Frigo;
import com.cnam.nfa036projet.model.ReleveTemp;
import com.cnam.nfa036projet.repository.FrigoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FrigoService {

    @Autowired
    private FrigoRepository frigoRepository ;


    public List<TempDuJour> lastTempList() {
        List<Frigo> frigoList = frigoRepository.findAll() ;
        List<TempDuJour> tempList = new ArrayList<>();
        for (Frigo frigo : frigoList) {
            TempDuJour releve = new TempDuJour();
            releve.setNomFrigo(frigo.getNomFrigo());
            List<ReleveTemp> releveTemp = frigo.getRelevesTemp();
            for (ReleveTemp temp:releveTemp) {
                if (temp.getDateEnregTemp().toLocalDate().isEqual(LocalDate.now())){
                    if (temp.getDateEnregTemp().toLocalTime().isBefore(LocalTime.of(12,0))){
                        releve.setTempMatin(temp.getTemperature());
                    } else {
                        releve.setTempAprem(temp.getTemperature());
                    }
                }
            }

            tempList.add(releve);
        }
        return tempList ;
    }

}
