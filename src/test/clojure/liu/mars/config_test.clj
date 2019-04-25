(ns liu.mars.config-test
  (:require [liu.mars.actor :refer [config-value]])
  (:require [clojure.test :refer :all])
  (:import (com.typesafe.config Config ConfigFactory)))

(deftest config-test
  "tests for clojure source to typesafe config data"
  (let [data {:actor  {:provider :remote}
              :remote {:enabled-transports ["akka.remote.netty.tcp"]
                       :netty              {:tcp {:hostname "192.168.99.1"
                                                  :port     25520}}}}
        ^Config config (-> (ConfigFactory/empty)
                           (.withValue "akka" (config-value data)))]
    (is (= (.getString config "akka.actor.provider") "remote"))
    (is (= (.getInt config "akka.remote.netty.tcp.port") 25520))
    (is (= (.getString config "akka.remote.netty.tcp.hostname") "192.168.99.1"))
    (is (= (-> config
               (.getList "akka.remote.enabled-transports")
               (.get 0)
               .unwrapped) "akka.remote.netty.tcp"))))