(ns liu.mars.clj-api
  (:require [jaskell.multi :refer [multi]])
  (:import [liu.mars MultiPartialFunction]
           (clojure.lang MultiFn)))



(defn scala-partial [f]
  (cond
    (fn? f)
    (with-meta (MultiPartialFunction. ^MultiFn f)
               (merge (meta f)
                      :tag 'liu.mars.MultiPartialFunction))
    (instance? MultiFn f)
    (with-meta (MultiPartialFunction. ^MultiFn f)
               (merge (meta f)
                      :tag 'liu.mars.MultiPartialFunction))
    :else (throw (IllegalArgumentException.
                   (format "need fn or multi fn but get %s type %s" (str f) (str (type f)))))))

(defmacro defpartial
  "Creates and installs a new method of scala partial function object associated with dispatch-value. "
  {:added "0.2.0"}
  [partial-obj dispatch-val & fn-tail]
  `(. ~(with-meta partial-obj {:tag 'liu.mars.MultiPartialFunction}) addMethod ~dispatch-val (fn ~@fn-tail)))

