package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Gem {
    private String id;
    private String name;
    private Preciousness preciousness;
    private String origin;
    private List<VisualParameters> visualParameters;
    private BigDecimal value;

    public Gem() {
        this.visualParameters = new ArrayList<>();
    }

    public Gem(String id, String name, Preciousness preciousness, String origin, 
               List<VisualParameters> visualParameters, BigDecimal value) {
        this.id = id;
        this.name = name;
        this.preciousness = preciousness;
        this.origin = origin;
        this.visualParameters = visualParameters != null ? visualParameters : new ArrayList<>();
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Preciousness getPreciousness() {
        return preciousness;
    }

    public void setPreciousness(Preciousness preciousness) {
        this.preciousness = preciousness;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public List<VisualParameters> getVisualParameters() {
        return visualParameters;
    }

    public void setVisualParameters(List<VisualParameters> visualParameters) {
        this.visualParameters = visualParameters;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gem gem = (Gem) o;
        return Objects.equals(id, gem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Gem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", preciousness=" + preciousness +
                ", origin='" + origin + '\'' +
                ", visualParameters=" + visualParameters +
                ", value=" + value +
                '}';
    }
}

