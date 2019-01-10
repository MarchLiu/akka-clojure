package liu.mars;

import akka.actor.*;
import clojure.lang.IFn;
import clojure.lang.MultiFn;
import scala.Option;
import scala.PartialFunction;
import scala.runtime.AbstractPartialFunction;
import scala.runtime.BoxedUnit;

import java.util.Optional;

public class ClojureActor extends AbstractActor {
    private MultiFn receiver;
    private IFn aroundReceive;
    private IFn preStart;
    private IFn postStop;
    private IFn restart;
    private IFn preRestart;
    private IFn postRestart;
    private IFn aroundPreStart;
    private IFn aroundPostStop;
    private IFn aroundPreRestart;
    private IFn aroundPostRestart;
    private IFn supervisor;
    private IFn unhandled;

    protected ClojureActor(MultiFn fn) {
        this.receiver = fn;
    }

    private Receive buildReceive(MultiFn fn){
        var _this = this;
        var partialFunction = new AbstractPartialFunction() {
            @Override
            public boolean isDefinedAt(Object message) {
                return receiver.getMethod(receiver.dispatchFn.invoke(_this, message)) != null;
            }

            @Override
            public Object apply(Object message) {
                return receiver.invoke(_this, message);
            }
        };
        return new Receive(partialFunction);
    }


    @Override
    public Receive createReceive() {
        return buildReceive(receiver);
    }

    public static Props props(MultiFn fn){
        return Props.create(ClojureActor.class, () -> new ClojureActor(fn));
    }

    public static Props propsWithInit(IFn initiator, MultiFn fn){
        return Props.create(ClojureActor.class, () -> {
            var result = new ClojureActor(fn);
            initiator.invoke(result);
            result.receiver = fn;
            return result;
        });
    }

    public IFn getPreStart() {
        return preStart;
    }

    public void setPreStart(IFn preStart) {
        this.preStart = preStart;
    }

    public IFn getPostStop() {
        return postStop;
    }

    public void setPostStop(IFn postStop) {
        this.postStop = postStop;
    }

    public IFn getRestart() {
        return restart;
    }

    public void setRestart(IFn restart) {
        this.restart = restart;
    }

    public IFn getPreRestart() {
        return preRestart;
    }

    public void setPreRestart(IFn preRestart) {
        this.preRestart = preRestart;
    }

    public IFn getPostRestart() {
        return postRestart;
    }

    public void setPostRestart(IFn postRestart) {
        this.postRestart = postRestart;
    }

    public IFn getAroundPreStart() {
        return aroundPreStart;
    }

    public void setAroundPreStart(IFn aroundPreStart) {
        this.aroundPreStart = aroundPreStart;
    }

    public IFn getAroundPostStop() {
        return aroundPostStop;
    }

    public void setAroundPostStop(IFn aroundPostStop) {
        this.aroundPostStop = aroundPostStop;
    }

    public IFn getAroundPreRestart() {
        return aroundPreRestart;
    }

    public void setAroundPreRestart(IFn aroundPreRestart) {
        this.aroundPreRestart = aroundPreRestart;
    }

    public IFn getAroundPostRestart() {
        return aroundPostRestart;
    }

    public void setAroundPostRestart(IFn aroundPostRestart) {
        this.aroundPostRestart = aroundPostRestart;
    }

    public IFn getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(IFn supervisor) {
        this.supervisor = supervisor;
    }

    public IFn getAroundReceive() {
        return aroundReceive;
    }

    public void setAroundReceive(IFn aroundReceive) {
        this.aroundReceive = aroundReceive;
    }

    public IFn getUnhandled() {
        return unhandled;
    }

    public void setUnhandled(IFn unhandled) {
        this.unhandled = unhandled;
    }

    @Override
    public void preStart() throws Exception {
        if (preStart == null){
            super.preStart();
        }else{
            preStart.invoke(this);
        }
    }

    @Override
    public void postStop() throws Exception {
        if (postStop == null){
            super.postStop();
        } else {
            postStop.invoke(this);
        }
    }

    @Override
    public void aroundPreStart() {
        if (aroundPreStart == null){
            super.aroundPreStart();
        }else{
            aroundPreStart.invoke(this);
        }
    }

    @Override
    public void aroundPostStop() {
        if (aroundPostStop == null){
            super.aroundPostStop();
        }else{
            aroundPostStop.invoke(this);
        }
    }

    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        if (preRestart == null) {
            super.preRestart(reason, message);
        } else {
            preRestart.invoke(this, reason, message);
        }
    }

    @Override
    public void aroundPreRestart(Throwable reason, Option<Object> message) {
        if (aroundPreRestart == null) {
            super.aroundPreRestart(reason, message);
        } else {
            aroundPreRestart.invoke(this, reason, message);
        }
    }

    @Override
    public void aroundPostRestart(Throwable reason) {
        if (aroundPostRestart == null) {
            super.aroundPostRestart(reason);
        } else {
            aroundPostRestart.invoke(this, reason);
        }
    }

    @Override
    public void preRestart(Throwable reason, Optional<Object> message) throws Exception {
        if (preRestart == null) {
            super.preRestart(reason, message);

        } else {
            preRestart.invoke(this, reason, message);
        }
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        if (postRestart == null) {
            super.postRestart(reason);
        } else {
            postRestart.invoke(this, reason);
        }
    }

    @Override
    public void aroundReceive(PartialFunction<Object, BoxedUnit> receive, Object msg) {
        if (aroundReceive == null) {
            super.aroundReceive(receive, msg);
        } else {
            aroundReceive.invoke(this, receive, msg);
        }
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        if (supervisor == null) {
            return super.supervisorStrategy();
        } else {
            return (SupervisorStrategy)supervisor.invoke(this);
        }
    }

    @Override
    public void unhandled(Object message) {
        if(unhandled == null) {
            super.unhandled(message);
        } else {
            unhandled.invoke(this, message);
        }
    }

    public void become(MultiFn fn){
        getContext().become(buildReceive(fn));
    }

    public void unbecome(){
        getContext().unbecome();
    }


}
