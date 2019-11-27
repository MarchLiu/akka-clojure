(ns liu.mars.clojure-actor-test
  (:require [clojure.test :refer :all]
            [liu.mars.actor :as actor :refer [!]])
  (:import (akka.actor ActorSystem)
           (liu.mars ClojureActor)
           (akka.testkit.javadsl TestKit)
           (java.util.function Supplier Function)))

(defmulti receiver "receive matchers for basic workflow" (fn [this message] (class message)))
(defmethod receiver String [this message]
  (println (str "receive a text message " message " in " this))
  (.tell (.getSender this) (str "reply message for " message) (.getSelf this)))
(defmethod receiver Long [this message]
  (println (str "receive a long message " message " in " this))
  (.tell (.getSender this) (str "reply message for " message) (.getSelf this)))

(deftest basic-test
  "tests for basic actor workflow"
  (let [system (ActorSystem/create "test")
        test-kit (TestKit. system)
        await #(.awaitCond test-kit (reify Supplier (get [this] (.msgAvailable test-kit))))
        self (.getRef test-kit)
        actor (.actorOf system (ClojureActor/props receiver))]
    (.tell actor "test message" self)
    (await)
    (.expectMsgPF test-kit "check text messsage" (reify Function
                                                   (apply [this message]
                                                     (is (= "reply message for test message" message)))))
    (.tell actor 2131431234 self)
    (await)
    (.expectMsgClass test-kit String)
    (TestKit/shutdownActorSystem system)))

(deftest init-test
  "tests for clojure actor by creator"
  (let [system (ActorSystem/create "test")
        test-kit (TestKit. system)
        await #(.awaitCond test-kit (reify Supplier (get [this] (.msgAvailable test-kit))))
        self (.getRef test-kit)
        initiator (fn [actor]
                    (doto actor
                      (.setPreStart #(println (str % " is going to start")))
                      (.setPostStop #(println (str % " stopped")))))
        actor (.actorOf system (ClojureActor/propsWithInit initiator receiver))]
    (.tell actor "test message" self)
    (await)
    (.expectMsgPF test-kit "check text messsage" (reify Function
                                                   (apply [this message]
                                                     (is (= "reply message for test message" message)))))
    (.tell actor 2131431234 self)
    (await)
    (.expectMsgClass test-kit String)
    (.stop system actor)
    (TestKit/shutdownActorSystem system)))

(deftest inplace-test
  "tests for create actor by shortcut function"
  (let [system (ActorSystem/create "test")
        test-kit (TestKit. system)
        await #(.awaitCond test-kit (reify Supplier (get [this] (.msgAvailable test-kit))))
        self (.getRef test-kit)
        of (actor/of system)
        actor (of "actor"
                  (fn [this message] (:category message))
                  :order (fn [this message]
                           (let [sender (.getSender this)
                                 self (.getSelf this)
                                 ! #(! %1 %2 self)]
                             (println (str "receive a order message " message " in " this))
                             (! sender (str "reply message for " (:content message)))))
                  :form (fn [this message]
                          (let [sender (.getSender this)
                                self (.getSelf this)
                                ! #(! %1 %2 self)]
                            (println (str "receive a form message " message " in " this))
                            (! sender (str "reply message for " (:content message))))))]
    (! actor {:category :order :content "test order"} self)
    (await)
    (.expectMsgPF
      test-kit
      "check order esssage"
      (reify Function
        (apply [this message]
          (is (= "reply message for test order" message)))))
    (! actor {:category :form :content "test form"} self)
    (await)
    (.expectMsgPF
      test-kit
      "check form messsage"
      (reify Function
        (apply [this message]
          (is (= "reply message for test form" message)))))
    (TestKit/shutdownActorSystem system)))
