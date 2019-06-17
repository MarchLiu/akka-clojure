(ns liu.mars.scala-partial
  (:require [jaskell.multi :refer [multi]])
  (:import [liu.mars MultiPartialFunction]
           (clojure.lang MultiFn IFn)))

(def ^{:private true}
  default-hierarchy (make-hierarchy))

(defmacro defpartial
  "Creates a new scala partial function with the associated dispatch function.
  The docstring and attr-map are optional.

  Options are key-value pairs and may be one of:

  :default

  The default dispatch value, defaults to :default

  :hierarchy

  The value used for hierarchical dispatch (e.g. ::square is-a ::shape)

  Hierarchies are type-like relationships that do not depend upon type
  inheritance. By default Clojure's multimethods dispatch off of a
  global hierarchy map.  However, a hierarchy relationship can be
  created with the derive function used to augment the root ancestor
  created with make-hierarchy.

  Multimethods expect the value of the hierarchy option to be supplied as
  a reference type e.g. a var (i.e. via the Var-quote dispatch macro #'
  or the var special form)."
  {:arglists '([name docstring? attr-map? dispatch-fn & options])
   :added "1.0"}
  [mm-name & options]
  (let [docstring   (if (string? (first options))
                      (first options)
                      nil)
        options     (if (string? (first options))
                      (next options)
                      options)
        m           (if (map? (first options))
                      (first options)
                      {})
        options     (if (map? (first options))
                      (next options)
                      options)
        dispatch-fn (first options)
        options     (next options)
        m           (if docstring
                      (assoc m :doc docstring)
                      m)
        m           (if (meta mm-name)
                      (conj (meta mm-name) m)
                      m)
        mm-name (with-meta mm-name m)]
    (when (= (count options) 1)
      (throw (Exception. "The syntax for defmulti has changed. Example: (defmulti name dispatch-fn :default dispatch-value)")))
    (let [options   (apply hash-map options)
          default   (get options :default :default)
          hierarchy (get options :hierarchy #'default-hierarchy)]
      `(let [v# (def ~mm-name)]
         (when-not (and (.hasRoot v#) (instance? clojure.lang.MultiFn (deref v#)))
           (def ~mm-name
             (new liu.mars.MultiPartialFunction ~(name mm-name) ~dispatch-fn ~default ~hierarchy)))))))

(defmacro scala-partial
  "Creates a new \"anonymous\" scala partial function with the associated dispatch function.
  The docstring is optional.

  like fn, you can assign a name for this partial method, it will pass into MultiPartialFunction
  construction function.

  Options are key-value pairs and may be one of:

  :default

  The default dispatch value, defaults to :default

  :hierarchy

  The value used for hierarchical dispatch (e.g. ::square is-a ::shape)

  Hierarchies are type-like relationships that do not depend upon type
  inheritance. By default Clojure's multimethods dispatch off of a
  global hierarchy map.  However, a hierarchy relationship can be
  created with the derive function used to augment the root ancestor
  created with make-hierarchy.

  Multimethods expect the value of the hierarchy option to be supplied as
  a reference type e.g. a var (i.e. via the Var-quote dispatch macro #'
  or the var special form)."
  {:arglists '([name? dispatch-fn & options])
   :forms    '[(multi name? dispach-fn & options)]
   :added    "1.0"}
  [& sigs]
  (let [multi-name (if (symbol? (first sigs))
                     (first sigs)
                     nil)
        sigs (if multi-name (next sigs) sigs)
        dispatch-fn (first sigs)
        options (next sigs)]
    (let [options (apply hash-map options)
          default (get options :default :default)
          hierarchy (get options :hierarchy #'default-hierarchy)]
      (let [n (if multi-name
                (name multi-name)
                "muliti*")]
        `(new liu.mars.MultiPartialFunction ~n ~dispatch-fn ~default ~hierarchy)))))

(defmacro partial-method
  "Creates and installs a new method of scala partial function object associated with dispatch-value. "
  {:added "0.2.0"}
  [partial-obj dispatch-val & fn-tail]
  `(. ~(with-meta partial-obj {:tag 'liu.mars.MultiPartialFunction}) addMethod ~dispatch-val (fn ~@fn-tail)))

