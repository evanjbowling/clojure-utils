(ns com.evanjbowling.utils
  (:require
    [clojure.pprint]
    [clojure.walk]))

(defn ls
  "List the files similar to unix ls command."
  []
  (clojure.pprint/pprint (file-seq (java.io.File. "."))))

(defn p
  "Pretty print and sort keys of maps"
  [x]
  (let [sort-fn (fn [y]
                  (if (map? y)
                    (into (sorted-map) y)
                    y))]
    (clojure.pprint/pprint
      (clojure.walk/postwalk sort-fn x))))
