(ns liu.mars.partial-test
  (:require [clojure.test :refer :all])
  (:require [liu.mars.scala-partial :refer :all]))

(deftest simple-test
  (let [partial-function (scala-partial (fn [x] (:category x)))]
    (partial-method partial-function :inc [x]  (inc (:data x)))
    (partial-method partial-function :dec [x] (dec (:data x)))
    (partial-method partial-function :say [x] (format "say: %s" (:data x)))
    (is (= 6 (.apply partial-function {:category :inc :data 5})))
    (is (= 6 (partial-function {:category :inc :data 5})))
    (is (= 1 (.apply partial-function {:category :dec :data 2})))
    (is (= 1 (partial-function {:category :dec :data 2})))
    (let [test-str "this is a test."]
      (is (= (str "say: " test-str) (.apply partial-function {:category :say :data test-str})))
      (is (= (str "say: " test-str) (partial-function {:category :say :data test-str}))))))

