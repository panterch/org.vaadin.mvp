package org.vaadin.mvp.eventbus;

public class EventArgument {

  private Long id;
  
  private String name;

  public EventArgument(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }
  
}
