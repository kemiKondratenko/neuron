package com.kemi.system;

/**
 * Created by Eugene on 16.03.2016.
 */
public class Connection {

    private Neuron neuron;
    private Integer weight;

    public Connection(Neuron neuron, Integer weight) {
        this.neuron = neuron;
        this.weight = weight;
    }

    public Neuron getNeuron() {
        return neuron;
    }

    public void setNeuron(Neuron neuron) {
        this.neuron = neuron;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "neuron=" + neuron +
                ", weight=" + weight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Connection)) return false;

        Connection that = (Connection) o;

        if (neuron != null ? !neuron.equals(that.neuron) : that.neuron != null) return false;
        return weight != null ? weight.equals(that.weight) : that.weight == null;

    }

    @Override
    public int hashCode() {
        int result = neuron != null ? neuron.hashCode() : 0;
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        return result;
    }
}
