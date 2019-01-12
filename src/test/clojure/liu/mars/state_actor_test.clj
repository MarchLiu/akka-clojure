(ns liu.mars.state-actor-test
  (:require [liu.mars.actor :refer :all])
  (:require [clojure.test :refer :all])
  (:import (akka.actor ActorSystem)
           (liu.mars ClojureActor)
           (akka.testkit.javadsl TestKit)
           (java.util.function Supplier Function)))

(defmulti receiver "receive matchers for basic workflow"
          (fn [_ message]
            (:order message)))
(defmethod receiver :get [this message]
  (println (str "receive a get message " message " for state " @(.getState this)))
  (! (.getSender this) (get @(.getState this) (:key message)) (.getSelf this)))
(defmethod receiver :get-in [this message]
  (println (str "receive a get in message " message " for state " @(.getState this)))
  (! (.getSender this) (get-in @(.getState this) (:path message)) (.getSelf this)))
(defmethod receiver :post [this message]
  (let [fun (:function message)]
    (println (str "receive a post order " fun " : " (class fun)))
    (send (.getState this) fun)))

(deftest state-test
  "tests for clojure state actor by creator"
  (let [system (ActorSystem/create "test")
        test-kit (TestKit. system)
        await #(.awaitCond test-kit (reify Supplier (get [this] (.msgAvailable test-kit))))
        self (.getRef test-kit)
        text-message "a text message save in state"
        runtime-message "this data post in runtime"
        initiator (fn [actor]
                    (doto actor
                      (.setPreStart #(println (str % " is going to start")))
                      (.setPostStop #(println (str % " stopped"))))
                    (send (.getState actor) #(assoc % :data text-message)))
        actor (.actorOf system (ClojureActor/propsWithInit initiator receiver))]
    (try
      (.tell actor {:order :get :key :data} self)
      (await)
      (.expectMsgPF test-kit "check get messsage" (reify Function
                                                    (apply [this message]
                                                      (is (= text-message message)))))
      (.tell actor {:order :post :function #(assoc % :post-data runtime-message)} self)
      (.tell actor {:order :get-in :path [:post-data]} self)
      (await)
      (.expectMsgPF test-kit "check get in" (reify Function
                                              (apply [this message]
                                                (is (= runtime-message message)))))
      (.stop system actor)
      (finally
        (TestKit/shutdownActorSystem system)))))

(deftest op-test
  "tests for operators"
  (let [system (ActorSystem/create "test")
        test-kit (TestKit. system)
        await #(.awaitCond test-kit (reify Supplier (get [this] (.msgAvailable test-kit))))
        self (.getRef test-kit)
        text-message "a text message save in state"
        runtime-message "this data post in runtime"
        initiator (fn [actor]
                    (doto actor
                      (.setPreStart #(println (str % " is going to start")))
                      (.setPostStop #(println (str % " stopped"))))
                    (send (.getState actor) #(assoc % :data text-message)))
        actor (.actorOf system (ClojureActor/propsWithInit initiator receiver))]
    (try
      (! actor {:order :get :key :data} self)
      (await)
      (.expectMsgPF test-kit "check get messsage" (reify Function
                                                    (apply [this message]
                                                      (is (= text-message message)))))
      (is (= text-message (?? actor {:order :get :key :data})))
      (! actor {:order :post :function #(assoc % :post-data runtime-message)})
      (let [future (? actor {:order :get-in :path [:post-data]})]
        (is (= runtime-message @future)))
      (! actor {:order :post :function #(assoc % :message runtime-message)})
      (let [result (?? actor {:order :get :key :message})]
        (is (= runtime-message result)))
      (.stop system actor)
      (finally
        (TestKit/shutdownActorSystem system)))))
