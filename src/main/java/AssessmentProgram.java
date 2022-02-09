
public class AssessmentProgram
{
    public static int multiplyNum(int a, int b){
        return a * b;

    }

    public static int addNum(int a, int b){
        return a + b;

    }
 

    public static void main(String[] args) throws Exception
    {
        int a = 5;
        int b = 4;

        System.out.println("Program started")

        int mulRes = multiplyNum(5,4);
        System.out.println("Result: " + a + " * " + b + " = " + mulRes);

        int addRes = addNum(5,4);
        System.out.println("Result: " + a + " + " + b + " = " + addRes);

        System.out.println("Program ended");




    }
}
