package com.parkingfinder.parkingservice.util;

import org.springframework.scheduling.annotation.Async;

/**
* Utility interface that needs to be implemented by
 * classes that read data from database asynchronously
* */
public interface DataLoader {
    /**
     * Asynchronous method for loading data from database
     * */
    @Async
    public void loadDataFromDatabase();
}
