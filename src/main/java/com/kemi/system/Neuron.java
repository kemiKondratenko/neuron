package com.kemi.system;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Eugene on 16.03.2016.
 */
public class Neuron {

    private List<Connection> neurons;
    private String word;
    private int count;

    public Neuron(String word) {
        this.word = word;
        neurons = Lists.newArrayList();
        count = 0;
    }

    public List<Connection> getNeurons() {
        return neurons;
    }

    public void setNeurons(List<Connection> neurons) {
        this.neurons = neurons;
    }

    public void addNeurons(List<Connection> neurons) {
        this.neurons.addAll(neurons);
    }

    public void addNeurons(Connection neuron) {
        this.neurons.add(neuron);
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Neuron)) return false;

        Neuron neuron = (Neuron) o;

        if (neurons != null ? !neurons.equals(neuron.neurons) : neuron.neurons != null) return false;
        return word != null ? word.equals(neuron.word) : neuron.word == null;

    }

    @Override
    public int hashCode() {
        int result = neurons != null ? neurons.hashCode() : 0;
        result = 31 * result + (word != null ? word.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Neuron{" +
                "neurons=" + neurons +
                ", word='" + word + '\'' +
                ", count='" + count + '\'' +
                '}';
    }

    public void ink() {
        count++;
    }
}
