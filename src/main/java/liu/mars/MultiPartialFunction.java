package liu.mars;

import clojure.lang.*;
import jaskell.util.CR;
import scala.runtime.AbstractPartialFunction;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MultiPartialFunction<A> extends AbstractPartialFunction<A, Object> {
    private static final Keyword dft = CR.keyword("default");
    private static final IRef defaultHierarchy = (IRef)CR.invoke("clojure.core", "make-hierarchy");

    private MultiFn multi;
    private final ReentrantReadWriteLock rw;

    public MultiPartialFunction(IFn fn) {
        rw = new ReentrantReadWriteLock();
        this.multi = new MultiFn(null, fn, dft, defaultHierarchy);
    }

    public MultiPartialFunction(MultiFn multi){
        rw = new ReentrantReadWriteLock();
        this.multi = multi;
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
}
