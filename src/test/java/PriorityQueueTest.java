import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TClass{}
class PriorityQueueTest {
    //1、
    public String getTrace(Throwable t) {
        StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer= stringWriter.getBuffer();
        return buffer.toString();
    }

    //2、
    public static String getExceptionAllinformation(Exception ex){
        String sOut = "";
        StackTraceElement[] trace = ex.getStackTrace();
        for (StackTraceElement s : trace) {
            sOut += "\tat " + s + "\r\n";
        }
        return sOut;
    }

    //3、
    public static String getExceptionAllinformation_01(Exception ex) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream pout = new PrintStream(out);
        ex.printStackTrace(pout);
        String ret = new String(out.toByteArray());
        pout.close();
        try {
            out.close();
        } catch (Exception e) {
        }
        return ret;
    }

    //4、
    private static String toString_02(Throwable e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }
    static Stream<Arguments> streamProvider(){
        return Stream.of(
                Arguments.of((Object) new int[]{-4,7,-3,1},(Object) new int[]{-4,-3,1,7}),
                Arguments.of((Object) new int[]{60,0,-5,-100},(Object) new int[]{-100,-5,0,60}),
                Arguments.of((Object) new int[]{6,4,1,3,2,5},(Object) new int[]{1,2,3,4,5,6}),
                Arguments.of((Object) new int[]{-60,-70,-40,-50},(Object) new int[]{-70,-60,-50,-40}),
                Arguments.of((Object) new int[]{1,-1,0},(Object) new int[]{-1,0,1})
        );
    }
    @ParameterizedTest(name="#{index} - Test with Argument={0},{1}")
    @MethodSource("streamProvider")
    public void PriorityQueue_RunTest(int[] random_array, int[] correct_array){
        PriorityQueue<Integer> pq=new PriorityQueue<Integer>();
        int index=0;
        int[] result=new int[random_array.length];
        int s;
        for(int i=0;i<random_array.length;i++){
            pq.add(random_array[i]);
        }
        for(int i=0;i<random_array.length;i++){
            s=pq.poll();
            result[i]=s;
        }
        assertArrayEquals(correct_array,result);

    }
    @org.junit.jupiter.api.Test
    void whenExceptionThrown_thenAssertionSucceeds1(){
        Exception exception=assertThrows(ClassCastException.class,()->{
            PriorityQueue<TClass> t=new PriorityQueue<>();
            t.add(new TClass());
        });

        String expectedMessage="class TClass cannot be cast to class java.lang.Comparable (TClass is in unnamed module of loader 'app'; java.lang.Comparable is in module java.base of loader 'bootstrap')";

        String actualMessage=exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @org.junit.jupiter.api.Test
    void whenExceptionThrown_thenAssertionSucceeds2(){
        Exception exception=assertThrows(NullPointerException.class,()->{
            Collection c=null;
            PriorityQueue<Integer> t=new PriorityQueue<Integer>(c);

        });
        String actualMessage=exception.getMessage();
        assertEquals(actualMessage,null);
    }
    @org.junit.jupiter.api.Test
    void whenExceptionThrown_thenAssertionSucceeds3(){
        Exception exception=assertThrows(IllegalArgumentException.class,()->{
            PriorityQueue<TClass> t=new PriorityQueue<TClass>(-1);

        });
        String actualMessage=exception.getMessage();
        assertEquals(actualMessage,null);
    }
}