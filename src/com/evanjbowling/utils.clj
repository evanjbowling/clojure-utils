(ns com.evanjbowling.utils
  (:require
   [clojure.pprint]
   [clojure.walk])
  (:import
   (java.io FileInputStream)))

;;
;; convenience
;;

(defn ls
  "List the files similar to unix ls command."
  []
  (clojure.pprint/pprint (file-seq (java.io.File. "."))))

(defn p
  "Pretty print and sort keys of maps"
  [x]
  (-> (fn [y]
        (if (map? y)
          (into (sorted-map) y)
          y))
      (clojure.walk/postwalk x)
      clojure.pprint/pprint))

(defn print-file-bytes
  "Print contents of file in hex with decimal offset.
  Optional width parameter to set number of bytes printed
  per row."
  ([f] (print-file-bytes f 4))
  ([f width]
   (let [in (FileInputStream. ^String f)
         offset (fn [n width] (int (/ (dec n) width)))
         print-buffer
         (fn [off ba]
           (printf "%04d  %s\n"
                   off
                   (clojure.string/join \space
                                        (map #(format "%02X" %) ba))))]
     (try
       (loop [b (.read in)
              buffer []
              n 0]
         (if (= -1 b)
           (if (empty? buffer)
             nil
             (print-buffer (offset n width) buffer))
           (let [buffer' (conj buffer b)
                 n' (inc n)
                 rbuffer (if (= (count buffer') width) [] buffer')]
             (when (= (count buffer') width)
               (print-buffer (offset n' width) buffer'))
             (recur (.read in) rbuffer n'))))
       (finally
         (when (not= nil in)
           (.close in)))))))

;;
;; concurrency
;;

(defn state-description
  "Description of thread state"
  [s]
  ({:new           "not yet started"
    :runnable      "currently executing in jvm"
    :blocked       "blocked waiting for a monitor lock"
    :waiting       "waiting indefinitely for another thread to perform an action"
    :timed-waiting "waiting for another thread to perform an action for up to specified waiting time"
    :terminated    "exited"} s))

(defn state
  "Get state of thread as keyword"
  [^java.lang.Thread t]
  (let [s (.getState t)]
    (cond
      (= java.lang.Thread$State/NEW s)           :new
      (= java.lang.Thread$State/RUNNABLE s)      :runnable
      (= java.lang.Thread$State/BLOCKED s)       :blocked
      (= java.lang.Thread$State/WAITING s)       :waiting
      (= java.lang.Thread$State/TIMED_WAITING s) :timed-waiting
      (= java.lang.Thread$State/TERMINATED s)    :terminated)))

(defn inspect
  "Inspect thread details"
  [^java.lang.Thread t]
  (let [s (state t)]
    {:id                (.getId t)
     :name              (.getName t)
     :group             (.getThreadGroup t)
     :state             s
     :state-description (state-description s)
     :alive?            (.isAlive t)
     :interrupted?      (.isInterrupted t)
     :priority          (.getPriority t)
     :daemon?           (.isDaemon t)}))

(defn inspect-all
  "Inspect all threads"
  []
  (map inspect (keys (Thread/getAllStackTraces))))
