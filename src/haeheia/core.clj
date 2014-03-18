(ns haeheia.core
    (:require [clj-http.client :as http]
              [net.cgrand.enlive-html :as html]
              [clojure.data.json :as json]
              [clojure.string :as string])
    (:gen-class)
    (:import [java.io StringReader]))

(def hsession "_hh_session_v_5")

(def json-headers 
  {:headers 
   {"Accept" 
    "*/*;q=0.5, text/javascript, application/javascript, application/ecmascript, application/x-ecmascript"}})

(defn do-login [username password]
  (http/post "https://www.heiaheia.com/account/authenticate"
             (merge json-headers 
                    {:form-params {"user[email]" username
                                   "user[password]" password}})))

(defn login [username password]
  (let [login-response (do-login username password)]
    (if (.contains (:body login-response) "Login failed")
      (throw (IllegalArgumentException. 
              (str "Login failed: " (:body login-response)))) 
      {:cookies { hsession 
                 (-> login-response 
                     :cookies 
                     (get hsession) 
                     (dissoc :discard :expires :domain))}})))

(defn main-page [session]
  (Thread/sleep 300)
  (http/get "https://www.heiaheia.com/" session))

(defn search-logs-uri [main-page-result] 
  (str (first (re-seq #"https://www.heiaheia.com/users/\d+" 
                      (:body main-page-result)))
       "/training_logs"))
  
(defn logs [session logs-uri year page]
  (http/get (str logs-uri "?year=" year "&page=" page "&update_log=request-for-year")
            (merge session json-headers)))

(defn fetch-log [session year page]
  (let [logs-uri (-> session main-page search-logs-uri)]
    (logs session logs-uri year page)))

(defn feed-header [feed-item]
  (let [h (html/select feed-item [:h3.feed-item-header :a])
        heia-id (first (re-seq #"\d+" (-> h first :attrs :href)))
        title (-> h first :content first)]
    {:hid heia-id :title title}))

(defn extract-activity [act]
  (let [td (string/split act  #", ")
        duration (if (= (count td) 2) (string/trim (second td)) 
                     (if (= (count td) 1)
                       (first (re-seq #"\d.*$" (first td)))
                       "0 min"))
        distance (if (= (count td) 2) (first (re-seq #"\d.*$" (first td))) nil)
        sport (string/trim (first (re-seq #"^[^\d]+" act)))
        result {:sport sport :duration (if duration duration "0 min")}]
    (if distance
      (assoc result :distance distance)
      result)))

(defn feed-content [feed-item]
  (let [activity (-> (html/select feed-item [:span :a]) first :content first)
        time-sport-duration (extract-activity activity)
        date-trash (-> (html/select feed-item [:span]) first :content last)
        date (first (re-seq #"\d\d\.\d\d\.\d+" date-trash))]
    (assoc time-sport-duration :creationDate date)))

(defn feed-paragraphs [feed-item]
  {:body
   (apply str
          (filter string? 
                  (flatten (map :content 
                                (html/select feed-item [:p.feed-comment-without-first-sentence])))))})

(defn process-item [feed-item]
  (try 
    (merge (feed-header feed-item) 
           (feed-paragraphs feed-item) 
           (feed-content feed-item))
    (catch Exception e
      {:unknown (str e)})))
  
(defn as-feed-items [log]
    (html/select 
     (html/html-resource (StringReader. (log "content")))
     [:div.feed-item]))

(defn process-nodes [nodes]
  (filter :sport (map process-item nodes)))

(defn fetch-year [session year]
  (loop [page 1 all-items []]
    (Thread/sleep 300)
    (let [new-log (json/read-str (:body (fetch-log session year (str page))))
          new-items (as-feed-items new-log)
          result (concat all-items new-items)]
      (if (new-log "next_page")
        (recur (inc page) result)
        result))))

(defn print-nodes [exs]
  (doseq [line (mapv (fn [ex] (apply str (interpose ";" ((juxt :creationDate :sport :title :duration :distance) ex)))) exs)]
    (println line)))

(defn -main [username password year]
  (let [session (login username password)
        nodes (fetch-year session year)
        json-nodes (process-nodes nodes)]
    (print-nodes json-nodes)
    (spit (str year ".json") (json/write-str json-nodes)))) 
