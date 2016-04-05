package com.kemi.tfidf;

import com.google.common.collect.Sets;
import com.kemi.entities.JsonDots;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by Eugene on 03.04.2016.
 */
@Service
public class MostUsefulFinder {

    private static final double D = 0.;

    public void findMostUseful(List<JsonDots> dots) {
        normalize(dots);
        Set<JsonDots> jsonDotses = Sets.newHashSet(dots);
        Double minX = getMinX(jsonDotses);
        Double minY = getMinY(jsonDotses);
        Double maxX = getMaxX(jsonDotses);
        Double maxY = getMaxY(jsonDotses);
        Double currentDensity;
        Double density = density(minX, minY, maxY, maxX, Double.valueOf(jsonDotses.size()));
        Integer count;
        Boolean changed;
        do {
            changed = false;
            Double minYLocal = getMinY(jsonDotses, minY);
            count = countY(jsonDotses, minY);
            currentDensity = density(minX, minYLocal, maxY, maxX, Double.valueOf(jsonDotses.size() - count));
            if (currentDensity >= density - D) {
                removeY(jsonDotses, minY);
                minY = minYLocal;
                density = currentDensity;
                changed = true;
            }

            Double maxYLocal = getMaxY(jsonDotses, maxY);
            count = countY(jsonDotses, maxY);
            currentDensity = density(minX, minY, maxYLocal, maxX, Double.valueOf(jsonDotses.size() - count));
            if (currentDensity >= density - D) {
                removeY(jsonDotses, maxY);
                maxY = maxYLocal;
                density = currentDensity;
                changed = true;
            }

            Double maxXLocal = getMaxX(jsonDotses, maxX);
            count = countX(jsonDotses, maxX);
            currentDensity = density(minX, minY, maxY, maxXLocal, Double.valueOf(jsonDotses.size() - count));
            if (currentDensity >= density - D) {
                removeX(jsonDotses, maxX);
                maxX = maxXLocal;
                density = currentDensity;
                changed = true;
            }

            Double minXLocal = getMinX(jsonDotses, minX);
            count = countX(jsonDotses, minX);
            currentDensity = density(minXLocal, minY, maxY, maxX, Double.valueOf(jsonDotses.size() - count));
            if (currentDensity >= density - D) {
                removeX(jsonDotses, minX);
                minX = minXLocal;
                density = currentDensity;
                changed = true;
            }
        } while (changed && !jsonDotses.isEmpty() && !jsonDotses.isEmpty());
        color(dots, jsonDotses);
    }

    private void normalize(List<JsonDots> dots) {
        for (JsonDots dot : dots) {
            dot.setY_axis(dot.getY_axis() * 10000000);
        }
    }

    private Double density(Double minX, Double minY, Double maxY, Double maxX, Double size) {
        Double dY = (maxY - minY);
        Double dX = (maxX - minX);
        Double area = dY * dX;
        Double result = size / area;
        return result;
    }

    private void removeX(Set<JsonDots> jsonDotses, Double min) {
        Set<JsonDots> toRemove = Sets.newHashSet();
        for (JsonDots jsonDotse : jsonDotses) {
            if (jsonDotse.getX_axis().compareTo(min) == 0) {
                toRemove.add(jsonDotse);
            }
        }
        jsonDotses.removeAll(toRemove);
    }

    private void removeY(Set<JsonDots> jsonDotses, Double min) {
        Set<JsonDots> toRemove = Sets.newHashSet();
        for (JsonDots jsonDotse : jsonDotses) {
            if (jsonDotse.getY_axis().compareTo(min) == 0) {
                toRemove.add(jsonDotse);
            }
        }
        jsonDotses.removeAll(toRemove);
    }

    private int countX(Set<JsonDots> jsonDotses, Double min) {
        Set<JsonDots> toRemove = Sets.newHashSet();
        for (JsonDots jsonDotse : jsonDotses) {
            if (jsonDotse.getX_axis().compareTo(min) == 0) {
                toRemove.add(jsonDotse);
            }
        }
        return toRemove.size();
    }

    private int countY(Set<JsonDots> jsonDotses, Double min) {
        Set<JsonDots> toRemove = Sets.newHashSet();
        for (JsonDots jsonDotse : jsonDotses) {
            if (jsonDotse.getY_axis().compareTo(min) == 0) {
                toRemove.add(jsonDotse);
            }
        }
        return toRemove.size();
    }

    private void color(List<JsonDots> dots, Set<JsonDots> jsonDotses) {
        for (JsonDots dot : dots) {
            if (jsonDotses.contains(dot)) {
                dot.setColor("blue");
            }
        }
    }

    private Double getMinX(Iterable<JsonDots> dots) {
        Double min_x = Double.MAX_VALUE;
        for (JsonDots dot : dots) {
            if (min_x > dot.getX_axis()) {
                min_x = dot.getX_axis();
            }
        }
        return min_x;
    }

    private Double getMinX(Iterable<JsonDots> dots, Double minY) {
        Double min_x = Double.MAX_VALUE;
        for (JsonDots dot : dots) {
            if (!dot.getX_axis().equals(minY) && min_x > dot.getX_axis()) {
                min_x = dot.getX_axis();
            }
        }
        return min_x;
    }

    private Double getMinY(Iterable<JsonDots> dots) {
        Double min_y = Double.MAX_VALUE;
        for (JsonDots dot : dots) {
            if (min_y > dot.getY_axis()) {
                min_y = dot.getY_axis();
            }
        }
        return min_y;
    }

    private Double getMinY(Set<JsonDots> dots, Double minY) {
        Double min_y = Double.MAX_VALUE;
        for (JsonDots dot : dots) {
            if (!dot.getY_axis().equals(minY) && min_y > dot.getY_axis()) {
                min_y = dot.getY_axis();
            }
        }
        return min_y;
    }

    private Double getMaxX(Iterable<JsonDots> dots) {
        Double max_x = Double.MIN_VALUE;
        for (JsonDots dot : dots) {
            if (max_x < dot.getX_axis()) {
                max_x = dot.getX_axis();
            }
        }
        return max_x;
    }


    private Double getMaxX(Iterable<JsonDots> dots, Double minY) {
        Double max_x = Double.MIN_VALUE;
        for (JsonDots dot : dots) {
            if (!dot.getX_axis().equals(minY) && max_x < dot.getX_axis()) {
                max_x = dot.getX_axis();
            }
        }
        return max_x;
    }

    private Double getMaxY(Iterable<JsonDots> dots) {
        Double max_y = Double.MIN_VALUE;
        for (JsonDots dot : dots) {
            if (max_y < dot.getY_axis()) {
                max_y = dot.getY_axis();
            }
        }
        return max_y;
    }

    private Double getMaxY(Iterable<JsonDots> dots, Double minY) {
        Double max_y = Double.MIN_VALUE;
        for (JsonDots dot : dots) {
            if (!dot.getY_axis().equals(minY) && max_y < dot.getY_axis()) {
                max_y = dot.getY_axis();
            }
        }
        return max_y;
    }

}
