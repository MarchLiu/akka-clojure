(ns liu.mars.actor
  (:import (akka.pattern Patterns)
           (java.time Duration)
           (akka.actor ActorRef AbstractActor)
           (com.typesafe.config ConfigValueFactory)
           (akka.dispatch Dispatcher)))

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
