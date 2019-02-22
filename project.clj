(defproject com.evanjbowling.utils "0.1.0-SNAPSHOT"
  :description "Personal collection of clojure utility methods."
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins      [[lein-cljfmt         "0.6.4"]]
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :target-path "target/%s"
  :repl-options {:init-ns com.evanjbowling.utils}
  :global-vars {*warn-on-reflection* true}
  :profiles {:uberjar {:aot :all}})
