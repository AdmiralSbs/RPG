package com.admiralsbs.RPG;

public abstract class Usable extends Handheld {
    private static final long serialVersionUID = -13L;

    public Usable(String n, String d) {
        name = n;
        description = d;
    }

    public abstract void use(Entity e);
}
