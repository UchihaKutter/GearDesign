package geardesigner.beans;

import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

//待办 2021/8/5: 可控制调整显示位数的Bean
public class DecimalProperty implements Property<Double> {
    @Override
    public void bind(final ObservableValue<? extends Double> observable) {

    }

    @Override
    public void unbind() {

    }

    @Override
    public boolean isBound() {
        return false;
    }

    @Override
    public void bindBidirectional(final Property<Double> other) {

    }

    @Override
    public void unbindBidirectional(final Property<Double> other) {

    }

    @Override
    public Object getBean() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void addListener(final ChangeListener<? super Double> listener) {

    }

    @Override
    public void removeListener(final ChangeListener<? super Double> listener) {

    }

    @Override
    public Double getValue() {
        return null;
    }

    @Override
    public void setValue(final Double value) {

    }

    @Override
    public void addListener(final InvalidationListener listener) {

    }

    @Override
    public void removeListener(final InvalidationListener listener) {

    }

}
