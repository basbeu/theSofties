package ch.epfl.sweng.favors;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.favors.database.computationModule;

import static junit.framework.TestCase.assertEquals;


public class computationModuleTest {

   List<Integer> list = new ArrayList<Integer>(5);

    @Test
    public void returnCorrectSum(){
        list.add(1);
        list.add(1);
        assertEquals(2, computationModule.performArrayOfTokensSum((ArrayList)list));

    }

    @Test
    public void clearArrayCorrectly(){
        list.add(1);
        list.add(1);
        computationModule.resetTokensArray((ArrayList)list);
        assertEquals(0, list.size());

    }

    @Test
    public void retrieveTokenUnit(){
        list.add(1);
        list.add(1);
        assertEquals(0, computationModule.retrieveOneUnityperTokenAndSum((ArrayList)list));


    }
}
