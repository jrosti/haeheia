(defproject haeheia "0.0.1"
  :description "Hae heiaheian harkat"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-http "0.9.1"]
                 [org.clojure/data.json "0.2.4"]
                 [enlive "1.1.5"]]
  :aot  [haeheia.core]
  :main  haeheia.core)

