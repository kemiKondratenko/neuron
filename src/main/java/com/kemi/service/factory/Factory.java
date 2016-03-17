package com.kemi.service.factory;

import com.google.common.collect.Maps;
import com.kemi.system.Neuron;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Eugene on 17.03.2016.
 */
@Service
public class Factory {

    private Map<String, Neuron> cech = Maps.newHashMap();

    public Neuron neuron(String token) {
        if(!cech.containsKey(token)){
            cech.put(token, new Neuron(token));
        }
        Neuron neuron = cech.get(token);
        neuron.ink();
        return neuron;
    }

}
