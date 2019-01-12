(ns liu.mars.actor
  (:import (akka.pattern Patterns)
           (scala.concurrent Await)
           (akka.util Timeout)
           (java.time Duration)
           (akka.actor ActorRef)))

(defn ??
  ([actor message  ^Timeout timeout]
   (-> (Patterns/ask actor message timeout)
       (Await/result (.duration timeout))))
  ([actor message]
   (let [timeout (Timeout/create (Duration/ofSeconds 1))]
     (-> (Patterns/ask actor message timeout)
         (Await/result (.duration timeout))))))

(defn ?
  ([actor message ^Timeout timeout]
   (Patterns/ask actor message timeout))
  ([actor message]
   (let [timeout (Timeout/create (Duration/ofSeconds 1))]
     (? actor message timeout))))

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

