package liu.mars;

import clojure.lang.*;
import jaskell.util.CR;
import scala.runtime.AbstractPartialFunction;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import static clojure.lang.AFn.applyToHelper;

public class MultiPartialFunction<A> extends AbstractPartialFunction<A, Object> implements IFn {
    private static final Keyword dft = CR.keyword("default");
    private static final IRef defaultHierarchy =
            (IRef)CR.invoke("clojure.core", "ref",
                    CR.invoke("clojure.core", "make-hierarchy"));

    private MultiFn multi;
    private final ReentrantReadWriteLock rw;
    private final String name;

    public MultiPartialFunction(IFn fn) {
        rw = new ReentrantReadWriteLock();
        this.multi = new MultiFn("", fn, dft, defaultHierarchy);
        this.name = "";
    }

    public MultiPartialFunction(String name, IFn dispatchFn, Object defaultDispatchVal, IRef hierarchy) {
        rw = new ReentrantReadWriteLock();
        this.multi = new MultiFn(name, dispatchFn, defaultDispatchVal, hierarchy);
        this.name = name;
    }

    public MultiPartialFunction(MultiFn multi){
        rw = new ReentrantReadWriteLock();
        this.multi = multi;
        this.name = "";
    }

    @Override
    public boolean isDefinedAt(A x) {
        return multi.getMethod(multi.invoke(x)) != null;
    }

    @Override
    public Object apply(A x) {
        return multi.invoke(x);
    }

    public MultiPartialFunction addMethod(Object dispatchVal, IFn method) {
        rw.writeLock().lock();
        try{
            this.multi = multi.addMethod(dispatchVal, method);
            return this;
        } finally {
            rw.writeLock().unlock();
        }
    }

    public IFn getMethod(Object dispatchVal) {
        return this.multi.getMethod(dispatchVal);
    }

    private IFn getFn(Object dispatchVal) {
        IFn targetFn = getMethod(dispatchVal);
        if(targetFn == null)
            throw new IllegalArgumentException(String.format("No method in multimethod '%s' for dispatch value: %s",
                    this.name, dispatchVal));
        return targetFn;
    }

    public Object invoke() {
        return getFn(multi.dispatchFn.invoke()).invoke();
    }

    public Object invoke(Object arg1) {
        return getFn(multi.dispatchFn.invoke(arg1)).invoke(Util.ret1(arg1,arg1=null));
    }

    public Object invoke(Object arg1, Object arg2) {
        return getFn(multi.dispatchFn.invoke(arg1, arg2)).
                invoke(Util.ret1(arg1,arg1=null), Util.ret1(arg2,arg2=null));
    }

