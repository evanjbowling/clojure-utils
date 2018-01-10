(ns com.evanjbowling.utils
  (:require
    [clojure.pprint :as pp]))

(defn ls
  "List the files similar to unix ls command."
  []
  (pp/pprint (file-seq (java.io.File. "."))))

