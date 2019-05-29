(ns liu.mars.actor
  (:require [jaskell.util :refer [keywordize-it]])
  (:import (akka.pattern Patterns)
           (java.time Duration)
           (akka.actor ActorRef AbstractActor ActorSystem)
           (com.typesafe.config ConfigValueFactory ConfigValue)
           (akka.dispatch Dispatcher)
           (clojure.lang MultiFn IFn Agent)
           (liu.mars ClojureActor)))

(defn ?
  ([actor message ^Duration timeout]
   (future (-> actor
               (Patterns/ask message timeout)
               (.toCompletableFuture)
               (.get))))
  ([actor message]
   (let [timeout (Duration/ofSeconds 1)]
     (? actor message timeout))))

(defn ??
  ([actor message ^Duration timeout]
    @(? actor message timeout))
  ([actor message]
    @(? actor message)))

(defn ?->
  ([^AbstractActor actor message sender ^Dispatcher dispatcher ^Duration timeout]
   (-> actor
       (Patterns/ask message timeout)
       (.toCompletableFuture)
       (Patterns/pipe dispatcher)
       (.to sender)))
  ([actor message sender dispatcher]
   (?-> actor message sender dispatcher (Duration/ofSeconds 1))))

(defn !
  ([actor message sender]
   (.tell actor message sender))
  ([actor message]
   (.tell actor message (ActorRef/noSender))))

(defn actor-of
  ([^ActorSystem system ^MultiFn receiver ^String name]
   (.actorOf system (ClojureActor/props receiver) name))
  ([^ActorSystem system ^IFn init ^MultiFn receiver ^String name]
   (.actorOf system (ClojureActor/propsWithInit init receiver) name))
  ([^ActorSystem system ^IFn init ^Agent state ^MultiFn receiver ^String name]
   (.actorOf system (ClojureActor/propsWithStateInit init state receiver) name)))

(defn new-state
  []
  (agent {}))

(defn deref-state
  [state]
  @state)

(defn get-state
  [state k]
  (get @state k))

(defn get-state-in [state path]
  (get-in @state path))

(defn config-value
  [data]
  (cond
    (map? data) (ConfigValueFactory/fromMap
                  (into {} (map (fn [kv] [(config-value (key kv)) (config-value (val kv))])) data))
    (sequential? data) (ConfigValueFactory/fromIterable
                         (into [] (map config-value) data))
    (keyword? data) (name data)
    :else (ConfigValueFactory/fromAnyRef data)))

(defn config-value-to-clj
  [^ConfigValue config]
  (-> config
      .unwrapped
      keywordize-it))