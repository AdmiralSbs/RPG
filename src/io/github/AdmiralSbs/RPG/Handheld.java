package io.github.AdmiralSbs.RPG;
import java.io.Serializable;

public abstract class Handheld implements Serializable { //Anything without coordinates
   private static final long serialVersionUID = -11L;
   protected String name;
   protected String description;

   public String getName() {
      return name;
   }
   public String getDescription() {
      return description;
   }
}