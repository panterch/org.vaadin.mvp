package org.vaadin.mvp.presenter;

/**
 * Interface for customizers to configure presenter factories.
 * 
 * <p>
 * Presenter factories will call the customizer after creating a presenter
 * instance allowing for flexible customization needs.
 * </p>
 * 
 * @author tam
 */
public interface IPresenterFactoryCustomizer {
  
  /**
   * Customize the presenter instance.
   * 
   * @param presenter
   */
  public abstract void customize(IPresenter presenter);

}
