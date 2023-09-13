(ns webscraper.core
  (:gen-class)
  (:require [reaver :as rv]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(def hacker-news (slurp "https://economictimes.indiatimes.com/archivelist/starttime-44959.cms"))

(defn extract-news-details [news]
  (let [news-url (str "https://economictimes.indiatimes.com" (:url news))
        headline (:headline news)
        news-page (slurp news-url)
        extracted-data (-> news-page
                           (rv/parse)
                           (rv/extract [:author :date :news] ".auth" rv/text ".jsdtTime" rv/text ".artText" rv/text))]
    (assoc extracted-data :url news-url :heading headline)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [day-news-list (-> hacker-news
                          (rv/parse)
                          (rv/extract-from ".content > li" [:headline :url] "a" rv/text "a" (rv/attr :href))
                          (take 10)
                          (map extract-news-details))
        csv-data (->> day-news-list
                      (map (fn [news]
                             {:url (:url news)
                              :heading (:heading news)
                              :author (:author news)
                              :date (:date news)
                              :news (:news news)}))
                      (cons {:url "URL" :heading "Heading" :author "Author" :date "Date" :news "News"})
                      (csv/write-csv {:quote? true}))]
    (io/writer "news_data.csv" csv-data)))

;; (ns webscraper.core

;;   (:gen-class)
;;   (:require [reaver :as rv])
;;   )

;; (def hacker-news (slurp "https://economictimes.indiatimes.com/archivelist/starttime-44959.cms"))

;; (defn get_news [news]

;;   (let
;;       [
;;        news_url   (str "https://economictimes.indiatimes.com" (:url news))
;;        head_line   (:headline news)
;;        ]
;;     (assoc (rv/extract (rv/parse (slurp news_url) ) [:author :date :news] ".auth" rv/text ".jsdtTime" rv/text ".artText" rv/text) :url news_url :heading head_line)
;;     )

;;   )

;; (defn -main
;;   "I don't do a whole lot ... yet."
;;   [& args]
;;   (let
;;       [day_news_list (rv/extract-from (rv/parse hacker-news) ".content > li"
;;                                       [:headline :url]
;;                                       "a" rv/text
;;                                       "a" (rv/attr :href))
;;  ]
;;     ;; (map get_news day_news_list)
;;     (map get_news (take 10 day_news_list))
;;      ;; (rv/extract (rv/parse first_news) [] ".urls") 
;;     ;; (rv/extract (rv/parse first_news) [] ) 
;;   )

;;   (println (rv/parse hacker-news))

;;   (rv/parse hacker-news)
;;   )
