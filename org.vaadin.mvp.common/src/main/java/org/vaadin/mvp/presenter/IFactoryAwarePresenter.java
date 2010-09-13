package org.vaadin.mvp.presenter;

/**
 * Interface for presenters that require access to the presenter factory.
 * 
 * @author tam
 */
public interface IFactoryAwarePresenter {

  /**
   * Set the presenter factory.
   * 
   * @param presenterFactory
   */
  public abstract void setPresenterFactory(IPresenterFactory presenterFactory);
  
}
