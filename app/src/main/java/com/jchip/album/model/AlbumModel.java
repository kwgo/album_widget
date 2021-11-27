package com.jchip.album.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

public class AlbumModel implements Serializable {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addModelListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removeModelListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void fireModelListener(String name, Object oldValue, Object newValue) {
        support.firePropertyChange(name, oldValue, newValue);
    }

}
