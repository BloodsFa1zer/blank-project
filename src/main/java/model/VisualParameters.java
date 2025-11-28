package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VisualParameters {
    private List<Color> colors;
    private BigDecimal transparency;
    private Integer facets;

    public VisualParameters() {
        this.colors = new ArrayList<>();
    }

    public VisualParameters(List<Color> colors, BigDecimal transparency, Integer facets) {
        this.colors = colors != null ? colors : new ArrayList<>();
        this.transparency = transparency;
        this.facets = facets;
    }

    public List<Color> getColors() {
        return colors;
    }

    public void setColors(List<Color> colors) {
        this.colors = colors;
    }

    public BigDecimal getTransparency() {
        return transparency;
    }

    public void setTransparency(BigDecimal transparency) {
        this.transparency = transparency;
    }

    public Integer getFacets() {
        return facets;
    }

    public void setFacets(Integer facets) {
        this.facets = facets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisualParameters that = (VisualParameters) o;
        return Objects.equals(colors, that.colors) &&
                Objects.equals(transparency, that.transparency) &&
                Objects.equals(facets, that.facets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(colors, transparency, facets);
    }

    @Override
    public String toString() {
        return "VisualParameters{" +
                "colors=" + colors +
                ", transparency=" + transparency +
                ", facets=" + facets +
                '}';
    }
}

