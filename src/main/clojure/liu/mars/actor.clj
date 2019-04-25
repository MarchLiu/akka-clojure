(ns liu.mars.actor
  (:import (akka.pattern Patterns)
           (java.time Duration)
           (akka.actor ActorRef)
           (com.typesafe.config ConfigValueFactory)))

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
  ([actor message sender timeout dispatcher]
   (-> (? actor message timeout)
       (Patterns/pipe dispatcher)
       (.to sender)))
  ([actor message sender dispatcher]
   (-> (? actor message)
       (Patterns/pipe dispatcher)
       (.to sender))))

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
