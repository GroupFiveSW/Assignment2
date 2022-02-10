import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssessmentProgramTest {

    @Test
    void addNumTest(){
        int a = 5;
        int b = 4;
        assertEquals(9, AssessmentProgram.addNum(a,b), "addNumTest returned wrong result");
    }

    @Test
    void addNumHighNumbersTest(){
        int a = 200;
        int b = 1000;
        assertEquals(1200, AssessmentProgram.addNum(a,b), "addNumHighNumbersTest returned wrong result");

    }

    @Test
    void multiplyNum(){
        int a = 2;
        int b = 3;
        assertEquals(6, AssessmentProgram.multiplyNum(a,b), "multiplyNum returned wrong result");

    }


}
