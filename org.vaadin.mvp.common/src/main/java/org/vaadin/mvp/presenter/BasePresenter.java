package org.vaadin.mvp.presenter;

import org.vaadin.mvp.eventbus.EventBus;

import com.vaadin.Application;

/**
 * Abstract base class for presenters.
 * 
 * @author tam
 *
 * @param <V> Type of the view for this presenter
 * @param <E> Type of the event bus used by the presenter
 */
public abstract class BasePresenter<V, E extends EventBus> implements IPresenter<V, E> {

  private boolean bound = false;

  protected E eventBus = null;
  protected V view = null;

  @Override
  public void setEventBus(E eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public E getEventBus() {
    return this.eventBus;
  }

  @Override
  public void setView(V view) {
    this.view = view;
  }

  @Override
  public V getView() {
    return this.view;
  }

  @Override
  public void bind() {
    // override in implementation
  }

  @Override
  public void bindIfNeeded() {
    if (!bound) {
      this.bind();
      this.bound = true;
    }
  }

}
