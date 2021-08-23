package geardesigner.beans;

import geardesigner.units.BaseUnit;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * @author SUPERSTATION
 */
public class ParamBean<E extends BaseUnit> implements Serializable {
    private static final long serialVersionUID = 5080586713701486554L;
    private final String name;
    private final E unit;

    private ParamBean(final String name, final E unit) {
        this.name = name;
        this.unit = unit;
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static final <E extends BaseUnit> @NotNull ParamBean<E> create(@NotNull String name, @Nullable E unit) {
        return new ParamBean<>(name, unit);
    }

    public String getName() {
        return name;
    }

    @Nullable
    public E getUnit() {
        return unit;
    }
}