    public Object invoke(Object arg1, Object arg2, Object arg3) {
        return getFn(multi.dispatchFn.invoke(arg1, arg2, arg3)).
                invoke(Util.ret1(arg1,arg1=null), Util.ret1(arg2,arg2=null), Util.ret1(arg3,arg3=null));
    }

    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4) {
        return getFn(multi.dispatchFn.invoke(arg1, arg2, arg3, arg4)).
                invoke(Util.ret1(arg1,arg1=null),
                        Util.ret1(arg2,arg2=null),
                        Util.ret1(arg3,arg3=null),
                        Util.ret1(arg4,arg4=null));
    }

    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) {
        return getFn(multi.dispatchFn.invoke(arg1, arg2, arg3, arg4, arg5)).
                invoke(Util.ret1(arg1,arg1=null),
                        Util.ret1(arg2,arg2=null),
                        Util.ret1(arg3,arg3=null),
                        Util.ret1(arg4,arg4=null),
                        Util.ret1(arg5,arg5=null));
    }

    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) {
        return getFn(multi.dispatchFn.invoke(arg1, arg2, arg3, arg4, arg5, arg6)).
                invoke(Util.ret1(arg1,arg1=null),
                        Util.ret1(arg2,arg2=null),
                        Util.ret1(arg3,arg3=null),
                        Util.ret1(arg4,arg4=null),
                        Util.ret1(arg5,arg5=null),
                        Util.ret1(arg6,arg6=null));
    }

    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7)
    {
        return getFn(multi.dispatchFn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7)).
                invoke(Util.ret1(arg1,arg1=null),
                        Util.ret1(arg2,arg2=null),
                        Util.ret1(arg3,arg3=null),
                        Util.ret1(arg4,arg4=null),
                        Util.ret1(arg5,arg5=null),
                        Util.ret1(arg6,arg6=null),
                        Util.ret1(arg7,arg7=null));
    }

    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8) {
        return getFn(multi.dispatchFn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8)).
                invoke(Util.ret1(arg1,arg1=null),
                        Util.ret1(arg2,arg2=null),
                        Util.ret1(arg3,arg3=null),
                        Util.ret1(arg4,arg4=null),
                        Util.ret1(arg5,arg5=null),
                        Util.ret1(arg6,arg6=null),
                        Util.ret1(arg7,arg7=null),
                        Util.ret1(arg8,arg8=null));
    }

    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9) {
        return getFn(multi.dispatchFn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9)).
                invoke(Util.ret1(arg1,arg1=null),
                        Util.ret1(arg2,arg2=null),
                        Util.ret1(arg3,arg3=null),
                        Util.ret1(arg4,arg4=null),
                        Util.ret1(arg5,arg5=null),
                        Util.ret1(arg6,arg6=null),
                        Util.ret1(arg7,arg7=null),
                        Util.ret1(arg8,arg8=null),
                        Util.ret1(arg9,arg9=null));
    }

    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10) {
        return getFn(multi.dispatchFn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10)).
                invoke(Util.ret1(arg1,arg1=null),
                        Util.ret1(arg2,arg2=null),
                        Util.ret1(arg3,arg3=null),
                        Util.ret1(arg4,arg4=null),
                        Util.ret1(arg5,arg5=null),
                        Util.ret1(arg6,arg6=null),
                        Util.ret1(arg7,arg7=null),
                        Util.ret1(arg8,arg8=null),
                        Util.ret1(arg9,arg9=null),
                        Util.ret1(arg10,arg10=null));
    }

    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11) {
        return getFn(multi.dispatchFn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11)).
                invoke(Util.ret1(arg1,arg1=null),
                        Util.ret1(arg2,arg2=null),
                        Util.ret1(arg3,arg3=null),
                        Util.ret1(arg4,arg4=null),
                        Util.ret1(arg5,arg5=null),
                        Util.ret1(arg6,arg6=null),
                        Util.ret1(arg7,arg7=null),
                        Util.ret1(arg8,arg8=null),
                        Util.ret1(arg9,arg9=null),
                        Util.ret1(arg10,arg10=null),
                        Util.ret1(arg11,arg11=null));
    }

    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12) {
        return getFn(multi.dispatchFn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12)).
                invoke(Util.ret1(arg1,arg1=null),
                        Util.ret1(arg2,arg2=null),
                        Util.ret1(arg3,arg3=null),
                        Util.ret1(arg4,arg4=null),
                        Util.ret1(arg5,arg5=null),
                        Util.ret1(arg6,arg6=null),
                        Util.ret1(arg7,arg7=null),
                        Util.ret1(arg8,arg8=null),
                        Util.ret1(arg9,arg9=null),
                        Util.ret1(arg10,arg10=null),
                        Util.ret1(arg11,arg11=null),
                        Util.ret1(arg12,arg12=null));
    }

    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13) {
        return getFn(multi.dispatchFn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13)).
                invoke(Util.ret1(arg1,arg1=null),
                        Util.ret1(arg2,arg2=null),
                        Util.ret1(arg3,arg3=null),
                        Util.ret1(arg4,arg4=null),
                        Util.ret1(arg5,arg5=null),
                        Util.ret1(arg6,arg6=null),
                        Util.ret1(arg7,arg7=null),
                        Util.ret1(arg8,arg8=null),
                        Util.ret1(arg9,arg9=null),
                        Util.ret1(arg10,arg10=null),
                        Util.ret1(arg11,arg11=null),
                        Util.ret1(arg12,arg12=null),
                        Util.ret1(arg13,arg13=null));
    }

    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14)
    {
        return getFn(
                multi.dispatchFn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14)).
                invoke(Util.ret1(arg1,arg1=null),
                        Util.ret1(arg2,arg2=null),
                        Util.ret1(arg3,arg3=null),
                        Util.ret1(arg4,arg4=null),
                        Util.ret1(arg5,arg5=null),
                        Util.ret1(arg6,arg6=null),
                        Util.ret1(arg7,arg7=null),
                        Util.ret1(arg8,arg8=null),
                        Util.ret1(arg9,arg9=null),
                        Util.ret1(arg10,arg10=null),
                        Util.ret1(arg11,arg11=null),
                        Util.ret1(arg12,arg12=null),
                        Util.ret1(arg13,arg13=null),
                        Util.ret1(arg14,arg14=null));
    }

    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                         Object arg15) {
        return getFn(
                multi.dispatchFn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14,
                        arg15)).
                invoke(Util.ret1(arg1,arg1=null),
                        Util.ret1(arg2,arg2=null),
                        Util.ret1(arg3,arg3=null),
                        Util.ret1(arg4,arg4=null),
                        Util.ret1(arg5,arg5=null),
                        Util.ret1(arg6,arg6=null),
                        Util.ret1(arg7,arg7=null),
                        Util.ret1(arg8,arg8=null),
                        Util.ret1(arg9,arg9=null),
                        Util.ret1(arg10,arg10=null),
                        Util.ret1(arg11,arg11=null),
                        Util.ret1(arg12,arg12=null),
                        Util.ret1(arg13,arg13=null),
                        Util.ret1(arg14,arg14=null),
                        Util.ret1(arg15,arg15=null));
    }

    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                         Object arg15, Object arg16) {
        return getFn(
                multi.dispatchFn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14,
                        arg15, arg16)).
                invoke(Util.ret1(arg1,arg1=null),
                        Util.ret1(arg2,arg2=null),
                        Util.ret1(arg3,arg3=null),
                        Util.ret1(arg4,arg4=null),
                        Util.ret1(arg5,arg5=null),
                        Util.ret1(arg6,arg6=null),
                        Util.ret1(arg7,arg7=null),
                        Util.ret1(arg8,arg8=null),
                        Util.ret1(arg9,arg9=null),
                        Util.ret1(arg10,arg10=null),
                        Util.ret1(arg11,arg11=null),
                        Util.ret1(arg12,arg12=null),
                        Util.ret1(arg13,arg13=null),
                        Util.ret1(arg14,arg14=null),
                        Util.ret1(arg15,arg15=null),
                        Util.ret1(arg16,arg16=null));
    }

    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                         Object arg15, Object arg16, Object arg17) {
        return getFn(
                multi.dispatchFn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14,
                        arg15, arg16, arg17)).
                invoke(Util.ret1(arg1,arg1=null),
                        Util.ret1(arg2,arg2=null),
                        Util.ret1(arg3,arg3=null),
                        Util.ret1(arg4,arg4=null),
                        Util.ret1(arg5,arg5=null),
                        Util.ret1(arg6,arg6=null),
                        Util.ret1(arg7,arg7=null),
                        Util.ret1(arg8,arg8=null),
                        Util.ret1(arg9,arg9=null),
                        Util.ret1(arg10,arg10=null),
                        Util.ret1(arg11,arg11=null),
                        Util.ret1(arg12,arg12=null),
                        Util.ret1(arg13,arg13=null),
                        Util.ret1(arg14,arg14=null),
                        Util.ret1(arg15,arg15=null),
                        Util.ret1(arg16,arg16=null),
                        Util.ret1(arg17,arg17=null));
    }

    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                         Object arg15, Object arg16, Object arg17, Object arg18) {
        return getFn(
                multi.dispatchFn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14,
                        arg15, arg16, arg17, arg18)).
                invoke(Util.ret1(arg1,arg1=null),
                        Util.ret1(arg2,arg2=null),
                        Util.ret1(arg3,arg3=null),
                        Util.ret1(arg4,arg4=null),
                        Util.ret1(arg5,arg5=null),
                        Util.ret1(arg6,arg6=null),
                        Util.ret1(arg7,arg7=null),
                        Util.ret1(arg8,arg8=null),
                        Util.ret1(arg9,arg9=null),
                        Util.ret1(arg10,arg10=null),
                        Util.ret1(arg11,arg11=null),
                        Util.ret1(arg12,arg12=null),
                        Util.ret1(arg13,arg13=null),
                        Util.ret1(arg14,arg14=null),
                        Util.ret1(arg15,arg15=null),
                        Util.ret1(arg16,arg16=null),
                        Util.ret1(arg17,arg17=null),
                        Util.ret1(arg18,arg18=null));
    }

    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                         Object arg15, Object arg16, Object arg17, Object arg18, Object arg19) {
        return getFn(
                multi.dispatchFn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14,
                        arg15, arg16, arg17, arg18, arg19)).
                invoke(Util.ret1(arg1,arg1=null),
                        Util.ret1(arg2,arg2=null),
                        Util.ret1(arg3,arg3=null),
                        Util.ret1(arg4,arg4=null),
                        Util.ret1(arg5,arg5=null),
                        Util.ret1(arg6,arg6=null),
                        Util.ret1(arg7,arg7=null),
                        Util.ret1(arg8,arg8=null),
                        Util.ret1(arg9,arg9=null),
                        Util.ret1(arg10,arg10=null),
                        Util.ret1(arg11,arg11=null),
                        Util.ret1(arg12,arg12=null),
                        Util.ret1(arg13,arg13=null),
                        Util.ret1(arg14,arg14=null),
                        Util.ret1(arg15,arg15=null),
                        Util.ret1(arg16,arg16=null),
                        Util.ret1(arg17,arg17=null),
                        Util.ret1(arg18,arg18=null),
                        Util.ret1(arg19,arg19=null));
    }

    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                         Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20)
    {
        return getFn(
                multi.dispatchFn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14,
                        arg15, arg16, arg17, arg18, arg19, arg20)).
                invoke(Util.ret1(arg1,arg1=null),
                        Util.ret1(arg2,arg2=null),
                        Util.ret1(arg3,arg3=null),
                        Util.ret1(arg4,arg4=null),
                        Util.ret1(arg5,arg5=null),
                        Util.ret1(arg6,arg6=null),
                        Util.ret1(arg7,arg7=null),
                        Util.ret1(arg8,arg8=null),
                        Util.ret1(arg9,arg9=null),
                        Util.ret1(arg10,arg10=null),
                        Util.ret1(arg11,arg11=null),
                        Util.ret1(arg12,arg12=null),
                        Util.ret1(arg13,arg13=null),
                        Util.ret1(arg14,arg14=null),
                        Util.ret1(arg15,arg15=null),
                        Util.ret1(arg16,arg16=null),
                        Util.ret1(arg17,arg17=null),
                        Util.ret1(arg18,arg18=null),
                        Util.ret1(arg19,arg19=null),
                        Util.ret1(arg20,arg20=null));
    }

    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7,
                         Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14,
                         Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20, Object... args)
    {
        return getFn(
                multi.dispatchFn.invoke(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14,
                        arg15, arg16, arg17, arg18, arg19, arg20, args)).
                invoke(Util.ret1(arg1,arg1=null),
                        Util.ret1(arg2,arg2=null),
                        Util.ret1(arg3,arg3=null),
                        Util.ret1(arg4,arg4=null),
                        Util.ret1(arg5,arg5=null),
                        Util.ret1(arg6,arg6=null),
                        Util.ret1(arg7,arg7=null),
                        Util.ret1(arg8,arg8=null),
                        Util.ret1(arg9,arg9=null),
                        Util.ret1(arg10,arg10=null),
                        Util.ret1(arg11,arg11=null),
                        Util.ret1(arg12,arg12=null),
                        Util.ret1(arg13,arg13=null),
                        Util.ret1(arg14,arg14=null),
                        Util.ret1(arg15,arg15=null),
                        Util.ret1(arg16,arg16=null),
                        Util.ret1(arg17,arg17=null),
                        Util.ret1(arg18,arg18=null),
                        Util.ret1(arg19,arg19=null),
                        Util.ret1(arg20,arg20=null),
                        args);
    }

    @Override
    public Object applyTo(ISeq arglist) {
        return applyToHelper(this, Util.ret1(arglist,arglist = null));
    }

    @Override
    public Object call() {
        return invoke();
    }

    @Override
    public void run(){
        invoke();
    }
}
