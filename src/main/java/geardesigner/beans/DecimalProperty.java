package geardesigner.beans;

import com.sun.javafx.binding.ExpressionHelper;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;

/**
 * 可变有效小数位数的十进制Property类型
 *
 * @author SUPERSTATION
 */ //待办 2021/8/5: 可控制调整显示位数的Bean
public strictfp class DecimalProperty extends DoubleProperty {

    Decimal value;
    int digit;
    ObservableDoubleValue observable = null;
    InvalidationListener listener = null;
    boolean valid = true;
    ExpressionHelper<Number> helper = null;

    private static final Object DEFAULT_BEAN = null;
    private static final String DEFAULT_NAME = "";
    private static final int DEFAULT_DIGIT = 2;

    private final Object bean;
    private final String name;

    public DecimalProperty(Object bean, String name, Decimal initialValue, int initialDigit) {
        value = initialValue;
        digit = initialDigit;
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

    DecimalProperty(Object bean, String name) {
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

    public DecimalProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME);
    }

    public DecimalProperty(Decimal initialValue) {
        this(DEFAULT_BEAN, DEFAULT_NAME, initialValue, DEFAULT_DIGIT);
    }

    public DecimalProperty(String name, Decimal initialValue) {
        this(DEFAULT_BEAN, name, initialValue, DEFAULT_DIGIT);
    }

    public DecimalProperty(Decimal initialValue, int initialDigit) {
        this(DEFAULT_BEAN, DEFAULT_NAME, initialValue, initialDigit);
    }

    @Override
    public Object getBean() {
        return bean;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Create a unidirection binding for this {@code Property}.
     * <p>
     * Note that JavaFX has all the bind calls implemented through weak listeners. This means the bound property
     * can be garbage collected and stopped from being updated.
     *
     * @param observable The observable this {@code Property} should be bound to.
     * @throws NullPointerException if {@code observable} is {@code null}
     */
    @Override
    public void bind(final ObservableValue<? extends Number> observable) {

    }

    /**
     * Remove the unidirectional binding for this {@code Property}.
     * <p>
     * If the {@code Property} is not bound, calling this method has no effect.
     *
     * @see #bind(ObservableValue)
     */
    @Override
    public void unbind() {

    }

    /**
     * Can be used to check, if a {@code Property} is bound.
     *
     * @return {@code true} if the {@code Property} is bound, {@code false}
     * otherwise
     * @see #bind(ObservableValue)
     */
    @Override
    public boolean isBound() {
        return false;
    }

    /**
     * Returns the current value of this {@code ObservableDoubleValue}.
     *
     * @return The current value
     */
    @Override
    public double get() {
        valid = true;
        return observable == null ? value.doubleValue() : observable.get();
    }

    private void markInvalid() {
        if (valid) {
            valid = false;
            invalidated();
            fireValueChangedEvent();
        }
    }


    /**
     * Sends notifications to all attached
     * {@link javafx.beans.InvalidationListener InvalidationListeners} and
     * {@link javafx.beans.value.ChangeListener ChangeListeners}.
     * <p>
     * This method is called when the value is changed, either manually by
     * calling {@link #set(double)} or in case of a bound property, if the
     * binding becomes invalid.
     */
    protected void fireValueChangedEvent() {
        ExpressionHelper.fireValueChangedEvent(helper);
    }

    /**
     * The method {@code invalidated()} can be overridden to receive
     * invalidation notifications. This is the preferred option in
     * {@code Objects} defining the property, because it requires less memory.
     * <p>
     * The default implementation is empty.
     */
    protected void invalidated() {
    }

    /**
     * Set the wrapped value.
     * Unlike {@link #setValue(Number) },
     * this method uses primitive double.
     *
     * @param newValue The new value
     */
    @Override
    public void set(final double newValue) {
        if (isBound()) {
            throw new java.lang.RuntimeException((getBean() != null && getName() != null ?
                    getBean().getClass().getSimpleName() + "." + getName() + " : " : "") + "A bound value cannot be set.");
        }
        if (!value.equals(newValue)) {
            value = new Decimal(newValue);
            markInvalid();
        }
    }

    public void setValue(final Decimal value) {
        if (value != null) {
            this.value = value;
        }
    }

    public int getDigit() {
        return digit;
    }

    public void setDigit(final int d) {
        if (digit != d) {
            digit = d;
            markInvalid();
        }
    }

    /**
     * Adds a {@link ChangeListener} which will be notified whenever the value
     * of the {@code ObservableValue} changes. If the same listener is added
     * more than once, then it will be notified more than once. That is, no
     * check is made to ensure uniqueness.
     * <p>
     * Note that the same actual {@code ChangeListener} instance may be safely
     * registered for different {@code ObservableValues}.
     * <p>
     * The {@code ObservableValue} stores a strong reference to the listener
     * which will prevent the listener from being garbage collected and may
     * result in a memory leak. It is recommended to either unregister a
     * listener by calling {@link #removeListener(ChangeListener)
     * removeListener} after use or to use an instance of
     * {@link WeakChangeListener} avoid this situation.
     *
     * @param listener The listener to register
     * @throws NullPointerException if the listener is null
     * @see #removeListener(ChangeListener)
     */
    @Override
    public void addListener(final ChangeListener<? super Number> listener) {

    }

    /**
     * Removes the given listener from the list of listeners that are notified
     * whenever the value of the {@code ObservableValue} changes.
     * <p>
     * If the given listener has not been previously registered (i.e. it was
     * never added) then this method call is a no-op. If it had been previously
     * added then it will be removed. If it had been added more than once, then
     * only the first occurrence will be removed.
     *
     * @param listener The listener to remove
     * @throws NullPointerException if the listener is null
     * @see #addListener(ChangeListener)
     */
    @Override
    public void removeListener(final ChangeListener<? super Number> listener) {

    }

    /**
     * Adds an {@link InvalidationListener} which will be notified whenever the
     * {@code Observable} becomes invalid. If the same
     * listener is added more than once, then it will be notified more than
     * once. That is, no check is made to ensure uniqueness.
     * <p>
     * Note that the same actual {@code InvalidationListener} instance may be
     * safely registered for different {@code Observables}.
     * <p>
     * The {@code Observable} stores a strong reference to the listener
     * which will prevent the listener from being garbage collected and may
     * result in a memory leak. It is recommended to either unregister a
     * listener by calling {@link #removeListener(InvalidationListener)
     * removeListener} after use or to use an instance of
     * {@link WeakInvalidationListener} avoid this situation.
     *
     * @param listener The listener to register
     * @throws NullPointerException if the listener is null
     * @see #removeListener(InvalidationListener)
     */
    @Override
    public void addListener(final InvalidationListener listener) {

    }

    /**
     * Removes the given listener from the list of listeners, that are notified
     * whenever the value of the {@code Observable} becomes invalid.
     * <p>
     * If the given listener has not been previously registered (i.e. it was
     * never added) then this method call is a no-op. If it had been previously
     * added then it will be removed. If it had been added more than once, then
     * only the first occurrence will be removed.
     *
     * @param listener The listener to remove
     * @throws NullPointerException if the listener is null
     * @see #addListener(InvalidationListener)
     */
    @Override
    public void removeListener(final InvalidationListener listener) {

    }

    /**
     * 返回该 {@code DecimalProperty} 对象的字面值
     *
     * @return 该 {@code DecimalProperty} 对象的字面值
     */
    @Override
    public String toString() {
        //待办 2021/8/6:
        return "测试中";
    }
}
