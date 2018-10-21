package ch.epfl.sweng.favors;

/**
 * Singleton class that give the execution Mode of the app : Test, Normal
 */
public final class TestMode {
    private final static TestMode instance = new TestMode();

    private boolean test;

    /**
     * Construct the singleton class
     */
    private TestMode(){
        if(instance != null)
            throw new IllegalStateException("TestMode already instantiated");

        test = false;
    }

    /**
     * @return the instance of the mode of execution
     */
    public final static TestMode getInstance(){
        return instance;
    }

    /**
     * @return true if the program is running in test mode
     */
    public boolean isTest(){
        return test;
    }

    /**
     * Setter for the test mode
     * @param test boolean meaning if the test mode is activated(true) or not (false)
     */
    public void setTest(boolean test){
        this.test = test;
    }
}
