(ns haeheia.csvtojson
  (:require [clojure.string :as string]
            [clojure.data.json :as json]))

(defn lines-in [filename]
  (-> filename slurp (string/split #"\n") rest))

(def kws 
  [:title 
   :sport
   :duration
   :distance
   :avghr
   :creationDate])

(def mapping
  (into 
   {} 
   (map (fn [e] [(second e) (first e)])
        (map-indexed 
         vector kws))))

(defn value-of [line-data kw]
  (let [index-of (kw mapping)]
    {kw (nth line-data index-of)}))

(defn transform-line [line]
  (let [line-data (string/split line #";")]
    (assoc (apply merge (map (partial value-of line-data) kws))
      :hid (str (hash line)))))

(defn process [lines]
  (map transform-line lines))

(defn csv2json [filename]
  (let [lines (lines-in filename)
        processed (process lines)]
    (spit "out.json" (json/write-str processed))))
