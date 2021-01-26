package com.parkingfinder.parkingservice.util;

/**
* Subject utility interface for implementing observer pattern
* */
public interface Subject {
    /**
     * Method that registers an observer with the subject
     * @param observer - Object that implements Observer interface
     *                 to be registered with Subject
     */
    public void registerObserver(Observer observer);
    /**
     * Method that removes an observer registered with the subject
     * @param observer - Object that implements Observer interface
     *                 to be removed from observers
     */
    public void removeObserver(Observer observer);
    /**
     * Method that notifies all observes when the Subject's state changes
     */
    public void notifyObservers();
}
