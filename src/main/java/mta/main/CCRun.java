package mta.main;

import java.lang.reflect.Method;

public class CCRun {

    public static void main(String args[]) throws Exception {
//        String progClass = args[0];
        String progClass = "mta.server.models.Student";
//        String progArgs[] = new String[args.length - 1];
//        System.arraycopy(args, 1, progArgs, 0, progArgs.length);

        CCLoader ccl = new CCLoader(CCRun.class.getClassLoader());
        Class clas = ccl.loadClass(progClass);
        Class mainArgType[] = { (new String[0]).getClass() };
//        Method mta.main = clas.getMethod("mta.main", mainArgType);
//        Object argsArray[] = { progArgs };
//        mta.main.invoke(null, argsArray);

        // Below method is used to check that the Foo is getting loaded
        // by our custom class loader i.e mta.main.CCLoader
        Method printCL = clas.getMethod("toString", null);
        Object invoke = printCL.invoke(clas.newInstance(), new Object[0]);
        System.out.println(invoke.toString());
    }

}
