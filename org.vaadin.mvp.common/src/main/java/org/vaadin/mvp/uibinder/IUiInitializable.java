package org.vaadin.mvp.uibinder;

/**
 * Interface for UiBinder type views which should be additionally initialized.
 * Implicitly adds the IUiBindable interface.
 * 
 * @author silvan
 */
public interface IUiInitializable extends IUiBindable {
  /**
   * Method will be called after the ui binding to perform additional
   * programmatic initialization.
   */
  void init();
}
